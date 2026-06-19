package com.sams.dao;

import com.sams.model.Lecturer;
import com.sams.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public boolean addLecturer(String name, String email, String subject) {
        Lecturer lecturer = new Lecturer();
        lecturer.setName(name);
        lecturer.setEmail(email);
        lecturer.setSubject(subject);
        return addLecturer(lecturer);
    }

    public boolean addLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO lecturers (name, email, subject_assigned) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturer.getName());
            ps.setString(2, lecturer.getEmail());
            ps.setString(3, lecturer.getSubject());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Lecturer> getAllLecturers() {
        List<Lecturer> list = new ArrayList<>();
        String sql = "SELECT lecturer_id, name, email, subject_assigned FROM lecturers ORDER BY lecturer_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setId(rs.getInt("lecturer_id"));
                lecturer.setName(rs.getString("name"));
                lecturer.setEmail(rs.getString("email"));
                lecturer.setSubject(rs.getString("subject_assigned"));
                list.add(lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateLecturer(Lecturer lecturer) {
        String sql = "UPDATE lecturers SET name=?, email=?, subject_assigned=? WHERE lecturer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lecturer.getName());
            ps.setString(2, lecturer.getEmail());
            ps.setString(3, lecturer.getSubject());
            ps.setInt(4, lecturer.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLecturer(int id) {
        String sql = "DELETE FROM lecturers WHERE lecturer_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
