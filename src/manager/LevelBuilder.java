package manager;

import java.util.List;

import model.Difficulty;
import model.Level;
import model.LevelInfo;
import model.MidiNote;
import model.Mode;


public class LevelBuilder {

    private List<MidiNote> referenceNotes;
    private Mode mode;
    private Difficulty difficulty;
    
    public LevelBuilder() {
        // Constructor logic if needed
    }

    public Level buildLevel(LevelInfo levelInfo) {
        // Additional logic to build the level 
        
        // temporary constructor. may get changed later
        return new Level(levelInfo.getMode(), levelInfo.getDifficulty());
    }


}
