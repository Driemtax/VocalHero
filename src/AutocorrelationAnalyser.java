public class AutocorrelationAnalyser {
    private static final int SAMPLE_RATE = 44100;

    public static double detectPitch(short[] audioBuffer) {
        int bestLag = -1;
        double maxCorrelation = Double.NEGATIVE_INFINITY;

        // Apply Hamming windowing function
        double[] windowedBuffer = applyHammingWindow(audioBuffer);

        // Compute autocorrelation for lags (skip small lag values)
        for (int lag = 24; lag < windowedBuffer.length / 2; lag++) {
            double correlation = 0.0;

            for (int i = 0; i < windowedBuffer.length - lag; i++) {
                correlation += windowedBuffer[i] * windowedBuffer[i + lag];
            }

            if (correlation > maxCorrelation) {
                maxCorrelation = correlation;
                bestLag = lag;
            }
        }

        if (bestLag > 0) {
            return (double) SAMPLE_RATE / bestLag;
        } else {
            return -1; // Could not detect pitch
        }
    }

    private static double[] applyHammingWindow(short[] input) {
        int N = input.length;
        double[] windowed = new double[N];
        for (int i = 0; i < N; i++) {
            double w = 0.54 - 0.46 * Math.cos((2 * Math.PI * i) / (N - 1));
            windowed[i] = input[i] * w;
        }
        return windowed;
    }
    
}
