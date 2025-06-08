// Authors:David Herrmann
package model;

public enum Difficulty{
    EASY,
    MEDIUM,
    HARD;

    public Range getDifficultyRange() {
    MidiNote.Note baseUserNote = MidiNote.Note.C4; //TODO get the actual base Note 

        return switch(this) {
            case EASY -> new Range(baseUserNote.ordinal() - 3, baseUserNote.ordinal() + 3);
            case MEDIUM -> new Range(baseUserNote.ordinal() - 5, baseUserNote.ordinal() + 5);
            case HARD -> new Range(baseUserNote.ordinal() - 8, baseUserNote.ordinal() + 8); //TODO adjust values to fit
        };
    }
}