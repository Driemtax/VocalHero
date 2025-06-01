// Author: Jonas Rumpf

package utils;

public class ScoreEvaluation {
    private static final int GOLD_THRESHOLD = 90;
    private static final int SILVER_THRESHOLD = 75;
    private static final int BRONZE_THRESHOLD = 60;
    
    private int score;
    private String medal;
    
    public ScoreEvaluation(int score) {
        this.score = score;
        this.medal = evaluateScore();
    }
    
    private String evaluateScore() {
        if (score >= GOLD_THRESHOLD) return "GOLD";
        if (score >= SILVER_THRESHOLD) return "SILVER";
        if (score >= BRONZE_THRESHOLD) return "BRONZE";
        return "NO_MEDAL";
    }
    
    public int getScore() {
        return score;
    }
    
    public String getMedal() {
        return medal;
    }

    //TODO: Method to save the score and medal to a file or database (or use a callback from another class)
}
