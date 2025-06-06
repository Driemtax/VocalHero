// Authors: Inaas Hammoush

package manager;

import java.util.List;
import java.util.ArrayList;

import model.Level;
import model.LevelInfo;
import model.MidiNote;
import model.Mode;
import utils.IntervalUtil;
import utils.NoteUtil;
import model.Difficulty;
import model.Interval;


public class LevelBuilder {
    private LevelInfo levelInfo;

    public LevelBuilder(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    public Level buildLevel() {
        Mode mode = levelInfo.getMode();
        Difficulty difficulty = levelInfo.getDifficulty();
        int levelNumber = levelInfo.getLevelNumber();
        if (mode == Mode.MELODY) {
            // For melody mode, we can generate a random melody from a predefined pool
            // List<MidiNote> referenceNotes = getMelodyReferenceNotes(); // we get this from a melody collection
            // return new Level(levelInfo.getMode(), levelInfo.getDifficulty(), referenceNotes);
            // Don't forget to set the recording duration for melodies, which is usually longer than 3 seconds

            // Placeholder for melody mode, as we need a melody collection to implement this
            return new Level(mode, difficulty, new ArrayList<MidiNote>(), levelNumber); 
        } else {
            // For note and interval modes, we generate reference notes based on the difficulty level
            MidiNote note = NoteUtil.getRandomNoteInRange(difficulty.getDifficultyRange()); 
            List<MidiNote> referenceNotes = List.of(note);

            Level level = new Level(mode, difficulty, referenceNotes, levelNumber);

            if (mode == Mode.INTERVAL) {
                // according to the level difficulty, we can set a maximum range for the interval
                // For easy levels we can use a maximum of 4 semitones, for medium levels a maximum of 8 semitones,
                // and for hard levels a maximum of 12 semitones
                Interval startingInterval = difficulty == Difficulty.easy ? Interval.m2 : difficulty == Difficulty.medium ? Interval.P4 : Interval.M6;
                Interval maxInterval = difficulty == Difficulty.easy ? Interval.M3 : difficulty == Difficulty.medium ? Interval.m6 : Interval.P8;
                Interval interval = IntervalUtil.getRandomIntervalInRange(startingInterval, maxInterval); 
                level.setIntervalName(interval.getName()); // Set the name of the interval for display purposes
                level.setTargetIntervalNote(NoteUtil.getNoteFromInterval(note, interval));
            }

            return level;
        }
    }     
        
    public LevelInfo getLevelInfo(){
        return levelInfo;
    }   
}
