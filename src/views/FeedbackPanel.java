// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;
import utils.ScoreEvaluation;
import controller.WindowController;

public class FeedbackPanel extends JPanel {
    private static final int MIN_SCORE = 60; // Mindestscore für Bestehen
    
    private ScoreEvaluation evaluation;
    private JLabel scoreLabel;
    private JLabel medalLabel;
    private ModernButton continueButton;
    private ModernButton retryButton;
    private ModernButton menuButton;
    private String category;
    private int level;
    private WindowController windowController;
    
    public FeedbackPanel(int score, String category, int level, WindowController controller) {
        this.category = category;
        this.level = level;
        this.windowController = controller;
        setBackground(new Color(50, 50, 50));
        evaluation = new ScoreEvaluation(score);
        initializeComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initializeComponents() {
        scoreLabel = new JLabel("Score: " + evaluation.getScore() + "%");
        medalLabel = new JLabel("Medal: " + evaluation.getMedal());
        
        // Buttons erstellen
        retryButton = new ModernButton("Retry");
        menuButton = new ModernButton("Menu");
        
        if (evaluation.getScore() >= MIN_SCORE) {
            continueButton = new ModernButton("Continue");
        }
        
        // Styling
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        scoreLabel.setForeground(Color.WHITE);
        
        medalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        medalLabel.setForeground(getMedalColor(evaluation.getMedal()));
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
        if (continueButton != null) {
            continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        
        // Add components
        add(Box.createVerticalStrut(100));
        add(scoreLabel);
        add(Box.createVerticalStrut(30));
        add(medalLabel);
        add(Box.createVerticalStrut(50));
        
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
    }
    
    private void setupListeners() {
        retryButton.addActionListener(e -> {
            windowController.showLevelScreen(category, level);
        });
        
        menuButton.addActionListener(e -> {
            windowController.showHome();
        });
        
        if (continueButton != null) {
            continueButton.addActionListener(e -> {
                windowController.showCategoryScreen();
            });
        }
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
    
    public ScoreEvaluation getEvaluation() {
        return evaluation;
    }
}
