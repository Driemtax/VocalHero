// Authors: Inaas Hammoush

package model;

/**
 * A DTO that represents the configuration for a training level, including difficulty and mode.
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class LevelInfo {

    private Mode mode;
    private Difficulty difficulty;
    private int levelNumber; // Unique identifier for the level, can be used for progress tracking (tbd)

    public LevelInfo(int level, Mode mode) {
        this.levelNumber = level;
        this.mode = mode;
    }

    // Getters and Setters

    public Difficulty getDifficulty() {return difficulty;}

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    public Mode getMode() {return mode;}

    public void setMode(Mode mode) {this.mode = mode;}

    public int getLevelNumber() {return levelNumber;}
}