package quizmaster.ui;

import quizmaster.model.QuizAttempt;
import quizmaster.model.User;

import javax.swing.*;
import java.awt.*;

public class ResultFrame extends JFrame {
    public ResultFrame(User user, QuizAttempt attempt) {
        setTitle("Quiz Results");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Quiz Results"), BorderLayout.NORTH);

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(new javax.swing.border.EmptyBorder(20, 50, 20, 50));
        
        JLabel catLabel = new JLabel("Category: " + (attempt.getCategoryName() != null ? attempt.getCategoryName() : "Mixed Quiz"));
        catLabel.setFont(UIConstants.FONT_SUBTITLE);
        p.add(catLabel);
        
        p.add(createLabel("Total Questions: " + attempt.getTotalQuestions()));
        p.add(createLabel("Attempted: " + attempt.getAttemptedQuestions()));
        p.add(createLabel("Correct Answers: " + attempt.getCorrectAnswers()));
        p.add(createLabel("Incorrect Answers: " + attempt.getIncorrectAnswers()));
        p.add(createLabel("Unanswered: " + attempt.getUnansweredQuestions()));
        p.add(createLabel("Total Marks: " + attempt.getTotalMarks()));
        p.add(createLabel("Obtained Marks: " + attempt.getObtainedMarks()));
        
        JLabel percLabel = new JLabel(String.format("Percentage: %.2f%%", attempt.getPercentage()));
        percLabel.setFont(UIConstants.FONT_SUBTITLE);
        if (attempt.getPercentage() >= 60.0) {
            percLabel.setForeground(UIConstants.COLOR_SUCCESS);
        } else {
            percLabel.setForeground(UIConstants.COLOR_DANGER);
        }
        p.add(percLabel);
        
        add(p, BorderLayout.CENTER);

        JPanel bp = new JPanel();
        JButton historyBtn = UIConstants.createButton("View History", UIConstants.COLOR_PRIMARY);
        historyBtn.addActionListener(e -> { new ResultHistoryFrame(user).setVisible(true); dispose(); });
        JButton dashBtn = UIConstants.createButton("Back to Dashboard", UIConstants.COLOR_PRIMARY);
        dashBtn.addActionListener(e -> { new StudentDashboardFrame(user).setVisible(true); dispose(); });
        JButton logoutBtn = UIConstants.createButton("Logout", UIConstants.COLOR_DANGER);
        logoutBtn.addActionListener(e -> { new LoginFrame().setVisible(true); dispose(); });
        
        bp.add(historyBtn); bp.add(dashBtn); bp.add(logoutBtn);
        add(bp, BorderLayout.SOUTH);
    }
    
    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_NORMAL);
        return l;
    }
}
