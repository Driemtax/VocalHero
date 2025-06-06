//Author: David Herrmann

package model;

public record LevelState(Mode mode, int level, float completion, boolean isUnlocked) implements Comparable<LevelState> {

    @Override
    public int compareTo(LevelState levelState){
        return Integer.compare(this.level, levelState.level);
    }
}

