package quizmaster.ui;

import quizmaster.model.User;
import quizmaster.service.AuthenticationService;
import quizmaster.util.AppConfig;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthenticationService authService;

    public LoginFrame() {
        authService = new AuthenticationService();
        setTitle(AppConfig.APP_TITLE);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel(AppConfig.APP_TITLE), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIConstants.COLOR_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UIConstants.FONT_NORMAL);
        centerPanel.add(userLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField();
        UIConstants.styleTextField(usernameField);
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UIConstants.FONT_NORMAL);
        centerPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField();
        UIConstants.styleTextField(passwordField);
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginBtn = UIConstants.createButton("Login", UIConstants.COLOR_PRIMARY);
        loginBtn.addActionListener(e -> login());
        centerPanel.add(loginBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIConstants.COLOR_BG);
        JButton registerBtn = new JButton("<html><u>Don't have an account? Register</u></html>");
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setForeground(UIConstants.COLOR_PRIMARY);
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        bottomPanel.add(registerBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            User user = authService.login(username, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (user.isAdmin()) {
                new AdminDashboardFrame(user).setVisible(true);
                dispose();
            } else if (user.isStudent()) {
                new StudentDashboardFrame(user).setVisible(true);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
