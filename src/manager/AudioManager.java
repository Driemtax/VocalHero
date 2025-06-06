// Authors: Inaas Hammoush, Lars Beer
package manager;

import javax.sound.sampled.*;
import javax.swing.SwingUtilities;

import audio.MelodyAnalyzer;
import audio.PitchDetector;
import audio.Player;
import audio.Recorder;
import model.MidiNote;
import model.RecordingFinishedCallback;
import model.AnalysisResult;
import model.AudioSettings;

import java.util.List;
import java.util.function.Consumer;


/**
 * AudioManager is responsible for managing audio recording, playback, and analysis.
 * It uses the Recorder and Player classes to handle audio input and output,
 * and the PitchDetector and MelodyAnalyzer classes for analyzing the recorded audio.
 * 
 * @author Inaas Hammoush
 * @version 1.0
 */
public class AudioManager {
    
    private Mixer.Info selectedMic;
    private Mixer.Info selectedSpeaker;
    private int recordingDuration;
    private Recorder recorder;
    private Player player;
    private PitchDetector pitchDetector;
    private MelodyAnalyzer melodyAnalyzer;
    private double SAMPLE_RATE;
    byte[] audioData;
    private List<MidiNote> referenceNotes;
    private AudioFormat format;

    public AudioManager(Mixer.Info selectedMic, Mixer.Info selectedSpeaker, List<MidiNote> referenceNotes, int recordingDuration) {
        this.recordingDuration = recordingDuration;
        this.selectedMic = selectedMic;
        this.selectedSpeaker = selectedSpeaker;
        this.audioData = null; // initially null, will be set after recording
        this.recorder = new Recorder();
        this.player = new Player();
        this.SAMPLE_RATE = AudioSettings.getSampleRate();
        this.format = AudioSettings.getFormat();
        this.pitchDetector = new PitchDetector(2048, SAMPLE_RATE);
        this.referenceNotes = referenceNotes;

    }

    public AudioFormat getFormat() {
        return format;
    }

    /**
     * Starts recording audio for a specified duration and provides live pitch graphing.
     * The pitch is calculated using the PitchDetector and passed to the provided listener.
     * @param pitchListener
     * 
     * example usage in TrainingController:
     * 
     * audioManager.startRecordingWithLivePitch(pitch -> {
     *      feedbackManager.updatePitchGraph(pitch);
     *     });
     */
    public boolean startRecordingWithLivePitchGraph(Consumer<Double> pitchListener, RecordingFinishedCallback updateUiAfterRecordingCallback) {
        recorder.setAudioChunkListener(chunk -> {
            double pitch = pitchDetector.getDominantFrequency(chunk);
            pitchListener.accept(pitch);
        });

        boolean success = startRecording(updateUiAfterRecordingCallback);
        return success;

    }

    /**
     * Starts recording audio for a specified duration.
     */
    public boolean startRecording(RecordingFinishedCallback updateUiAfterRecordingCallback) {
        if (recorder == null) {
            System.err.println("AudioManager: Recorder ist null!");
            // Still need to invoke the callback to avoid deadlock in GUI
            if (updateUiAfterRecordingCallback != null) SwingUtilities.invokeLater(
                () -> updateUiAfterRecordingCallback.onRecordingFinished(false));
            return false;
        }
        if (selectedMic == null) {
            System.err.println("AudioManager: Kein Mikrofon ausgewählt!");
             if (updateUiAfterRecordingCallback != null) SwingUtilities.invokeLater(
                () -> updateUiAfterRecordingCallback.onRecordingFinished(false));
            return false;
        }
        if (format == null) { // Zusätzlicher Check
            System.err.println("AudioManager: AudioFormat ist nicht gesetzt!");
            if (updateUiAfterRecordingCallback != null) SwingUtilities.invokeLater(
                () -> updateUiAfterRecordingCallback.onRecordingFinished(false));
            return false;
        }
        try {
            return recorder.startRecording(recordingDuration, selectedMic, (boolean success) -> {
                // This callback is executed after the recording is finished
                audioData = recorder.getAudioData();
                // Execute the UI callback when processing is done
                if (updateUiAfterRecordingCallback != null) {
                    System.out.println("TrainingController: Führe UI-Update-Callback aus der View aus.");
                    updateUiAfterRecordingCallback.onRecordingFinished(success); 
                }
            });
        } catch (LineUnavailableException e) {
            // TODO: Handle exception through GUI
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stops the current recording.
     */
    public void stopRecording() {
        recorder.stopRecording();
    }

    /**
     * Gets the lastest recorded audio data. This will either be called after a recording is finished
     * or in between recordings to get the latest audio data.
     * @return byte[] of the audio data
     */
    public byte[] getRecordedAudioData() {
        if (recorder == null) return new byte[0];
        return recorder.getAudioData();
    }

    /**
     * Plays the recorded audio data.
     */
    public void playAudio() {
        try {
            player.play(audioData);
        } catch (LineUnavailableException e) {
            // TODO: Handle exception through GUI
            e.printStackTrace();
        }
    }

    /**
     * Plays a frequency using the synthesizer to generate an instrument sound.
     * @param referenceNotes List of reference notes to play
     * @param onPlaybackFinishedCallback callback to execute when playback is finished
     */
    public boolean playReference(List<MidiNote> referenceNotes, Runnable updateUiAfterPlaybackCallback) {
        if (player == null) {
            System.err.println("AudioManager: Player ist nicht initialisiert!");
            // still execute the callback to avoid deadlock in GUI
            if (updateUiAfterPlaybackCallback != null) {
                SwingUtilities.invokeLater(updateUiAfterPlaybackCallback);
            }
            return false;
        }
        // System.out.println("AudioManager: Spiele Referenznote (Freq: " + frequency + ", Dauer: " + durationMs + "ms)");
        return player.playNotes(referenceNotes, updateUiAfterPlaybackCallback);
    }

    /**
     * Analyzes the recorded audio data using the MelodyAnalyzer.
     * It compares the detected notes with the reference notes and returns the analysis result.
     * @return AnalysisResult
     */
    public AnalysisResult analyzeMelody() {
        this.melodyAnalyzer = new MelodyAnalyzer(referenceNotes, SAMPLE_RATE, audioData);
        try {
            return melodyAnalyzer.analyze();
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
            return null;
        }
        
    }

    /**
     * Detects the pitch of the recorded audio data using the PitchDetector.
     * @return double dominant frequency
     */
    public double detectPitchOfRecordedAudio() {
        if (audioData == null || audioData.length == 0) {
            System.err.println("AudioManager: Keine Audiodaten zum Analysieren vorhanden.");
            return -1; 
        }
        
        try {
            // TODO: Improve accuracy
            // Right now, even if I sing a note all over the place (e.g. I cant hold it steady)
            // it still returns me an excellent result with the dominant frequency.
            return pitchDetector.getDominantFrequency(audioData);
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
            return -1;
        }
    }

    // Beim Beenden der Anwendung den Player-Synthesizer schließen
    public void cleanup() {
        if (player != null) {
            player.close();
        }
    }
}
