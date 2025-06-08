//Authors: Lars Beer, Inaas Hammoush, Jonas Rumpf
package controller;

import views.*;
import views.SplashScreen;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import manager.FeedbackManager;
import model.Feedback;
import model.LevelInfo;
import model.Level;
import model.Mode;
import model.RecordingFinishedCallback;
import model.MidiNote;
import model.MidiNote.*;

import java.util.List;

import javax.sound.sampled.*;

public class WindowController extends JFrame {
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
            // TODO: move showStartPanel to WindowController
            contentPanel.showStartPanel(); // Zeigt das StartPanel im ContentPanel
            cardLayout.show(rootPanel, APP);
        } else {
            System.err.println("Fehler: Haupt-UI nicht initialisiert, bevor showApp() aufgerufen wurde.");
        }
    }

    public void showAppFirst() {
        if (cardLayout != null && rootPanel != null && contentPanel != null) {
            showBaseVoiceRecordScreen(); // Zeigt das StartPanel im ContentPanel
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

        if (trainingController.isFirstStart()) {
            showAppFirst();
        } else {
            showHome();
        }
        setVisible(true);
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public void showSplashScreen() {
        // This is the Runnable that will be executed when the splash screen is
        // finished.
        Runnable onSplashScreenFinished = () -> {
            initAndShowMainApplicationUI();
        };

        // The SplashScreen will be shown and will call the onSplashScreenFinished
        // Runnable when it is done.
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
            contentPanel.add(new LevelSelectionPanel(this, mode, trainingController.getLevels(mode)),
                    BorderLayout.CENTER);
        } catch (IOException e) {
            // TODO show error
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

    public void showBaseVoiceRecordScreen() {

        contentPanel.removeAll();
        contentPanel.add(new BaseVoicePanel(this), BorderLayout.CENTER);
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
            System.err
                    .println("WindowController: TrainingController ist null. PitchListener kann nicht gesetzt werden.");
        }
    }

    /**
     * Starts the training session with the given level info.
     * This will be called by the HomeScreen to start a new training session.
     *
     * @param category The category of the level.
     * @param level    The level number.
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
     * This will be called by the LevelScreen to get the reference notes for the
     * current level.
     *
     * @return List of MidiNote objects representing the reference notes for the
     *         current level.
     */
    public List<MidiNote> getReferenceNotesForCurrentLevel() {
        if (trainingController != null) {
            return trainingController.getReferenceNotesForCurrentLevel();
        } else {
            System.err.println(
                    "WindowController: TrainingController ist null. Referenznoten können nicht abgerufen werden.");
            return List.of(); // Return an empty list if the controller is null
        }
    }

    /**
     * Starts the recording with a live pitch graph and notifies if audio is too
     * quiet.
     * This will be called by the LevelScreen to start the recording with live
     * feedback.
     *
     * @param updateUiAfterRecordingCallback Callback for UI update after recording.
     * @param onTooQuiet                     Callback for notifying the UI that the
     *                                       audio is too quiet.
     */
    public boolean startRecordingForLevel(RecordingFinishedCallback updateUiAfterRecordingCallback,
            Runnable onTooQuiet) {
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
     * @param level    The level number.
     * @param score    The score achieved in the level.
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
     * Plays the reference note for the current level.
     * This will be called by the LevelScreen to play the reference note.
     * The callback will be executed after the playback is finished.
     * 
     * @param updateUiAfterPlaybackCallback
     */
    public boolean playReference(Runnable updateUiAfterPlaybackCallback) {
        if (trainingController != null) {
            return trainingController.playReference(updateUiAfterPlaybackCallback);
        } else {
            System.err.println(
                    "WindowController: TrainingController ist null. Referenzton kann nicht abgespielt werden.");
            // reactivate UI buttons, even if the playback cannot be started
            if (updateUiAfterPlaybackCallback != null) {
                SwingUtilities.invokeLater(updateUiAfterPlaybackCallback);
            }

            return false;
        }
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void cleanup() {
        trainingController.cleanup();
    }

    public Note getPlayerVoice() {
        return trainingController.getPlayerVoice();
    }

    public void recordForBaseVoice(RecordingFinishedCallback finished) {
        trainingController.recordForBaseVoice(finished);
    }
}
