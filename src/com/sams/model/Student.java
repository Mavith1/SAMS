package com.sams.model;

public class Student {

    private int studentId;
    private String regNo;
    private String studentName;
    private String email;
    private String phone;
    private int courseId;

    public Student() {
    }

    public Student(int studentId, String regNo, String studentName,
                   String email, String phone, int courseId) {
        this.studentId = studentId;
        this.regNo = regNo;
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}