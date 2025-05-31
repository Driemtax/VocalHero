// Author: Jonas Rumpf

package model;

import javax.sound.sampled.Mixer;

public class AudioSettings {
    private static Mixer.Info inputDevice;
    private static Mixer.Info outputDevice;

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
