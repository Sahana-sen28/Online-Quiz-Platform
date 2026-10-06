package quizmaster.service;

import quizmaster.dao.QuestionDAO;
import quizmaster.dao.QuizAttemptDAO;
import quizmaster.model.Question;
import quizmaster.model.QuizAttempt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminService {

    private final QuestionDAO questionDAO;
    private final QuizAttemptDAO quizAttemptDAO;

    public AdminService() {
        this.questionDAO = new QuestionDAO();
        this.quizAttemptDAO = new QuizAttemptDAO();
    }

    public List<Question> getAllQuestions() {
        return questionDAO.findAll();
    }

    public List<Question> getFilteredQuestions(Integer categoryId, String difficulty, String activeStatus) {
        return questionDAO.findByFilters(categoryId, difficulty, activeStatus);
    }

    public boolean addQuestion(Question q) {
        if (isValid(q)) {
            return questionDAO.create(q);
        }
        return false;
    }

    public boolean updateQuestion(Question q) {
        if (isValid(q)) {
            return questionDAO.update(q);
        }
        return false;
    }

    public boolean deleteQuestion(int questionId) {
        return questionDAO.delete(questionId);
    }

    private boolean isValid(Question q) {
        return q != null && q.getQuestionText() != null && !q.getQuestionText().isEmpty()
                && q.getOptionA() != null && !q.getOptionA().isEmpty()
                && q.getOptionB() != null && !q.getOptionB().isEmpty()
                && q.getOptionC() != null && !q.getOptionC().isEmpty()
                && q.getOptionD() != null && !q.getOptionD().isEmpty()
                && q.getCorrectOption() != null && !q.getCorrectOption().isEmpty()
                && q.getMarks() > 0;
    }

    public List<QuizAttempt> getAllAttempts() {
        return quizAttemptDAO.findAll();
    }

    public Map<String, Object> getStatistics() {
        List<QuizAttempt> attempts = getAllAttempts();
        Map<String, Object> stats = new HashMap<>();

        if (attempts == null || attempts.isEmpty()) {
            stats.put("totalAttempts", 0);
            stats.put("averageScore", 0.0);
            stats.put("highestScore", 0.0);
            stats.put("categoryStats", new ArrayList<Map<String, Object>>());
            return stats;
        }

        int totalAttempts = attempts.size();
        double totalScore = 0.0;
        double highestScore = 0.0;

        Map<String, List<QuizAttempt>> attemptsByCategory = new HashMap<>();

        for (QuizAttempt attempt : attempts) {
            double percent = attempt.getPercentage();
            totalScore += percent;
            if (percent > highestScore) {
                highestScore = percent;
            }

            String catName = attempt.getCategoryName() != null ? attempt.getCategoryName() : "Mixed";
            attemptsByCategory.computeIfAbsent(catName, k -> new ArrayList<>()).add(attempt);
        }

        double averageScore = totalScore / totalAttempts;

        List<Map<String, Object>> categoryStats = new ArrayList<>();
        for (Map.Entry<String, List<QuizAttempt>> entry : attemptsByCategory.entrySet()) {
            String catName = entry.getKey();
            List<QuizAttempt> catAttempts = entry.getValue();

            int catCount = catAttempts.size();
            double catTotalScore = 0.0;
            double catHighest = 0.0;

            for (QuizAttempt qa : catAttempts) {
                double pct = qa.getPercentage();
                catTotalScore += pct;
                if (pct > catHighest) {
                    catHighest = pct;
                }
            }

            Map<String, Object> catStat = new HashMap<>();
            catStat.put("categoryName", catName);
            catStat.put("attemptCount", catCount);
            catStat.put("avgScore", catTotalScore / catCount);
            catStat.put("highestScore", catHighest);
            categoryStats.add(catStat);
        }

        stats.put("totalAttempts", totalAttempts);
        stats.put("averageScore", averageScore);
        stats.put("highestScore", highestScore);
        stats.put("categoryStats", categoryStats);

        return stats;
    }
}
