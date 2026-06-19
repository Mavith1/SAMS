package com.sams.view;

import com.sams.dao.UserDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        initComponents();
    }

    private void initComponents() {
        setTitle("SAMS - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel hero = new JPanel(new GridBagLayout());
        hero.setBackground(AppUI.NAVY);
        hero.setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36));

        JLabel appName = new JLabel("SAMS");
        appName.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 42));
        appName.setForeground(Color.WHITE);
        JLabel appText = new JLabel("<html>Student Attendance<br>Management System</html>");
        appText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 18));
        appText.setForeground(new Color(209, 213, 219));

        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = 0;
        left.anchor = GridBagConstraints.WEST;
        hero.add(appName, left);
        left.gridy = 1;
        left.insets = new Insets(10, 0, 0, 0);
        hero.add(appText, left);

        JPanel loginCard = AppUI.card();
        loginCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = AppUI.title("Login");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        loginCard.add(title, gbc);

        gbc.gridy++;
        JLabel hint = AppUI.subtitle("Use your admin or lecturer account");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        loginCard.add(hint, gbc);

        txtUsername = AppUI.textField(20);
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        txtPassword.setBorder(txtUsername.getBorder());

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        loginCard.add(AppUI.label("Username"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtUsername, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        loginCard.add(AppUI.label("Password"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtPassword, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        javax.swing.JButton btnLogin = AppUI.button("Login", AppUI.BLUE);
        loginCard.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());

        add(hero, BorderLayout.WEST);
        add(loginCard, BorderLayout.CENTER);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        String role = new UserDAO().checkLogin(username, password);
        if ("Admin".equalsIgnoreCase(role)) {
            new AdminDashboard().setVisible(true);
            dispose();
        } else if ("Lecturer".equalsIgnoreCase(role)) {
            new LecturerDashboard().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
