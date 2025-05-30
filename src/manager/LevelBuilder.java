// Authors: Inaas Hammoush

package manager;

import java.util.List;

import model.Difficulty;
import model.Level;
import model.LevelInfo;
import model.MidiNote;
import model.Note;
import utils.IntervalUtil;
import utils.NoteUtil;
import model.Interval;


public class LevelBuilder {
    private LevelInfo levelInfo;

    public LevelBuilder(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
    }

    public Level buildLevel() {
        return new Level(levelInfo.getMode(), levelInfo.getDifficulty(), 
                         levelInfo.getSelectedMic(), levelInfo.getSelectedSpeaker(), 
                         generateReferenceNotes());
    }

    private List<MidiNote> generateReferenceNotes() {
        // This method should generate reference notes based on the difficulty level.
        List<MidiNote> referenceNotes;
        Note note;

        switch (levelInfo.getMode()) {
            case NOTE:
                note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange()); 
                referenceNotes.add(note); // a function to convert Note to MidiNote is still needed
                return referenceNotes;
            case INTERVAL:
                // Generate a random note and interval based on difficulty
                note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange());
                // Here the logic seems to be faulty, maybe a range should be passed instead of interval
                Interval interval = IntervalUtil.getRandomIntervalInRange(null, null); 
                referenceNotes.add(NoteUtil.getNoteFromInterval(note, interval)); // a function to convert Note to MidiNote is still needed
                return referenceNotes;
            case MELODY:
                // Here we can get a random melody from a predefined pool
                return referenceNotes;
            default:
                throw new IllegalArgumentException("Unsupported mode: " + levelInfo.getMode());
        }
    }        
}
