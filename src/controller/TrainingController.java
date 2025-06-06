// Authors: Inaas Hammoush, Lars Beer, David Herrmann
package controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.Mixer;

import manager.*;
import model.LevelInfo;
import model.LevelState;
import model.MidiNote;
import model.Mode;
import model.PitchListener;
import model.RecordingFinishedCallback;
import utils.AudioPreferences;
import model.AnalysisResult;
import model.Feedback;
import model.Level;
import model.AudioSettings;
import utils.AudioUtil;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    Level level;
    LevelInfo levelInfo;
    private ProgressManager progressManager;
    private AnalysisResult analysisResult;
    private static AudioUtil audioUtil = new AudioUtil();

    public TrainingController() {
        initializeAudioSettings(); // Load saved audio settings or set defaults
        progressManager = new ProgressManager();
    }

    public void startTrainingSession(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;
        this.levelBuilder = new LevelBuilder(levelInfo);
        this.level = levelBuilder.buildLevel();
        // RecordingDuration still needs to be set in the Level object (default is 3
        // seconds and more for melodies)
        audioManager = new AudioManager(AudioSettings.getInputDevice(), AudioSettings.getOutputDevice(),
                level.getReferenceNotes(), level.getRecordingDuration());
        feedbackManager = new FeedbackManager(level.getReferenceNotes());
    }

    public void setPitchListener(PitchListener pitchListener) {
        feedbackManager.setPitchListener(pitchListener);
    }

    /**
     * Starts recording audio for a specified duration and provides live pitch
     * graphing.
     * 
     * @param updateUiAfterRecordingCallback Callback for updating the UI after
     *                                       recording is complete.
     */
    public boolean startRecordingWithLivePitchGraph(RecordingFinishedCallback updateUiAfterRecordingCallback) {

        return audioManager.startRecordingWithLivePitchGraph(
                pitch -> {
                    feedbackManager.updatePitchGraph(pitch);
                },
                (boolean success) -> {
                    // This callback is called when the recording is complete
                    setLevelFeedback(); // Analyze the recorded audio and set feedback
                    updateUiAfterRecordingCallback.onRecordingFinished(success);; // Update the UI after recording
                });
    }

    public void setLevelFeedback() {
        if (level.getMode() == Mode.NOTE || level.getMode() == Mode.INTERVAL) {
            // For NOTE and INTERVAL modes, detect the pitch of the sung note
            double pitch = audioManager.detectPitchOfRecordedAudio();
            System.out.println("TrainingController: Detected pitch: " + pitch);
            // set the Feedback Object in the Level object
            if (level.getMode() == Mode.INTERVAL) {
                // For INTERVAL mode, we need to compare the pitch with the target interval note
                double targetFrequency = level.getTargetIntervalNote().getFrequency();
                System.out.println("TrainingController: Target frequency for interval: " + targetFrequency);
                level.setFeedback(feedbackManager.calculateFeedbackForRecordedNote(pitch, targetFrequency));
            } else {
                // For NOTE mode, we compare with the reference note
                double referenceFrequency = level.getReferenceNotes().get(0).getFrequency();
                System.out.println("TrainingController: Target frequency for note: " + referenceFrequency);
                level.setFeedback(feedbackManager.calculateFeedbackForRecordedNote(pitch, referenceFrequency));
            }
        } else {
            // For MELODY mode, analyze the melody of the sung audio
            System.out.println("TrainingController: Analysing melody...");
            analysisResult = audioManager.analyzeMelody();
            // set the Feedback Object in the Level object
            level.setFeedback(feedbackManager.calculateFeedbackForRecordedMelody(analysisResult));
        }
        progressManager.updateLevel(levelInfo.getLevelNumber(), level.getMode(), level.getFeedback());
    }

    /**
     * Returns the Level Feedback (should be called by WindowController).
     * 
     * @return The current Level Feedback.
     */
    public Feedback getFeedback() {
        return level.getFeedback();
    }

    public Level getLevel() {
        return level;
    }

    /**
     * Returns the reference notes for the current level.
     * This will be called by the LevelScreen to get the reference notes for the current level.
     *
     * @return List of MidiNote objects representing the reference notes for the current level.
     */
    public List<MidiNote> getReferenceNotesForCurrentLevel() {
        if (level != null && level.getReferenceNotes() != null) {
            return level.getReferenceNotes();
        } else {
            System.err.println("TrainingController: Keine Referenznoten für das aktuelle Level verfügbar.");
            return List.of(); // Return an empty list if no notes are available
        }
    }

    /**
     * Spielt den Referenzton für ein bestimmtes Level ab.
     * 
     * @param updateUiAfterPlaybackCallback Callback zur Aktualisierung der View
     *                                      nach der Wiedergabe.
     */
    public boolean playReference(Runnable updateUiAfterPlaybackCallback) {

        // The Level object contains a list of reference MidiNotes
        // for now, the audioManager only plays one note and not the whole list

        if (level.getReferenceNotes() == null || level.getReferenceNotes().isEmpty()) {
            System.err.println("TrainingController: Keine Referenznoten für das Level verfügbar.");
            if (updateUiAfterPlaybackCallback != null) {
                updateUiAfterPlaybackCallback.run(); // Reactivate UI buttons even if no notes are available
            }
            return false;
        }
        return audioManager.playReference(level.getReferenceNotes(), updateUiAfterPlaybackCallback);
    }

    public List<Mixer.Info> getAvailableInputDevices() {
        return audioUtil.getAvailableMicrophones(AudioSettings.getFormat());
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        return audioUtil.getAvailableSpeakers(AudioSettings.getFormat());
    }

    private void initializeAudioSettings() {
        System.out.println("Initialisiere Audio-Einstellungen...");
        Mixer.Info input = AudioPreferences.loadSavedInputDevice();
        if (input == null) {
            input = audioUtil.getDefaultInputDevice();
        }

        Mixer.Info output = AudioPreferences.loadSavedOutputDevice();
        if (output == null) {
            output = audioUtil.getDefaultOutputDevice();
        }

        AudioSettings.setInputDevice(input);
        AudioSettings.setOutputDevice(output);
        System.out.println("Audio-Einstellungen initialisiert.");
    }

    public List<LevelState> getLevels(Mode mode) throws IOException {
        try {
            List<LevelState> levels = progressManager.parseLevels();
            Iterator<LevelState> iterator = levels.iterator();
            while (iterator.hasNext()) {
                LevelState levelState = iterator.next();
                if (levelState.mode() != mode) {
                    iterator.remove();
                }
            }
            Collections.sort(levels);
            return levels;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * call this function on exiting the programm to close the current synthesizer to avoid weird states
     */
    public void cleanup() {
        if (audioManager != null) {
            audioManager.cleanup();
        }
            
    }
}
