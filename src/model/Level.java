// Authors:David Herrmann
package model;

import java.util.ArrayList;
import java.util.List;

public enum Level {

    //TODO: Placeholder Level mit richtigen Levelnn ersetzen
    N1(Mode.note,Difficulty.easy),
    N2(Mode.note,Difficulty.medium),
    N3(Mode.note,Difficulty.hard),

    I1(Mode.note,Difficulty.easy),
    I2(Mode.note,Difficulty.medium),
    I3(Mode.note,Difficulty.hard),

    M1(Mode.note,Difficulty.easy),
    M2(Mode.note,Difficulty.medium),
    M3(Mode.note,Difficulty.hard);

    private final Mode mode;
    private final Difficulty difficulty;

    Level(Mode mode, Difficulty difficulty){
        this.mode = mode;
        this.difficulty = difficulty;
    }
}
