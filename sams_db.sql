CREATE DATABASE IF NOT EXISTS sams_db;
USE sams_db;

CREATE TABLE IF NOT EXISTS courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    reg_no VARCHAR(50) NOT NULL UNIQUE,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    course_id INT NOT NULL,
    CONSTRAINT fk_students_courses
        FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS lecturers (
    lecturer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject_assigned VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date DATE NOT NULL,
    CONSTRAINT fk_enrollments_students
        FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_enrollments_courses
        FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS class_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    subject VARCHAR(100) NOT NULL,
    class_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    lecturer_id INT NOT NULL,
    CONSTRAINT fk_schedules_courses
        FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_schedules_lecturers
        FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id INT NOT NULL,
    student_id INT NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    remarks VARCHAR(255),
    marked_date DATE NOT NULL,
    UNIQUE KEY uq_attendance_schedule_student (schedule_id, student_id),
    CONSTRAINT fk_attendance_schedules
        FOREIGN KEY (schedule_id) REFERENCES class_schedules(schedule_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_attendance_students
        FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT IGNORE INTO users (username, password, role) VALUES
('admin', 'admin123', 'Admin'),
('lecturer', 'lecturer123', 'Lecturer');

INSERT IGNORE INTO courses (course_id, course_name) VALUES
(1, 'CMJD'),
(2, 'GDSE');

INSERT IGNORE INTO lecturers (lecturer_id, name, email, subject_assigned) VALUES
(1, 'Default Lecturer', 'lecturer@sams.local', 'Object-Oriented Programming');

INSERT IGNORE INTO students (student_id, reg_no, student_name, email, phone, course_id) VALUES
(1, 'REG001', 'Sample Student One', 'student1@sams.local', '0711111111', 1),
(2, 'REG002', 'Sample Student Two', 'student2@sams.local', '0722222222', 1);

INSERT IGNORE INTO class_schedules (schedule_id, course_id, subject, class_date, start_time, end_time, lecturer_id) VALUES
(1, 1, 'Object-Oriented Programming', '2026-06-20', '09:00:00', '11:00:00', 1);

INSERT IGNORE INTO attendance (attendance_id, schedule_id, student_id, status, remarks, marked_date) VALUES
(1, 1, 1, 'Present', 'Sample attendance record', '2026-06-20'),
(2, 1, 2, 'Late', 'Arrived after class started', '2026-06-20');
