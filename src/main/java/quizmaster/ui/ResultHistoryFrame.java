package quizmaster.ui;

import quizmaster.model.QuizAttempt;
import quizmaster.model.User;
import quizmaster.service.ResultService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class ResultHistoryFrame extends JFrame {
    private User user;
    private ResultService resultService;

    public ResultHistoryFrame(User user) {
        this.user = user;
        this.resultService = new ResultService();

        setTitle("My Quiz History");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("My Quiz History"), BorderLayout.NORTH);

        String[] cols = {"Attempt ID", "Category", "Date", "Total Q", "Correct", "Incorrect", "Unanswered", "Marks", "Percentage"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backBtn = UIConstants.createButton("Back", UIConstants.COLOR_TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            new StudentDashboardFrame(user).setVisible(true);
            dispose();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            List<QuizAttempt> history = resultService.getStudentHistory(user.getUserId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (QuizAttempt a : history) {
                Vector<Object> row = new Vector<>();
                row.add(a.getAttemptId());
                row.add(a.getCategoryName() != null ? a.getCategoryName() : "Mixed Quiz");
                row.add(a.getStartTime() != null ? sdf.format(a.getStartTime()) : "N/A");
                row.add(a.getTotalQuestions());
                row.add(a.getCorrectAnswers());
                row.add(a.getIncorrectAnswers());
                row.add(a.getUnansweredQuestions());
                row.add(a.getObtainedMarks() + "/" + a.getTotalMarks());
                row.add(String.format("%.2f%%", a.getPercentage()));
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + ex.getMessage());
        }
    }
}
