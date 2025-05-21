package views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel rootPanel;
    private CardLayout cardLayout;
    private ContentPanel contentPanel;

    public static final String HOME = "home";
    public static final String APP = "app";

    public MainFrame() {
        setTitle("Gesangstrainer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);

        // HomeScreen ohne Sidebar
        HomeScreen homeScreen = new HomeScreen(this);

        // AppScreen mit Sidebar und ContentPanel
        contentPanel = new ContentPanel();
        Sidebar sidebar = new Sidebar(contentPanel, this);
        JPanel appPanel = new JPanel(new BorderLayout());
        appPanel.add(sidebar, BorderLayout.WEST);
        appPanel.add(contentPanel, BorderLayout.CENTER);

        rootPanel.add(homeScreen, HOME);
        rootPanel.add(appPanel, APP);

        setContentPane(rootPanel);
        showHome();

        setVisible(true);
    }

    public void showHome() {
        cardLayout.show(rootPanel, HOME);
    }

    public void showApp() {
        contentPanel.showStartPanel(); // zeigt StartPanel direkt beim Einstieg
        cardLayout.show(rootPanel, APP);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen(() -> {
            new MainFrame();
        }));
    }
}
