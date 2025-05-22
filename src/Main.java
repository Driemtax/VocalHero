import javax.swing.SwingUtilities;

import views.MainFrame;
import views.SplashScreen;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen(() -> {
            new MainFrame();
        }));
    }
}
