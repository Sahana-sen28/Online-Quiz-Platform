package quizmaster.ui;

import quizmaster.model.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboardFrame extends JFrame {
    private User user;

    public AdminDashboardFrame(User user) {
        this.user = user;
        setTitle("Admin Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Admin Dashboard"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(new EmptyBorder(30, 100, 30, 100));
        centerPanel.setBackground(UIConstants.COLOR_BG);

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName(), SwingConstants.CENTER);
        welcomeLabel.setFont(UIConstants.FONT_SUBTITLE);
        centerPanel.add(welcomeLabel);

        JButton manageQuestionsBtn = UIConstants.createButton("Manage Questions", UIConstants.COLOR_PRIMARY);
        manageQuestionsBtn.addActionListener(e -> {
            new QuestionManagementFrame(user).setVisible(true);
            dispose();
        });
        centerPanel.add(manageQuestionsBtn);

        JButton viewResultsBtn = UIConstants.createButton("View Results", UIConstants.COLOR_PRIMARY);
        viewResultsBtn.addActionListener(e -> {
            new AdminResultsFrame(user).setVisible(true);
            dispose();
        });
        centerPanel.add(viewResultsBtn);

        JButton viewStatisticsBtn = UIConstants.createButton("View Statistics", UIConstants.COLOR_PRIMARY);
        viewStatisticsBtn.addActionListener(e -> {
            new StatisticsFrame(user).setVisible(true);
            dispose();
        });
        centerPanel.add(viewStatisticsBtn);

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
