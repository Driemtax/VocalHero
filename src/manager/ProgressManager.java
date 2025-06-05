// Authors:David Herrmann
package manager;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.Feedback;
import model.LevelState;
import model.Mode;

public class ProgressManager {

    String levelUrl = System.getProperty("user.dir") + "/assets/level.csv";

    List<String> readLevels() throws IOException {
        return Files.readAllLines(Paths.get(URI.create(levelUrl)));
    }

    public List<LevelState> parseLevels() throws IOException, IllegalArgumentException {
        List<LevelState> levelStates = new ArrayList<>();
        try {
            List<String> lines = readLevels();

            for (String string : lines) {
                String[] parts = string.split(",");
                if (parts[0].equals("Mode")) {
                    continue;
                }

                if (parts.length == 4) {
                    Mode mode = switch (parts[0]) {
                        case "Note" -> Mode.NOTE;
                        case "Interval" -> Mode.INTERVAL;
                        case "Melody" -> Mode.MELODY;
                        default -> throw new IllegalArgumentException("Unknown mode: " + parts[0]);
                    };
                    int levelNumber = Integer.parseInt(parts[1]);
                    float Score = Float.parseFloat(parts[2]);
                    boolean isUnlocked = Boolean.parseBoolean(parts[3]);

                    levelStates.add(new LevelState(mode, levelNumber, Score, isUnlocked));
                }
            }
            return levelStates;
        } catch (IOException e) {
            throw new IOException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void updateLevel(int Level, Mode mode, Feedback feedback) {
        try {
            List<LevelState> lines = parseLevels();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
