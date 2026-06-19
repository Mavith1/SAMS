package com.sams.view;

import com.sams.model.AttendanceReport;
import com.sams.service.AttendanceService;
import com.sams.util.ValidationUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class AttendanceReportForm extends JFrame {

    private final AttendanceService attendanceService = new AttendanceService();

    private JTextField txtStudent;
    private JTextField txtCourse;
    private JTextField txtSubject;
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JTable tblReports;
    private JLabel lblSummary;

    public AttendanceReportForm() {
        initComponents();
        loadReports();
    }

    private void initComponents() {
        setTitle("Attendance Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1150, 620);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppUI.BACKGROUND);
        setLayout(new BorderLayout(16, 16));

        // ---- Header ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 14, 24));
        header.add(AppUI.title("Attendance Reports"), BorderLayout.WEST);
        header.add(AppUI.subtitle("Filter attendance records by student, course, subject, or date range"), BorderLayout.SOUTH);

        // ---- Filter card ----
        JPanel filterCard = AppUI.card();
        filterCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtStudent  = AppUI.textField(16);
        txtCourse   = AppUI.textField(16);
        txtSubject  = AppUI.textField(16);
        txtFromDate = AppUI.textField(12);
        txtToDate   = AppUI.textField(12);

        txtStudent.setToolTipText("Filter by student name (partial match)");
        txtCourse.setToolTipText("Filter by course name (partial match)");
        txtSubject.setToolTipText("Filter by subject (partial match)");
        txtFromDate.setToolTipText("Format: yyyy-MM-dd");
        txtToDate.setToolTipText("Format: yyyy-MM-dd");

        // Row 0 – Student / Course / Subject
        addField(filterCard, gbc, 0, 0, "Student", txtStudent);
        addField(filterCard, gbc, 0, 2, "Course",  txtCourse);
        addField(filterCard, gbc, 0, 4, "Subject", txtSubject);

        // Row 1 – From / To / Buttons
        addField(filterCard, gbc, 1, 0, "From Date (yyyy-MM-dd)", txtFromDate);
        addField(filterCard, gbc, 1, 2, "To Date (yyyy-MM-dd)",   txtToDate);

        JButton btnSearch = AppUI.button("Search", AppUI.BLUE);
        JButton btnClear  = AppUI.button("Clear",  AppUI.GRAY);
        JButton btnExport = AppUI.button("Export CSV", new Color(22, 163, 74));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSearch);
        btnPanel.add(btnClear);
        btnPanel.add(btnExport);

        gbc.gridx = 4; gbc.gridy = 1; gbc.gridwidth = 2;
        filterCard.add(btnPanel, gbc);
        gbc.gridwidth = 1;

        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setBackground(AppUI.BACKGROUND);
        filterWrapper.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
        filterWrapper.add(filterCard, BorderLayout.CENTER);

        // ---- Table ----
        tblReports = new JTable(new DefaultTableModel(
                new Object[]{"Student Name", "Reg No", "Course", "Subject", "Date", "Time Slot", "Lecturer", "Status", "Remarks"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        AppUI.table(tblReports);
        tblReports.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblReports.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblReports.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblReports.getColumnModel().getColumn(3).setPreferredWidth(140);
        tblReports.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblReports.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblReports.getColumnModel().getColumn(6).setPreferredWidth(130);
        tblReports.getColumnModel().getColumn(7).setPreferredWidth(75);
        tblReports.getColumnModel().getColumn(8).setPreferredWidth(180);

        // Color-coded status column
        tblReports.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
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

        // Alternate row shading
        tblReports.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                    setForeground(AppUI.NAVY);
                }
                return this;
            }
        });
        // Re-apply status renderer on top
        tblReports.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
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

        JScrollPane scrollPane = new JScrollPane(tblReports);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(AppUI.BACKGROUND);
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 22));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        // ---- Summary bar ----
        lblSummary = new JLabel(" ");
        lblSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSummary.setForeground(AppUI.GRAY);
        JPanel summaryBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryBar.setBackground(AppUI.BACKGROUND);
        summaryBar.setBorder(BorderFactory.createEmptyBorder(0, 22, 14, 22));
        summaryBar.add(lblSummary);

        // ---- Center content ----
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(AppUI.BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        center.add(filterWrapper, BorderLayout.NORTH);
        center.add(tableWrapper, BorderLayout.CENTER);

        // ---- Wire actions ----
        btnSearch.addActionListener(e -> {
            if (validateDateFilters()) loadReports();
        });
        btnClear.addActionListener(e -> clearFilters());
        btnExport.addActionListener(e -> exportCSV());

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(summaryBar, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, int col, String labelText, JTextField field) {
        gbc.gridx = col;   gbc.gridy = row; gbc.weightx = 0;
        panel.add(AppUI.label(labelText), gbc);
        gbc.gridx = col + 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
        gbc.weightx = 0;
    }

    private void loadReports() {
        if (txtFromDate != null && !validateDateFilters()) return;

        DefaultTableModel model = (DefaultTableModel) tblReports.getModel();
        model.setRowCount(0);

        List<AttendanceReport> reports = attendanceService.getAttendanceReports(
                txt(txtStudent), txt(txtCourse), txt(txtSubject),
                txt(txtFromDate), txt(txtToDate));

        long present = 0, absent = 0, late = 0;
        for (AttendanceReport r : reports) {
            model.addRow(new Object[]{
                r.getStudentName(), r.getRegNo(), r.getCourseName(),
                r.getSubject(), r.getClassDate(), r.getTimeSlot(),
                r.getLecturerName(), r.getStatus(), r.getRemarks()
            });
            switch (r.getStatus() == null ? "" : r.getStatus()) {
                case "Present": present++; break;
                case "Absent":  absent++;  break;
                case "Late":    late++;    break;
            }
        }

        int total = reports.size();
        if (total == 0) {
            lblSummary.setText("No records found for the selected filters.");
        } else {
            lblSummary.setText("Total: " + total
                    + "   |   Present: " + present
                    + "   |   Absent: "  + absent
                    + "   |   Late: "    + late);
        }
    }

    private void clearFilters() {
        txtStudent.setText("");
        txtCourse.setText("");
        txtSubject.setText("");
        txtFromDate.setText("");
        txtToDate.setText("");
        loadReports();
    }

    private void exportCSV() {
        DefaultTableModel model = (DefaultTableModel) tblReports.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export.", "Export", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setSelectedFile(new java.io.File("attendance_report.csv"));
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;

        java.io.File file = chooser.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file))) {
            // Header row
            pw.println("Student Name,Reg No,Course,Subject,Date,Time Slot,Lecturer,Status,Remarks");
            // Data rows
            for (int r = 0; r < model.getRowCount(); r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < model.getColumnCount(); c++) {
                    if (c > 0) sb.append(",");
                    Object val = model.getValueAt(r, c);
                    String cell = val == null ? "" : val.toString().replace("\"", "\"\"");
                    sb.append("\"").append(cell).append("\"");
                }
                pw.println(sb.toString());
            }
            JOptionPane.showMessageDialog(this,
                    "Report exported successfully to:\n" + file.getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Export failed: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateDateFilters() {
        String from = txt(txtFromDate);
        String to   = txt(txtToDate);
        if (!from.isEmpty() && !ValidationUtil.isValidDate(from)) {
            JOptionPane.showMessageDialog(this, "From Date must be in yyyy-MM-dd format.", "Validation", JOptionPane.WARNING_MESSAGE);
            txtFromDate.requestFocus();
            return false;
        }
        if (!to.isEmpty() && !ValidationUtil.isValidDate(to)) {
            JOptionPane.showMessageDialog(this, "To Date must be in yyyy-MM-dd format.", "Validation", JOptionPane.WARNING_MESSAGE);
            txtToDate.requestFocus();
            return false;
        }
        if (!from.isEmpty() && !to.isEmpty() && LocalDate.parse(to).isBefore(LocalDate.parse(from))) {
            JOptionPane.showMessageDialog(this, "To Date cannot be before From Date.", "Validation", JOptionPane.WARNING_MESSAGE);
            txtToDate.requestFocus();
            return false;
        }
        return true;
    }

    private String txt(JTextField f) {
        return f == null ? "" : f.getText().trim();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new AttendanceReportForm().setVisible(true));
    }
}
