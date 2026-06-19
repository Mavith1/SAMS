package com.sams.service;

import com.sams.dao.EnrollmentDAO;
import com.sams.model.Enrollment;

public class EnrollmentService {
    private final EnrollmentDAO enrollmentDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public boolean enrollStudent(Enrollment enrollment) {
      
        return enrollmentDAO.addEnrollment(enrollment);
    }
}