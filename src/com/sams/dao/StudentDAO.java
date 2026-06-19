package com.sams.dao;

import com.sams.model.Student;
import com.sams.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // 1. ශිෂ්‍යයෙකු ඇතුළත් කිරීම (Insert)
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (reg_no, student_name, email, phone, course_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, student.getRegNo());
            ps.setString(2, student.getStudentName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setInt(5, student.getCourseId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. සියලුම ශිෂ්‍යයන් ලබාගැනීම (Load to Table)
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. ශිෂ්‍යයෙකුගේ තොරතුරු වෙනස් කිරීම (Update)
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET reg_no=?, student_name=?, email=?, phone=?, course_id=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, student.getRegNo());
            ps.setString(2, student.getStudentName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getPhone());
            ps.setInt(5, student.getCourseId());
            ps.setInt(6, student.getStudentId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. ශිෂ්‍යයෙකු ඉවත් කිරීම (Delete)
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. නම මඟින් ශිෂ්‍යයන් සෙවීම (Search by Name - NEW)
    public List<Student> searchStudents(String name) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_name LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // නමේ මුලට හෝ අගට කී වර්ඩ් එක ගැහුණත් සර්ච් වෙන්න '%' එකතු කරයි
            ps.setString(1, "%" + name + "%");
            
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
    
    public int getStudentIdByName(String name) {
    int id = 0;
    String sql = "SELECT student_id FROM students WHERE student_name = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("student_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return id;
}
}