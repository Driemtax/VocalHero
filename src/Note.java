package src;

public class Note {
    private String symbol;
    private int position;

    public Note(String symbol, int position) {
        this.symbol = symbol;
        this.position = position;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPosition() {
        return position;
    }
}
