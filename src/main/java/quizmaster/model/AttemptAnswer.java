package quizmaster.model;

/**
 * Represents a row in the ATTEMPT_ANSWERS table.
 *
 * Columns: ATTEMPT_ANSWER_ID, ATTEMPT_ID, QUESTION_ID,
 *          SELECTED_OPTION, IS_CORRECT, MARKS_OBTAINED
 *
 * SELECTED_OPTION can be NULL when the student leaves a question unanswered.
 * IS_CORRECT can be "Y", "N", or NULL.
 */
public class AttemptAnswer {

    private int attemptAnswerId;
    private int attemptId;
    private int questionId;
    private String selectedOption;   // "A","B","C","D" or null
    private String isCorrect;        // "Y", "N", or null
    private double marksObtained;

    public AttemptAnswer() {
    }

    public AttemptAnswer(int attemptId, int questionId,
                         String selectedOption, String isCorrect,
                         double marksObtained) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.marksObtained = marksObtained;
    }

    // --- Getters and Setters ---

    public int getAttemptAnswerId() {
        return attemptAnswerId;
    }

    public void setAttemptAnswerId(int attemptAnswerId) {
        this.attemptAnswerId = attemptAnswerId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }

    @Override
    public String toString() {
        return "AttemptAnswer{questionId=" + questionId +
               ", selected=" + selectedOption +
               ", correct=" + isCorrect + "}";
    }
}
