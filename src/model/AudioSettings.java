// Author: Jonas Rumpf

package model;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;

public class AudioSettings {
    private static final double PLAYER_SAMPLE_RATE = 22100.0f;
    private static final double RECORDER_SAMPLE_RATE = 44100;
    private static AudioFormat playerFormat = new AudioFormat((float) PLAYER_SAMPLE_RATE, 16, 2, true, false);

    private static AudioFormat recorderFormat = new AudioFormat((float) RECORDER_SAMPLE_RATE, 16, 1, true, false);
    private static Mixer.Info inputDevice;
    private static Mixer.Info outputDevice;

    public static AudioFormat getPlayerFormat() {
        return playerFormat;
    }

    public static AudioFormat getRecorderFormat() {
        return recorderFormat;
    }

    public static void setPlayerFormat(AudioFormat audioFormat) {
        playerFormat = audioFormat;
    }

    public static double getPlayerSampleRate() {
        return PLAYER_SAMPLE_RATE;
    }

    public static double getRecorderSampleRate() {
        return RECORDER_SAMPLE_RATE;
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
