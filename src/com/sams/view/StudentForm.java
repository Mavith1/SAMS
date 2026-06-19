package com.sams.view;

import com.sams.dao.CourseDAO;
import com.sams.model.Course;
import com.sams.model.Student;
import com.sams.service.StudentService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class StudentForm extends JFrame {

    private final StudentService studentService = new StudentService();
    private final CourseDAO courseDAO = new CourseDAO();
    private final Map<Integer, String> courseNames = new HashMap<>();
    private int selectedStudentId = -1;

    private JTextField txtRegNo;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtSearch;
    private JComboBox<ComboBoxItem> cmbCourse;
    private JTable tblStudents;

    public StudentForm() {
        initComponents();
        loadCourses();
        loadStudents(studentService.getAllStudentsList());
    }

    private void initComponents() {
        setTitle("Student Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(980, 560);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(16, 16));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(AppUI.BACKGROUND);
        top.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 22, 0, 22));
        top.add(AppUI.title("Student Management"), BorderLayout.WEST);

        txtSearch = AppUI.textField(22);
        txtSearch.setToolTipText("Search by student name");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                searchStudents();
            }
        });
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(AppUI.BACKGROUND);
        searchPanel.add(AppUI.label("Search"));
        searchPanel.add(txtSearch);
        top.add(searchPanel, BorderLayout.EAST);

        JPanel form = AppUI.card();
        form.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRegNo = AppUI.textField(18);
        txtName = AppUI.textField(18);
        txtEmail = AppUI.textField(18);
        txtPhone = AppUI.textField(18);
        cmbCourse = new JComboBox<>();

        addRow(form, gbc, 0, 0, "Registration No", txtRegNo);
        addRow(form, gbc, 0, 2, "Student Name", txtName);
        addRow(form, gbc, 1, 0, "Email", txtEmail);
        addRow(form, gbc, 1, 2, "Phone", txtPhone);
        addRow(form, gbc, 2, 0, "Course", cmbCourse);

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
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        tblStudents = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Reg No", "Name", "Email", "Phone", "Course ID", "Course"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        AppUI.table(tblStudents);
        tblStudents.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectStudent();
            }
        });

        btnSave.addActionListener(e -> saveStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearFields());

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 22, 22, 22));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(tblStudents), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
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

    private void loadCourses() {
        cmbCourse.removeAllItems();
        courseNames.clear();
        for (Course course : courseDAO.getAllCourses()) {
            courseNames.put(course.getCourseId(), course.getCourseName());
            cmbCourse.addItem(new ComboBoxItem(course.getCourseId(), course.getCourseName()));
        }
    }

    private void loadStudents(List<Student> students) {
        DefaultTableModel model = (DefaultTableModel) tblStudents.getModel();
        model.setRowCount(0);
        for (Student student : students) {
            model.addRow(new Object[]{
                student.getStudentId(),
                student.getRegNo(),
                student.getStudentName(),
                student.getEmail(),
                student.getPhone(),
                student.getCourseId(),
                courseNames.getOrDefault(student.getCourseId(), "")
            });
        }
    }

    private Student readStudent() {
        ComboBoxItem course = (ComboBoxItem) cmbCourse.getSelectedItem();
        Student student = new Student();
        student.setStudentId(selectedStudentId);
        student.setRegNo(txtRegNo.getText().trim());
        student.setStudentName(txtName.getText().trim());
        student.setEmail(txtEmail.getText().trim());
        student.setPhone(txtPhone.getText().trim());
        student.setCourseId(course == null ? 0 : course.id);
        return student;
    }

    private void saveStudent() {
        if (!validateStudentInput()) {
            return;
        }
        if (studentService.registerStudent(readStudent())) {
            JOptionPane.showMessageDialog(this, "Student saved successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter Registration No, Student Name, and a valid Course.");
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table.");
            return;
        }
        if (!validateStudentInput()) {
            return;
        }
        if (studentService.modifyStudent(readStudent())) {
            JOptionPane.showMessageDialog(this, "Student updated successfully.");
            refresh();
        } else {
            JOptionPane.showMessageDialog(this, "Student could not be updated.");
        }
    }

    private boolean validateStudentInput() {
        if (ValidationUtil.isBlank(txtRegNo.getText())) {
            JOptionPane.showMessageDialog(this, "Registration No is required.");
            txtRegNo.requestFocus();
            return false;
        }
        if (ValidationUtil.isBlank(txtName.getText())) {
            JOptionPane.showMessageDialog(this, "Student Name is required.");
            txtName.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidPhone(txtPhone.getText())) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits and start with 0.");
            txtPhone.requestFocus();
            return false;
        }
        if (cmbCourse.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please create/select a course before registering students.");
            return false;
        }
        return true;
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student from the table.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete selected student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && studentService.removeStudent(selectedStudentId)) {
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            refresh();
        }
    }

    private void searchStudents() {
        String text = txtSearch.getText().trim();
        if (text.isEmpty()) {
            loadStudents(studentService.getAllStudentsList());
        } else {
            loadStudents(studentService.findStudentsByName(text));
        }
    }

    private void selectStudent() {
        int row = tblStudents.getSelectedRow();
        if (row == -1) {
            return;
        }
        selectedStudentId = Integer.parseInt(tblStudents.getValueAt(row, 0).toString());
        txtRegNo.setText(tblStudents.getValueAt(row, 1).toString());
        txtName.setText(tblStudents.getValueAt(row, 2).toString());
        txtEmail.setText(tblStudents.getValueAt(row, 3).toString());
        txtPhone.setText(tblStudents.getValueAt(row, 4).toString());
        selectCourse(Integer.parseInt(tblStudents.getValueAt(row, 5).toString()));
    }

    private void selectCourse(int courseId) {
        for (int i = 0; i < cmbCourse.getItemCount(); i++) {
            if (cmbCourse.getItemAt(i).id == courseId) {
                cmbCourse.setSelectedIndex(i);
                return;
            }
        }
    }

    private void refresh() {
        loadCourses();
        loadStudents(studentService.getAllStudentsList());
        clearFields();
    }

    private void clearFields() {
        selectedStudentId = -1;
        txtRegNo.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSearch.setText("");
        if (cmbCourse.getItemCount() > 0) {
            cmbCourse.setSelectedIndex(0);
        }
        tblStudents.clearSelection();
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
            return id + " - " + name;
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new StudentForm().setVisible(true));
    }
}
