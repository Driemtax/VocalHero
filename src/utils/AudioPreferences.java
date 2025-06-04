// Author: Jonas Rumpf, Lars Beer

package utils;

import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Mixer;

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
}