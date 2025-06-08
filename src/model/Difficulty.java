// Authors:David Herrmann
package model;

public enum Difficulty{
    easy,
    medium,
    hard;

    public Range getDifficultyRange(MidiNote.Note baseVoice) {
    MidiNote.Note baseUserNote = baseVoice; //TODO get the actual base Note 

        return switch(this) {
            case easy -> new Range(baseUserNote.ordinal() - 1, baseUserNote.ordinal() + 1);
            case medium -> new Range(baseUserNote.ordinal() - 2, baseUserNote.ordinal() + 2);
            case hard -> new Range(baseUserNote.ordinal() - 3, baseUserNote.ordinal() + 3); //TODO adjust values to fit
        };
    }
}