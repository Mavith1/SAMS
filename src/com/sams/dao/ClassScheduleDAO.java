package com.sams.dao;

import com.sams.model.ClassSchedule;
import com.sams.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassScheduleDAO {

    public boolean addSchedule(ClassSchedule schedule) {
        String sql = "INSERT INTO class_schedules (course_id, subject, class_date, start_time, end_time, lecturer_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, schedule.getCourseId());
            ps.setString(2, schedule.getSubject());
            ps.setString(3, schedule.getClassDate());
            ps.setString(4, schedule.getStartTime());
            ps.setString(5, schedule.getEndTime());
            ps.setInt(6, schedule.getLecturerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ClassSchedule> getAllSchedules() {
        List<ClassSchedule> list = new ArrayList<>();
        String sql = "SELECT cs.schedule_id, cs.course_id, c.course_name, cs.subject, cs.class_date, "
                + "cs.start_time, cs.end_time, cs.lecturer_id, l.name AS lecturer_name "
                + "FROM class_schedules cs "
                + "JOIN courses c ON cs.course_id = c.course_id "
                + "JOIN lecturers l ON cs.lecturer_id = l.lecturer_id "
                + "ORDER BY cs.class_date DESC, cs.start_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapSchedule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateSchedule(ClassSchedule schedule) {
        String sql = "UPDATE class_schedules SET course_id=?, subject=?, class_date=?, start_time=?, end_time=?, lecturer_id=? "
                + "WHERE schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, schedule.getCourseId());
            ps.setString(2, schedule.getSubject());
            ps.setString(3, schedule.getClassDate());
            ps.setString(4, schedule.getStartTime());
            ps.setString(5, schedule.getEndTime());
            ps.setInt(6, schedule.getLecturerId());
            ps.setInt(7, schedule.getScheduleId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM class_schedules WHERE schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ClassSchedule getScheduleById(int scheduleId) {
        String sql = "SELECT cs.schedule_id, cs.course_id, c.course_name, cs.subject, cs.class_date, "
                + "cs.start_time, cs.end_time, cs.lecturer_id, l.name AS lecturer_name "
                + "FROM class_schedules cs "
                + "JOIN courses c ON cs.course_id = c.course_id "
                + "JOIN lecturers l ON cs.lecturer_id = l.lecturer_id "
                + "WHERE cs.schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSchedule(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ClassSchedule mapSchedule(ResultSet rs) throws SQLException {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        schedule.setCourseId(rs.getInt("course_id"));
        schedule.setCourseName(rs.getString("course_name"));
        schedule.setSubject(rs.getString("subject"));
        schedule.setClassDate(rs.getString("class_date"));
        schedule.setStartTime(rs.getString("start_time"));
        schedule.setEndTime(rs.getString("end_time"));
        schedule.setLecturerId(rs.getInt("lecturer_id"));
        schedule.setLecturerName(rs.getString("lecturer_name"));
        return schedule;
    }
}
