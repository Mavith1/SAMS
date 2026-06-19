package com.sams.service;

import com.sams.dao.AttendanceDAO;
import com.sams.model.Attendance;
import com.sams.model.AttendanceReport;
import com.sams.model.Student;
import java.util.List;
import java.util.Map;

public class AttendanceService {

    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public List<Student> getStudentsForSchedule(int scheduleId) {
        return attendanceDAO.getStudentsForSchedule(scheduleId);
    }

    public Map<Integer, Attendance> getAttendanceBySchedule(int scheduleId) {
        return attendanceDAO.getAttendanceBySchedule(scheduleId);
    }

    public boolean saveAttendance(Attendance attendance) {
        if (attendance.getScheduleId() <= 0 || attendance.getStudentId() <= 0 || attendance.getStatus().isEmpty()) {
            return false;
        }
        return attendanceDAO.saveAttendance(attendance);
    }

    public List<AttendanceReport> getAttendanceReports(String studentName, String courseName, String subject,
                                                       String fromDate, String toDate) {
        return attendanceDAO.getAttendanceReports(studentName, courseName, subject, fromDate, toDate);
    }
}
