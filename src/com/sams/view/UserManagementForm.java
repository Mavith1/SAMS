package com.sams.view;

import com.sams.model.User;
import com.sams.service.UserService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class UserManagementForm extends JFrame {

    private final UserService userService = new UserService();
    private int selectedUserId = -1;

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JComboBox<String> cmbRole;
    private JTable tblUsers;

    public UserManagementForm() {
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(16, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppUI.BACKGROUND);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 0, 22));
        header.add(AppUI.title("User Management"), BorderLayout.WEST);

        JPanel form = AppUI.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = AppUI.textField(18);
        txtPassword = AppUI.textField(18);
        cmbRole = new JComboBox<>(new String[]{"Admin", "Lecturer"});

        addRow(form, gbc, 0, 0, "Username", txtUsername);
        addRow(form, gbc, 0, 2, "Password", txtPassword);
        addRow(form, gbc, 1, 0, "Role", cmbRole);

        javax.swing.JButton btnSave = AppUI.button("Save", AppUI.GREEN);
        javax.swing.JButton btnUpdate = AppUI.button("Update", AppUI.BLUE);
        javax.swing.JButton btnDelete = AppUI.button("Delete", AppUI.RED);
        javax.swing.JButton btnClear = AppUI.button("Clear", AppUI.GRAY);
        JPanel buttons = new JPanel();
        buttons.setBackground(java.awt.Color.WHITE);
        buttons.add(btnSave);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        tblUsers = new JTable(new DefaultTableModel(new Object[]{"ID", "Username", "Password", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        AppUI.table(tblUsers);
        tblUsers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectUser();
            }
        });

        btnSave.addActionListener(e -> saveUser());
        btnUpdate.addActionListener(e -> updateUser());
        btnDelete.addActionListener(e -> deleteUser());
        btnClear.addActionListener(e -> clearFields());

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 22, 22, 22));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(tblUsers), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, int col, String label, java.awt.Component field) {
        gbc.gridwidth = 1;
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(AppUI.label(label), gbc);
        gbc.gridx = col + 1;
        panel.add(field, gbc);
    }

    private void loadUsers() {
        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0);
        for (User user : userService.getAllUsers()) {
            model.addRow(new Object[]{user.getUserId(), user.getUsername(), user.getPassword(), user.getRole()});
        }
    }

    private User readUser() {
        User user = new User();
        user.setUserId(selectedUserId);
        user.setUsername(txtUsername.getText().trim());
        user.setPassword(txtPassword.getText().trim());
        user.setRole(cmbRole.getSelectedItem().toString());
        return user;
    }

    private void saveUser() {
        if (!validateUserInput()) {
            return;
        }
        if (userService.registerUser(readUser())) {
            JOptionPane.showMessageDialog(this, "User saved successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter username, password, and role.");
        }
    }

    private void updateUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table.");
            return;
        }
        if (!validateUserInput()) {
            return;
        }
        if (userService.modifyUser(readUser())) {
            JOptionPane.showMessageDialog(this, "User updated successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "User could not be updated.");
        }
    }

    private boolean validateUserInput() {
        if (!ValidationUtil.isValidUsername(txtUsername.getText())) {
            JOptionPane.showMessageDialog(this, "Username must be 3-30 characters and use only letters, numbers, or underscore.");
            txtUsername.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidPassword(txtPassword.getText())) {
            JOptionPane.showMessageDialog(this, "Password must contain at least 6 characters.");
            txtPassword.requestFocus();
            return false;
        }
        if (cmbRole.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            return false;
        }
        return true;
    }

    private void deleteUser() {
        if (selectedUserId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user from the table.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete selected user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && userService.removeUser(selectedUserId)) {
            JOptionPane.showMessageDialog(this, "User deleted successfully.");
            refresh();
        }
    }

    private void selectUser() {
        int row = tblUsers.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedUserId = Integer.parseInt(tblUsers.getValueAt(row, 0).toString());
        txtUsername.setText(tblUsers.getValueAt(row, 1).toString());
        txtPassword.setText(tblUsers.getValueAt(row, 2).toString());
        cmbRole.setSelectedItem(tblUsers.getValueAt(row, 3).toString());
    }

    private void refresh() {
        loadUsers();
        clearFields();
    }

    private void clearFields() {
        selectedUserId = -1;
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        tblUsers.clearSelection();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new UserManagementForm().setVisible(true));
    }
}
