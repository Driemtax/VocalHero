//Authors: Lars Beer
package controller;

import views.*;
import views.SplashScreen;

import java.awt.*;
import javax.swing.*;
import java.util.List;

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

    public void showCategoryScreen() {
        contentPanel.removeAll();
        contentPanel.add(new CategoryScreen(contentPanel), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showLevelSelection(String category) {
        contentPanel.removeAll();
        contentPanel.add(new LevelSelectionPanel(this, category), BorderLayout.CENTER);
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


    /**
     * Starts the recording with a live pitch graph.
     * This will be called by the LevelScreen to start the recording with live feedback.
     *
     * @param uiUpdateCallbackFromView
     */
    public void startRecordingForLevel(Runnable onRecordingFinishedCallback) {
        if (trainingController != null) {
            trainingController.startRecording(onRecordingFinishedCallback);
        } else {
            System.err.println("WindowController: TrainingController ist null. Aufnahme kann nicht gestartet werden.");
            // reactivate UI buttons, even if the recording cannot be started
            if (onRecordingFinishedCallback != null) {
                SwingUtilities.invokeLater(onRecordingFinishedCallback);
            }
        }
    }

    /**
     * Plays the reference note for the current level.
     * This will be called by the LevelScreen to play the reference note.
     * The callback will be executed after the playback is finished.
     * @param uiUpdateCallbackFromView
     */
    public void playReferenceNote(Runnable uiUpdateCallbackFromView) {
        if (trainingController != null) {
            trainingController.playReferenceNote(uiUpdateCallbackFromView);
        } else {
            System.err.println("WindowController: TrainingController ist null. Referenzton kann nicht abgespielt werden.");
            // reactivate UI buttons, even if the playback cannot be started
            if (uiUpdateCallbackFromView != null) {
                SwingUtilities.invokeLater(uiUpdateCallbackFromView);
            }
        }
    }

    public void showResults(int score, String category, int level) {
        FeedbackPanel feedbackPanel = new FeedbackPanel(score, category, level, this);
        contentPanel.removeAll();
        contentPanel.add(feedbackPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
