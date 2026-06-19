package com.sams.service;

import com.sams.dao.LecturerDAO;
import com.sams.model.Lecturer;
import java.util.List;

public class LecturerService {

    private final LecturerDAO lecturerDAO = new LecturerDAO();

    public boolean registerLecturer(Lecturer lecturer) {
        if (lecturer.getName().isEmpty() || lecturer.getEmail().isEmpty() || lecturer.getSubject().isEmpty()) {
            return false;
        }
        return lecturerDAO.addLecturer(lecturer);
    }

    public List<Lecturer> getAllLecturersList() {
        return lecturerDAO.getAllLecturers();
    }

    public boolean modifyLecturer(Lecturer lecturer) {
        if (lecturer.getName().isEmpty() || lecturer.getEmail().isEmpty() || lecturer.getSubject().isEmpty()) {
            return false;
        }
        return lecturerDAO.updateLecturer(lecturer);
    }

    public boolean removeLecturer(int id) {
        return lecturerDAO.deleteLecturer(id);
    }
}
