import model.Interval;
import model.Note;

public class Main {
    public static void main(String[] args) {

        Note note = Note.getNoteFromInterval(Note.A2, Interval.P4,false);
        System.out.println(note.getName());
        
    }
}
