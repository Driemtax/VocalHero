// Authors: Inaas Hammoush
package controller;


import manager.*;
import model.LevelInfo;
import model.Level;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    private ProgressManager progressManager;

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
}
