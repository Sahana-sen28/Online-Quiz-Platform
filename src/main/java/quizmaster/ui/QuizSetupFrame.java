package quizmaster.ui;

import quizmaster.model.Category;
import quizmaster.model.Question;
import quizmaster.model.User;
import quizmaster.service.QuizService;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class QuizSetupFrame extends JFrame {
    private User user;
    private QuizService quizService;
    
    private JComboBox<CategoryItem> categoryBox;
    private JComboBox<String> difficultyBox;
    private JComboBox<Integer> countBox;
    private JLabel availableLabel;

    private static class CategoryItem {
        Integer id;
        String name;
        CategoryItem(Integer id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }

    public QuizSetupFrame(User user) {
        this.user = user;
        this.quizService = new QuizService();

        setTitle("Quiz Setup");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UIConstants.createHeaderPanel("Quiz Setup"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        categoryBox = new JComboBox<>();
        categoryBox.addItem(new CategoryItem(null, "Mixed Quiz"));
        try {
            for (Category c : quizService.getCategories()) {
                categoryBox.addItem(new CategoryItem(c.getCategoryId(), c.getCategoryName()));
            }
        } catch(Exception e) {}

        difficultyBox = new JComboBox<>(new String[]{"EASY", "MEDIUM", "HARD"});
        countBox = new JComboBox<>(new Integer[]{5, 10, 15, 20});
        availableLabel = new JLabel("0 questions available");

        gbc.gridx = 0; gbc.gridy = 0; centerPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; centerPanel.add(categoryBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1; centerPanel.add(new JLabel("Difficulty:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; centerPanel.add(difficultyBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; centerPanel.add(new JLabel("Number of Questions:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; centerPanel.add(countBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(availableLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton startBtn = UIConstants.createButton("Start Quiz", UIConstants.COLOR_SUCCESS);
        startBtn.addActionListener(e -> startQuiz());
        JButton backBtn = UIConstants.createButton("Back", UIConstants.COLOR_TEXT_SECONDARY);
        backBtn.addActionListener(e -> {
            new StudentDashboardFrame(user).setVisible(true);
            dispose();
        });
        
        bottomPanel.add(startBtn);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        categoryBox.addActionListener(e -> updateAvailableCount());
        difficultyBox.addActionListener(e -> updateAvailableCount());
        updateAvailableCount();
    }

    private void updateAvailableCount() {
        try {
            CategoryItem cat = (CategoryItem) categoryBox.getSelectedItem();
            Integer catId = cat.id;
            String diff = (String) difficultyBox.getSelectedItem();
            int count = quizService.getAvailableQuestionCount(catId, diff);
            availableLabel.setText(count + " questions available");
        } catch (Exception ex) {}
    }

    private void startQuiz() {
        try {
            CategoryItem cat = (CategoryItem) categoryBox.getSelectedItem();
            Integer catId = cat.id;
            String diff = (String) difficultyBox.getSelectedItem();
            int requestedCount = (Integer) countBox.getSelectedItem();
            
            int available = quizService.getAvailableQuestionCount(catId, diff);
            if (available < requestedCount) {
                JOptionPane.showMessageDialog(this, "Not enough questions available. Please select a smaller number or change criteria.");
                return;
            }
            
            List<Question> questions = quizService.getQuizQuestions(catId, diff, requestedCount);
            if (questions == null || questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Failed to load questions.");
                return;
            }
            
            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            new QuizFrame(user, questions, catId, startTime).setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error starting quiz: " + ex.getMessage());
        }
    }
}
