package com.sams.view;

import com.sams.model.Attendance;
import com.sams.model.ClassSchedule;
import com.sams.model.Student;
import com.sams.service.AttendanceService;
import com.sams.service.ClassScheduleService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.Font;

public class AttendanceMarkingForm extends JFrame {

    private final ClassScheduleService scheduleService = new ClassScheduleService();
    private final AttendanceService attendanceService = new AttendanceService();

    private JComboBox<ComboBoxItem> cmbSchedule;
    private JTable tblAttendance;
    private JLabel lblStatus;

    public AttendanceMarkingForm() {
        initComponents();
        loadSchedules();
    }

    private void initComponents() {
        setTitle("Attendance Marking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(16, 16));

        // ---- Header ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 14, 24));
        header.add(AppUI.title("Attendance Marking"), BorderLayout.WEST);
        header.add(AppUI.subtitle("Select a class schedule and mark attendance for each student"), BorderLayout.SOUTH);

        // ---- Schedule selector card ----
        JPanel selectorCard = AppUI.card();
        selectorCard.setLayout(new GridBagLayout());
        selectorCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(14, 18, 14, 18)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbSchedule = new JComboBox<>();
        cmbSchedule.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbSchedule.setPreferredSize(new java.awt.Dimension(520, 32));

        JButton btnLoad = AppUI.button("Load Students", AppUI.BLUE);
        JButton btnSave = AppUI.button("Save Attendance", AppUI.GREEN);

        gbc.gridx = 0; gbc.gridy = 0;
        selectorCard.add(AppUI.label("Class Schedule"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        selectorCard.add(cmbSchedule, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        selectorCard.add(btnLoad, gbc);
        gbc.gridx = 3;
        selectorCard.add(btnSave, gbc);

        JPanel selectorWrapper = new JPanel(new BorderLayout());
        selectorWrapper.setBackground(AppUI.BACKGROUND);
        selectorWrapper.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
        selectorWrapper.add(selectorCard, BorderLayout.CENTER);

        // ---- Attendance table ----
        tblAttendance = new JTable(new DefaultTableModel(
                new Object[]{"Student ID", "Reg No", "Student Name", "Status", "Remarks"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        });
        AppUI.table(tblAttendance);
        tblAttendance.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblAttendance.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblAttendance.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblAttendance.getColumnModel().getColumn(3).setPreferredWidth(110);
        tblAttendance.getColumnModel().getColumn(4).setPreferredWidth(260);

        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblAttendance.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

        // Color-coded status renderer
        tblAttendance.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = value == null ? "" : value.toString();
                    switch (status) {
                        case "Present": setForeground(AppUI.GREEN); break;
                        case "Absent":  setForeground(AppUI.RED);   break;
                        case "Late":    setForeground(new Color(202, 138, 4)); break;
                        default:        setForeground(AppUI.NAVY);
                    }
                }
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblAttendance);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(AppUI.BACKGROUND);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        // ---- Status bar ----
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(AppUI.GRAY);
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(AppUI.BACKGROUND);
        statusBar.setBorder(BorderFactory.createEmptyBorder(0, 22, 14, 22));
        statusBar.add(lblStatus);

        // ---- Center content ----
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        center.add(selectorWrapper, BorderLayout.NORTH);
        center.add(tableWrapper, BorderLayout.CENTER);

        // ---- Wire actions ----
        btnLoad.addActionListener(e -> loadStudentsForSelectedSchedule());
        btnSave.addActionListener(e -> saveAttendance());
        cmbSchedule.addActionListener(e -> loadStudentsForSelectedSchedule());

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void loadSchedules() {
        cmbSchedule.removeAllItems();
        List<ClassSchedule> schedules = scheduleService.getAllSchedulesList();
        for (ClassSchedule schedule : schedules) {
            String label = schedule.getClassDate() + "  |  " + schedule.getCourseName()
                    + "  |  " + schedule.getSubject() + "  |  " + schedule.getStartTime();
            cmbSchedule.addItem(new ComboBoxItem(schedule.getScheduleId(), label));
        }
        if (schedules.isEmpty()) {
            lblStatus.setText("No class schedules found. Please create a schedule first.");
            lblStatus.setForeground(AppUI.RED);
        }
        loadStudentsForSelectedSchedule();
    }

    private void loadStudentsForSelectedSchedule() {
        ComboBoxItem selected = (ComboBoxItem) cmbSchedule.getSelectedItem();
        DefaultTableModel model = (DefaultTableModel) tblAttendance.getModel();
        model.setRowCount(0);
        if (selected == null) {
            return;
        }

        List<Student> students = attendanceService.getStudentsForSchedule(selected.id);
        Map<Integer, Attendance> existingAttendance = attendanceService.getAttendanceBySchedule(selected.id);

        for (Student student : students) {
            Attendance attendance = existingAttendance.get(student.getStudentId());
            String status  = attendance == null ? "Present" : attendance.getStatus();
            String remarks = attendance == null || attendance.getRemarks() == null ? "" : attendance.getRemarks();
            model.addRow(new Object[]{
                student.getStudentId(),
                student.getRegNo(),
                student.getStudentName(),
                status,
                remarks
            });
        }

        int total = students.size();
        long marked = existingAttendance.size();
        if (total == 0) {
            lblStatus.setText("No students enrolled in this course.");
            lblStatus.setForeground(AppUI.RED);
        } else {
            lblStatus.setText("Students: " + total + "   |   Already marked: " + marked
                    + "   |   Pending: " + (total - marked));
            lblStatus.setForeground(AppUI.GRAY);
        }
    }

    private void saveAttendance() {
        ComboBoxItem selected = (ComboBoxItem) cmbSchedule.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a class schedule.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tblAttendance.isEditing()) {
            tblAttendance.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) tblAttendance.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students found for this schedule's course.", "No Data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int saved = 0;
        int failed = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            Attendance attendance = new Attendance();
            attendance.setScheduleId(selected.id);
            attendance.setStudentId(Integer.parseInt(model.getValueAt(i, 0).toString()));
            attendance.setStatus(model.getValueAt(i, 3).toString());
            Object remarksValue = model.getValueAt(i, 4);
            attendance.setRemarks(remarksValue == null ? "" : remarksValue.toString());
            if (attendanceService.saveAttendance(attendance)) {
                saved++;
            } else {
                failed++;
            }
        }

        if (failed == 0) {
            JOptionPane.showMessageDialog(this,
                    "Attendance saved successfully for " + saved + " student(s).",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    saved + " record(s) saved. " + failed + " record(s) could not be saved.",
                    "Partial Save", JOptionPane.WARNING_MESSAGE);
        }
        loadStudentsForSelectedSchedule();
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
        java.awt.EventQueue.invokeLater(() -> new AttendanceMarkingForm().setVisible(true));
    }
}
