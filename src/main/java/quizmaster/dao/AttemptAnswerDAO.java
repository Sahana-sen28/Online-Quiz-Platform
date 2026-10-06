package quizmaster.dao;

import quizmaster.model.AttemptAnswer;
import quizmaster.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AttemptAnswers table.
 */
public class AttemptAnswerDAO {

    /**
     * Creates a batch of AttemptAnswers. This method manages its own connection and transaction.
     * @param answers The list of answers to insert.
     * @return true if successful, false otherwise.
     */
    public boolean createBatch(List<AttemptAnswer> answers) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            boolean result = createBatch(conn, answers);
            if (result) {
                conn.commit();
            } else {
                conn.rollback();
            }
            return result;
        } catch (SQLException e) {
            System.err.println("Error executing batch create for attempt answers: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates a batch of AttemptAnswers using an existing connection for transaction support.
     * @param conn The existing connection.
     * @param answers The list of answers to insert.
     * @return true if successful, false otherwise.
     */
    public boolean createBatch(Connection conn, List<AttemptAnswer> answers) {
        if (answers == null || answers.isEmpty()) return true;
        
        String idSql = "SELECT NVL(MAX(ATTEMPT_ANSWER_ID), 0) FROM ATTEMPT_ANSWERS";
        String insertSql = "INSERT INTO ATTEMPT_ANSWERS (ATTEMPT_ANSWER_ID, ATTEMPT_ID, QUESTION_ID, SELECTED_OPTION, IS_CORRECT, MARKS_OBTAINED) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement idPs = conn.prepareStatement(idSql);
             ResultSet idRs = idPs.executeQuery()) {
             
            int startId = 0;
            if (idRs.next()) {
                startId = idRs.getInt(1);
            }
            
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (AttemptAnswer ans : answers) {
                    startId++;
                    ps.setInt(1, startId);
                    ps.setInt(2, ans.getAttemptId());
                    ps.setInt(3, ans.getQuestionId());
                    
                    if (ans.getSelectedOption() != null) {
                        ps.setString(4, ans.getSelectedOption());
                    } else {
                        ps.setNull(4, Types.CHAR);
                    }
                    
                    if (ans.getIsCorrect() != null) {
                        ps.setString(5, ans.getIsCorrect());
                    } else {
                        ps.setNull(5, Types.CHAR);
                    }
                    
                    ps.setDouble(6, ans.getMarksObtained());
                    
                    ps.addBatch();
                }
                
                int[] results = ps.executeBatch();
                return results.length == answers.size();
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing createBatch: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all answers for a given attempt ID.
     * @param attemptId the attempt ID
     * @return List of AttemptAnswer for the attempt.
     */
    public List<AttemptAnswer> findByAttemptId(int attemptId) {
        List<AttemptAnswer> list = new ArrayList<>();
        String sql = "SELECT ATTEMPT_ANSWER_ID, ATTEMPT_ID, QUESTION_ID, SELECTED_OPTION, IS_CORRECT, MARKS_OBTAINED FROM ATTEMPT_ANSWERS WHERE ATTEMPT_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attemptId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AttemptAnswer aa = new AttemptAnswer();
                    aa.setAttemptAnswerId(rs.getInt("ATTEMPT_ANSWER_ID"));
                    aa.setAttemptId(rs.getInt("ATTEMPT_ID"));
                    aa.setQuestionId(rs.getInt("QUESTION_ID"));
                    aa.setSelectedOption(rs.getString("SELECTED_OPTION"));
                    aa.setIsCorrect(rs.getString("IS_CORRECT"));
                    aa.setMarksObtained(rs.getDouble("MARKS_OBTAINED"));
                    list.add(aa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding attempt answers by attempt ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
