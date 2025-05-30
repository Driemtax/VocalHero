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

        levelBuilder = new LevelBuilder(levelInfo);
        Level level = levelBuilder.buildLevel();
        audioManager = new AudioManager(level.getSelectedMic(), level.getSelectedSpeaker(), level.getReferenceNotes(), level.getRecordingDuration());
        feedbackManager = new FeedbackManager();
        progressManager = new ProgressManager();
    }

    public void startRecordingWithLivePitchGraph() {
        audioManager.startRecordingWithLivePitchGraph(pitch -> {
            //this could be moved to WindowController
            feedbackManager.updatePitchGraph(pitch);
        });
    }
}
