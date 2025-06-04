package audio;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.swing.SwingUtilities;

import model.MidiNote;

public class Player {
    private Synthesizer synthesizer;

    public Player() {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the audio data using the selected mixer
     * @throws LineUnavailableException
     *
     * @param mixerInfo the selected mixer
     * @param audioData the audio data to play
     * @param format the audio format of the data
     * @throws LineUnavailableException
     */
    public void play(Mixer.Info mixerInfo, byte[] audioData, AudioFormat format) throws LineUnavailableException {
        if (audioData == null) {
            System.err.println("Keine Audiodaten zum Abspielen vorhanden.");
            return;
        }

        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
        
        // Try to get the data line from the selected mixer to write the audio data to
        // We could use the default mixer if the selected one is not available
        try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(dataLineInfo)) {
             line.open(format);
             line.start();
             line.write(audioData, 0, audioData.length);
             line.drain(); // awaits the end of the audio data
        }
    }

    /**
     * Plays one or more notes using the synthesizer to generate an instrument sound.
     * For single notes, the duration is set to 1 second by default. In this case pass a list with one MidiNote.
     * 
     * @param notes a list of MidiNote objects to play
     * @param onPlaybackFinishedCallback a callback to execute when playback is finished to updatte UI and controller
     */
    public void playNotes(List<MidiNote> notes, Runnable onPlaybackFinishedCallback) {
        if (synthesizer == null || !synthesizer.isOpen()) {
            System.err.println("Synthesizer ist nicht verfügbar.");
            // still execute the callback to avoid deadlock in GUI
            if (onPlaybackFinishedCallback != null) {
                SwingUtilities.invokeLater(onPlaybackFinishedCallback);
            }
            return;
        }

        // Spawn a new thread to play the notes
        // This is necessary to avoid blocking the Event Dispatch Thread (EDT) in Swing applications
        new Thread(() -> {
            MidiChannel channel = null;
            try {
                MidiChannel[] channels = synthesizer.getChannels();
                if (channels == null || channels.length == 0) {
                    System.err.println("MelodyPlayer: Keine MIDI-Kanäle verfügbar.");
                    return; // Callback will be executed in this case due to the finally block
                }
                channel = channels[0];
                channel.programChange(0); // only set instrument once, e.g. Acoustic Grand Piano = 0

                for (MidiNote noteModel : notes) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("MelodyPlayer-Thread: Wiedergabe vorzeitig beendet (interrupted).");
                        break; // Exit the loop if the thread is interrupted
                    }

                    double frequency = noteModel.getFrequency();
                    int durationMs = (int) (noteModel.getDuration() * 1000); 
                    if (durationMs <= 0) continue; // ignore notes with non-positive duration

                    int midiNoteNumber = frequencyToMidiNote(frequency);
                    int velocity = 100;

                    System.out.println("MelodyPlayer-Thread: NoteOn (Note: " + midiNoteNumber + ", Freq: " + frequency + ", Dauer: " + durationMs + "ms)");
                    channel.noteOn(midiNoteNumber, velocity);
                    try {
                        Thread.sleep(durationMs);
                    } catch (InterruptedException e) {
                        System.out.println("MelodyPlayer-Thread: Sleep unterbrochen, spiele nächste Note nicht.");
                        channel.noteOff(midiNoteNumber); // turn of current note if sleep is interrupted
                        Thread.currentThread().interrupt();
                        break; 
                    }
                    // After the note duration, turn off the note
                    System.out.println("MelodyPlayer-Thread: NoteOff (Note: " + midiNoteNumber + ")");
                    channel.noteOff(midiNoteNumber);
                }
            } catch (Exception e) {
                 System.err.println("MelodyPlayer-Thread: Unerwarteter Fehler während der Wiedergabe: " + e.getMessage());
                 e.printStackTrace();
            }
            finally {
                // Important: Always turn off the channel to ensure no notes are left on
                if (channel != null) {
                    channel.allNotesOff();
                }

                if (onPlaybackFinishedCallback != null) {
                    SwingUtilities.invokeLater(onPlaybackFinishedCallback);
                }
            }
        }, "MelodyPlaybackThread-" + System.currentTimeMillis()).start();
    }

    /**
     * Converts a frequency to a MIDI note number
     * @param frequency the frequency in Hz
     * @return the MIDI note number
     */
    private int frequencyToMidiNote(double frequency) {
        if (frequency <= 0) return 0;
        // formula to convert frequency to MIDI note number
        /*
         * MIDI note number = 69 + 12 * log2(frequency / 440)
         * where 440 Hz is the frequency of MIDI note 69 (A4)
         */
        return (int) Math.round(12 * (Math.log(frequency / 440.0) / Math.log(2)) + 69);
    }

    /**
     * Releases the resources used by the synthesizer, should be called when player is not needed anymore
     */
    public void close() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }

    // This will probably be deleted, cause we wont neeed it. It would give us the ability to 
    // load nicer SoundFonts, but we can use the default ones for now.
    /**
     * Loads a SoundFont (.sf2) file into the synthesizer to generate nicer sounds
     * @param sf2Path the path to the SoundFont file
     *
     * @param sf2Path
     */
    public void loadSoundFont(String sf2Path) {
    if (synthesizer == null || !synthesizer.isOpen()) {
        System.err.println("Synthesizer ist nicht verfügbar.");
        return;
    }
    try {
        File sf2File = new File(sf2Path);
        if (!sf2File.exists()) {
            System.err.println("SoundFont nicht gefunden unter: " + sf2Path);
            return;
        }
        Soundbank soundbank = MidiSystem.getSoundbank(sf2File);
        synthesizer.loadAllInstruments(soundbank);
        System.out.println("SoundFont erfolgreich geladen!");
    } catch (InvalidMidiDataException | IOException e) {
        e.printStackTrace();
    }
}
}
