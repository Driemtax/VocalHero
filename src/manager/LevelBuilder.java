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
        if (mode == Mode.MELODY) {
            // For melody mode, we can generate a random melody from a predefined pool
            // List<MidiNote> referenceNotes = getMelodyReferenceNotes(); // we get this d´from a melody collection
            // return new Level(levelInfo.getMode(), levelInfo.getDifficulty(), referenceNotes);
            // Don't forget to set the recording duration for melodies, which is usually longer than 3 seconds

            // Placeholder for melody mode, as we need a melody collection to implement this
            return new Level(mode, difficulty, new ArrayList<MidiNote>()); 
        } else {
            // For note and interval modes, we generate reference notes based on the difficulty level
            MidiNote note = NoteUtil.getRandomNoteInRange(difficulty.getDifficultyRange()); 
            List<MidiNote> referenceNotes = List.of(note);

            Level level = new Level(mode, difficulty, referenceNotes);

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

    // public Level buildLevel() {
    //     Level level = new Level(levelInfo.getMode(), levelInfo.getDifficulty(), generateReferenceNotes());
    //     if (levelInfo.getMode() == model.Mode.INTERVAL) {
    //         // Set the target note for interval training
    //         MidiNote targetNote = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange());
    //         level.setTargetIntervalNote(targetNote);
    //     }
    // }

    // private List<MidiNote> generateReferenceNotes() {
    //     // This method should generate reference notes based on the difficulty level.
    //     List<MidiNote> referenceNotes = new ArrayList<>();
    //     MidiNote note;

    //     switch (levelInfo.getMode()) {
    //         case NOTE:
    //             note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange()); 
    //             referenceNotes.add(note);
    //             return referenceNotes;
    //         case INTERVAL:
    //             // Generate a random note and interval based on difficulty
    //             note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange());
                
    //             // according to the level number, we can set a maximum range for the interval
    //             // For level 1-2 we can use a maximum of 4 semitones, for level 3-4 a maximum of 8 semitones,
    //             // and for levels 5-6 a maximum of 12 semitones
    //             int maxSemitones = levelInfo.getLevelNumber() <= 2 ? 4 : levelInfo.getLevelNumber() <= 4 ? 8 : 12;
                
    //             Interval interval = IntervalUtil.getRandomIntervalInRange(maxSemitones); 
                
    //             referenceNotes.add(note);
    //             referenceNotes.add(NoteUtil.getNoteFromInterval(note, interval));
                
    //             return referenceNotes;
    //         case MELODY:
    //             // Here we can get a random melody from a predefined pool
    //             return referenceNotes;
    //         default:
    //             throw new IllegalArgumentException("Unsupported mode: " + levelInfo.getMode());
    //     }
    // }        
}
