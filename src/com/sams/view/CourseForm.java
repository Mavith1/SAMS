package com.sams.view;

import com.sams.model.Course;
import com.sams.service.CourseService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class CourseForm extends JFrame {

    private final CourseService courseService = new CourseService();
    private int selectedCourseId = -1;

    private JTextField txtCourseName;
    private JTable tblCourses;

    public CourseForm() {
        initComponents();
        loadCourses();
    }

    private void initComponents() {
        setTitle("Course Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(760, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(16, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppUI.BACKGROUND);
        header.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 0, 22));
        header.add(AppUI.title("Course Management"), BorderLayout.WEST);

        JPanel form = AppUI.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCourseName = AppUI.textField(26);
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(AppUI.label("Course Name"), gbc);
        gbc.gridx = 1;
        form.add(txtCourseName, gbc);

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
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        tblCourses = new JTable(new DefaultTableModel(new Object[]{"ID", "Course Code", "Course Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        AppUI.table(tblCourses);
        tblCourses.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectCourse();
            }
        });

        btnSave.addActionListener(e -> saveCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnClear.addActionListener(e -> clearFields());

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 22, 22, 22));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(tblCourses), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void loadCourses() {
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);
        for (Course course : courseService.getAllCoursesList()) {
            model.addRow(new Object[]{
                course.getCourseId(),
                "C-" + String.format("%03d", course.getCourseId()),
                course.getCourseName()
            });
        }
    }

    private Course readCourse() {
        Course course = new Course();
        course.setCourseId(selectedCourseId);
        course.setCourseCode(selectedCourseId == -1 ? "" : "C-" + String.format("%03d", selectedCourseId));
        course.setCourseName(txtCourseName.getText().trim());
        return course;
    }

    private void saveCourse() {
        if (!validateCourseInput()) {
            return;
        }
        if (courseService.registerCourse(readCourse())) {
            JOptionPane.showMessageDialog(this, "Course saved successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Course could not be saved.");
        }
    }

    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course from the table.");
            return;
        }
        if (!validateCourseInput()) {
            return;
        }
        if (courseService.modifyCourse(readCourse())) {
            JOptionPane.showMessageDialog(this, "Course updated successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Course could not be updated. It may be used by students or schedules.");
        }
    }

    private boolean validateCourseInput() {
        String courseName = txtCourseName.getText().trim();
        if (ValidationUtil.isBlank(courseName)) {
            JOptionPane.showMessageDialog(this, "Course name is required.");
            txtCourseName.requestFocus();
            return false;
        }
        if (courseName.length() < 2) {
            JOptionPane.showMessageDialog(this, "Course name must contain at least 2 characters.");
            txtCourseName.requestFocus();
            return false;
        }
        return true;
    }

    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course from the table.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete selected course?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && courseService.removeCourse(selectedCourseId)) {
            JOptionPane.showMessageDialog(this, "Course deleted successfully.");
            refresh();
        } else if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Course could not be deleted. It may be used by students or schedules.");
        }
    }

    private void selectCourse() {
        int row = tblCourses.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedCourseId = Integer.parseInt(tblCourses.getValueAt(row, 0).toString());
        txtCourseName.setText(tblCourses.getValueAt(row, 2).toString());
    }

    private void refresh() {
        loadCourses();
        clearFields();
    }

    private void clearFields() {
        selectedCourseId = -1;
        txtCourseName.setText("");
        tblCourses.clearSelection();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new CourseForm().setVisible(true));
    }
}
