// Authors:David Herrmann
package model;

public enum Difficulty{
    EASY,
    MEDIUM,
    HARD;

    public Range getDifficultyRange(MidiNote.Note baseVoice) {

        return switch(this) {
            case EASY -> new Range(baseVoice.ordinal() - 1, baseVoice.ordinal() + 1);
            case MEDIUM -> new Range(baseVoice.ordinal() - 2, baseVoice.ordinal() + 2);
            case HARD -> new Range(baseVoice.ordinal() - 3, baseVoice.ordinal() + 3);
        };
    }
}