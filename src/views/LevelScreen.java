// Authors: Jonas Rumpf, Lars Beer, Inaas Hammoush
package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import i18n.LanguageManager;
import model.Mode;
import model.MidiNote;
import java.util.List;

public class LevelScreen extends JPanel {
    private WindowController windowController;

    private RecordingButton startRecordingButton;
    private ModernButton playReferenceButton;
    private PitchGraphPanel pitchPanel;
    private ScorePanel scorePanel;

    // Maybe show this information in the UI later.
    private Mode currentMode;
    private int currentLevel;

    public LevelScreen(WindowController controller, Mode mode, int level) {
        this.windowController = controller;
        this.currentMode = mode;
        this.currentLevel = level;

        System.out.println("LevelScreen: Initializing for category: " + currentMode.getName() + ", level: " + currentLevel);

        setLayout(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(20, 20, 20));
        startRecordingButton = new RecordingButton();
        playReferenceButton = new ModernButton(LanguageManager.get("levelscreen.play_reference"));
        buttonPanel.add(startRecordingButton);
        buttonPanel.add(playReferenceButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        // Help Button erstellen und rechts ausrichten
        ModernHelpButton helpButton = new ModernHelpButton(currentMode);
        JPanel helpButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        helpButtonPanel.setBackground(new Color(20, 20, 20));
        helpButtonPanel.add(helpButton);
        topPanel.add(helpButtonPanel, BorderLayout.EAST);

        // Create main content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        pitchPanel = new PitchGraphPanel();
        windowController.setPitchListener(pitchPanel); // Set the pitch listener for live updates
        List<MidiNote> referenceNotes = windowController.getReferenceNotesForCurrentLevel();
        scorePanel = new ScorePanel(referenceNotes);
        contentPanel.add(pitchPanel);
        contentPanel.add(scorePanel);

        // Add panels to main layout
        add(topPanel, BorderLayout.NORTH);  // Changed from NORTH to SOUTH
        add(contentPanel, BorderLayout.CENTER);

        // Add button listeners (empty for now, will be connected to TrainingController later)
        startRecordingButton.addActionListener(e -> {
            startRecordingButton.setEnabled(false);
            startRecordingButton.setRecording(true);
            playReferenceButton.setEnabled(false);

            // Callback for when recording is finished
            Runnable updateUiAfterRecordingCallback = () -> {
                startRecordingButton.setEnabled(true);
                startRecordingButton.setRecording(false);
                playReferenceButton.setEnabled(true);
                System.out.println("LevelScreen: Aufnahme beendet. Button wieder aktiviert.");
                windowController.showResults(currentMode, currentLevel);
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


