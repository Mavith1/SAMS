package com.sams.view;

import com.sams.dao.CourseDAO;
import com.sams.model.ClassSchedule;
import com.sams.model.Course;
import com.sams.model.Lecturer;
import com.sams.service.ClassScheduleService;
import com.sams.service.LecturerService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ClassScheduleForm extends JFrame {

    private final ClassScheduleService scheduleService = new ClassScheduleService();
    private final CourseDAO courseDAO = new CourseDAO();
    private final LecturerService lecturerService = new LecturerService();
    private int selectedScheduleId = -1;

    private JComboBox<ComboBoxItem> cmbCourse;
    private JComboBox<ComboBoxItem> cmbLecturer;
    private JTextField txtSubject;
    private JTextField txtDate;
    private JTextField txtStartTime;
    private JTextField txtEndTime;
    private JTable tblSchedules;

    public ClassScheduleForm() {
        initComponents();
        loadComboBoxes();
        loadSchedules();
    }

    private void initComponents() {
        setTitle("Class Scheduling");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbCourse = new JComboBox<>();
        cmbLecturer = new JComboBox<>();
        txtSubject = new JTextField(18);
        txtDate = new JTextField("2026-06-20", 18);
        txtStartTime = new JTextField("09:00", 18);
        txtEndTime = new JTextField("11:00", 18);

        addRow(formPanel, gbc, 0, "Course", cmbCourse);
        addRow(formPanel, gbc, 1, "Subject", txtSubject);
        addRow(formPanel, gbc, 2, "Date (yyyy-MM-dd)", txtDate);
        addRow(formPanel, gbc, 3, "Start Time (HH:mm)", txtStartTime);
        addRow(formPanel, gbc, 4, "End Time (HH:mm)", txtEndTime);
        addRow(formPanel, gbc, 5, "Lecturer", cmbLecturer);

        JButton btnSave = new JButton("Save");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        tblSchedules = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Course", "Subject", "Date", "Start", "End", "Lecturer"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblSchedules.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectScheduleFromTable();
            }
        });

        btnSave.addActionListener(e -> saveSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnClear.addActionListener(e -> clearFields());

        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(tblSchedules), BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component component) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void loadComboBoxes() {
        cmbCourse.removeAllItems();
        for (Course course : courseDAO.getAllCourses()) {
            cmbCourse.addItem(new ComboBoxItem(course.getCourseId(), course.getCourseName()));
        }

        cmbLecturer.removeAllItems();
        for (Lecturer lecturer : lecturerService.getAllLecturersList()) {
            cmbLecturer.addItem(new ComboBoxItem(lecturer.getId(), lecturer.getName()));
        }
    }

    private void loadSchedules() {
        DefaultTableModel model = (DefaultTableModel) tblSchedules.getModel();
        model.setRowCount(0);
        List<ClassSchedule> schedules = scheduleService.getAllSchedulesList();
        for (ClassSchedule schedule : schedules) {
            model.addRow(new Object[]{
                schedule.getScheduleId(),
                schedule.getCourseName(),
                schedule.getSubject(),
                schedule.getClassDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getLecturerName()
            });
        }
    }

    private ClassSchedule readScheduleFromFields() {
        ComboBoxItem course = (ComboBoxItem) cmbCourse.getSelectedItem();
        ComboBoxItem lecturer = (ComboBoxItem) cmbLecturer.getSelectedItem();
        ClassSchedule schedule = new ClassSchedule();
        schedule.setScheduleId(selectedScheduleId);
        schedule.setCourseId(course == null ? 0 : course.id);
        schedule.setLecturerId(lecturer == null ? 0 : lecturer.id);
        schedule.setSubject(txtSubject.getText().trim());
        schedule.setClassDate(txtDate.getText().trim());
        schedule.setStartTime(txtStartTime.getText().trim());
        schedule.setEndTime(txtEndTime.getText().trim());
        return schedule;
    }

    private void saveSchedule() {
        if (!validateScheduleInput()) {
            return;
        }
        if (scheduleService.createSchedule(readScheduleFromFields())) {
            JOptionPane.showMessageDialog(this, "Class schedule saved successfully.");
            loadSchedules();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill all schedule details.");
        }
    }

    private void updateSchedule() {
        if (selectedScheduleId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule from the table.");
            return;
        }
        if (!validateScheduleInput()) {
            return;
        }
        if (scheduleService.modifySchedule(readScheduleFromFields())) {
            JOptionPane.showMessageDialog(this, "Class schedule updated successfully.");
            loadSchedules();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating class schedule.");
        }
    }

    private boolean validateScheduleInput() {
        if (cmbCourse.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please create/select a course.");
            return false;
        }
        if (cmbLecturer.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please create/select a lecturer.");
            return false;
        }
        if (ValidationUtil.isBlank(txtSubject.getText())) {
            JOptionPane.showMessageDialog(this, "Subject is required.");
            txtSubject.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidDate(txtDate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Date must be in yyyy-MM-dd format.");
            txtDate.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidTime(txtStartTime.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Start Time must be in HH:mm format.");
            txtStartTime.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidTime(txtEndTime.getText().trim())) {
            JOptionPane.showMessageDialog(this, "End Time must be in HH:mm format.");
            txtEndTime.requestFocus();
            return false;
        }
        if (!ValidationUtil.isEndTimeAfterStart(txtStartTime.getText().trim(), txtEndTime.getText().trim())) {
            JOptionPane.showMessageDialog(this, "End Time must be after Start Time.");
            txtEndTime.requestFocus();
            return false;
        }
        return true;
    }

    private void deleteSchedule() {
        if (selectedScheduleId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a schedule from the table.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this schedule?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && scheduleService.removeSchedule(selectedScheduleId)) {
            JOptionPane.showMessageDialog(this, "Class schedule deleted successfully.");
            loadSchedules();
            clearFields();
        }
    }

    private void selectScheduleFromTable() {
        int row = tblSchedules.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedScheduleId = Integer.parseInt(tblSchedules.getValueAt(row, 0).toString());
        selectComboByText(cmbCourse, tblSchedules.getValueAt(row, 1).toString());
        txtSubject.setText(tblSchedules.getValueAt(row, 2).toString());
        txtDate.setText(tblSchedules.getValueAt(row, 3).toString());
        txtStartTime.setText(tblSchedules.getValueAt(row, 4).toString());
        txtEndTime.setText(tblSchedules.getValueAt(row, 5).toString());
        selectComboByText(cmbLecturer, tblSchedules.getValueAt(row, 6).toString());
    }

    private void selectComboByText(JComboBox<ComboBoxItem> comboBox, String text) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).name.equals(text)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearFields() {
        selectedScheduleId = -1;
        if (cmbCourse.getItemCount() > 0) {
            cmbCourse.setSelectedIndex(0);
        }
        if (cmbLecturer.getItemCount() > 0) {
            cmbLecturer.setSelectedIndex(0);
        }
        txtSubject.setText("");
        txtDate.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        tblSchedules.clearSelection();
    }

    private static class ComboBoxItem {
        private final int id;
        private final String name;

        ComboBoxItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new ClassScheduleForm().setVisible(true));
    }
}
