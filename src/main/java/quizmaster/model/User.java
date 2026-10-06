package quizmaster.model;

import java.sql.Timestamp;

/**
 * Represents a row in the USERS table.
 *
 * Columns: USER_ID, USERNAME, PASSWORD_HASH, FULL_NAME, ROLE, CREATED_AT
 */
public class User {

    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role;          // "ADMIN" or "STUDENT"
    private Timestamp createdAt;

    public User() {
    }

    public User(int userId, String username, String passwordHash,
                String fullName, String role, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(role);
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username + "', role='" + role + "'}";
    }
}
