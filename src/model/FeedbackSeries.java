// Authors:Daid Herrmann
package model;

import java.util.ArrayList;
import java.util.List;

public class FeedbackSeries {
    List<Feedback> Scores = new ArrayList<>();

    FeedbackSeries(){
    }

    FeedbackSeries(Feedback feedback){
        Scores.add(feedback);
    }

    FeedbackSeries(List<Feedback> feedbacks){
        Scores.addAll(feedbacks);
    }

    public void addFeedback(Feedback feedback){
        Scores.add(feedback);
    }

    public void addFeedback(List<Feedback> feedbacks){
        Scores.addAll(feedbacks);
    }

    public List<Feedback> getScores() {
        return Scores;
    }
}
