package quizmaster;

import quizmaster.ui.LoginFrame;
import quizmaster.ui.UIConstants;
import quizmaster.util.AppConfig;
import quizmaster.util.DBConnection;

import javax.swing.*;
import java.awt.Font;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Starting " + AppConfig.APP_TITLE + "...");
        
        if (!DBConnection.testConnection()) {
            JOptionPane.showMessageDialog(null, 
                "Unable to connect to Oracle Database.\nPlease ensure the database is running and configuration is correct.", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
