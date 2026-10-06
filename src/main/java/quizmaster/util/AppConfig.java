package quizmaster.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized application configuration.
 *
 * Loads database configuration from 'db.properties' in the working directory
 * (or from environment variables). If not found, falls back to default settings.
 */
public class AppConfig {

    public static final String APP_TITLE = "QuizMaster — Online Quiz Platform";
    public static final String APP_VERSION = "1.0";

    public static String DB_URL = "jdbc:oracle:thin:@localhost:1521/freepdb1";
    public static String DB_USERNAME = "system";
    public static String DB_PASSWORD = "system";
    public static int QUIZ_TIME_SECONDS = 300;

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        Properties props = new Properties();

        // 1. Check for db.properties file in current directory
        File propFile = new File("db.properties");
        if (propFile.exists()) {
            try (InputStream input = new FileInputStream(propFile)) {
                props.load(input);
            } catch (Exception e) {
                System.err.println("Warning: Could not read db.properties: " + e.getMessage());
            }
        }

        // 2. Read values from properties file or fallback to environment variables / defaults
        String url = props.getProperty("DB_URL", System.getenv("DB_URL"));
        if (url != null && !url.trim().isEmpty()) {
            DB_URL = url.trim();
        }

        String user = props.getProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
        if (user != null && !user.trim().isEmpty()) {
            DB_USERNAME = user.trim();
        }

        String pass = props.getProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));
        if (pass != null && !pass.trim().isEmpty()) {
            DB_PASSWORD = pass.trim();
        }

        String timeStr = props.getProperty("QUIZ_TIME_SECONDS", System.getenv("QUIZ_TIME_SECONDS"));
        if (timeStr != null) {
            try {
                QUIZ_TIME_SECONDS = Integer.parseInt(timeStr.trim());
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private AppConfig() {
    }
}
