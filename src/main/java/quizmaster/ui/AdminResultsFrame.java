package quizmaster.ui;

import quizmaster.model.QuizAttempt;
import quizmaster.model.User;
import quizmaster.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class AdminResultsFrame extends JFrame {
    private User user;
    private AdminService adminService;
    private JTable table;
    private DefaultTableModel tableModel;

    public AdminResultsFrame(User user) {
        this.user = user;
        this.adminService = new AdminService();

        setTitle("Student Results");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Student Results"), BorderLayout.NORTH);

        String[] cols = {"Attempt ID", "Student", "Category", "Date", "Total Q", "Correct", "Incorrect", "Unanswered", "Marks", "Percentage"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backBtn = UIConstants.createButton("Back", UIConstants.COLOR_TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            new AdminDashboardFrame(user).setVisible(true);
            dispose();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        try {
            List<QuizAttempt> attempts = adminService.getAllAttempts();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (QuizAttempt a : attempts) {
                Vector<Object> row = new Vector<>();
                row.add(a.getAttemptId());
                row.add(a.getStudentName() != null ? a.getStudentName() : "User " + a.getUserId());
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading results: " + e.getMessage());
        }
    }
}
