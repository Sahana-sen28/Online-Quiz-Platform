package quizmaster.ui;

import quizmaster.model.User;
import quizmaster.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class StatisticsFrame extends JFrame {
    private User user;
    private AdminService adminService;

    public StatisticsFrame(User user) {
        this.user = user;
        this.adminService = new AdminService();

        setTitle("Quiz Statistics");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Quiz Statistics"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        summaryPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        
        JLabel totalAttemptsLabel = new JLabel("Total Attempts: 0");
        totalAttemptsLabel.setFont(UIConstants.FONT_SUBTITLE);
        JLabel avgScoreLabel = new JLabel("Average Score: 0%");
        avgScoreLabel.setFont(UIConstants.FONT_SUBTITLE);
        JLabel highestScoreLabel = new JLabel("Highest Score: 0%");
        highestScoreLabel.setFont(UIConstants.FONT_SUBTITLE);
        
        summaryPanel.add(totalAttemptsLabel);
        summaryPanel.add(avgScoreLabel);
        summaryPanel.add(highestScoreLabel);
        
        centerPanel.add(summaryPanel, BorderLayout.NORTH);

        String[] cols = {"Category", "Attempts", "Avg Score (%)", "Highest Score (%)"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backBtn = UIConstants.createButton("Back", UIConstants.COLOR_TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            new AdminDashboardFrame(user).setVisible(true);
            dispose();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            Map<String, Object> stats = adminService.getStatistics();
            if (stats != null) {
                totalAttemptsLabel.setText("Total Attempts: " + stats.get("totalAttempts"));
                avgScoreLabel.setText(String.format("Average Score: %.2f%%", (Double)stats.get("averageScore")));
                highestScoreLabel.setText(String.format("Highest Score: %.2f%%", (Double)stats.get("highestScore")));
                
                @SuppressWarnings("unchecked")
                List<Map<String,Object>> catStats = (List<Map<String,Object>>) stats.get("categoryStats");
                if (catStats != null) {
                    for (Map<String,Object> m : catStats) {
                        Vector<Object> row = new Vector<>();
                        row.add(m.get("categoryName"));
                        row.add(m.get("attemptCount"));
                        row.add(String.format("%.2f%%", (Double)m.get("avgScore")));
                        row.add(String.format("%.2f%%", (Double)m.get("highestScore")));
                        tableModel.addRow(row);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + ex.getMessage());
        }
    }
}
