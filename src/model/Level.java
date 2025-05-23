// Authors:
package model;

import java.util.List;

public class Level {
    
    private List<MidiNote> referenceNotes;

    public void setReferenceNotes(List<MidiNote> referenceNotes) {
        this.referenceNotes = referenceNotes;
    }

    public List<MidiNote> getReferenceNotes() {
        return referenceNotes;
    }
}
