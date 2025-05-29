// Authors:David Herrmann
package model;

import java.util.List;

public class Level {
    
    private List<MidiNote> referenceNotes;
    private final Mode mode;
    private final Difficulty difficulty;

    public Level(Mode mode, Difficulty difficulty) {
        this.mode = mode;
        this.difficulty = difficulty;
    }

    public void setReferenceNotes(List<MidiNote> referenceNotes) {
        this.referenceNotes = referenceNotes;
    }

    public List<MidiNote> getReferenceNotes() {
        return referenceNotes;
    }
}
