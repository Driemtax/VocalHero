// Authors: Inaas Hammoush

package model;

/**
 * A DTO that represents the configuration for a training level, including selected audio devices,
 * difficulty, and mode.
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class LevelInfo {

    private Mode mode;
    private Difficulty difficulty;
    // private final int levelNumber; // Unique identifier for the level, can be used for progress tracking (tbd)

    public LevelInfo(int recordingDuration, Difficulty difficulty, Mode mode) {
        this.difficulty = difficulty;
        this.mode = mode;
    }

    // Getters and Setters

    public Difficulty getDifficulty() {return difficulty;}

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    public Mode getMode() {return mode;}

    public void setMode(Mode mode) {this.mode = mode;}
}