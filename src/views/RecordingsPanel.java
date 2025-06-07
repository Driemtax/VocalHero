// Authors: Inaas Hammoush

package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.WindowController;
import i18n.LanguageManager;


public class RecordingsPanel extends JPanel {

    private final RecordingDeletionHandler deletionHandler = new RecordingDeletionHandler();
    private WindowController windowController;
    private JLabel titleLabel;
    private ModernButton playButton;
    private ModernButton deleteButton;
    
    public RecordingsPanel(WindowController controller) {
        this.windowController = controller;
        setBackground(new Color(50, 50, 50));
        initialize();
        // Additional components and layout can be added here
    }

    public void initialize() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel(LanguageManager.get("recordings.title"));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel recordingsPanel = new JPanel();
        recordingsPanel.setLayout(new BoxLayout(recordingsPanel, BoxLayout.Y_AXIS));
        recordingsPanel.setBackground(new Color(50, 50, 50));
        recordingsPanel.setForeground(Color.WHITE);
        
        add(new JScrollPane(recordingsPanel), BorderLayout.CENTER);


        // Populate recordings
        List<String> recordings = getRecordings();
        for (String recording : recordings) {
            JPanel recordingRow = createRecordingRow(recording);
            recordingsPanel.add(recordingRow);
            recordingsPanel.add(Box.createVerticalStrut(10));
        }

    }

    public List<String> getRecordings() {
        return windowController.getSavedRecordings();
    }

    private JPanel createRecordingRow(String recordingName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(50, 50, 50));

        JLabel nameLabel = new JLabel(recordingName);
        nameLabel.setForeground(Color.WHITE);

        playButton = new ModernButton(LanguageManager.get("recordings.play"));
        playButton.setToolTipText(LanguageManager.get("recordings.play_tooltip"));
        playButton.addActionListener(e -> {windowController.playWavFile(recordingName);});
        deleteButton = new ModernButton(LanguageManager.get("recordings.delete"));
        deleteButton.addActionListener(e -> {
        deletionHandler.confirmAndDelete(this, recordingName, () -> {
            boolean deleted = windowController.deleteRecording(recordingName);
            if (deleted) refreshRecordings();
            return deleted;
            });
        });

        panel.add(nameLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(playButton);
        panel.add(Box.createHorizontalGlue());
        panel.add(deleteButton);

        return panel;
    }
    

    public void refreshRecordings() {
        removeAll();
        initialize();
        revalidate();
        repaint();
    }

}
