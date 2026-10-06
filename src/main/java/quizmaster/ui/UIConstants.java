package quizmaster.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class UIConstants {
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);
    
    public static final Color COLOR_PRIMARY = new Color(25, 118, 210);
    public static final Color COLOR_PRIMARY_DARK = new Color(13, 71, 161);
    public static final Color COLOR_SUCCESS = new Color(46, 125, 50);
    public static final Color COLOR_DANGER = new Color(198, 40, 40);
    public static final Color COLOR_WARNING = new Color(245, 124, 0);
    public static final Color COLOR_BG = new Color(245, 245, 245);
    public static final Color COLOR_WHITE = Color.WHITE;
    public static final Color COLOR_TEXT = new Color(33, 33, 33);
    public static final Color COLOR_TEXT_SECONDARY = new Color(117, 117, 117);
    
    public static final int PADDING = 20;

    public static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(COLOR_WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_PRIMARY);
        return label;
    }

    public static JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARY);
        panel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(COLOR_WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_NORMAL);
        field.setPreferredSize(new Dimension(200, 30));
    }

    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_NORMAL);
        box.setPreferredSize(new Dimension(200, 30));
    }
}
