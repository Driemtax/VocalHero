// Authors: Inaas Hammoush, Lars Beer
package controller;


import java.util.List;

import javax.sound.sampled.Mixer;

import manager.*;
import model.LevelInfo;
import model.Mode;
import utils.AudioPreferences;
import model.AnalysisResult;
import model.Feedback;
import model.Level;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    Level level;
    private ProgressManager progressManager;
    private AnalysisResult analysisResult;

    public TrainingController() {
        // Initialize managers (we need the AudioManager before we start a training session)
        audioManager = new AudioManager(null, null, null, 0);
        feedbackManager = new FeedbackManager();
        progressManager = new ProgressManager();
    }

    public void startTrainingSession(LevelInfo levelInfo) {
        this.levelBuilder = new LevelBuilder(levelInfo);
        this.level = levelBuilder.buildLevel();
        audioManager = new AudioManager(level.getSelectedMic(), level.getSelectedSpeaker(), level.getReferenceNotes(), level.getRecordingDuration());
    }

    public void setPitchListener( PitchListener pitchListener) {
        feedbackManager.setPitchListener(pitchListener);
    }

    /**
     * Starts recording audio for a specified duration and provides live pitch graphing.
     * @param updateUiAfterRecordingCallback Callback for updating the UI after recording is complete.
     */
    public void startRecordingWithLivePitchGraph(Runnable updateUiAfterRecordingCallback) {

        audioManager.startRecordingWithLivePitchGraph(
        pitch -> {feedbackManager.updatePitchGraph(pitch);},
        () -> {
            // This callback is called when the recording is complete
            setLevelFeedback(); // Analyze the recorded audio and set feedback
            updateUiAfterRecordingCallback.run(); // Update the UI after recording
        });
    }

    public void setLevelFeedback() {
        if (level.getMode() == Mode.NOTE || level.getMode() == Mode.INTERVAL) {
            // For NOTE and INTERVAL modes, detect the pitch of the sung note
            double pitch = audioManager.detectPitchOfRecordedAudio();
            // set the Feedback Object in the Level object
            // TODO: Create a Feedback object based on the detected pitch
            level.setFeedback(feedbackManager.calculateFeedbackForRecordedNote(pitch, pitch)); // Placeholder for Feedback object, to be implemented later
        } else {
            // For MELODY mode, analyze the melody of the sung audio
            System.out.println("TrainingController: Analysing melody...");
            analysisResult = audioManager.analyzeMelody();
            // set the Feedback Object in the Level object
            // TODO: Create a Feedback object based on the analysis result 
            level.setFeedback(feedbackManager.calculateFeedbackForRecordedMelody(analysisResult)); // Placeholder for Feedback object, to be implemented later
        }
    }

    /**
     * Returns the Level Feedback (should be called by WindowController).
     * @return The current Level Feedback.
     */
    public Feedback getFeedback() {
        return level.getFeedback();
    }


    /**
     * Spielt den Referenzton für ein bestimmtes Level ab.
     * @param updateUiAfterPlaybackCallback Callback zur Aktualisierung der View nach der Wiedergabe.
     */
    public void playReference(Runnable updateUiAfterPlaybackCallback) {

        // The Level object contains a list of reference MidiNotes 
        // for now, the audioManager only plays one note and not the whole list

        if (level.getReferenceNotes() == null || level.getReferenceNotes().isEmpty()) {
            System.err.println("TrainingController: Keine Referenznoten für das Level verfügbar.");
            if (updateUiAfterPlaybackCallback != null) {
                updateUiAfterPlaybackCallback.run(); // Reactivate UI buttons even if no notes are available
            }
            return;
        }
        audioManager.playReference(level.getReferenceNotes(), updateUiAfterPlaybackCallback);
    }

    // TODO: remove these methods from AudioPreferences into a separate util class
    public List<Mixer.Info> getAvailableInputDevices() {
        return AudioPreferences.getAvailableMicrophones(audioManager.getFormat());
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        return AudioPreferences.getAvailableSpeakers(audioManager.getFormat());
    }

    // We should do this in constructor of training controller and use AudioSettings in the rest of Application
    // private void initializeAudioSettings() {
    //     System.out.println("Initialisiere Audio-Einstellungen...");
    //     Mixer.Info input = AudioPreferences.loadSavedInputDevice();
    //     if (input == null) {
    //         input = AudioSettings.getDefaultInputDevice();
    //     }

    //     Mixer.Info output = AudioPreferences.loadSavedOutputDevice();
    //     if (output == null) {
    //         output = AudioSettings.getDefaultOutputDevice();
    //     }

    //     AudioSettings.setInputDevice(input);
    //     AudioSettings.setOutputDevice(output);
    //     System.out.println("Audio-Einstellungen initialisiert.");
    //     // Hier könntest du auch den TrainingController informieren oder er könnte dies selbst tun.
    // }
}
