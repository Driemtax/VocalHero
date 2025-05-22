package audio;

import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import views.PitchGraphPanel;

public class MicrophonePitchDetector extends Thread {
    private final PitchGraphPanel graphPanel;
    private boolean running = true;

    private static final float SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 2048;

    // Optional: für Median-Filter
    private final List<Double> history = new ArrayList<>();
    private final int medianWindow = 5;

    public MicrophonePitchDetector(PitchGraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public void run() {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            //TargetDataLine microphone = AudioSystem.getTargetDataLine(format);

            Mixer.Info selectedInput = AudioSettings.getInputDevice();
            if (selectedInput == null) {
                System.err.println("Kein Eingabegerät ausgewählt.");
                return;
            }

            Mixer mixer = AudioSystem.getMixer(selectedInput);
            TargetDataLine microphone = (TargetDataLine) mixer.getLine(new DataLine.Info(TargetDataLine.class, format));


            microphone.open(format, BUFFER_SIZE);
            microphone.start();

            byte[] buffer = new byte[BUFFER_SIZE];

            while (running) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    double frequency = detectFrequency(buffer, format);
                    if (frequency > 0) {
                        double targetFrequency = getFrequencyForNote("C4"); // z. B. 262.5 wenn nötig
                        double cents = frequencyToCents(frequency, targetFrequency);
                        //System.out.printf("Ist: %.2f Hz → Abweichung: %.2f Cent\n", frequency, cents);
                        // LOW-PASS-FILTER
                        //smoothedCents = alpha * cents + (1 - alpha) * smoothedCents;

                        // MEDIAN-ALTERNATIVE:
                        history.add(cents);
                        double median = smoothMedian(history, medianWindow);
                        double displayCents = applyTolerance(median, 30.0);
                        graphPanel.addPitchValue(displayCents);

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

    private double applyTolerance(double cents, double tolerance) {
        if (Math.abs(cents) <= tolerance) return 0.0;
        double sign = Math.signum(cents);
        return sign * (Math.abs(cents) - tolerance);
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

        // Autokorrelation
        int maxLag = 1000; // sinnvoll für ~50 Hz – 1000 Hz
        double bestCorrelation = Double.NEGATIVE_INFINITY;
        int bestLag = -1;

        double[] correlations = new double[maxLag];
        for (int lag = 30; lag < maxLag; lag++) {
            double sum = 0;
            for (int i = 0; i < samples.length - lag; i++) {
                sum += samples[i] * samples[i + lag];
            }
            correlations[lag] = sum;
        }

        for (int i = 30; i < maxLag - 1; i++) {
            if (correlations[i] > bestCorrelation) {
                bestCorrelation = correlations[i];
                bestLag = i;
            }
        }

        // Parabolische Interpolation
        double y0 = correlations[bestLag - 1];
        double y1 = correlations[bestLag];
        double y2 = correlations[bestLag + 1];

        double p = 0.5 * (y0 - y2) / (y0 - 2 * y1 + y2); // Interpolierter Offset in Sample-Einheiten
        double refinedLag = bestLag + p;

        return SAMPLE_RATE / refinedLag;

    }

    private double frequencyToCents(double freq, double refFreq) {
        return 1200 * Math.log(freq / refFreq) / Math.log(2);
    }

    private double getFrequencyForNote(String noteName) {
        // Annahme: A4 = 440 Hz
        String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
        int semitoneOffset = 0;

        String note = noteName.replaceAll("[0-9]", "");
        int octave = Integer.parseInt(noteName.replaceAll("[^0-9]", ""));
        for (int i = 0; i < noteNames.length; i++) {
            if (noteNames[i].equals(note)) {
                semitoneOffset = i;
                break;
            }
        }

        int midi = (octave + 1) * 12 + semitoneOffset; // MIDI-Note
        return 440.0 * Math.pow(2, (midi - 69) / 12.0);
    }


    private double smoothMedian(List<Double> values, int windowSize) {
        if (values.size() < windowSize) return values.get(values.size() - 1);
        List<Double> window = values.subList(values.size() - windowSize, values.size());
        List<Double> sorted = new ArrayList<>(window);
        sorted.sort(Double::compareTo);
        return sorted.get(windowSize / 2);
    }
}
