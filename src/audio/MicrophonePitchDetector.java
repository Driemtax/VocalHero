package audio;

import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import views.PitchGraphPanel;

public class MicrophonePitchDetector extends Thread {
    private final PitchGraphPanel graphPanel;
    private boolean running = true;

    private static final float SAMPLE_RATE = 48000;
    private static final int BUFFER_SIZE = 2048;

    private final double alpha = 0.1; // Glättung: je kleiner, desto träger
    private double smoothedCents = 0.0;

    // Optional: für Median-Filter
    private final List<Double> history = new ArrayList<>();
    private final int medianWindow = 5;

    public MicrophonePitchDetector(PitchGraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void run() {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format, BUFFER_SIZE);
            microphone.start();

            byte[] buffer = new byte[BUFFER_SIZE];

            while (running) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    double frequency = detectFrequency(buffer, format);
                    if (frequency > 0) {
                        double cents = frequencyToCents(frequency, 261.63); // Vergleich zu C4

                        // LOW-PASS-FILTER
                        //smoothedCents = alpha * cents + (1 - alpha) * smoothedCents;

                        // MEDIAN-ALTERNATIVE:
                        history.add(cents);
                        double median = smoothMedian(history, medianWindow);
                        graphPanel.addPitchValue(median);

                        //graphPanel.addPitchValue(smoothedCents);
                    }
                }
            }

            microphone.stop();
            microphone.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDetection() {
        running = false;
    }

    // Zero-Crossing Pitch Detection
    private double detectFrequency(byte[] audioBytes, AudioFormat format) {
        int[] samples = new int[audioBytes.length / 2];
        for (int i = 0; i < samples.length; i++) {
            int high = audioBytes[2 * i];
            int low = audioBytes[2 * i + 1];
            samples[i] = (short)((high << 8) | (low & 0xff));
        }

        int crossings = 0;
        for (int i = 1; i < samples.length; i++) {
            if ((samples[i - 1] < 0 && samples[i] >= 0) || (samples[i - 1] > 0 && samples[i] <= 0)) {
                crossings++;
            }
        }

        double seconds = samples.length / SAMPLE_RATE;
        return crossings / (2.0 * seconds);
    }

    private double frequencyToCents(double freq, double refFreq) {
        return 1200 * Math.log(freq / refFreq) / Math.log(2);
    }

    private double smoothMedian(List<Double> values, int windowSize) {
        if (values.size() < windowSize) return values.get(values.size() - 1);
        List<Double> window = values.subList(values.size() - windowSize, values.size());
        List<Double> sorted = new ArrayList<>(window);
        sorted.sort(Double::compareTo);
        return sorted.get(windowSize / 2);
    }
}
