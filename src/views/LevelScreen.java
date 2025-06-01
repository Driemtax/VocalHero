// Authors: Jonas Rumpf, Lars Beer
package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;

public class LevelScreen extends JPanel {
    private WindowController windowController;

    private RecordingButton startRecordingButton;
    private ModernButton playReferenceButton;
    private PitchGraphPanel pitchPanel;
    private ScorePanel scorePanel;

    // Maybe show this information in the UI later.
    private String currentCategory;
    private int currentLevel;

    public LevelScreen(WindowController controller, String category, int level) {
        this.windowController = controller;
        this.currentCategory = category;
        this.currentLevel = level;

        System.out.println("LevelScreen: Initializing for category: " + category + ", level: " + level);

        setLayout(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 20));
        startRecordingButton = new RecordingButton();
        playReferenceButton = new ModernButton("Play Reference");
        buttonPanel.add(startRecordingButton);
        buttonPanel.add(playReferenceButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        // Help Button erstellen und rechts ausrichten
        ModernHelpButton helpButton = new ModernHelpButton(category);
        JPanel helpButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        helpButtonPanel.setBackground(new Color(20, 20, 20));
        helpButtonPanel.add(helpButton);
        topPanel.add(helpButtonPanel, BorderLayout.EAST);

        // Create main content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        pitchPanel = new PitchGraphPanel();
        scorePanel = new ScorePanel();
        contentPanel.add(pitchPanel);
        contentPanel.add(scorePanel);

        // Add panels to main layout
        add(topPanel, BorderLayout.NORTH);  // Changed from NORTH to SOUTH
        add(contentPanel, BorderLayout.CENTER);

        // Add button listeners (empty for now, will be connected to TrainingController later)
        startRecordingButton.addActionListener(e -> {
            // TODO: Do we need visual changes for the buttons to look disabled?
            startRecordingButton.setEnabled(false);
            startRecordingButton.setRecording(true);
            playReferenceButton.setEnabled(false);

            // Callback for when recording is finished
            // TODO: return a feedback here and show in UI
            Runnable onRecordingFinishedCallback = () -> {
                startRecordingButton.setEnabled(true);
                startRecordingButton.setRecording(false);
                playReferenceButton.setEnabled(true);
                System.out.println("LevelScreen: Aufnahme beendet. Button wieder aktiviert.");
                // Hier könntest du weitere UI-Updates machen, z.B. Ergebnisse anzeigen
                int testScore = 100;
                windowController.showResults(testScore, currentCategory, currentLevel);
            };
            
            windowController.startRecordingForLevel(onRecordingFinishedCallback); 
        });

        playReferenceButton.addActionListener(e -> {
            // TODO: Do we need visual changes for the buttons to look disabled?
            playReferenceButton.setEnabled(false);
            startRecordingButton.setEnabled(false);

            // Callback for when playback is finished
            Runnable onPlaybackFinishedCallback = () -> {
                playReferenceButton.setEnabled(true);
                startRecordingButton.setEnabled(true);
            };

            // Play the reference note for the current level
            windowController.playReferenceNote(onPlaybackFinishedCallback);
        });
    }

    public void stop() {
        // Will be used to clean up resources when implementing TrainingController
    }
}


