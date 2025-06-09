package test;

import java.util.ArrayList;
import java.util.List;

import audio.PitchDetector;
import model.AudioSettings;

public class testPitchDetector {
    public static void main(String[] args) {
        List<byte[]> testSounds = generateTestSounds(3, AudioSettings.getSampleRate());

        PitchDetector detector = new PitchDetector(2048, AudioSettings.getSampleRate());

        for (byte[] test : testSounds) {
            System.out.println("Result: " + detector.getDominantFrequency(test));
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

     private static List<byte[]> generateTestSounds(double durationSeconds, double sampleRate) {
        double[] frequencies = {
                261.63, // C4
                293.66, // D4
                329.63, // E4
                349.23, // F4
                392.00, // G4
                440.00, // A4
                493.88, // H4 (B4)
                523.25  // C5
        };
        List<byte[]> sounds = new ArrayList<>();
        for (double freq : frequencies) {
            sounds.add(generateSineWaveBytes(freq, durationSeconds, sampleRate));
        }
        return sounds;
    }
}
