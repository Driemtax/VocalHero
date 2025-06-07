//Authors: Lars Beer, Inaas Hammoush, Jonas Rumpf
package controller;

import views.*;
import views.SplashScreen;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import model.Feedback;
import model.LevelInfo;
import model.Level;
import model.Mode;
import model.RecordingFinishedCallback;
import model.MidiNote;

import java.util.List;

import javax.sound.sampled.*;


public class WindowController extends JFrame{
    private TrainingController trainingController;

    private SplashScreen splashScreen;
    private Level currentLevel;

    private JPanel rootPanel;
    private CardLayout cardLayout;
    private ContentPanel contentPanel;

    public static final String HOME = "home";
    public static final String APP = "app";
    
    public WindowController(TrainingController controller) {
        this.trainingController = controller;
    }

    public void showHome() {
        if (cardLayout != null && rootPanel != null) {
            cardLayout.show(rootPanel, HOME);
        } else {
            System.err.println("Fehler: Haupt-UI nicht initialisiert, bevor showHome() aufgerufen wurde.");
        }
    }

    public void showApp() {
        if (cardLayout != null && rootPanel != null && contentPanel != null) {
            contentPanel.showStartPanel(); // Zeigt das StartPanel im ContentPanel
            cardLayout.show(rootPanel, APP);
        } else {
            System.err.println("Fehler: Haupt-UI nicht initialisiert, bevor showApp() aufgerufen wurde.");
        }
    }

    private void initAndShowMainApplicationUI() {
        setTitle("Vocal Hero");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);
        contentPanel = new ContentPanel(this);

        // HomeScreen ohne Sidebar
        HomeScreen homeScreen = new HomeScreen(this);
        Sidebar sidebar = new Sidebar(contentPanel, this);
        JPanel appPanel = new JPanel(new BorderLayout());
        appPanel.add(sidebar, BorderLayout.WEST);
        appPanel.add(contentPanel, BorderLayout.CENTER);

        rootPanel.add(homeScreen, HOME);
        rootPanel.add(appPanel, APP);

        setContentPane(rootPanel);

        showHome();
        setVisible(true);
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public void showSplashScreen() {
        // This is the Runnable that will be executed when the splash screen is finished.
        Runnable onSplashScreenFinished = () -> {
            initAndShowMainApplicationUI();
        };
        
        // The SplashScreen will be shown and will call the onSplashScreenFinished Runnable when it is done.
        splashScreen = new SplashScreen(onSplashScreenFinished);
    }

    public void showHomeScreen() {

    }

    public void showLevelScreen(Mode mode, int level) {
        // We need to notify trainingController about the level to start here
        LevelInfo levelInfo = new LevelInfo(level, mode);
        startTrainingSession(levelInfo);
        
        contentPanel.removeAll();
        contentPanel.add(new LevelScreen(this, mode, level), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showSettingsScreen() {
        contentPanel.removeAll();
        contentPanel.add(new SettingsScreen(this), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showCategoryScreen() {
        contentPanel.removeAll();
        contentPanel.add(new CategoryScreen(contentPanel), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showLevelSelection(Mode mode) {
        contentPanel.removeAll();
        try {
            contentPanel.add(new LevelSelectionPanel(this, mode ,trainingController.getLevels(mode)), BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showProgressScreen() {
        contentPanel.removeAll();
        contentPanel.add(new ProgressPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showTutorialsScreen() {
        contentPanel.removeAll();
        contentPanel.add(new TutorialsScreen(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showRecordingsScreen() {
        contentPanel.removeAll();
        contentPanel.add(new RecordingsPanel(this), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public String[] getAudioInputDevices() {
        List<Mixer.Info> inputDevices = trainingController.getAvailableInputDevices();

        String[] inputDeviceNames = new String[inputDevices.size()];
        for (int i = 0; i < inputDevices.size(); i++) {
            inputDeviceNames[i] = inputDevices.get(i).getName();
        }
        
        return inputDeviceNames;
    }

    public String[] getAudioOutputDevices() {
        List<Mixer.Info> mixers = trainingController.getAvailableOutputDevices();

        String[] outputDeviceNames = new String[mixers.size()];
        for (int i = 0; i < mixers.size(); i++) {
            outputDeviceNames[i] = mixers.get(i).getName();
        }
        return outputDeviceNames;
    }

    public void setPitchListener(PitchGraphPanel pitchListener) {
        if (trainingController != null) {
            trainingController.setPitchListener(pitchListener::addPitchValue);
        } else {
            System.err.println("WindowController: TrainingController ist null. PitchListener kann nicht gesetzt werden.");
        }
    }


    /**
     * Starts the training session with the given level info.
     * This will be called by the HomeScreen to start a new training session.
     *
     * @param category The category of the level.
     * @param level The level number.
     */
    public void startTrainingSession(LevelInfo levelInfo) {
        if (trainingController != null) {
            trainingController.startTrainingSession(levelInfo);
            this.currentLevel = trainingController.getLevel();
        } else {
            System.err.println("WindowController: TrainingController ist null. Training kann nicht gestartet werden.");
        }
    }   
    
    /**
     * Returns the reference notes for the current level.
     * This will be called by the LevelScreen to get the reference notes for the current level.
     *
     * @return List of MidiNote objects representing the reference notes for the current level.
     */
    public List<MidiNote> getReferenceNotesForCurrentLevel() {
        if (trainingController != null) {
            return trainingController.getReferenceNotesForCurrentLevel();
        } else {
            System.err.println("WindowController: TrainingController ist null. Referenznoten können nicht abgerufen werden.");
            return List.of(); // Return an empty list if the controller is null
        }
    }

    /**
     * Starts the recording with a live pitch graph and notifies if audio is too quiet.
     * This will be called by the LevelScreen to start the recording with live feedback.
     *
     * @param updateUiAfterRecordingCallback Callback for UI update after recording.
     * @param onTooQuiet Callback for notifying the UI that the audio is too quiet.
     */
    public boolean startRecordingForLevel(RecordingFinishedCallback updateUiAfterRecordingCallback, Runnable onTooQuiet) {
        boolean success = false;
        if (trainingController != null) {
            success = trainingController.startRecordingWithLivePitchGraph(updateUiAfterRecordingCallback, onTooQuiet);
            return success;
        } else {
            final boolean finalSuccess = success;
            System.err.println("WindowController: TrainingController ist null. Aufnahme kann nicht gestartet werden.");
            // reactivate UI buttons, even if the recording cannot be started
            if (updateUiAfterRecordingCallback != null) {
                SwingUtilities.invokeLater(
                    () -> updateUiAfterRecordingCallback.onRecordingFinished(finalSuccess));
            }
            return false;
        }
    }

    /**
     * Shows the results screen with the given category, level, and score.
     * This will be called by the LevelScreen to show the results after recording.
     *
     * @param category The category of the level.
     * @param level The level number.
     * @param score The score achieved in the level.
     */
    public void showResults(Mode mode, int level) {
        Feedback feedback = trainingController.getFeedback();
        System.out.println("Score: " + feedback.score());
        System.out.println("Feedback Message: " + feedback.getFeedbackMessage());
        System.out.println("Feedback Medal: " + feedback.getFeedbackMedal());
        FeedbackPanel feedbackPanel = new FeedbackPanel(feedback, mode, level, this);
        contentPanel.removeAll();
        contentPanel.add(feedbackPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Saves the recording to a file.
     * This will be called by the FeedbackPanel to save the recording after the training session.
     * 
     * @return true if the recording was saved successfully, false otherwise.
     */
    public boolean saveRecording() {
        if (trainingController != null) {
            try {
                return trainingController.saveRecording(createRecordingFileName());
            } catch (Exception e) {
                System.err.println("WindowController: Fehler beim Speichern der Aufnahme: " + e.getMessage());
                return false;
            }
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufnahme kann nicht gespeichert werden.");
            return false;
        }
    }

    /**
     * Creates a unique filename for the recording based on the current level and timestamp.
     * The filename will be in the format: level_<levelNumber>_<mode>_<timestamp>.wav
     * where <timestamp> is formatted as yyyy-MM-dd_HH-mm-ss to ensure it is safe for filenames.
     *
     * @return A string representing the unique filename for the recording.
     */
    public String createRecordingFileName() {
        Instant now = Instant.now();

        // Format Instant to a safe filename
        String safeTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
                .withZone(ZoneId.systemDefault())
                .format(now);

        return "level_" + currentLevel.getLevelNumber() + "_" + currentLevel.getMode().toString() + "_" + safeTimestamp + ".wav";
    }

    /**
     * Returns a list of saved recordings.
     * This will be called by the RecordingsPanel to get the list of saved recordings.
     * @return List of recording file names
     */
    public List<String> getSavedRecordings() {
        if (trainingController != null) {
            return trainingController.getAvailableRecordings();
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufnahmen können nicht abgerufen werden.");
            return List.of(); // Return an empty list if the controller is null
        }
    }

    /**
     * Plays the reference note for the current level.
     * This will be called by the LevelScreen to play the reference note.
     * The callback will be executed after the playback is finished.
     * @param updateUiAfterPlaybackCallback
     */
    public boolean playReference(Runnable updateUiAfterPlaybackCallback) {
        if (trainingController != null) {
            return trainingController.playReference(updateUiAfterPlaybackCallback);
        } else {
            System.err.println("WindowController: TrainingController ist null. Referenzton kann nicht abgespielt werden.");
            // reactivate UI buttons, even if the playback cannot be started
            if (updateUiAfterPlaybackCallback != null) {
                SwingUtilities.invokeLater(updateUiAfterPlaybackCallback);
            }

            return false;
        }
    }

    /**
     * Plays the recorded audio data.
     * This will be called by the FeedbackPanel to play the recorded audio after a recording is finished.
     */
    public void playRecordedAudio() {
        if (trainingController != null) {
            trainingController.playRecordedAudio();
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufgenommene Audiodaten können nicht abgespielt werden.");
        }
    }

    /**
     * Plays a WAV file from the given file name.
     * @param fileName
     */
    public void playWavFile(String fileName) {
        if (trainingController != null) {
            trainingController.playWavFile(fileName);
        } else {
            System.err.println("WindowController: TrainingController ist null. WAV-Daten können nicht abgespielt werden.");
        }
    }

    /**
     * Deletes a recording file.
     * This will be called by the RecordingsPanel to delete a recording.
     * @param fileName
     */
    public boolean deleteRecording(String fileName) {
        if (trainingController != null) {
            return trainingController.deleteRecording(fileName);
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufnahme kann nicht gelöscht werden.");
            return false;
        }
    }

    /**
     * Returns the current level of the training session.
     *
     * @return The current Level object.
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void cleanup() {
        trainingController.cleanup();
    }
}
