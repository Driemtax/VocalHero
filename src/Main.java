import javax.swing.SwingUtilities;

import controller.TrainingController;
import controller.WindowController;

public class Main {
    public static void main(String[] args) {
        final TrainingController trainingController = new TrainingController();
        final WindowController windowController = new WindowController(trainingController);
        SwingUtilities.invokeLater(() -> windowController.showSplashScreen());
    }
} 