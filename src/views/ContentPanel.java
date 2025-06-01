// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;

public class ContentPanel extends JPanel {
    private WindowController windowController;
    public ContentPanel(WindowController controller) {
        this.windowController = controller;
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));
    }

    public void showStartPanel() {
        removeAll();
        add(new StartPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showCategoryScreen() {
        removeAll();
        add(new CategoryScreen(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showLevelSelection(String category) {
        removeAll();
        add(new LevelSelectionPanel(windowController, category), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showDefaultMessage() {
        showStartPanel(); // z. B. falls nichts gewählt
    }

}



