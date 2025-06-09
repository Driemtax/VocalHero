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
        this.levelNumber = levelNumber;
        this.mode = mode;
    }

    // Getters and Setters

    public Difficulty getDifficulty() {return difficulty;}

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    // Method to determine difficulty based on level number
    // Later I think it might be better to read the difficulty from a file
    public Difficulty getLevelDifficulty(int levelNumber) {
        
        if (getMode() == Mode.MELODY) {
            if (levelNumber == 1) {return Difficulty.EASY;} 
            else if (levelNumber == 2) {return Difficulty.MEDIUM;} 
            else {return Difficulty.HARD;}
        } else {
            if (levelNumber <= 2) {return Difficulty.EASY;} 
            else if (levelNumber > 2 && levelNumber <= 4) {return Difficulty.MEDIUM;} 
            else {return Difficulty.HARD;}
        }

    }

    public Mode getMode() {return mode;}

    public void setMode(Mode mode) {this.mode = mode;}

    public int getLevelNumber() {return levelNumber;}
}