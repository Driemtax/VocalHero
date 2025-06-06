// Author: Jonas Rumpf, Inaas Hammoush

package views;

import javax.swing.*;
import java.awt.*;
import controller.WindowController;
import i18n.LanguageManager;
import model.Feedback;
import model.Mode;

public class FeedbackPanel extends JPanel {
    private static final int MIN_SCORE = 60; // Mindestscore für Bestehen
    
    private JLabel scoreLabel;
    private JLabel medalLabel;
    private JLabel saveLabel;
    private JLabel confirmationLabel;
    private ModernButton continueButton;
    private ModernButton retryButton;
    private ModernButton menuButton;
    private ModernButton saveButton;
    private Mode mode;
    private int level;
    private WindowController windowController;
    private Feedback feedback;
    
    public FeedbackPanel(Feedback feedback, Mode mode, int level, WindowController controller) {
        this.mode = mode;
        this.level = level;
        this.windowController = controller;
        this.feedback = feedback;
        setBackground(new Color(50, 50, 50));
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeComponents() {
        scoreLabel = new JLabel(LanguageManager.get("feedback.score") + ": " + feedback.score() + "%");
        medalLabel = new JLabel(LanguageManager.get("feedback.medal") + ": " + feedback.getFeedbackMedal());
        saveLabel = new JLabel(LanguageManager.get("feedback.save_label"));
        confirmationLabel = new JLabel(" "); // Placeholder for confirmation message

        // Buttons erstellen
        retryButton = new ModernButton(LanguageManager.get("feedback.retry"));
        menuButton = new ModernButton(LanguageManager.get("feedback.menu"));
        
        if (feedback.score() >= MIN_SCORE) {
            continueButton = new ModernButton(LanguageManager.get("feedback.continue"));
        }

        // Button for save prompt
        saveButton = new ModernButton(LanguageManager.get("feedback.save_button"));

        
        // Styling
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        
        medalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        medalLabel.setForeground(getMedalColor(feedback.getFeedbackMedal()));

        saveLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        saveLabel.setForeground(Color.WHITE);

        confirmationLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        confirmationLabel.setForeground(new Color(180, 255, 180));
        
    }
    
    private Color getMedalColor(String medal) {
        switch(medal) {
            case "GOLD": return new Color(255, 215, 0);
            case "SILVER": return new Color(192, 192, 192);
            case "BRONZE": return new Color(205, 127, 50);
            default: return Color.WHITE;
        }
    }
    
    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Center components
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        medalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (continueButton != null) {
            continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        
        // Add components
        add(Box.createVerticalStrut(100));
        add(scoreLabel);
        add(Box.createVerticalStrut(30));
        add(medalLabel);
        //add(Box.createVerticalStrut(50));
        
        // Button Panel für horizontale Anordnung
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(50, 50, 50));
        
        buttonPanel.add(retryButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        if (continueButton != null) {
            buttonPanel.add(continueButton);
            buttonPanel.add(Box.createHorizontalStrut(20));
        }
        buttonPanel.add(menuButton);
        
        add(buttonPanel);

        // Save prompt panel
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));
        savePanel.setBackground(new Color(50, 50, 50));

        savePanel.add(saveLabel);
        savePanel.add(Box.createVerticalStrut(10));

        savePanel.add(saveButton);
        savePanel.add(Box.createVerticalStrut(10));

        savePanel.add(confirmationLabel);

        add(savePanel);
    }
    
    private void setupListeners() {
        retryButton.addActionListener(e -> {
            windowController.showLevelScreen(mode, level);
        });
        
        menuButton.addActionListener(e -> {
            windowController.showHome();
        });
        
        if (continueButton != null) {
            continueButton.addActionListener(e -> {
                windowController.showCategoryScreen();
            });
        }

        saveButton.addActionListener(e -> {
            // Logic to save the recording
            boolean success = windowController.saveRecording();
            if (success) {
                confirmationLabel.setText(LanguageManager.get("feedback.save_success"));
            } else {
                confirmationLabel.setText(LanguageManager.get("feedback.save_failure"));
            }
        });
    }


    
    public JButton getRetryButton() {
        return retryButton;
    }
    
    public JButton getContinueButton() {
        return continueButton;
    }
    
    public JButton getMenuButton() {
        return menuButton;
    }
}
