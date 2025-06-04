// Authors: Inaas Hammoush, Lars Beer
package controller;


import java.util.List;

import javax.sound.sampled.Mixer;

import manager.*;
import model.LevelInfo;
import model.Mode;
import model.PitchListener;
import utils.AudioPreferences;
import model.AnalysisResult;
import model.Feedback;
import model.Level;
import model.AudioSettings;
import utils.AudioUtil;
import utils.Helper;

public class TrainingController {
    private AudioManager audioManager;
    private FeedbackManager feedbackManager;
    private LevelBuilder levelBuilder;
    Level level;
    private ProgressManager progressManager;
    private AnalysisResult analysisResult;
    private static AudioUtil audioUtil = new AudioUtil();

    public TrainingController() {
        initializeAudioSettings(); // Load saved audio settings or set defaults
        progressManager = new ProgressManager();
    }

    public void startTrainingSession(LevelInfo levelInfo) {
        this.levelBuilder = new LevelBuilder(levelInfo);
        this.level = levelBuilder.buildLevel();
        // RecordingDuration still needs to be set in the Level object (default is 3 seconds and more for melodies) 
        audioManager = new AudioManager(AudioSettings.getInputDevice(), AudioSettings.getOutputDevice(), level.getReferenceNotes(), level.getRecordingDuration());
        feedbackManager = new FeedbackManager(level.getReferenceNotes());
    }

    public void setPitchListener( PitchListener pitchListener) {
        feedbackManager.setPitchListener(pitchListener);
    }

    /**
     * Starts recording audio for a specified duration and provides live pitch graphing.
     * @param updateUiAfterRecordingCallback Callback for updating the UI after recording is complete.
     */
    public void startRecordingWithLivePitchGraph(Runnable updateUiAfterRecordingCallback) {
        // Get the target frequency from the first reference note
        double targetFrequency = level.getReferenceNotes().get(0).getFrequency();

        audioManager.startRecordingWithLivePitchGraph(
        pitch -> {
            // Convert frequency to cent offset before updating the pitch graph
            double centOffset = Helper.frequencyToCentOffset(pitch, targetFrequency);
            feedbackManager.updatePitchGraph(centOffset);
        },
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
            System.out.println("TrainingController: Detected pitch: " + pitch);
            // set the Feedback Object in the Level object
            level.setFeedback(feedbackManager.calculateFeedbackForRecordedNote(pitch, level.getReferenceNotes().get(0).getFrequency()));
        } else {
            // For MELODY mode, analyze the melody of the sung audio
            System.out.println("TrainingController: Analysing melody...");
            analysisResult = audioManager.analyzeMelody();
            // set the Feedback Object in the Level object
            level.setFeedback(feedbackManager.calculateFeedbackForRecordedMelody(analysisResult));
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

    public List<Mixer.Info> getAvailableInputDevices() {
        return audioUtil.getAvailableMicrophones(audioManager.getFormat());
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        return audioUtil.getAvailableSpeakers(audioManager.getFormat());
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
        audioManager = new AudioManager(input, output, null, 3); // Default recording duration of 3 seconds
        System.out.println("Audio-Einstellungen initialisiert.");
    }
}
