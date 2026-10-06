package quizmaster.service;

import quizmaster.dao.AttemptAnswerDAO;
import quizmaster.dao.QuizAttemptDAO;
import quizmaster.model.AttemptAnswer;
import quizmaster.model.QuizAttempt;

import java.util.List;

public class ResultService {

    private final QuizAttemptDAO quizAttemptDAO;
    private final AttemptAnswerDAO attemptAnswerDAO;

    public ResultService() {
        this.quizAttemptDAO = new QuizAttemptDAO();
        this.attemptAnswerDAO = new AttemptAnswerDAO();
    }

    public List<QuizAttempt> getStudentHistory(int userId) {
        return quizAttemptDAO.findByUserId(userId);
    }

    public List<AttemptAnswer> getAttemptDetails(int attemptId) {
        return attemptAnswerDAO.findByAttemptId(attemptId);
    }
}
