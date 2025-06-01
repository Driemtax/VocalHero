package audio;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.swing.SwingUtilities;

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
     * Plays a frequency using the synthesizer to generate an instrument sound
     * @param frequency
     * @param durationMs
     */
    public void playNote(double frequency, int durationMs, Runnable onPlaybackFinishedCallback) {
        if (synthesizer == null || !synthesizer.isOpen()) {
            System.err.println("Synthesizer ist nicht verfügbar.");
            // still execute the callback to avoid deadlock in GUI
            if (onPlaybackFinishedCallback != null) {
                SwingUtilities.invokeLater(onPlaybackFinishedCallback);
            }
            return;
        }

        new Thread(() -> {
            MidiChannel[] channels = synthesizer.getChannels();
            if (channels == null || channels.length == 0) {
                System.err.println("Player: Keine MIDI-Kanäle verfügbar.");
                 if (onPlaybackFinishedCallback != null) {
                    SwingUtilities.invokeLater(onPlaybackFinishedCallback);
                }
                return;
            }
            // Use Channel 0 and set the instrument to a default sound (e.g., 0=Acoustic Grand Piano)
            MidiChannel channel = channels[0];
            channel.programChange(0); // Instrument 0 (Piano)

            int midiNote = frequencyToMidiNote(frequency);
            int velocity = 100; // velocity describes how hard the key of the instrument is pressed

            try {
                System.out.println("Player-Thread: NoteOn (Note: " + midiNote + ", Freq: " + frequency + ")");
                channel.noteOn(midiNote, velocity);
                Thread.sleep(durationMs);
            } catch (InterruptedException e) {
                System.out.println("Player-Thread: Wiedergabe unterbrochen.");
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } finally {
                System.out.println("Player-Thread: NoteOff (Note: " + midiNote + ")");
                channel.noteOff(midiNote); // Important to turn off the note after the duration

                // Execute the callback on the Event Dispatch Thread (EDT) to update the UI
                if (onPlaybackFinishedCallback != null) {
                    SwingUtilities.invokeLater(onPlaybackFinishedCallback);
                }
            }
        }, "PlaybackThread-" + System.currentTimeMillis()).start();
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
