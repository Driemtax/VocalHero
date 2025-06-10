//Author: David Herrmann
package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import model.LevelState;
import model.Mode;

public class ProgressUtil {
    public static List<LevelState> getProgressByMode(Mode mode, List<LevelState> progress) {
        List<LevelState> filtered = new ArrayList<>();
        for (LevelState levelState : progress) {
            if (levelState.mode() == mode) {
                filtered.add(levelState);
            }
        }
        Collections.sort(filtered);
        return filtered;
    }

    public static int[] getLevelAndCompleted(List<LevelState> levelStates) {
        int[] completeLevels = new int[] { 0, 0 };
        int levels = 0, complete = 0;
        for (LevelState levelState : levelStates) {
            levels++;
            if (levelState.completion() >= 50.0) {
                complete++;
            }
        }
        completeLevels[1] = levels;
        completeLevels[0] = complete;
        return completeLevels;
    }
}
