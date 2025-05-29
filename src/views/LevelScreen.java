package views;

import javax.swing.*;
import java.awt.*;

public class LevelScreen extends JPanel {

    private ModernButton startRecordingButton;
    private ModernButton playReferenceButton;
    private PitchGraphPanel pitchPanel;
    private ScorePanel scorePanel;

    public LevelScreen() {
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
        scorePanel = new ScorePanel();
        contentPanel.add(pitchPanel);
        contentPanel.add(scorePanel);

        // Add panels to main layout
        add(buttonPanel, BorderLayout.NORTH);  // Changed from NORTH to SOUTH
        add(contentPanel, BorderLayout.CENTER);

        // Add button listeners (empty for now, will be connected to TrainingController later)
        startRecordingButton.addActionListener(e -> {
            // Will be implemented when TrainingController is ready
        });

        playReferenceButton.addActionListener(e -> {
            // Will be implemented when TrainingController is ready
        });
    }

    public void stop() {
        // Will be used to clean up resources when implementing TrainingController
    }
}


