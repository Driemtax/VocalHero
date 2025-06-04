// Authors: Inaas Hammoush, Jonas Rumpf

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
    private int levelNumber; 

    public LevelInfo(int levelNumber, Mode mode) {
        this.difficulty = getLevelDifficulty(levelNumber);
        this.levelNumber = level;
        this.mode = mode;
    }

    // Getters and Setters

    public Difficulty getDifficulty() {return difficulty;}

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    // Method to determine difficulty based on level number
    // Later I think it might be better to read the difficulty from a file
    public Difficulty getLevelDifficulty(int levelNumber) {
        if (levelNumber <= 3) {
            return Difficulty.easy;
        } else if (levelNumber > 3 && levelNumber <= 6) {
            return Difficulty.medium;
        } else {
            return Difficulty.hard;
        }
    }

    public Mode getMode() {return mode;}

    public void setMode(Mode mode) {this.mode = mode;}

    public int getLevelNumber() {return levelNumber;}
}