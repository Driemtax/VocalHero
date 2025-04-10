package src;

public class Note {
    private NoteType type;
    private String symbol;
    private int position;

    public Note(NoteType type, String symbol, int position) {
        this.type = type;
        this.symbol = symbol;
        this.position = position;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPosition() {
        return position;
    }

    public NoteType getType(){
        return type;
    }
}
