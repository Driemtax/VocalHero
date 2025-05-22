package audio;

import javax.sound.sampled.AudioSystem;
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

    public static Mixer.Info getDefaultInputDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getTargetLineInfo().length > 0) {
                return info;
            }
        }
        return null;
    }

    public static Mixer.Info getDefaultOutputDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getSourceLineInfo().length > 0) {
                return info;
            }
        }
        return null;
    }
}
