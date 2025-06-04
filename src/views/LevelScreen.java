// Authors: Jonas Rumpf, Lars Beer
package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import model.LevelInfo;

public class LevelScreen extends JPanel {
    private WindowController windowController;

    private ModernButton startRecordingButton;
    private ModernButton playReferenceButton;
    private PitchGraphPanel pitchPanel;
    private ScorePanel scorePanel;

    // Maybe show this information in the UI later.
    private String currentCategory;
    private int currentLevel;

    public LevelScreen(WindowController controller) {
        this.windowController = controller;

        setLayout(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 20));
        startRecordingButton = new ModernButton("Start Recording");
        playReferenceButton = new ModernButton("Play Reference");
        buttonPanel.add(startRecordingButton);
        buttonPanel.add(playReferenceButton);

        // Create main content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        pitchPanel = new PitchGraphPanel();
        windowController.setPitchListener(pitchPanel); // Set the pitch listener for live updates
        scorePanel = new ScorePanel();
        contentPanel.add(pitchPanel);
        contentPanel.add(scorePanel);

        // Add panels to main layout
        add(buttonPanel, BorderLayout.NORTH);  // Changed from NORTH to SOUTH
        add(contentPanel, BorderLayout.CENTER);

        // Add button listeners (empty for now, will be connected to TrainingController later)
        startRecordingButton.addActionListener(e -> {
            startRecordingButton.setEnabled(false);
            playReferenceButton.setEnabled(false);

            // Callback for when recording is finished
            Runnable updateUiAfterRecordingCallback = () -> {
                startRecordingButton.setEnabled(true);
                playReferenceButton.setEnabled(true);
                System.out.println("LevelScreen: Aufnahme beendet. Button wieder aktiviert.");
                // Hier könntest du weitere UI-Updates machen, z.B. Ergebnisse anzeigen
                windowController.showResults();
            };

            windowController.startRecordingForLevel(updateUiAfterRecordingCallback);
        });

        playReferenceButton.addActionListener(e -> {
            playReferenceButton.setEnabled(false);
            startRecordingButton.setEnabled(false);

            // Callback for when playback is finished
            Runnable updateUiAfterPlaybackCallback = () -> {
                playReferenceButton.setEnabled(true);
                startRecordingButton.setEnabled(true);
            };

            // Play the reference note for the current level
            windowController.playReference(updateUiAfterPlaybackCallback);
        });
    }

    public void stop() {
        // Will be used to clean up resources when implementing TrainingController
    }
}


