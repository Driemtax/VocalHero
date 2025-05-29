package model.Util;

import model.Note;
import model.Interval;

public class NoteUtil {
    /**
     * generate a random note in the range from and to
     * 
     * @param from lowest possible note (inclusive)
     * @param to   highest possible note (inclusive)
     * @return a random note in the range from and to
     */
    public static Note getRandomNoteInRange(Note from, Note to) {
        Note[] notes = Note.values();
        int start = from.ordinal();
        int end = to.ordinal();
        int index = start + (int) (Math.random() * (end - start + 1));
        return notes[index];
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
    public static Note getNoteFromInterval(Note startNote, Interval interval) {
        return getNoteFromInterval(startNote, interval, false);
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
    public static Note getNoteFromInterval(Note startNote, Interval interval, Boolean descending) {
        int noteIndex = 0;
        int newIndex = 0;
        Note intervalNote = startNote;
        for (Note note : Note.values()) {
            if (note == startNote) {
                noteIndex = note.ordinal();
                if (descending) {
                    newIndex = noteIndex - interval.getSemitones();
                } else {
                    newIndex = noteIndex + interval.getSemitones();
                }
                if (newIndex < 0 || newIndex > (Note.values().length - 1)) {
                    throw new IndexOutOfBoundsException("No Note for index: " + newIndex);
                }
                intervalNote = Note.values()[newIndex];
                return intervalNote;
            }
        }
        throw new IllegalStateException("This code should be unreachable");
    }
}