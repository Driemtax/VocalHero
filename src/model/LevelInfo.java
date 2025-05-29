// Authors: Inaas Hammoush

package model;

import javax.sound.sampled.Mixer;

public class LevelInfo {

    // These fields are still not confirmed, we need to discuss further to finalize the design
    private Mixer.Info selectedMic;
    private Mixer.Info selectedSpeaker;
    private int recordingDuration;
    private Mode mode;
    private Difficulty difficulty;

    public LevelInfo(Mixer.Info selectedMic, Mixer.Info selectedSpeaker, int recordingDuration) {
        this.selectedMic = selectedMic;
        this.selectedSpeaker = selectedSpeaker;
        this.recordingDuration = recordingDuration;
    }

    public Mixer.Info getSelectedMic() {
        return selectedMic;
    }

    public Mixer.Info getSelectedSpeaker() {
        return selectedSpeaker;
    }

    public int getRecordingDuration() {
        return recordingDuration;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Mode getMode() {
        return mode;
    }
}