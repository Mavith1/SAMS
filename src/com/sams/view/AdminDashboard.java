package com.sams.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        initComponents();
    }

    private void initComponents() {
        setTitle("SAMS - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 520);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(18, 18));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 24, 18, 24));
        header.add(AppUI.title("Admin Dashboard"), BorderLayout.WEST);
        header.add(AppUI.subtitle("Manage master data, schedules, attendance, and reports"), BorderLayout.SOUTH);

        JPanel grid = new JPanel(new GridLayout(3, 3, 16, 16));
        grid.setBackground(AppUI.BACKGROUND);
        grid.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 24, 20, 24));

        grid.add(navButton("Student Management", "Register, update, search, and delete students", () -> new StudentForm().setVisible(true)));
        grid.add(navButton("Course Management", "Maintain course records", () -> new CourseForm().setVisible(true)));
        grid.add(navButton("Lecturer Management", "Maintain lecturers and subjects", () -> new LecturerForm().setVisible(true)));
        grid.add(navButton("Class Scheduling", "Create and manage class sessions", () -> new ClassScheduleForm().setVisible(true)));
        grid.add(navButton("Attendance Marking", "Mark present, absent, or late", () -> new AttendanceMarkingForm().setVisible(true)));
        grid.add(navButton("Attendance Reports", "Filter attendance by student, course, subject, and date", () -> new AttendanceReportForm().setVisible(true)));
        grid.add(navButton("User Management", "Maintain admin and lecturer login accounts", () -> new UserManagementForm().setVisible(true)));

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
        java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
