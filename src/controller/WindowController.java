//Authors: Lars Beer, Inaas Hammoush, Jonas Rumpf
package controller;

import views.*;
import views.SplashScreen;

import java.awt.*;
import javax.swing.*;

import manager.FeedbackManager;
import model.Feedback;
import model.LevelInfo;
import model.Mode;

import java.util.List;
import java.util.logging.Level;

import javax.sound.sampled.*;


public class WindowController extends JFrame{
    private TrainingController trainingController;

    private SplashScreen splashScreen;
    private HomeScreen homeScreen;
    private LevelScreen levelScreen;
    private SettingsScreen settingsScreen;

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

    //TODO: need to somehow notify trainingController and LevelBuilder about training method and level
    public void showLevelScreen(String category, int level) {
        LevelInfo levelInfo = new LevelInfo(level, Mode.fromString(category));
        trainingController.startTrainingSession(levelInfo); // Start the training session with the given level info
        contentPanel.removeAll();
        contentPanel.add(new LevelScreen(this, category, level), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showSettingsScreen() {
        contentPanel.removeAll();
        contentPanel.add(new SettingsScreen(this), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showLevelSelection(String category) {
        removeAll();
        add(new LevelSelectionPanel(this, category), BorderLayout.CENTER);
        revalidate();
        repaint();
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
            // showLevelScreen(category, level);
        } else {
            System.err.println("WindowController: TrainingController ist null. Training kann nicht gestartet werden.");
        }
    }    

    /**
     * Starts the recording with a live pitch graph.
     * This will be called by the LevelScreen to start the recording with live feedback.
     *
     * @param updateUiAfterRecordingCallback
     */
    public void startRecordingForLevel(Runnable updateUiAfterRecordingCallback) {
        if (trainingController != null) {
            trainingController.startRecordingWithLivePitchGraph(updateUiAfterRecordingCallback);
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufnahme kann nicht gestartet werden.");
            // reactivate UI buttons, even if the recording cannot be started
            if (updateUiAfterRecordingCallback != null) {
                SwingUtilities.invokeLater(updateUiAfterRecordingCallback);
            }
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
    public void showResults() {
        Feedback feedback = trainingController.getFeedback();
        System.out.println("Score: " + feedback.score());
        System.out.println("Feedback Message: " + feedback.getFeedbackMessage());
        System.out.println("Feedback Medal: " + feedback.getFeedbackMedal());
    //     contentPanel.removeAll();
    //     contentPanel.add(new ResultScreen(this, category, level, score), BorderLayout.CENTER);
    //     contentPanel.revalidate();
    //     contentPanel.repaint();
     }

    /**
     * Plays the reference note for the current level.
     * This will be called by the LevelScreen to play the reference note.
     * The callback will be executed after the playback is finished.
     * @param updateUiAfterPlaybackCallback
     */
    public void playReference(Runnable updateUiAfterPlaybackCallback) {
        if (trainingController != null) {
            trainingController.playReference(updateUiAfterPlaybackCallback);
        } else {
            System.err.println("WindowController: TrainingController ist null. Referenzton kann nicht abgespielt werden.");
            // reactivate UI buttons, even if the playback cannot be started
            if (updateUiAfterPlaybackCallback != null) {
                SwingUtilities.invokeLater(updateUiAfterPlaybackCallback);
            }
        }
    }
}
