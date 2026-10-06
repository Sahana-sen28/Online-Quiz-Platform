package quizmaster.ui;

import quizmaster.model.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class StudentDashboardFrame extends JFrame {
    private User user;

    public StudentDashboardFrame(User user) {
        this.user = user;
        setTitle("Student Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Student Dashboard"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        centerPanel.setBorder(new EmptyBorder(40, 100, 40, 100));
        centerPanel.setBackground(UIConstants.COLOR_BG);

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName(), SwingConstants.CENTER);
        welcomeLabel.setFont(UIConstants.FONT_SUBTITLE);
        centerPanel.add(welcomeLabel);

        JButton startQuizBtn = UIConstants.createButton("Start Quiz", UIConstants.COLOR_SUCCESS);
        startQuizBtn.addActionListener(e -> {
            new QuizSetupFrame(user).setVisible(true);
            dispose();
        });
        centerPanel.add(startQuizBtn);

        JButton myResultsBtn = UIConstants.createButton("My Results", UIConstants.COLOR_PRIMARY);
        myResultsBtn.addActionListener(e -> {
            new ResultHistoryFrame(user).setVisible(true);
            dispose();
        });
        centerPanel.add(myResultsBtn);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UIConstants.COLOR_BG);
        JButton logoutBtn = UIConstants.createButton("Logout", UIConstants.COLOR_DANGER);
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
