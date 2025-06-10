// Authors: Jonas Rumpf
package model;

import i18n.LanguageManager;

public record Feedback(float score) {

    //Here maybe we can have the final feedback message that is calculated based on the functions in the FeedbackManager
    //Just an example of how it could look like (or the medals that Jonas suggested):
    public String getFeedbackMessage() {
        if (score >= 90) {
            return "Excellent!";
        } else if (score >= 75) {
            return "Good job!";
        } else if (score >= 50) {
           return "Keep practicing!";
        } else {
            return "Needs improvement.";
        }
    }

    public String getFeedbackMedal() {
        if (score >= 90) {
            return LanguageManager.get("feedback.medal.gold");
        } else if (score >= 75) {
            return LanguageManager.get("feedback.medal.silver");
        } else if (score >= 50) {
            return LanguageManager.get("feedback.medal.bronze");
        } else {
            return LanguageManager.get("feedback.retry_label");
        }
    }
}
