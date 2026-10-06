package quizmaster.model;

import java.sql.Timestamp;

/**
 * Represents a row in the QUIZ_ATTEMPTS table.
 *
 * Columns: ATTEMPT_ID, USER_ID, CATEGORY_ID, START_TIME, END_TIME,
 *          TOTAL_QUESTIONS, ATTEMPTED_QUESTIONS, CORRECT_ANSWERS,
 *          INCORRECT_ANSWERS, UNANSWERED_QUESTIONS, TOTAL_MARKS,
 *          OBTAINED_MARKS, PERCENTAGE
 *
 * NOTE: CATEGORY_ID can be NULL for a Mixed Quiz.
 *       We use Integer (boxed) instead of int so it can hold null.
 */
public class QuizAttempt {

    private int attemptId;
    private int userId;
    private Integer categoryId;       // null for Mixed Quiz
    private Timestamp startTime;
    private Timestamp endTime;
    private int totalQuestions;
    private int attemptedQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private int unansweredQuestions;
    private double totalMarks;
    private double obtainedMarks;
    private double percentage;

    // Transient fields for display purposes
    private String categoryName;
    private String studentName;

    public QuizAttempt() {
    }

    // --- Getters and Setters ---

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(int attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public int getUnansweredQuestions() {
        return unansweredQuestions;
    }

    public void setUnansweredQuestions(int unansweredQuestions) {
        this.unansweredQuestions = unansweredQuestions;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(double obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "QuizAttempt{attemptId=" + attemptId + ", userId=" + userId +
               ", percentage=" + percentage + "%}";
    }
}
