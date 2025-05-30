// Authors: Inaas Hammoush

package model;

import javax.sound.sampled.Mixer;

/**
 * A DTO that represents the configuration for a training level, including selected audio devices,
 * difficulty, and mode.
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class LevelInfo {

    private Mixer.Info selectedMic;
    private Mixer.Info selectedSpeaker;
    private Mode mode;
    private Difficulty difficulty;
    // private final int levelNumber; // Unique identifier for the level, can be used for progress tracking (tbd)

    public LevelInfo(Mixer.Info selectedMic, Mixer.Info selectedSpeaker, int recordingDuration, Difficulty difficulty, Mode mode) {
        this.selectedMic = selectedMic;
        this.selectedSpeaker = selectedSpeaker;
        this.difficulty = difficulty;
        this.mode = mode;
    }

    // Getters and Setters
    public Mixer.Info getSelectedMic() {return selectedMic;}

    public void setSelectedMic(Mixer.Info selectedMic) {this.selectedMic = selectedMic;}

    public Mixer.Info getSelectedSpeaker() {return selectedSpeaker;}

    public void setSelectedSpeaker(Mixer.Info selectedSpeaker) {this.selectedSpeaker = selectedSpeaker;}

    public Difficulty getDifficulty() {return difficulty;}

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    public Mode getMode() {return mode;}

    public void setMode(Mode mode) {this.mode = mode;}
}