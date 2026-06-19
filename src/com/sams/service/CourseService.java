package com.sams.service;

import com.sams.dao.CourseDAO;
import com.sams.model.Course;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO = new CourseDAO();

    public boolean registerCourse(Course course) {
        return courseDAO.addCourse(course);
    }

    public List<Course> getAllCoursesList() {
        return courseDAO.getAllCourses();
    }

    public boolean modifyCourse(Course course) {
        return courseDAO.updateCourse(course);
    }

    public boolean removeCourse(int id) {
        return courseDAO.deleteCourse(id);
    }
}