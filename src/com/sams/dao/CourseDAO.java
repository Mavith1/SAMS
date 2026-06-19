package com.sams.dao;

import com.sams.model.Course;
import com.sams.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // 1. පාඨමාලාව ඇතුළත් කිරීම (Insert Course)
    public boolean addCourse(Course course) {
        // ⚠️ Workbench එකේ තියෙන්නේ course_name විතරක් නිසා query එක අප්ඩේට් කර ඇත
        String sql = "INSERT INTO courses (course_name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, course.getCourseName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. සියලුම පාඨමාලා ලැයිස්තුව ලබාගැනීම (Select All Courses)
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Course course = new Course();
                // 🛠️ Workbench එකේ තියෙන නිවැරදිම Column නම් 2 විතරක් මෙතනට දමා ඇත
                course.setCourseId(rs.getInt("course_id"));
                course.setCourseName(rs.getString("course_name"));
                
                // Model එකේ error එකක් නොවෙන්න course_code එක දැනට හිස්ව තබයි
                course.setCourseCode(""); 
                
                list.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. පාඨමාලාව යාවත්කාලීන කිරීම (Update Course)
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET course_name=? WHERE course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, course.getCourseName());
            ps.setInt(2, course.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. පාඨමාලාව ඉවත් කිරීම (Delete Course)
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM courses WHERE course_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getCourseIdByName(String name) {
    int id = 0;
    String sql = "SELECT course_id FROM courses WHERE course_name = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            id = rs.getInt("course_id");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return id;
}
}