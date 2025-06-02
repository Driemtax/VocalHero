// Authors: David Herrmann
package model;

public record Feedback(float score) {

    // Here maybe we can have the final feedback message that is calculated based on the functions in the FeedbackManager
    // Just an example of how it could look like (or the medals that Jonas suggested):
    // public String getFeedbackMessage() {
    //     if (score >= 90) {
    //         return "Excellent!";
    //     } else if (score >= 75) {
    //         return "Good job!";
    //     } else if (score >= 50) {
    //         return "Keep practicing!";
    //     } else {
    //         return "Needs improvement.";
    //     }
    // }
}
