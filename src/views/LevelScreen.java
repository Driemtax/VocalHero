// Authors: Jonas Rumpf, Lars Beer, Inaas Hammoush
package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import i18n.LanguageManager;
import model.Mode;
import model.RecordingFinishedCallback;
import model.MidiNote;
import java.util.List;

public class LevelScreen extends JPanel {
    private WindowController windowController;

    private RecordingButton startRecordingButton;
    private ModernButton playReferenceButton;
    private PitchGraphPanel pitchPanel;
    private ScorePanel scorePanel;
    private JLabel statusLabel;

    // Maybe show this information in the UI later.
    private Mode currentMode;
    private int currentLevel;
    private int currentDifficulty;

    private final int[] countdown = {3};
    private Timer timer = new Timer(1000, null);

    public LevelScreen(WindowController controller, Mode mode, int level) {
        this.windowController = controller;
        this.currentMode = mode;
        this.currentLevel = level;
        this.currentDifficulty = windowController.getCurrentDifficulty();

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

        // Status label to show status of recording/playback
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setBackground(new Color(20, 20, 20));
        statusLabel = new JLabel(LanguageManager.get("levelscreen.ready"), SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusPanel.add(statusLabel);
        topPanel.add(statusPanel, BorderLayout.SOUTH);

        // Task Panel
        JPanel taskPanel = new JPanel(new GridLayout(2, 1));
        taskPanel.setBackground(new Color(80, 80, 80));
        JLabel taskLabel = null;
        JLabel songTextLabel = new JLabel("");;
        if (currentMode == Mode.INTERVAL) {
            String intervalName = windowController.getCurrentLevel().getIntervalName();
            taskLabel = new JLabel(LanguageManager.get("task.interval.1") + intervalName + LanguageManager.get("task.interval.2"));
        } else if (currentMode == Mode.MELODY) {
            taskLabel = new JLabel(LanguageManager.get("task.melody"));
            switch (currentDifficulty) {
                case 1:
                    songTextLabel = new JLabel(LanguageManager.get("task.melodyText.easy"));
                    break;
                case 2:
                    songTextLabel = new JLabel(LanguageManager.get("task.melodyText.medium"));
                    break;
                case 3:
                    songTextLabel = new JLabel(LanguageManager.get("task.melodyText.hard"));
                    break;
                default:
                    break;
            }
        } else {
            taskLabel = new JLabel(LanguageManager.get("task.single_note"));
        }

        taskLabel.setForeground(Color.WHITE);
        taskLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        taskLabel.setBackground(new Color(20, 20, 20));
        taskLabel.setHorizontalAlignment(SwingConstants.CENTER);
        taskLabel.setVerticalAlignment(SwingConstants.CENTER);
        taskPanel.add(taskLabel);


        songTextLabel.setForeground(Color.WHITE);
        songTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        songTextLabel.setBackground(new Color(20, 20, 20));
        songTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        songTextLabel.setVerticalAlignment(SwingConstants.CENTER);
        taskPanel.add(songTextLabel);
        

        // Create main content panel
        JPanel contentPanel = new JPanel(new GridLayout(3, 1));
        pitchPanel = new PitchGraphPanel();
        windowController.setPitchListener(pitchPanel); // Set the pitch listener for live updates
        pitchPanel.setPitchActivityListener(() -> {
            if (LanguageManager.get("levelscreen.too_quiet").equals(statusLabel.getText())) {
                statusLabel.setText(LanguageManager.get("levelscreen.recording"));
            }
        });
        List<MidiNote> referenceNotes = windowController.getReferenceNotesForCurrentLevel();
        scorePanel = new ScorePanel(referenceNotes);
        contentPanel.add(pitchPanel);
        contentPanel.add(taskPanel);
        contentPanel.add(scorePanel);

        // Add panels to main layout
        add(topPanel, BorderLayout.NORTH);  // Changed from NORTH to SOUTH
        add(contentPanel, BorderLayout.CENTER);

        // Add button listeners (empty for now, will be connected to TrainingController later)
        startRecordingButton.addActionListener(e -> {
            startRecordingButton.setEnabled(false);
            startRecordingButton.setRecording(true);
            playReferenceButton.setEnabled(false);

            // Set timer for recording countdown
            timer.addActionListener(ev -> {
                if (countdown[0] > 0) {
                    statusLabel.setText(LanguageManager.get("levelscreen.countdown") + " " + countdown[0]);
                    countdown[0]--;
                } else {
                    timer.stop();
                    statusLabel.setText(LanguageManager.get("levelscreen.recording"));

                    RecordingFinishedCallback updateUiAfterRecordingCallback = (boolean success) -> {
                        if (success) {
                            statusLabel.setText(LanguageManager.get("levelscreen.finished"));
                            startRecordingButton.setEnabled(true);
                            startRecordingButton.setRecording(false);
                            playReferenceButton.setEnabled(true);
                            System.out.println("LevelScreen: Aufnahme beendet. Button wieder aktiviert.");
                            windowController.showResults(currentMode, currentLevel);
                        }
                    };

                    // Callback für "Audio zu leise"
                    Runnable onTooQuiet = () -> {
                        statusLabel.setText(LanguageManager.get("levelscreen.too_quiet"));
                    };

                    // Starte die Aufnahme mit beiden Callbacks
                    boolean success = windowController.startRecordingForLevel(updateUiAfterRecordingCallback, onTooQuiet);
                    if (!success) {
                        statusLabel.setText(LanguageManager.get("levelscreen.error"));
                        startRecordingButton.setEnabled(true);
                        startRecordingButton.setRecording(false);
                        playReferenceButton.setEnabled(true);
                        System.out.println("LevelScreen: Aufnahme konnte nicht gestartet werden.");
                    }
                }
            });
            timer.setInitialDelay(0);
            timer.start();
        });
        
        // action listener for play reference button
        playReferenceButton.addActionListener(e -> {
            playReferenceButton.setEnabled(false);
            startRecordingButton.setEnabled(false);
    
            // Callback for when playback is finished
            Runnable updateUiAfterPlaybackCallback = () -> {
                playReferenceButton.setEnabled(true);
                startRecordingButton.setEnabled(true);
            };
    
            // Play the reference note for the current level
            boolean success = windowController.playReference(updateUiAfterPlaybackCallback);

            if (!success) {
                final String originalText = statusLabel.getText();
                statusLabel.setText("MIDI-Wiedergabe nicht möglich. Bitte überprüfen Sie Ihre System-Soundeinstellungen.");

                // We need a timer to wait until the statusLabel will be resetted.
                Timer revertTimer = new Timer(2000, wait -> {
                    statusLabel.setText(originalText);
                });

                revertTimer.setRepeats(false);
                revertTimer.start();
            }


        });   
    }

    public void stop() {
        // Will be used to clean up resources when implementing TrainingController
    }
}


