package com.sams.service;

import com.sams.dao.StudentDAO;
import com.sams.model.Student;
import java.util.List;

public class StudentService {
    
    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public boolean registerStudent(Student student) {
        // මෙතන තමයි Business Logic ලියන්නේ (උදා: Data validation)
        if (student.getRegNo().isEmpty() || student.getStudentName().isEmpty()) {
            return false;
        }
        return studentDAO.addStudent(student);
    }

    public List<Student> getAllStudentsList() {
        return studentDAO.getAllStudents();
    }

    public boolean modifyStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    public boolean removeStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    public List<Student> findStudentsByName(String name) {
        return studentDAO.searchStudents(name);
    }
}