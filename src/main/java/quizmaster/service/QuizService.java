package quizmaster.service;

import quizmaster.dao.AttemptAnswerDAO;
import quizmaster.dao.CategoryDAO;
import quizmaster.dao.QuestionDAO;
import quizmaster.dao.QuizAttemptDAO;
import quizmaster.model.AttemptAnswer;
import quizmaster.model.Category;
import quizmaster.model.Question;
import quizmaster.model.QuizAttempt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizService {

    private final CategoryDAO categoryDAO;
    private final QuestionDAO questionDAO;
    private final QuizAttemptDAO quizAttemptDAO;
    private final AttemptAnswerDAO attemptAnswerDAO;

    public QuizService() {
        this.categoryDAO = new CategoryDAO();
        this.questionDAO = new QuestionDAO();
        this.quizAttemptDAO = new QuizAttemptDAO();
        this.attemptAnswerDAO = new AttemptAnswerDAO();
    }

    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }

    public int getAvailableQuestionCount(Integer categoryId, String difficulty) {
        return questionDAO.countAvailable(categoryId, difficulty);
    }

    public List<Question> getQuizQuestions(Integer categoryId, String difficulty, int count) {
        return questionDAO.findRandomQuestions(categoryId, difficulty, count);
    }

    public QuizAttempt submitQuiz(int userId, Integer categoryId, List<Question> questions, Map<Integer, String> answers, Timestamp startTime, Timestamp endTime) {
        int totalQuestions = questions != null ? questions.size() : 0;
        int attemptedQuestions = 0;
        int correctAnswersCount = 0;
        int incorrectAnswersCount = 0;
        int unansweredQuestionsCount = 0;
        double totalMarks = 0;
        double obtainedMarks = 0;

        if (questions != null) {
            for (Question q : questions) {
                totalMarks += q.getMarks();
                String selectedOption = answers != null ? answers.get(q.getQuestionId()) : null;
                if (selectedOption != null && !selectedOption.trim().isEmpty()) {
                    attemptedQuestions++;
                    if (selectedOption.equalsIgnoreCase(q.getCorrectOption())) {
                        correctAnswersCount++;
                        obtainedMarks += q.getMarks();
                    } else {
                        incorrectAnswersCount++;
                    }
                } else {
                    unansweredQuestionsCount++;
                }
            }
        }

        double percentage = totalMarks > 0 ? (obtainedMarks / totalMarks) * 100.0 : 0.0;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(userId);
        attempt.setCategoryId(categoryId);
        attempt.setStartTime(startTime);
        attempt.setEndTime(endTime);
        attempt.setTotalQuestions(totalQuestions);
        attempt.setAttemptedQuestions(attemptedQuestions);
        attempt.setCorrectAnswers(correctAnswersCount);
        attempt.setIncorrectAnswers(incorrectAnswersCount);
        attempt.setUnansweredQuestions(unansweredQuestionsCount);
        attempt.setTotalMarks(totalMarks);
        attempt.setObtainedMarks(obtainedMarks);
        attempt.setPercentage(percentage);

        if (categoryId != null) {
            Category cat = categoryDAO.findById(categoryId);
            if (cat != null) {
                attempt.setCategoryName(cat.getCategoryName());
            } else {
                attempt.setCategoryName("Category #" + categoryId);
            }
        } else {
            attempt.setCategoryName("Mixed Quiz");
        }

        // 1. Insert into QUIZ_ATTEMPTS
        int attemptId = quizAttemptDAO.create(attempt);
        if (attemptId == -1) {
            System.err.println("Error: Failed to insert QuizAttempt into database.");
            return null;
        }

        // 2. Insert into ATTEMPT_ANSWERS
        if (questions != null && !questions.isEmpty()) {
            List<AttemptAnswer> answerList = new ArrayList<>();
            for (Question q : questions) {
                String selectedOption = answers != null ? answers.get(q.getQuestionId()) : null;
                boolean isCorrect = (selectedOption != null && selectedOption.equalsIgnoreCase(q.getCorrectOption()));
                double marksForQuestion = isCorrect ? q.getMarks() : 0.0;

                AttemptAnswer aa = new AttemptAnswer();
                aa.setAttemptId(attemptId);
                aa.setQuestionId(q.getQuestionId());
                aa.setSelectedOption((selectedOption != null && !selectedOption.trim().isEmpty()) ? selectedOption.trim() : null);
                aa.setIsCorrect(isCorrect ? "Y" : "N");
                aa.setMarksObtained(marksForQuestion);
                answerList.add(aa);
            }
            attemptAnswerDAO.createBatch(answerList);
        }

        return attempt;
    }
}
