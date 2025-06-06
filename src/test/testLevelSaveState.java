//Author: David Herrmann
package test;

import java.io.IOException;
import java.util.List;

import manager.ProgressManager;
import model.Feedback;
import model.Mode;

public class testLevelSaveState {

    public static void main(String[] args) {
        ProgressManager progressManager = new ProgressManager();
        try {
            List<String> lines = progressManager.readLevels();
            for (String string : lines) {
                System.out.println(string);
            }

            progressManager.updateLevel(2, Mode.NOTE, new Feedback(90));

            lines = progressManager.readLevels();
            for (String string : lines) {
                System.out.println(string);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
