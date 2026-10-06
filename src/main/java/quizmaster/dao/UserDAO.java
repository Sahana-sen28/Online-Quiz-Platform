package quizmaster.dao;

import quizmaster.model.User;
import quizmaster.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Data Access Object for Users table.
 */
public class UserDAO {

    /**
     * Finds a user by their username.
     * @param username the username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD_HASH, FULL_NAME, ROLE, CREATED_AT FROM USERS WHERE USERNAME = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new user in the database.
     * @param user the User object to create
     * @return true if created successfully, false otherwise
     */
    public boolean createUser(User user) {
        String idSql = "SELECT NVL(MAX(USER_ID), 0) + 1 FROM USERS";
        String insertSql = "INSERT INTO USERS (USER_ID, USERNAME, PASSWORD_HASH, FULL_NAME, ROLE, CREATED_AT) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement idPs = conn.prepareStatement(idSql);
             ResultSet idRs = idPs.executeQuery()) {
            
            int newId = 1;
            if (idRs.next()) {
                newId = idRs.getInt(1);
            }
            
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, newId);
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getPasswordHash());
                ps.setString(4, user.getFullName());
                ps.setString(5, user.getRole());
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    user.setUserId(newId);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Finds a user by their ID.
     * @param userId the user ID to search for
     * @return User object if found, null otherwise
     */
    public User findById(int userId) {
        String sql = "SELECT USER_ID, USERNAME, PASSWORD_HASH, FULL_NAME, ROLE, CREATED_AT FROM USERS WHERE USER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("USER_ID"));
        user.setUsername(rs.getString("USERNAME"));
        user.setPasswordHash(rs.getString("PASSWORD_HASH"));
        user.setFullName(rs.getString("FULL_NAME"));
        user.setRole(rs.getString("ROLE"));
        Timestamp ts = rs.getTimestamp("CREATED_AT");
        if (ts != null) {
            user.setCreatedAt(ts);
        }
        return user;
    }
}
