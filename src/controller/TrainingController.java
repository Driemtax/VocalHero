// Authors: Inaas Hammoush, Lars Beer
package controller;


import java.util.List;

import javax.sound.sampled.Mixer;

import manager.*;
import model.LevelInfo;
import utils.AudioPreferences;
import model.Level;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    private ProgressManager progressManager;
    private byte[] audioData;

    public TrainingController() {
        // Initialize managers (we need this way before we start a training session)
        audioManager = new AudioManager(null, null, null, 0);
        feedbackManager = new FeedbackManager();
        levelBuilder = new LevelBuilder();
        progressManager = new ProgressManager();
    }

    public void startTrainingSession(LevelInfo levelInfo) {

        levelBuilder = new LevelBuilder();
        Level level = levelBuilder.buildLevel(levelInfo);
        audioManager = new AudioManager(levelInfo.getSelectedMic(), levelInfo.getSelectedSpeaker(), level.getReferenceNotes(), levelInfo.getRecordingDuration());
        feedbackManager = new FeedbackManager();
        progressManager = new ProgressManager();
    }

    public void startRecordingWithLivePitchGraph() {
        audioManager.startRecordingWithLivePitchGraph(pitch -> {
            feedbackManager.updatePitchGraph(pitch);
        });
    }

    public void startRecording(Runnable onRecordingFinishedCallback) {
        // This callback will be executed when the recording is finished.
        Runnable actionsAfterRecordingIsComplete = () -> {
            System.out.println("TrainingController: Aufnahme tatsächlich beendet (im EDT Kontext). Hole Audiodaten.");
            byte[] recordedData = audioManager.getRecordedAudioData();
            System.out.println("TrainingController: " + recordedData.length + " Bytes aufgenommen.");
            this.audioData = recordedData;

            // TODO: Process audio data here, e.g. analyze it or save it.

            // Execute the UI callback when processing is done
            if (onRecordingFinishedCallback != null) {
                System.out.println("TrainingController: Führe UI-Update-Callback aus der View aus.");
                onRecordingFinishedCallback.run(); 
            }
        };
        
        // actually start the recording
        audioManager.startRecording(3, actionsAfterRecordingIsComplete);
    }

    /**
     * Spielt den Referenzton für ein bestimmtes Level ab.
     * @param uiUpdateCallbackFromView Callback zur Aktualisierung der View nach der Wiedergabe.
     */
    public void playReferenceNote(Runnable uiUpdateCallbackFromView) {
        // This callback will be executed when the playback is complete.
        Runnable actionsAfterPlaybackIsComplete = () -> {
            if (uiUpdateCallbackFromView != null) {
                uiUpdateCallbackFromView.run();
            }
        };

        // TODO: Pass actual frequency and duration from the level object
        audioManager.playReferenceNote(440.0, 1, actionsAfterPlaybackIsComplete);
    }

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
