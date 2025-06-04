package utils;

import model.MidiNote;
import model.Interval;
import model.MidiNote;
import model.Range;

public class NoteUtil {
    /**
     * generate a random note in the range from and to
     * 
     * @param range the range in which the note should be generated, which holds from and to
     * @return a random note in the range from and to
     */
    public static MidiNote getRandomNoteInRange(Range range) {
        // TODO: Check edge cases for range
        MidiNote midiNote;
        MidiNote.Note[] notes = MidiNote.Note.getAllNotes();
        int start = range.min();
        int end = range.max();
        int index = start + (int) (Math.random() * (end - start + 1));
        if (index < 0 || index >= notes.length) {
            throw new IndexOutOfBoundsException("Index out of bounds for note array: " + index);
        }

        midiNote = new MidiNote(notes[index]);
        return midiNote;
    }

    // public static Note getNoteFromFrequency(double frequency){

    // }

    /**
     * get a Note based on a Note and an Interval sets descending to false as
     * default
     * 
     * @param startNote note from which you want the interval
     * @param interval  interval you want
     * @return the note the interval after the startnote, or startnote TODO replace
     *         with exception
     */
    public static MidiNote getNoteFromInterval(MidiNote startNote, Interval interval) {
        return getNoteFromInterval(startNote.getNoteDefinition(), interval, false);
    }

    /**
     * get a Note based on a Note and an Interval
     * 
     * @param startNote  note from which you want the interval
     * @param interval   interval you want
     * @param descending is the interval descendeng or not
     * @return the note the interval after the startnote, or startnote TODO replace
     *         with exception
     */
    public static MidiNote getNoteFromInterval(MidiNote.Note startNote, Interval interval, Boolean descending) {
        int noteIndex = 0;
        int newIndex = 0;
        MidiNote.Note intervalNote = startNote;
        for (MidiNote.Note note : MidiNote.Note.getAllNotes()) {
            if (note == startNote) {
                noteIndex = note.ordinal();
                if (descending) {
                    newIndex = noteIndex - interval.getSemitones();
                } else {
                    newIndex = noteIndex + interval.getSemitones();
                }
                if (newIndex < 0 || newIndex > (MidiNote.Note.getAllNotes().length - 1)) {
                    throw new IndexOutOfBoundsException("No Note for index: " + newIndex);
                }
                intervalNote = MidiNote.Note.getAllNotes()[newIndex];
                return new MidiNote(intervalNote);
            }
        }
        throw new IllegalStateException("This code should be unreachable");
    }

    public static MidiNote toMidiNote(Note note, double startTime, double duration) {
        return new MidiNote(startTime, duration, note.getFrequency());
    }
}