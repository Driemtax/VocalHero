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
        // Das ist das Runnable, das ausgeführt wird, NACHDEM der SplashScreen fertig ist.
        Runnable onSplashScreenFinished = () -> {
            initAndShowMainApplicationUI();
        };
        
        // Der SplashScreen sollte sich selbst sichtbar machen und nach Ablauf des Runnables disposen.
        splashScreen = new SplashScreen(onSplashScreenFinished);
    }

    public void showHomeScreen() {

    }

    //TODO: need to somehow notify trainingController and LevelBuilder about training method and level
    public void showLevelScreen(String category, int level) {
        contentPanel.removeAll();
        contentPanel.add(new LevelScreen(), BorderLayout.CENTER);
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



}
