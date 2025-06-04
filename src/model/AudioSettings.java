// Author: Jonas Rumpf

package model;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

public class AudioSettings {
    private static final double SAMPLE_RATE = 44100.0;
    private static AudioFormat format = new AudioFormat((float) SAMPLE_RATE, 16, 2, true, false);

    private static Mixer.Info inputDevice;
    private static Mixer.Info outputDevice;

    public static AudioFormat getFormat() {
        return format;
    }

    public static double getSampleRate() {
        return SAMPLE_RATE;
    }

    public static Mixer.Info getInputDevice() {
        return inputDevice;
    }

    public static void setInputDevice(Mixer.Info input) {
        inputDevice = input;
    }

    public static Mixer.Info getOutputDevice() {
        return outputDevice;
    }

    public static void setOutputDevice(Mixer.Info output) {
        outputDevice = output;
    }
}
