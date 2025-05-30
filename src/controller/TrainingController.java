// Authors: Inaas Hammoush, Lars Beer
package controller;


import java.util.List;

import javax.sound.sampled.Mixer;

import manager.*;
import model.LevelInfo;
import utils.AudioPreferences;
import model.AudioSettings;
import model.Level;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    private ProgressManager progressManager;

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

    public List<Mixer.Info> getAvailableInputDevices() {
        return audioManager.getAvailableMicrophones();
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        return audioManager.getAvailableSpeakers();
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
