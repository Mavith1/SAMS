package com.sams.model;

public class Attendance {

    private int attendanceId;
    private int scheduleId;
    private int studentId;
    private String status;
    private String remarks;
    private String markedDate;

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getMarkedDate() {
        return markedDate;
    }

    public void setMarkedDate(String markedDate) {
        this.markedDate = markedDate;
    }
}
