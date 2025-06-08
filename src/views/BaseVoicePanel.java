//Author:David Herrmann

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import i18n.LanguageManager;
import model.RecordingFinishedCallback;

public class BaseVoicePanel extends JPanel {

    private JTextArea descriptionLabel;
    private JLabel resultLabel;
    private ModernButton startRecordButton;
    private ModernButton startButton;
    private WindowController windowController;

    public BaseVoicePanel(WindowController windowController) {
        this.windowController = windowController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 50, 50));
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    RecordingFinishedCallback finished = _1 -> {
        updatePitchLabel(getPlayerVoice());
    };

    private void setupListeners() {
        startRecordButton.addActionListener(_1 -> windowController.recordForBaseVoice(finished));
        startButton.addActionListener(_1 -> windowController.showHome());
    }

    private void setupLayout() {
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startRecordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        descriptionLabel.setMargin(new Insets(10, 10, 10, 10));
        descriptionLabel.setMaximumSize(new Dimension(900, 400));
        descriptionLabel.setPreferredSize(new Dimension(900, 400));

        add(Box.createVerticalStrut(100));
        add(descriptionLabel);
        add(Box.createVerticalStrut(50));
        add(resultLabel);
        add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(50, 50, 50));
        buttonPanel.setMaximumSize(new Dimension(600, 200));

        buttonPanel.add(startRecordButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(startButton);

        add(buttonPanel);
    }

    private void initializeComponents() {
        descriptionLabel = new JTextArea(LanguageManager.get("baseVoice.description"));
        resultLabel = new JLabel(LanguageManager.get("baseVoice.current") + getPlayerVoice());

        startRecordButton = new ModernButton(LanguageManager.get("baseVoice.record"));
        startButton = new ModernButton(LanguageManager.get("baseVoice.start"));

        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setBackground(new Color(50, 50, 50));
        descriptionLabel.setEnabled(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);

        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        resultLabel.setForeground(Color.WHITE);
    }

    private String getPlayerVoice() {
        return windowController.getPlayerVoice().getName();
    }

    void updatePitchLabel(String pitch){
        resultLabel.setText(LanguageManager.get("baseVoice.current") + pitch);
    }
}
