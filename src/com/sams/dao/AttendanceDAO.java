package com.sams.dao;

import com.sams.model.Attendance;
import com.sams.model.AttendanceReport;
import com.sams.model.Student;
import com.sams.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAO {

    public List<Student> getStudentsForSchedule(int scheduleId) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.student_id, s.reg_no, s.student_name, s.email, s.phone, s.course_id "
                + "FROM students s "
                + "JOIN class_schedules cs ON s.course_id = cs.course_id "
                + "WHERE cs.schedule_id=? "
                + "ORDER BY s.student_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setRegNo(rs.getString("reg_no"));
                    student.setStudentName(rs.getString("student_name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    student.setCourseId(rs.getInt("course_id"));
                    list.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Integer, Attendance> getAttendanceBySchedule(int scheduleId) {
        Map<Integer, Attendance> map = new HashMap<>();
        String sql = "SELECT attendance_id, schedule_id, student_id, status, remarks, marked_date "
                + "FROM attendance WHERE schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setAttendanceId(rs.getInt("attendance_id"));
                    attendance.setScheduleId(rs.getInt("schedule_id"));
                    attendance.setStudentId(rs.getInt("student_id"));
                    attendance.setStatus(rs.getString("status"));
                    attendance.setRemarks(rs.getString("remarks"));
                    attendance.setMarkedDate(rs.getString("marked_date"));
                    map.put(attendance.getStudentId(), attendance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean saveAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance (schedule_id, student_id, status, remarks, marked_date) "
                + "VALUES (?, ?, ?, ?, CURRENT_DATE) "
                + "ON DUPLICATE KEY UPDATE status=VALUES(status), remarks=VALUES(remarks), marked_date=CURRENT_DATE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, attendance.getScheduleId());
            ps.setInt(2, attendance.getStudentId());
            ps.setString(3, attendance.getStatus());
            ps.setString(4, attendance.getRemarks());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AttendanceReport> getAttendanceReports(String studentName, String courseName, String subject,
                                                       String fromDate, String toDate) {
        List<AttendanceReport> list = new ArrayList<>();

        // Build query dynamically based on which filters are provided
        StringBuilder sql = new StringBuilder(
                "SELECT s.student_name, s.reg_no, c.course_name, cs.subject, cs.class_date, "
                + "CONCAT(cs.start_time, ' - ', cs.end_time) AS time_slot, l.name AS lecturer_name, "
                + "a.status, a.remarks "
                + "FROM attendance a "
                + "JOIN students s ON a.student_id = s.student_id "
                + "JOIN class_schedules cs ON a.schedule_id = cs.schedule_id "
                + "JOIN courses c ON cs.course_id = c.course_id "
                + "JOIN lecturers l ON cs.lecturer_id = l.lecturer_id "
                + "WHERE 1=1 ");

        List<String> params = new ArrayList<>();

        if (studentName != null && !studentName.isEmpty()) {
            sql.append("AND s.student_name LIKE ? ");
            params.add("%" + studentName + "%");
        }
        if (courseName != null && !courseName.isEmpty()) {
            sql.append("AND c.course_name LIKE ? ");
            params.add("%" + courseName + "%");
        }
        if (subject != null && !subject.isEmpty()) {
            sql.append("AND cs.subject LIKE ? ");
            params.add("%" + subject + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append("AND cs.class_date >= ? ");
            params.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append("AND cs.class_date <= ? ");
            params.add(toDate);
        }
        sql.append("ORDER BY cs.class_date DESC, s.student_name");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AttendanceReport report = new AttendanceReport();
                    report.setStudentName(rs.getString("student_name"));
                    report.setRegNo(rs.getString("reg_no"));
                    report.setCourseName(rs.getString("course_name"));
                    report.setSubject(rs.getString("subject"));
                    report.setClassDate(rs.getString("class_date"));
                    report.setTimeSlot(rs.getString("time_slot"));
                    report.setLecturerName(rs.getString("lecturer_name"));
                    report.setStatus(rs.getString("status"));
                    report.setRemarks(rs.getString("remarks"));
                    list.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
