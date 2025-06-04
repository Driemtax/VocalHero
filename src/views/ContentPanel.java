// Author: Jonas Rumpf

package views;

import javax.swing.*;
import java.awt.*;

import controller.WindowController;
import model.Mode;

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

    public void showLevelSelection(Mode mode) {
        removeAll();
        add(new LevelSelectionPanel(windowController, mode), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showDefaultMessage() {
        showStartPanel(); // z. B. falls nichts gewählt
    }

}



