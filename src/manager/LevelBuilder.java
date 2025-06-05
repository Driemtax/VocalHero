// Authors: Inaas Hammoush

package manager;

import java.util.List;
import java.util.ArrayList;

import model.Level;
import model.LevelInfo;
import model.MidiNote;
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
                         generateReferenceNotes());
    }

    private List<MidiNote> generateReferenceNotes() {
        // This method should generate reference notes based on the difficulty level.
        List<MidiNote> referenceNotes = new ArrayList<>();
        MidiNote note;

        switch (levelInfo.getMode()) {
            case NOTE:
                note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange()); 
                referenceNotes.add(note);
                return referenceNotes;
            case INTERVAL:
                // Generate a random note and interval based on difficulty
                note = NoteUtil.getRandomNoteInRange(levelInfo.getDifficulty().getDifficultyRange());
                
                // according to the level number, we can set a maximum range for the interval
                // For level 1-2 we can use a maximum of 4 semitones, for level 3-4 a maximum of 8 semitones,
                // and for levels 5-6 a maximum of 12 semitones
                int maxSemitones = levelInfo.getLevelNumber() <= 2 ? 4 : levelInfo.getLevelNumber() <= 4 ? 8 : 12;
                
                Interval interval = IntervalUtil.getRandomIntervalInRange(maxSemitones); 
                
                referenceNotes.add(note);
                referenceNotes.add(NoteUtil.getNoteFromInterval(note, interval));
                
                return referenceNotes;
            case MELODY:
                // Here we can get a random melody from a predefined pool
                return referenceNotes;
            default:
                throw new IllegalArgumentException("Unsupported mode: " + levelInfo.getMode());
        }
    }        
}
