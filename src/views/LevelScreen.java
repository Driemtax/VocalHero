package views;

import javax.swing.*;
import java.awt.*;

public class LevelScreen extends JPanel {
    public LevelScreen() {
        setLayout(new GridLayout(2, 1)); // oben/unten aufgeteilt

        add(new PitchGraphPanel());
        add(new ScorePanel());
    }
}

