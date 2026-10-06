package quizmaster.ui;

import quizmaster.model.Category;
import quizmaster.model.Question;
import quizmaster.model.User;
import quizmaster.service.AdminService;
import quizmaster.service.QuizService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class QuestionManagementFrame extends JFrame {
    private User user;
    private AdminService adminService;
    private QuizService quizService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<CategoryItem> categoryFilterBox;
    private JComboBox<String> difficultyFilterBox;
    
    // Helper class for ComboBox items
    private static class CategoryItem {
        Integer id;
        String name;
        CategoryItem(Integer id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    public QuestionManagementFrame(User user) {
        this.user = user;
        this.adminService = new AdminService();
        this.quizService = new QuizService();

        setTitle("Question Management");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Question Management"), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout());
        categoryFilterBox = new JComboBox<>();
        categoryFilterBox.addItem(new CategoryItem(null, "All Categories"));
        for (Category c : quizService.getCategories()) {
            categoryFilterBox.addItem(new CategoryItem(c.getCategoryId(), c.getCategoryName()));
        }
        difficultyFilterBox = new JComboBox<>(new String[]{"All", "EASY", "MEDIUM", "HARD"});
        
        JButton filterBtn = UIConstants.createButton("Filter", UIConstants.COLOR_PRIMARY);
        filterBtn.addActionListener(e -> loadData());
        
        topPanel.add(new JLabel("Category:"));
        topPanel.add(categoryFilterBox);
        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(difficultyFilterBox);
        topPanel.add(filterBtn);
        
        add(topPanel, BorderLayout.NORTH); // Overwrite North, so we pack inside a header container
        
        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.add(UIConstants.createHeaderPanel("Question Management"), BorderLayout.NORTH);
        headerContainer.add(topPanel, BorderLayout.CENTER);
        add(headerContainer, BorderLayout.NORTH);

        String[] cols = {"ID", "Question", "Category", "Difficulty", "Marks", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton addBtn = UIConstants.createButton("Add Question", UIConstants.COLOR_SUCCESS);
        addBtn.addActionListener(e -> showQuestionDialog(null));
        JButton editBtn = UIConstants.createButton("Edit Question", UIConstants.COLOR_WARNING);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int qId = (int) tableModel.getValueAt(row, 0);
                Question q = getQuestionById(qId);
                if (q != null) showQuestionDialog(q);
            } else {
                JOptionPane.showMessageDialog(this, "Select a question to edit.");
            }
        });
        JButton deleteBtn = UIConstants.createButton("Delete Question", UIConstants.COLOR_DANGER);
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int qId = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (adminService.deleteQuestion(qId)) {
                            loadData();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error deleting question: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a question to delete.");
            }
        });
        JButton backBtn = UIConstants.createButton("Back", UIConstants.COLOR_TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            new AdminDashboardFrame(user).setVisible(true);
            dispose();
        });

        bottomPanel.add(addBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }
    
    private List<Question> currentList;

    private void loadData() {
        try {
            CategoryItem cat = (CategoryItem) categoryFilterBox.getSelectedItem();
            Integer catId = (cat != null) ? cat.id : null;
            String diff = difficultyFilterBox.getSelectedItem().toString();
            if ("All".equals(diff)) diff = null;
            
            if (catId == null && diff == null) {
                currentList = adminService.getAllQuestions();
            } else {
                currentList = adminService.getFilteredQuestions(catId, diff, null);
            }
            
            tableModel.setRowCount(0);
            for (Question q : currentList) {
                Vector<Object> row = new Vector<>();
                row.add(q.getQuestionId());
                row.add(q.getQuestionText());
                row.add(q.getCategoryName() != null ? q.getCategoryName() : "Unknown");
                row.add(q.getDifficulty());
                row.add(q.getMarks());
                row.add(q.getActiveStatus());
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage());
        }
    }

    private Question getQuestionById(int id) {
        if (currentList == null) return null;
        for (Question q : currentList) {
            if (q.getQuestionId() == id) return q;
        }
        return null;
    }

    private void showQuestionDialog(Question q) {
        JDialog dialog = new JDialog(this, q == null ? "Add Question" : "Edit Question", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);
        
        JPanel p = new JPanel(new GridLayout(0, 2, 10, 10));
        p.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        
        JTextArea qText = new JTextArea(3, 20);
        JComboBox<CategoryItem> catBox = new JComboBox<>();
        for (Category c : quizService.getCategories()) {
            catBox.addItem(new CategoryItem(c.getCategoryId(), c.getCategoryName()));
        }
        JComboBox<String> diffBox = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
        JTextField optA = new JTextField();
        JTextField optB = new JTextField();
        JTextField optC = new JTextField();
        JTextField optD = new JTextField();
        JComboBox<String> correctOptBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        JTextField marksField = new JTextField("1.0");
        JComboBox<String> activeBox = new JComboBox<>(new String[]{"Y", "N"});

        if (q != null) {
            qText.setText(q.getQuestionText());
            for (int i=0; i<catBox.getItemCount(); i++) {
                if (catBox.getItemAt(i).id == q.getCategoryId()) { catBox.setSelectedIndex(i); break; }
            }
            diffBox.setSelectedItem(q.getDifficulty());
            optA.setText(q.getOptionA());
            optB.setText(q.getOptionB());
            optC.setText(q.getOptionC());
            optD.setText(q.getOptionD());
            correctOptBox.setSelectedItem(q.getCorrectOption());
            marksField.setText(String.valueOf(q.getMarks()));
            activeBox.setSelectedItem(q.getActiveStatus());
        }

        p.add(new JLabel("Question Text:")); p.add(new JScrollPane(qText));
        p.add(new JLabel("Category:")); p.add(catBox);
        p.add(new JLabel("Difficulty:")); p.add(diffBox);
        p.add(new JLabel("Option A:")); p.add(optA);
        p.add(new JLabel("Option B:")); p.add(optB);
        p.add(new JLabel("Option C:")); p.add(optC);
        p.add(new JLabel("Option D:")); p.add(optD);
        p.add(new JLabel("Correct Option:")); p.add(correctOptBox);
        p.add(new JLabel("Marks:")); p.add(marksField);
        p.add(new JLabel("Active (Y/N):")); p.add(activeBox);

        JPanel btnPanel = new JPanel();
        JButton saveBtn = UIConstants.createButton("Save", UIConstants.COLOR_PRIMARY);
        saveBtn.addActionListener(e -> {
            try {
                Question newQ = new Question();
                if (q != null) newQ.setQuestionId(q.getQuestionId());
                newQ.setQuestionText(qText.getText().trim());
                newQ.setCategoryId(((CategoryItem)catBox.getSelectedItem()).id);
                newQ.setDifficulty(diffBox.getSelectedItem().toString());
                newQ.setOptionA(optA.getText().trim());
                newQ.setOptionB(optB.getText().trim());
                newQ.setOptionC(optC.getText().trim());
                newQ.setOptionD(optD.getText().trim());
                newQ.setCorrectOption(correctOptBox.getSelectedItem().toString());
                newQ.setMarks(Double.parseDouble(marksField.getText().trim()));
                newQ.setActiveStatus(activeBox.getSelectedItem().toString());
                
                boolean res = q == null ? adminService.addQuestion(newQ) : adminService.updateQuestion(newQ);
                if (res) {
                    dialog.dispose();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to save question.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        JButton cancelBtn = UIConstants.createButton("Cancel", UIConstants.COLOR_TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(saveBtn); btnPanel.add(cancelBtn);
        dialog.add(p, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
