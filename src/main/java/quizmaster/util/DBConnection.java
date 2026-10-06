package quizmaster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a centralized mechanism for obtaining JDBC connections
 * to the Oracle database.
 *
 * Usage:
 *   Connection conn = DBConnection.getConnection();
 */
public class DBConnection {

    // Static initializer — load the Oracle JDBC driver once
    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("==========================================================");
            System.err.println("ERROR: Oracle JDBC Driver (ojdbc) not found on classpath!");
            System.err.println("Make sure the Oracle JDBC dependency is available.");
            System.err.println("If using Maven, run: mvn clean compile exec:java");
            System.err.println("==========================================================");
        }
    }

    /**
     * Creates and returns a new database connection using the credentials
     * defined in AppConfig.
     *
     * @return a live Connection to the Oracle database
     * @throws SQLException if the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                    AppConfig.DB_URL,
                    AppConfig.DB_USERNAME,
                    AppConfig.DB_PASSWORD
            );
        } catch (SQLException e) {
            System.err.println("==========================================================");
            System.err.println("ERROR: Unable to connect to Oracle Database.");
            System.err.println("URL      : " + AppConfig.DB_URL);
            System.err.println("Username : " + AppConfig.DB_USERNAME);
            System.err.println();
            System.err.println("Please check:");
            System.err.println("  1. Oracle Database is running");
            System.err.println("  2. DB_URL in AppConfig.java is correct");
            System.err.println("  3. DB_USERNAME and DB_PASSWORD are correct");
            System.err.println("  4. Oracle Listener is running (lsnrctl status)");
            System.err.println("  5. Oracle JDBC driver is on the classpath");
            System.err.println();
            System.err.println("Oracle Error: " + e.getMessage());
            System.err.println("==========================================================");
            throw e;
        }
    }

    /**
     * Tests the database connection and prints the result.
     *
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful!");
                return true;
            }
        } catch (SQLException e) {
            // Error already printed in getConnection()
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Testing Oracle Database Connection...");
        System.out.println("URL: " + AppConfig.DB_URL);
        System.out.println("Username: " + AppConfig.DB_USERNAME);
        boolean success = testConnection();
        if (success) {
            System.out.println("SUCCESS: Database connection established successfully!");
        } else {
            System.out.println("FAILURE: Could not connect to database. Please update DB_URL, DB_USERNAME, and DB_PASSWORD in AppConfig.java.");
        }
    }

    // Private constructor — this class should not be instantiated
    private DBConnection() {
    }
}
