// Authors: Inaas Hammoush, Lars Beer
package manager;

import javax.sound.sampled.*;
import audio.MelodyAnalyzer;
import audio.PitchDetector;
import audio.Player;
import audio.Recorder;
import model.MidiNote;
import model.AnalysisResult;
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
    private static final double SAMPLE_RATE = 44100.0;
    byte[] audioData;
    private AudioFormat format = new AudioFormat((float)SAMPLE_RATE, 16, 2, true, false);

    public AudioManager(Mixer.Info selectedMic, Mixer.Info selectedSpeaker, List<MidiNote> referenceNotes, int recordingDuration) {
        this.recordingDuration = recordingDuration;
        this.selectedMic = selectedMic;
        this.selectedSpeaker = selectedSpeaker;
        this.audioData = null; // initially null, will be set after recording
        this.recorder = new Recorder();
        this.player = new Player();
        this.pitchDetector = new PitchDetector(2048, SAMPLE_RATE);
        this.melodyAnalyzer = new MelodyAnalyzer(referenceNotes, SAMPLE_RATE, audioData);
    }

    /**
     * Starts recording audio for a specified duration and provides live pitch graphing.
     * The pitch is calculated using the PitchDetector and passed to the provided listener.
     * @param pitchListener
     * 
     * example usage in TrainingController:
     * 
     * audioManager.startRecordingWithLivePitch(3, pitch -> {
     *      feedbackManager.updatePitchGraph(pitch);
     *     });
     */
    public void startRecordingWithLivePitchGraph(Consumer<Double> pitchListener) {
        recorder.setAudioChunkListener(chunk -> {
            double pitch = pitchDetector.getDominantFrequency(chunk);
            pitchListener.accept(pitch);
        });

        try {
            recorder.startRecording(recordingDuration, selectedMic);
            audioData = recorder.getAudioData();
        } catch (LineUnavailableException e) {
            // TODO: Handle exception through GUI
            e.printStackTrace();
        }
    }

    /**
     * Starts recording audio for a specified duration.
     * @param duration
     */
    public void startRecording(int duration) {
        try {
            recorder.startRecording(duration, selectedMic);
            audioData = recorder.getAudioData();
        } catch (LineUnavailableException e) {
            // TODO: Handle exception through GUI
            e.printStackTrace();
        }
    }

    /**
     * Stops the current recording.
     */
    public void stopRecording() {
        recorder.stopRecording();
    }

    /**
     * Plays the recorded audio data.
     */
    public void playAudio() {
        try {
            player.play(selectedSpeaker, audioData, format);
        } catch (LineUnavailableException e) {
            // TODO: Handle exception through GUI
            e.printStackTrace();
        }
    }

    /**
     * Analyzes the recorded audio data using the MelodyAnalyzer.
     * It compares the detected notes with the reference notes and returns the analysis result.
     * @return AnalysisResult
     */
    public AnalysisResult analyzeMelody() {
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
    public double detectPitch() {
        try {
            return pitchDetector.getDominantFrequency(audioData);
        } catch (Exception e) {
            // TODO: Handle exception appropriately
            e.printStackTrace();
            return -1;
        }
    }

    public List<Mixer.Info> getAvailableMicrophones() {
        return recorder.getAvailableMicrophones();
    }

    public List<Mixer.Info> getAvailableSpeakers() {
        return player.getAvailableSpeakers(format);
    }
}
