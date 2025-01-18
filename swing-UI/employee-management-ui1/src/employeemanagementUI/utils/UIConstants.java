package employeemanagementUI.utils;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class UIConstants {
    public static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    public static final Color SECONDARY_COLOR = new Color(238, 238, 238);
    public static final Color ACCENT_COLOR = new Color(66, 139, 202);
    public static final Color ERROR_COLOR = new Color(217, 83, 79);
    public static final Color SUCCESS_COLOR = new Color(92, 184, 92);
    
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    public static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 12);
    
    public static void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(LABEL_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
}