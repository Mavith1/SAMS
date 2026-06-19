package com.sams.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LecturerDashboard extends JFrame {

    public LecturerDashboard() {
        initComponents();
    }

    private void initComponents() {
        setTitle("SAMS - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 360);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(18, 18));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 24, 18, 24));
        header.add(AppUI.title("Lecturer Dashboard"), BorderLayout.WEST);
        header.add(AppUI.subtitle("Mark attendance and review attendance records"), BorderLayout.SOUTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 16, 16));
        grid.setBackground(AppUI.BACKGROUND);
        grid.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));
        grid.add(navButton("Attendance Marking", "Record Present, Absent, or Late for each student", () -> new AttendanceMarkingForm().setVisible(true)));
        grid.add(navButton("Attendance Reports", "View filtered attendance records", () -> new AttendanceReportForm().setVisible(true)));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(AppUI.BACKGROUND);
        footer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 24, 20, 24));
        JButton logout = AppUI.button("Logout", AppUI.GRAY);
        logout.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        footer.add(logout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton navButton(String title, String description, Runnable action) {
        JButton button = new JButton("<html><b>" + title + "</b><br><span style='font-weight:normal'>" + description + "</span></html>");
        button.setHorizontalAlignment(JButton.LEFT);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(AppUI.NAVY);
        button.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(229, 231, 235)),
                javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        button.addActionListener(e -> action.run());
        return button;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LecturerDashboard().setVisible(true));
    }
}
