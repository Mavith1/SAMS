package com.sams.dao;

import com.sams.model.Enrollment;
import com.sams.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, s.student_name, c.course_name, e.enrollment_date " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getInt("enrollment_id"));
                e.setStudentName(rs.getString("student_name"));
                e.setCourseName(rs.getString("course_name"));
                e.setEnrollmentDate(rs.getString("enrollment_date"));
                list.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addEnrollment(Enrollment e) {
        String sql = "INSERT INTO enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getStudentId());
            ps.setInt(2, e.getCourseId());
            ps.setString(3, e.getEnrollmentDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            return false;
        }
    }
    public boolean deleteEnrollment(int enrollmentId) {
    String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        
        ps.setInt(1, enrollmentId);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    
}