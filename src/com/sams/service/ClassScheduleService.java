package com.sams.service;

import com.sams.dao.ClassScheduleDAO;
import com.sams.model.ClassSchedule;
import java.util.List;

public class ClassScheduleService {

    private final ClassScheduleDAO classScheduleDAO = new ClassScheduleDAO();

    public boolean createSchedule(ClassSchedule schedule) {
        if (schedule.getCourseId() <= 0 || schedule.getLecturerId() <= 0
                || schedule.getSubject().isEmpty() || schedule.getClassDate().isEmpty()
                || schedule.getStartTime().isEmpty() || schedule.getEndTime().isEmpty()) {
            return false;
        }
        return classScheduleDAO.addSchedule(schedule);
    }

    public List<ClassSchedule> getAllSchedulesList() {
        return classScheduleDAO.getAllSchedules();
    }

    public boolean modifySchedule(ClassSchedule schedule) {
        if (schedule.getScheduleId() <= 0) {
            return false;
        }
        return createOrUpdate(schedule);
    }

    private boolean createOrUpdate(ClassSchedule schedule) {
        if (schedule.getCourseId() <= 0 || schedule.getLecturerId() <= 0
                || schedule.getSubject().isEmpty() || schedule.getClassDate().isEmpty()
                || schedule.getStartTime().isEmpty() || schedule.getEndTime().isEmpty()) {
            return false;
        }
        return classScheduleDAO.updateSchedule(schedule);
    }

    public boolean removeSchedule(int id) {
        return classScheduleDAO.deleteSchedule(id);
    }
}
