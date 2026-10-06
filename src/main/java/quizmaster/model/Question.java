package quizmaster.model;

/**
 * Represents a row in the QUESTIONS table.
 *
 * Columns: QUESTION_ID, QUESTION_TEXT, CATEGORY_ID, DIFFICULTY,
 *          OPTION_A, OPTION_B, OPTION_C, OPTION_D,
 *          CORRECT_OPTION, MARKS, ACTIVE_STATUS
 */
public class Question {

    private int questionId;
    private String questionText;
    private int categoryId;
    private String difficulty;      // "EASY", "MEDIUM", "HARD"
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;   // "A", "B", "C", "D"
    private double marks;
    private String activeStatus;    // "Y" or "N"

    // Transient field — category name for display purposes (not stored in QUESTIONS table)
    private String categoryName;

    public Question() {
    }

    // --- Getters and Setters ---

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isActive() {
        return "Y".equalsIgnoreCase(activeStatus);
    }

    /**
     * Returns the text of the specified option.
     *
     * @param option "A", "B", "C", or "D"
     * @return the option text, or null if invalid
     */
    public String getOptionText(String option) {
        return switch (option.toUpperCase()) {
            case "A" -> optionA;
            case "B" -> optionB;
            case "C" -> optionC;
            case "D" -> optionD;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return "Question{id=" + questionId + ", text='" + questionText + "'}";
    }
}
