// Authors: Inaas Hammoush

package manager;

import java.util.List;

import model.Level;
import model.LevelInfo;
import model.MidiNote;
import model.Mode;
import utils.FileUtils;
import utils.IntervalUtil;
import utils.MidiParser;
import utils.NoteUtil;
import model.Difficulty;
import model.Interval;


public class LevelBuilder {
    private LevelInfo levelInfo;

    public LevelBuilder(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    public Level buildLevel(MidiNote.Note baseVoice) {
        Mode mode = levelInfo.getMode();
        Difficulty difficulty = levelInfo.getDifficulty();
        if (mode == Mode.MELODY) {
            // For melody mode, we can choose a random melody from a predefined pool
            String melodyPath = FileUtils.chooseMelody(difficulty);
            List<MidiNote> referenceNotes = MidiParser.parseMidiFile(melodyPath); // we get this from a melody collection
            // Don't forget to set the recording duration for melodies, which is usually longer than 3 seconds
            int melodyLength = (int) MidiParser.getLengthOfMelody(referenceNotes);
            Level level = new Level(levelInfo.getMode(), levelInfo.getDifficulty(), referenceNotes);
            level.setRecordingDuration(melodyLength);
            
            return level;
        } else {
            // For note and interval modes, we generate reference notes based on the difficulty level
            MidiNote note = NoteUtil.getRandomNoteInRange(difficulty.getDifficultyRange(baseVoice)); 
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
        
    public LevelInfo getLevelInfo(){
        return levelInfo;
    }   
}
