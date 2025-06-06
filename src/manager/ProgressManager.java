// Authors:David Herrmann
package manager;

import java.io.File;
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

    String levelUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "assets" + File.separator + "level.csv";

    /** 
     * Get the level.csv file as a List of Strings
     * 
     * @return a List of each line of the file as a String
     */
    public List<String> readLevels() throws IOException {
        System.out.println("Level.csv Url: " + levelUrl);
        return Files.readAllLines(Paths.get(levelUrl));
    }

    /** 
     * parses the level.csv and return the data inside as LevelState objects
     * 
     * @return a List of each line of the file as a {@link LevelState} Object
     */
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
                        case "Einzelnoten" -> Mode.NOTE;
                        case "Intervalle" -> Mode.INTERVAL;
                        case "Melodien" -> Mode.MELODY;
                        default -> throw new IllegalArgumentException("Unknown mode: " + parts[0]);
                    };
                    int levelNumber = Integer.parseInt(parts[1]);
                    float Score = Float.parseFloat(parts[2]);
                    boolean isUnlocked = Boolean.parseBoolean(parts[3]);

                    levelStates.add(new LevelState(mode, levelNumber, Score, isUnlocked));
                }
                else {
                    throw new IllegalStateException();
                }
            }
            return levelStates;
        } catch (IOException e) {
            throw new IOException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /** 
     * updates the level.csv file after the end of a level
     * 
     * @param level the number of the level
     * @param mode the mode of the level
     * @param feedback a {@link Feedback} object, contains the score with which the level was completed
     */
    public void updateLevel(int level, Mode mode, Feedback feedback) {
        try {
            boolean newLevelUnlocked = false;
            List<LevelState> lines = parseLevels();
            try (FileWriter writer = new FileWriter(levelUrl)) {

                writer.write("Mode,Level,Completion,unlocked\n");

                for (LevelState levelState : lines) {
                    boolean unlock;
                    if (newLevelUnlocked && level != 1) {
                        unlock = true;
                    } else {
                        unlock = levelState.isUnlocked();
                    }

                    if (levelState.mode() == mode && levelState.level() == level) {
                        levelState = new LevelState(levelState.mode(), levelState.level(), feedback.score(),
                                levelState.isUnlocked());

                        if (feedback.score() >= 50) {
                            newLevelUnlocked = true;
                        }
                    } else {
                        newLevelUnlocked = false;
                    }

                    writer.write(levelState.mode().getName()
                            + ',' + Integer.toString(levelState.level())
                            + ',' + Float.toString(levelState.completion())
                            + ',' + Boolean.toString(unlock) + '\n');
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
