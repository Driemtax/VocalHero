// Authors: Lars Beer
package utils;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.MidiNote;

public class MidiParser {
    private static final String MELODY_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "assets" + File.separator + "melodies" + File.separator;

    /**
     * Parses a midi file and extracts all notes from it. Combines all notes from all channels into a single list.
     * Therefore only works fine on single channeled midi files.
     * @param fileName
     * @return the list of notes
     */
    public static List<MidiNote> parseMidiFile(String fileName) {
        List<MidiNote> midiNotes = new ArrayList<>();
        List<MidiNote> withRests = new ArrayList<>();

        try {
            Sequence sequence = MidiSystem.getSequence(new File(MELODY_PATH + fileName));
            double ticksPerQuarter = sequence.getResolution();
            float tempoBPM = 72f; // Fallback, if not overwritten later
            double microsecondsPerQuarter = 500000.0; // default value if midi file has no tempo event

            // Look for a tempo event in the midi file
            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();
                    if (message instanceof MetaMessage) {
                        MetaMessage mm = (MetaMessage) message;
                        if (mm.getType() == 0x51) { // Tempo-Event
                            byte[] data = mm.getData();
                            microsecondsPerQuarter = ((data[0] & 0xFF) << 16) |
                                                     ((data[1] & 0xFF) << 8) |
                                                     (data[2] & 0xFF);
                            tempoBPM = (float) (60000000.0 / microsecondsPerQuarter);
                            break;
                        }
                    }
                }
            }

            // extract notes
            for (Track track : sequence.getTracks()) {
                // Map for Note-On Events: midiNumber -> startTick
                java.util.Map<Integer, Long> noteOnMap = new java.util.HashMap<>();
                for (int i = 0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();
                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        int command = sm.getCommand();
                        int midiNumber = sm.getData1();
                        long tick = event.getTick();

                        if (command == ShortMessage.NOTE_ON && sm.getData2() > 0) {
                            // Note-On
                            noteOnMap.put(midiNumber, tick);
                        } else if ((command == ShortMessage.NOTE_OFF) ||
                                   (command == ShortMessage.NOTE_ON && sm.getData2() == 0)) {
                            // Note-Off
                            Long startTick = noteOnMap.remove(midiNumber);
                            if (startTick != null) {
                                long endTick = tick;
                                double startTime = ticksToSeconds(startTick, ticksPerQuarter, tempoBPM);
                                double endTime = ticksToSeconds(endTick, ticksPerQuarter, tempoBPM);
                                double duration = endTime - startTime;

                                // Note-Enum suchen
                                MidiNote.Note noteEnum = null;
                                for (MidiNote.Note n : MidiNote.Note.values()) {
                                    if (n.getMidiNumber() == midiNumber) {
                                        noteEnum = n;
                                        break;
                                    }
                                }
                                if (noteEnum != null) {
                                    midiNotes.add(new MidiNote(startTime, duration, noteEnum));
                                }
                            }
                        }
                    }
                }
            }

            // now calculate rests between notes and add them
            midiNotes.sort(Comparator.comparingDouble(MidiNote::getStartTime));
            double previosEnd = 0.0;

            for (MidiNote note : midiNotes) {
                if (note.getStartTime() > previosEnd + 1e-6) { // add small tolerance
                    double pauseStart = previosEnd;
                    double pauseDuration = note.getStartTime() - previosEnd;
                    
                    // we add a dummy note to play for a rest
                    withRests.add(new MidiNote(pauseStart, pauseDuration, MidiNote.Note.REST));
                }
                withRests.add(note);
                previosEnd = Math.max(previosEnd, note.getStartTime() + note.getDuration());
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return withRests;
    }

    // Helper: Convert ticks into seconds
    private static double ticksToSeconds(long tick, double ticksPerQuarter, float tempoBPM) {
        // 1 Quarter Note = 60 / BPM seconds
        double secondsPerTick = (60.0 / tempoBPM) / ticksPerQuarter;
        return tick * secondsPerTick;
    }

    /**
     * Calculates the length of a melody. This approach does not just add up all durations of each note, since two notes can overlap.
     * By remembering the latest ending of all notes we are garanteed to get the duration of the melody.
     * @param melodyList the notes
     * @return the length in seconds
     */
    public static double getLengthOfMelody(List<MidiNote> melodyList) {
        double lengthInSeconds = 0.0;

        for (MidiNote note : melodyList){
            double noteEnd = note.getStartTime() + note.getDuration();
            if (noteEnd > lengthInSeconds) {
                lengthInSeconds = noteEnd;
            }
        }

        return lengthInSeconds;
    }
}

