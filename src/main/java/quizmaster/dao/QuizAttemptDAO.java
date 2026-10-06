package quizmaster.dao;

import quizmaster.model.QuizAttempt;
import quizmaster.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for QuizAttempts table.
 */
public class QuizAttemptDAO {

    /**
     * Gets the next available attempt ID.
     * @return The next ID, or -1 if there was an error.
     */
    public int getNextAttemptId() {
        String idSql = "SELECT NVL(MAX(ATTEMPT_ID), 0) + 1 FROM QUIZ_ATTEMPTS";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement idPs = conn.prepareStatement(idSql);
             ResultSet idRs = idPs.executeQuery()) {
             if (idRs.next()) return idRs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error getting next attempt ID: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Creates a new quiz attempt record.
     * @param attempt the attempt to insert
     * @return The generated attempt ID, or -1 if unsuccessful.
     */
    public int create(QuizAttempt attempt) {
        int attemptId = getNextAttemptId();
        if (attemptId == -1) return -1;
        
        String sql = "INSERT INTO QUIZ_ATTEMPTS (ATTEMPT_ID, USER_ID, CATEGORY_ID, START_TIME, END_TIME, TOTAL_QUESTIONS, ATTEMPTED_QUESTIONS, CORRECT_ANSWERS, INCORRECT_ANSWERS, UNANSWERED_QUESTIONS, TOTAL_MARKS, OBTAINED_MARKS, PERCENTAGE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, attemptId);
            ps.setInt(2, attempt.getUserId());
            
            if (attempt.getCategoryId() != null) {
                ps.setInt(3, attempt.getCategoryId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setTimestamp(4, attempt.getStartTime());
            ps.setTimestamp(5, attempt.getEndTime());
            
            ps.setInt(6, attempt.getTotalQuestions());
            ps.setInt(7, attempt.getAttemptedQuestions());
            ps.setInt(8, attempt.getCorrectAnswers());
            ps.setInt(9, attempt.getIncorrectAnswers());
            ps.setInt(10, attempt.getUnansweredQuestions());
            ps.setDouble(11, attempt.getTotalMarks());
            ps.setDouble(12, attempt.getObtainedMarks());
            ps.setDouble(13, attempt.getPercentage());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                attempt.setAttemptId(attemptId);
                return attemptId;
            }
        } catch (SQLException e) {
            System.err.println("Error creating quiz attempt: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Finds quiz attempts for a specific user.
     * @param userId the user ID
     * @return List of QuizAttempt matching the user.
     */
    public List<QuizAttempt> findByUserId(int userId) {
        List<QuizAttempt> list = new ArrayList<>();
        String sql = "SELECT QA.*, C.CATEGORY_NAME " +
                     "FROM QUIZ_ATTEMPTS QA LEFT JOIN CATEGORIES C ON QA.CATEGORY_ID = C.CATEGORY_ID " +
                     "WHERE QA.USER_ID = ? ORDER BY QA.START_TIME DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToAttempt(rs, false));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding quiz attempts by user: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves all quiz attempts.
     * @return List of all quiz attempts.
     */
    public List<QuizAttempt> findAll() {
        List<QuizAttempt> list = new ArrayList<>();
        String sql = "SELECT QA.*, C.CATEGORY_NAME, U.FULL_NAME AS STUDENT_NAME " +
                     "FROM QUIZ_ATTEMPTS QA " +
                     "JOIN USERS U ON QA.USER_ID = U.USER_ID " +
                     "LEFT JOIN CATEGORIES C ON QA.CATEGORY_ID = C.CATEGORY_ID " +
                     "ORDER BY QA.START_TIME DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                 list.add(mapRowToAttempt(rs, true));
             }
        } catch (SQLException e) {
            System.err.println("Error finding all quiz attempts: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    private QuizAttempt mapRowToAttempt(ResultSet rs, boolean includeStudentName) throws SQLException {
        QuizAttempt qa = new QuizAttempt();
        qa.setAttemptId(rs.getInt("ATTEMPT_ID"));
        qa.setUserId(rs.getInt("USER_ID"));
        
        int catId = rs.getInt("CATEGORY_ID");
        if (!rs.wasNull()) {
            qa.setCategoryId(catId);
        } else {
            qa.setCategoryId(null);
        }
        
        Timestamp st = rs.getTimestamp("START_TIME");
        if (st != null) qa.setStartTime(st);
        
        Timestamp et = rs.getTimestamp("END_TIME");
        if (et != null) qa.setEndTime(et);
        
        qa.setTotalQuestions(rs.getInt("TOTAL_QUESTIONS"));
        qa.setAttemptedQuestions(rs.getInt("ATTEMPTED_QUESTIONS"));
        qa.setCorrectAnswers(rs.getInt("CORRECT_ANSWERS"));
        qa.setIncorrectAnswers(rs.getInt("INCORRECT_ANSWERS"));
        qa.setUnansweredQuestions(rs.getInt("UNANSWERED_QUESTIONS"));
        qa.setTotalMarks(rs.getDouble("TOTAL_MARKS"));
        qa.setObtainedMarks(rs.getDouble("OBTAINED_MARKS"));
        qa.setPercentage(rs.getDouble("PERCENTAGE"));
        qa.setCategoryName(rs.getString("CATEGORY_NAME"));
        
        if (includeStudentName) {
            qa.setStudentName(rs.getString("STUDENT_NAME"));
        }
        
        return qa;
    }
}
