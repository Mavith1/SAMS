package com.sams.view;

import com.sams.model.Lecturer;
import com.sams.service.LecturerService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class LecturerForm extends JFrame {

    private final LecturerService lecturerService = new LecturerService();
    private int selectedLecturerId = -1;

    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtSubject;
    private JTable tblLecturers;

    public LecturerForm() {
        initComponents();
        loadLecturers();
    }

    private void initComponents() {
        setTitle("Lecturer Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 460);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);

        JPanel formPanel = AppUI.card();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = AppUI.textField(24);
        txtEmail = AppUI.textField(24);
        txtSubject = AppUI.textField(24);

        addFormRow(formPanel, gbc, 0, "Name", txtName);
        addFormRow(formPanel, gbc, 1, "Email", txtEmail);
        addFormRow(formPanel, gbc, 2, "Subject", txtSubject);

        JButton btnSave = AppUI.button("Save", AppUI.GREEN);
        JButton btnUpdate = AppUI.button("Update", AppUI.BLUE);
        JButton btnDelete = AppUI.button("Delete", AppUI.RED);
        JButton btnClear = AppUI.button("Clear", AppUI.GRAY);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        tblLecturers = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Subject"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        AppUI.table(tblLecturers);
        tblLecturers.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectLecturerFromTable();
            }
        });

        btnSave.addActionListener(e -> saveLecturer());
        btnUpdate.addActionListener(e -> updateLecturer());
        btnDelete.addActionListener(e -> deleteLecturer());
        btnClear.addActionListener(e -> clearFields());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppUI.BACKGROUND);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 0, 22));
        header.add(AppUI.title("Lecturer Management"), BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 22, 22, 22));
        center.add(formPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(tblLecturers), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(AppUI.label(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadLecturers() {
        DefaultTableModel model = (DefaultTableModel) tblLecturers.getModel();
        model.setRowCount(0);
        for (Lecturer lecturer : lecturerService.getAllLecturersList()) {
            model.addRow(new Object[]{
                lecturer.getId(),
                lecturer.getName(),
                lecturer.getEmail(),
                lecturer.getSubject()
            });
        }
    }

    private Lecturer readLecturerFromFields() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(selectedLecturerId);
        lecturer.setName(txtName.getText().trim());
        lecturer.setEmail(txtEmail.getText().trim());
        lecturer.setSubject(txtSubject.getText().trim());
        return lecturer;
    }

    private void saveLecturer() {
        if (!validateLecturerInput()) {
            return;
        }
        if (lecturerService.registerLecturer(readLecturerFromFields())) {
            JOptionPane.showMessageDialog(this, "Lecturer saved successfully.");
            loadLecturers();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all lecturer details.");
        }
    }

    private void updateLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer from the table.");
            return;
        }
        if (!validateLecturerInput()) {
            return;
        }
        if (lecturerService.modifyLecturer(readLecturerFromFields())) {
            JOptionPane.showMessageDialog(this, "Lecturer updated successfully.");
            loadLecturers();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating lecturer.");
        }
    }

    private boolean validateLecturerInput() {
        if (ValidationUtil.isBlank(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "Lecturer name is required.");
            txtName.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid lecturer email address.");
            txtEmail.requestFocus();
            return false;
        }
        if (ValidationUtil.isBlank(txtSubject.getText())) {
            JOptionPane.showMessageDialog(this, "Assigned subject is required.");
            txtSubject.requestFocus();
            return false;
        }
        return true;
    }

    private void deleteLecturer() {
        if (selectedLecturerId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lecturer from the table.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this lecturer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && lecturerService.removeLecturer(selectedLecturerId)) {
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully.");
            loadLecturers();
            clearFields();
        }
    }

    private void selectLecturerFromTable() {
        int row = tblLecturers.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedLecturerId = Integer.parseInt(tblLecturers.getValueAt(row, 0).toString());
        txtName.setText(tblLecturers.getValueAt(row, 1).toString());
        txtEmail.setText(tblLecturers.getValueAt(row, 2).toString());
        txtSubject.setText(tblLecturers.getValueAt(row, 3).toString());
    }

    private void clearFields() {
        selectedLecturerId = -1;
        txtName.setText("");
        txtEmail.setText("");
        txtSubject.setText("");
        tblLecturers.clearSelection();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LecturerForm().setVisible(true));
    }
}
