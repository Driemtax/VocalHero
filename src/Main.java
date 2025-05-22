import model.Interval;
import model.Note;

public class Main { 
    public static void main(String[] args) {

        Note note = Note.getNoteFromInterval(Note.C5, Interval.P4, true);
        System.out.println(note.getName());
        
    }
}
