package quizmaster.dao;

import quizmaster.model.Question;
import quizmaster.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Questions table.
 */
public class QuestionDAO {

    /**
     * Retrieves all questions.
     * @return List of all questions.
     */
    public List<Question> findAll() {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT Q.QUESTION_ID, Q.QUESTION_TEXT, Q.CATEGORY_ID, Q.DIFFICULTY, Q.OPTION_A, " +
                     "Q.OPTION_B, Q.OPTION_C, Q.OPTION_D, Q.CORRECT_OPTION, Q.MARKS, Q.ACTIVE_STATUS, C.CATEGORY_NAME " +
                     "FROM QUESTIONS Q JOIN CATEGORIES C ON Q.CATEGORY_ID = C.CATEGORY_ID ORDER BY Q.QUESTION_ID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToQuestion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all questions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves questions matching the provided filters.
     * Null filters are ignored.
     * @param categoryId the category ID (nullable)
     * @param difficulty the difficulty (nullable)
     * @param activeStatus the active status (nullable)
     * @return List of matching questions.
     */
    public List<Question> findByFilters(Integer categoryId, String difficulty, String activeStatus) {
        List<Question> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT Q.QUESTION_ID, Q.QUESTION_TEXT, Q.CATEGORY_ID, Q.DIFFICULTY, Q.OPTION_A, " +
            "Q.OPTION_B, Q.OPTION_C, Q.OPTION_D, Q.CORRECT_OPTION, Q.MARKS, Q.ACTIVE_STATUS, C.CATEGORY_NAME " +
            "FROM QUESTIONS Q JOIN CATEGORIES C ON Q.CATEGORY_ID = C.CATEGORY_ID WHERE 1=1 ");
        
        if (categoryId != null) sql.append(" AND Q.CATEGORY_ID = ?");
        if (difficulty != null && !difficulty.isEmpty()) sql.append(" AND Q.DIFFICULTY = ?");
        if (activeStatus != null && !activeStatus.isEmpty()) sql.append(" AND Q.ACTIVE_STATUS = ?");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int idx = 1;
            if (categoryId != null) ps.setInt(idx++, categoryId);
            if (difficulty != null && !difficulty.isEmpty()) ps.setString(idx++, difficulty);
            if (activeStatus != null && !activeStatus.isEmpty()) ps.setString(idx++, activeStatus);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToQuestion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering questions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieves a random set of active questions based on optional category and difficulty filters.
     * @param categoryId the category ID (nullable)
     * @param difficulty the difficulty (nullable)
     * @param count the number of questions to retrieve
     * @return List of matching questions in random order.
     */
    public List<Question> findRandomQuestions(Integer categoryId, String difficulty, int count) {
        List<Question> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT Q.QUESTION_ID, Q.QUESTION_TEXT, Q.CATEGORY_ID, Q.DIFFICULTY, Q.OPTION_A, " +
            "Q.OPTION_B, Q.OPTION_C, Q.OPTION_D, Q.CORRECT_OPTION, Q.MARKS, Q.ACTIVE_STATUS, C.CATEGORY_NAME " +
            "FROM QUESTIONS Q JOIN CATEGORIES C ON Q.CATEGORY_ID = C.CATEGORY_ID WHERE Q.ACTIVE_STATUS = 'Y' ");
                     
        if (categoryId != null) sql.append(" AND Q.CATEGORY_ID = ?");
        if (difficulty != null && !difficulty.isEmpty()) sql.append(" AND Q.DIFFICULTY = ?");
        
        sql.append(" ORDER BY DBMS_RANDOM.VALUE FETCH FIRST ? ROWS ONLY");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int idx = 1;
            if (categoryId != null) ps.setInt(idx++, categoryId);
            if (difficulty != null && !difficulty.isEmpty()) ps.setString(idx++, difficulty);
            ps.setInt(idx++, count);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToQuestion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding random questions: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Counts the number of available active questions based on filters.
     * @param categoryId the category ID (nullable)
     * @param difficulty the difficulty (nullable)
     * @return The number of available active questions.
     */
    public int countAvailable(Integer categoryId, String difficulty) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM QUESTIONS WHERE ACTIVE_STATUS = 'Y'");
        if (categoryId != null) sql.append(" AND CATEGORY_ID = ?");
        if (difficulty != null && !difficulty.isEmpty()) sql.append(" AND DIFFICULTY = ?");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
             
            int idx = 1;
            if (categoryId != null) ps.setInt(idx++, categoryId);
            if (difficulty != null && !difficulty.isEmpty()) ps.setString(idx++, difficulty);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting available questions: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Finds a specific question by its ID.
     * @param questionId the question ID
     * @return Question object if found, null otherwise.
     */
    public Question findById(int questionId) {
        String sql = "SELECT Q.QUESTION_ID, Q.QUESTION_TEXT, Q.CATEGORY_ID, Q.DIFFICULTY, Q.OPTION_A, " +
                     "Q.OPTION_B, Q.OPTION_C, Q.OPTION_D, Q.CORRECT_OPTION, Q.MARKS, Q.ACTIVE_STATUS, C.CATEGORY_NAME " +
                     "FROM QUESTIONS Q JOIN CATEGORIES C ON Q.CATEGORY_ID = C.CATEGORY_ID WHERE Q.QUESTION_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToQuestion(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding question by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new question in the database.
     * @param q the question to create
     * @return true if successful, false otherwise
     */
    public boolean create(Question q) {
        String idSql = "SELECT NVL(MAX(QUESTION_ID), 0) + 1 FROM QUESTIONS";
        String sql = "INSERT INTO QUESTIONS (QUESTION_ID, QUESTION_TEXT, CATEGORY_ID, DIFFICULTY, OPTION_A, OPTION_B, OPTION_C, OPTION_D, CORRECT_OPTION, MARKS, ACTIVE_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement idPs = conn.prepareStatement(idSql);
             ResultSet idRs = idPs.executeQuery()) {
             
            int newId = 1;
            if (idRs.next()) newId = idRs.getInt(1);
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newId);
                ps.setString(2, q.getQuestionText());
                ps.setInt(3, q.getCategoryId());
                ps.setString(4, q.getDifficulty());
                ps.setString(5, q.getOptionA());
                ps.setString(6, q.getOptionB());
                ps.setString(7, q.getOptionC());
                ps.setString(8, q.getOptionD());
                ps.setString(9, q.getCorrectOption());
                ps.setDouble(10, q.getMarks());
                ps.setString(11, q.getActiveStatus());
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    q.setQuestionId(newId);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating question: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates an existing question.
     * @param q the question to update
     * @return true if successful, false otherwise
     */
    public boolean update(Question q) {
        String sql = "UPDATE QUESTIONS SET QUESTION_TEXT = ?, CATEGORY_ID = ?, DIFFICULTY = ?, OPTION_A = ?, OPTION_B = ?, OPTION_C = ?, OPTION_D = ?, CORRECT_OPTION = ?, MARKS = ?, ACTIVE_STATUS = ? WHERE QUESTION_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q.getQuestionText());
            ps.setInt(2, q.getCategoryId());
            ps.setString(3, q.getDifficulty());
            ps.setString(4, q.getOptionA());
            ps.setString(5, q.getOptionB());
            ps.setString(6, q.getOptionC());
            ps.setString(7, q.getOptionD());
            ps.setString(8, q.getCorrectOption());
            ps.setDouble(9, q.getMarks());
            ps.setString(10, q.getActiveStatus());
            ps.setInt(11, q.getQuestionId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating question: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a question by its ID.
     * @param questionId the question ID
     * @return true if successful, false otherwise
     */
    public boolean delete(int questionId) {
        String sql = "DELETE FROM QUESTIONS WHERE QUESTION_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting question: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Question mapRowToQuestion(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setQuestionId(rs.getInt("QUESTION_ID"));
        q.setQuestionText(rs.getString("QUESTION_TEXT"));
        q.setCategoryId(rs.getInt("CATEGORY_ID"));
        q.setDifficulty(rs.getString("DIFFICULTY"));
        q.setOptionA(rs.getString("OPTION_A"));
        q.setOptionB(rs.getString("OPTION_B"));
        q.setOptionC(rs.getString("OPTION_C"));
        q.setOptionD(rs.getString("OPTION_D"));
        q.setCorrectOption(rs.getString("CORRECT_OPTION"));
        q.setMarks(rs.getDouble("MARKS"));
        q.setActiveStatus(rs.getString("ACTIVE_STATUS"));
        q.setCategoryName(rs.getString("CATEGORY_NAME"));
        return q;
    }
}
