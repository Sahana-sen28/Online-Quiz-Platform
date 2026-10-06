package quizmaster.ui;

import quizmaster.model.Question;
import quizmaster.model.QuizAttempt;
import quizmaster.model.User;
import quizmaster.service.QuizService;
import quizmaster.util.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizFrame extends JFrame {
    private User user;
    private List<Question> questions;
    private Integer categoryId;
    private Timestamp startTime;
    private QuizService quizService;
    
    private int currentIndex = 0;
    private Map<Integer, String> answers = new HashMap<>();
    private boolean submitted = false;
    
    private JLabel progressLabel;
    private JLabel timerLabel;
    private JTextArea questionText;
    private JRadioButton optA, optB, optC, optD;
    private ButtonGroup bg;
    private JButton prevBtn, nextBtn, submitBtn;
    private JLabel answeredCountLabel;
    
    private int timeRemaining;
    private Timer timer;

    public QuizFrame(User user, List<Question> questions, Integer categoryId, Timestamp startTime) {
        this.user = user;
        this.questions = questions;
        this.categoryId = categoryId;
        this.startTime = startTime;
        this.quizService = new QuizService();
        this.timeRemaining = AppConfig.QUIZ_TIME_SECONDS;

        setTitle("Quiz in Progress");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (!submitted) {
                    int ans = JOptionPane.showConfirmDialog(QuizFrame.this, "Quiz is running! Are you sure you want to exit? (Progress will be lost)", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (ans == JOptionPane.YES_OPTION) {
                        timer.stop();
                        new StudentDashboardFrame(user).setVisible(true);
                        dispose();
                    }
                }
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        progressLabel = new JLabel("Question 1 of " + questions.size());
        progressLabel.setFont(UIConstants.FONT_SUBTITLE);
        timerLabel = new JLabel("Time Remaining: --:--");
        timerLabel.setFont(UIConstants.FONT_SUBTITLE);
        timerLabel.setForeground(UIConstants.COLOR_DANGER);
        topPanel.add(progressLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        
        questionText = new JTextArea();
        questionText.setEditable(false);
        questionText.setLineWrap(true);
        questionText.setWrapStyleWord(true);
        questionText.setFont(UIConstants.FONT_SUBTITLE);
        questionText.setBackground(UIConstants.COLOR_BG);
        centerPanel.add(new JScrollPane(questionText), BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optA = new JRadioButton(); optA.setFont(UIConstants.FONT_NORMAL);
        optB = new JRadioButton(); optB.setFont(UIConstants.FONT_NORMAL);
        optC = new JRadioButton(); optC.setFont(UIConstants.FONT_NORMAL);
        optD = new JRadioButton(); optD.setFont(UIConstants.FONT_NORMAL);
        bg = new ButtonGroup();
        bg.add(optA); bg.add(optB); bg.add(optC); bg.add(optD);
        optionsPanel.add(optA); optionsPanel.add(optB); optionsPanel.add(optC); optionsPanel.add(optD);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        
        answeredCountLabel = new JLabel("Answered: 0/" + questions.size());
        centerPanel.add(answeredCountLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        prevBtn = UIConstants.createButton("Previous", UIConstants.COLOR_PRIMARY);
        prevBtn.addActionListener(e -> { saveCurrentAnswer(); currentIndex--; loadQuestion(); });
        
        nextBtn = UIConstants.createButton("Next", UIConstants.COLOR_PRIMARY);
        nextBtn.addActionListener(e -> { saveCurrentAnswer(); currentIndex++; loadQuestion(); });
        
        submitBtn = UIConstants.createButton("Submit", UIConstants.COLOR_SUCCESS);
        submitBtn.addActionListener(e -> {
            saveCurrentAnswer();
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) doSubmit(false);
        });
        
        bottomPanel.add(prevBtn); bottomPanel.add(nextBtn); bottomPanel.add(submitBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadQuestion();

        timer = new Timer(1000, e -> {
            timeRemaining--;
            updateTimerLabel();
            if (timeRemaining <= 0) {
                timer.stop();
                saveCurrentAnswer();
                JOptionPane.showMessageDialog(this, "Time's up! Quiz auto-submitted.");
                doSubmit(true);
            }
        });
        timer.start();
        updateTimerLabel();
    }
    
    private void updateTimerLabel() {
        int m = timeRemaining / 60;
        int s = timeRemaining % 60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d", m, s));
    }

    private void saveCurrentAnswer() {
        if (questions == null || questions.isEmpty()) return;
        Question q = questions.get(currentIndex);
        if (optA.isSelected()) answers.put(q.getQuestionId(), "A");
        else if (optB.isSelected()) answers.put(q.getQuestionId(), "B");
        else if (optC.isSelected()) answers.put(q.getQuestionId(), "C");
        else if (optD.isSelected()) answers.put(q.getQuestionId(), "D");
        updateAnsweredCount();
    }
    
    private void updateAnsweredCount() {
        answeredCountLabel.setText("Answered: " + answers.size() + "/" + questions.size());
    }

    private void loadQuestion() {
        Question q = questions.get(currentIndex);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
        questionText.setText(q.getQuestionText());
        optA.setText("A: " + q.getOptionA());
        optB.setText("B: " + q.getOptionB());
        optC.setText("C: " + q.getOptionC());
        optD.setText("D: " + q.getOptionD());
        
        bg.clearSelection();
        String saved = answers.get(q.getQuestionId());
        if ("A".equals(saved)) optA.setSelected(true);
        else if ("B".equals(saved)) optB.setSelected(true);
        else if ("C".equals(saved)) optC.setSelected(true);
        else if ("D".equals(saved)) optD.setSelected(true);
        
        prevBtn.setEnabled(currentIndex > 0);
        nextBtn.setEnabled(currentIndex < questions.size() - 1);
        updateAnsweredCount();
    }

    private void doSubmit(boolean auto) {
        if (submitted) return;
        submitted = true;
        timer.stop();
        Timestamp endTime = new Timestamp(System.currentTimeMillis());
        try {
            QuizAttempt attempt = quizService.submitQuiz(user.getUserId(), categoryId, questions, answers, startTime, endTime);
            if (attempt != null) {
                new ResultFrame(user, attempt).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save quiz attempt in database. Please check your database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
                new StudentDashboardFrame(user).setVisible(true);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error submitting quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
