// Authors:David Herrmann
package model;

import java.util.ArrayList;
import java.util.List;

public class Level {

    Mode mode;
    Difficulty difficulty;

    Level(Mode mode, Difficulty difficulty){
        this.mode = mode;
        this.difficulty = difficulty;
    }

    List<Note> getLevel() {
        List<Note> notes = new ArrayList<>();
        Range range = difficulty.getDifficultyRange();

        switch (mode) {
            case note:
                for(int i = 0; i < 5; i++){
                    notes.add(
                        Note.getRandomNoteInRange(
                            Note.values()[range.min()], 
                            Note.values()[range.min()]
                            )
                        );
                }
                break;
        
            case interval:
                for(int i = 0; i < 5; i++){
                    Note note = Note.getRandomNoteInRange(Note.values()[range.min()], Note.values()[range.min()]);
                    notes.add(note);
                    Interval interval = Interval.getRandomIntervalInRange(Interval.m2, Interval.P8);
                    notes.add(
                        Note.getNoteFromInterval(note, interval)
                    );
                }
                break;
            
            case melody:
            //TODO implement this
            
                break;

            default:
                throw new IndexOutOfBoundsException();
        }

        return notes;
    }

}
