import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

import AudioProcessing.FrequencyAnalyzer;
import Helper.Note;

public class Main {
    public static void main(String[] args) {
        MicRecorderWithCountdown.main(args);

        // Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        // for (Mixer.Info mixerInfo : mixers) {
        //     Mixer mixer = AudioSystem.getMixer(mixerInfo);
        //     Line.Info[] lineInfos = mixer.getSourceLineInfo();
        //     for (Line.Info lineInfo : lineInfos) {
        //         if (lineInfo instanceof DataLine.Info) {
        //             DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;
        //             AudioFormat[] formats = dataLineInfo.getFormats();
        //             System.out.println("Supported formats for " + mixerInfo.getName() + ":");
        //             for (AudioFormat format : formats) {
        //                 System.out.println(format.toString());
        //             }
        //         }
        //     }
        // }
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
