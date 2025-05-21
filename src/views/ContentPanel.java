package views;

import javax.swing.*;
import java.awt.*;

public class ContentPanel extends JPanel {
    public ContentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));
    }

    public void showStartPanel() {
        removeAll();
        add(new StartPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showLevelSelection(String category) {
        removeAll();
        add(new LevelSelectionPanel(category), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showDefaultMessage() {
        showStartPanel(); // z. B. falls nichts gewählt
    }
}



