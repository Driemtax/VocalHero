public class Main {
    public static void main(String[] args) {
        double targetFrequency = 440.0; // Beispiel: A4
        double tolerance = 10.0;
        double durationSeconds = 3.0;
        double sampleRate = 22050.0;

        byte[] testSignal = generateSineWaveBytes(targetFrequency, durationSeconds, sampleRate);
        
        FrequencyAnalyzer analyzer = new FrequencyAnalyzer(2048);
        double medianFrequency = analyzer.getDominantFrequency(testSignal);
        if (Math.abs(medianFrequency - targetFrequency) < tolerance) {
            System.out.println("Richtig gesungen! Erkannte Frequenz: " + medianFrequency + " Hz");
        } else {
            System.out.println("Falsch gesungen! Erkannte Frequenz: " + medianFrequency + " Hz. Erwartet: " + targetFrequency + " Hz");
        }
    }

    private static byte[] generateSineWaveBytes(double frequency, double durationSeconds, double sampleRate) {
        int numSamples = (int)(durationSeconds * sampleRate);
        byte[] byteArray = new byte[numSamples * 2]; // 2 Bytes pro Sample (16 Bit)
        double amplitude = 0.8 * 32767; // 80% der maximalen Amplitude, um Clipping zu vermeiden
    
        for (int i = 0; i < numSamples; i++) {
            // Sinuswert berechnen
            double value = amplitude * Math.sin(2 * Math.PI * frequency * i / sampleRate);
            // In 16 Bit signed umwandeln
            short s = (short) value;
            // Little Endian: zuerst Low-Byte, dann High-Byte
            byteArray[2 * i]     = (byte) (s & 0xFF);      // Low-Byte
            byteArray[2 * i + 1] = (byte) ((s >> 8) & 0xFF); // High-Byte
        }
        return byteArray;
    }
    
    
}
