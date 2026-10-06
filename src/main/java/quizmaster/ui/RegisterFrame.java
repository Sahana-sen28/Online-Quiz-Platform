package quizmaster.ui;

import quizmaster.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private AuthenticationService authService;

    public RegisterFrame() {
        authService = new AuthenticationService();
        setTitle("Register");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Student Registration"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIConstants.COLOR_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        fullNameField = new JTextField();
        UIConstants.styleTextField(fullNameField);
        centerPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        usernameField = new JTextField();
        UIConstants.styleTextField(usernameField);
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        passwordField = new JPasswordField();
        UIConstants.styleTextField(passwordField);
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        confirmPasswordField = new JPasswordField();
        UIConstants.styleTextField(confirmPasswordField);
        centerPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JButton registerBtn = UIConstants.createButton("Register", UIConstants.COLOR_SUCCESS);
        registerBtn.addActionListener(e -> register());
        centerPanel.add(registerBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIConstants.COLOR_BG);
        JButton backBtn = new JButton("<html><u>Back to Login</u></html>");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setForeground(UIConstants.COLOR_PRIMARY);
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void register() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (authService.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Username is already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = authService.register(username, password, fullName);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
