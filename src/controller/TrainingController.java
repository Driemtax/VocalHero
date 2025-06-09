// Authors: Inaas Hammoush, Lars Beer, David Herrmann
package controller;

import java.io.IOException;
import java.util.Collections;
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
import utils.FileUtils;

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
        audioManager = new AudioManager(AudioSettings.getInputDevice(), level.getReferenceNotes(), level.getRecordingDuration());
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
     * @param onTooQuiet Callback for notifying the UI that the audio is too quiet.
     */
    public boolean startRecordingWithLivePitchGraph(RecordingFinishedCallback updateUiAfterRecordingCallback, Runnable onTooQuiet) {

        return audioManager.startRecordingWithLivePitchGraph(
                pitch -> {
                    feedbackManager.updatePitchGraph(pitch);
                },
                onTooQuiet,
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
     * Saves the recorded audio to a file.
     * This method will be called by the WindowController to save the recording.
     * @param fileName 
     * @return true if the recording was saved successfully, false otherwise.
     */
    public boolean saveRecording(String fileName) {
        try {
            return audioManager.saveRecording(fileName);
        } catch (Exception e) {
            System.err.println("TrainingController: Fehler beim Speichern der Aufnahme: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns a list of available recordings.
     * This will be called by the WindowController to get the list of available recordings.
     * @return List of recording file names
     */
    public List<String> getAvailableRecordings() {
        return FileUtils.listRecordings();
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
        
        if (level.getReferenceNotes() == null || level.getReferenceNotes().isEmpty()) {
            System.err.println("TrainingController: Keine Referenznoten für das Level verfügbar.");
            if (updateUiAfterPlaybackCallback != null) {
                updateUiAfterPlaybackCallback.run(); // Reactivate UI buttons even if no notes are available
            }
            return false;
        }
        return audioManager.playReference(level.getReferenceNotes(), updateUiAfterPlaybackCallback);
    }

    /**
     * Plays the recorded audioData.
     */
    public void playRecordedAudio() {
        audioManager.playRecordedAudio();
    }

    /**
     * Plays a WAV file from the given byte array.
     * This method is used to play audio data that has been loaded from a file.
     * @param wavBytes byte array of the WAV file
     */
    public void playWavFile(String fileName) {
        byte[] wavBytes;
        try {
            wavBytes = FileUtils.loadRecordingFromWAV(fileName);
        } catch (Exception e) {
            System.err.println("TrainingController: Fehler beim Laden der WAV-Datei: " + e.getMessage());
            return;
        }

        // a new AudioManager is created to play the WAV file in case the page is accessed before the training session is started
        AudioManager audioManager = new AudioManager(AudioSettings.getOutputDevice(), null, 0);
        audioManager.playWavBytes(wavBytes);
    }


    public boolean deleteRecording(String fileName) {
        if (FileUtils.deleteRecording(fileName)) {
            System.out.println("TrainingController: Aufnahme erfolgreich gelöscht: " + fileName);
            return true;
        } else {
            System.err.println("TrainingController: Fehler beim Löschen der Aufnahme: " + fileName);
            return false;
        }
    }

    public List<Mixer.Info> getAvailableInputDevices() {
        return audioUtil.getAvailableMicrophones(AudioSettings.getRecorderFormat());
    }

    public List<Mixer.Info> getAvailableOutputDevices() {
        return audioUtil.getAvailableSpeakers(AudioSettings.getPlayerFormat());
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
