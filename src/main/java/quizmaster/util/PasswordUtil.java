package quizmaster.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Utility for hashing passwords using SHA-256.
 *
 * This class can also be run as a standalone program to generate
 * SHA-256 hashes for existing test accounts in the database.
 *
 * To generate a hash from the command line:
 *   mvn exec:java -Dexec.mainClass="quizmaster.util.PasswordUtil"
 */
public class PasswordUtil {

    /**
     * Hashes a plain-text password using SHA-256 and returns the
     * result as a lowercase hexadecimal string.
     *
     * @param plainPassword the plain-text password
     * @return the SHA-256 hash as a hex string
     */
    public static String hashPassword(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(
                    plainPassword.getBytes(StandardCharsets.UTF_8)
            );
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in all Java implementations
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Standalone entry point for generating password hashes.
     * Useful for converting existing plain-text test passwords
     * in the Oracle database to SHA-256 format.
     *
     * Run with:
     *   mvn exec:java -Dexec.mainClass="quizmaster.util.PasswordUtil"
     *
     * Then use the generated hash to update the database:
     *   UPDATE USERS SET PASSWORD_HASH = '<generated_hash>' WHERE USERNAME = '<username>';
     */
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   QuizMaster — SHA-256 Password Hash Generator  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("This tool generates SHA-256 hashes for passwords.");
        System.out.println("Use the generated hash to update PASSWORD_HASH in");
        System.out.println("the USERS table for existing test accounts.");
        System.out.println();
        System.out.println("Type a password and press Enter (type 'exit' to quit):");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Password: ");
            String input = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Goodbye!");
                break;
            }
            if (input.isEmpty()) {
                System.out.println("  (empty input, try again)");
                continue;
            }
            String hash = hashPassword(input);
            System.out.println("  SHA-256 Hash: " + hash);
            System.out.println();
            System.out.println("  SQL to update an existing user:");
            System.out.println("    UPDATE USERS SET PASSWORD_HASH = '" + hash + "'");
            System.out.println("    WHERE USERNAME = '<your_username>';");
            System.out.println("    COMMIT;");
            System.out.println();
        }
        scanner.close();
    }
}
