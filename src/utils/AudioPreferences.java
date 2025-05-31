// Author: Jonas Rumpf, Lars Beer

package utils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AudioPreferences {
    private static final Preferences prefs = Preferences.userRoot().node("vocalhero/audio");

    public static void saveSelectedDevices(Mixer.Info input, Mixer.Info output) {
        prefs.put("input", input != null ? input.getName() : "");
        prefs.put("output", output != null ? output.getName() : "");
    }

    public static Mixer.Info loadSavedInputDevice() {
        String saved = prefs.get("input", "");
        return findDeviceByName(saved, true);
    }

    public static Mixer.Info loadSavedOutputDevice() {
        String saved = prefs.get("output", "");
        return findDeviceByName(saved, false);
    }

    private static Mixer.Info findDeviceByName(String name, boolean input) {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            boolean matches = input
                    ? mixer.getTargetLineInfo().length > 0
                    : mixer.getSourceLineInfo().length > 0;

            if (matches && info.getName().equals(name)) {
                return info;
            }
        }
        return null;
    }

    /**
     * Returns all available microphones
     * @return List<Mixer.Info>
     */
    public static List<Mixer.Info> getAvailableMicrophones(AudioFormat format) {
        List<Mixer.Info> microphones = new ArrayList<>();
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            Line.Info[] targetLines = mixer.getTargetLineInfo();
            for (Line.Info lineInfo : targetLines) {
                if (lineInfo instanceof DataLine.Info) {
                    try {
                        TargetDataLine testLine = (TargetDataLine) mixer.getLine(lineInfo);
                        testLine.open(format);
                        testLine.close();
                        microphones.add(info);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return microphones;
    }

    /**
     * Returns all available speakers
     * @param format the audio format to check for compatibility
     * @return List<Mixer.Info> of available speakers
     */
    public static List<Mixer.Info> getAvailableSpeakers(AudioFormat format) {
        List<Mixer.Info> speakers = new ArrayList<>();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(info)) {
                speakers.add(mixerInfo);
            }
        }
        return speakers;
    }

    /**
     * Returns the default input device (microphone) of the system
     * @return Mixer.Info of the default input device or null if none found
     */
    public static Mixer.Info getDefaultInputDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getTargetLineInfo().length > 0) {
                return info;
            }
        }
        return null;
    }

    /**
     * Returns the default output device (speaker) of the system
     * @return Mixer.Info of the default output device or null if none found
     */
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