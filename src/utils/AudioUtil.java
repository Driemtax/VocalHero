package utils;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioUtil {
    
    /**
     * Returns all available microphones
     * @return List<Mixer.Info>
     */
    public List<Mixer.Info> getAvailableMicrophones(AudioFormat format) {
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
    public List<Mixer.Info> getAvailableSpeakers(AudioFormat format) {
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
    public Mixer.Info getDefaultInputDevice() {
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
    public Mixer.Info getDefaultOutputDevice() {
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.getSourceLineInfo().length > 0) {
                return info;
            }
        }
        return null;
    }
}
