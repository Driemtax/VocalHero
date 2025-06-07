// Authors:David Herrmann
package model;

public enum Difficulty{
    EASY,
    MEDIUM,
    HARD;

    public Range getDifficultyRange() {
    MidiNote.Note baseUserNote = MidiNote.Note.C4; //TODO get the actual base Note 

        return switch(this) {
            case EASY -> new Range(baseUserNote.ordinal() - 1, baseUserNote.ordinal() + 1);
            case MEDIUM -> new Range(baseUserNote.ordinal() - 2, baseUserNote.ordinal() + 2);
            case HARD -> new Range(baseUserNote.ordinal() - 3, baseUserNote.ordinal() + 3); //TODO adjust values to fit
        };
    }
}