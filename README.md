# Student Attendance Management System (SAMS)

SAMS is a Java desktop application for managing students, courses, lecturers, class schedules, attendance marking, and attendance reports for an educational institution.

The system supports two user roles:

- Admin: manages users, students, courses, lecturers, class schedules, attendance, and reports.
- Lecturer: marks attendance and views attendance reports.

## Technologies Used

- Java
- Java Swing
- JDBC
- MySQL
- NetBeans IDE
- MySQL Connector/J

## Project Structure

```text
src/com/sams/model      Entity classes
src/com/sams/dao        Database access classes
src/com/sams/service    Business logic layer
src/com/sams/view       Swing user interfaces
src/com/sams/util       Database connection utility
sams_db.sql             Database schema and sample data
```

## Main Functional Modules

- User login and role-based access
- Course Management
- Student Management
- Lecturer Management
- User Management
- Class Scheduling
- Attendance Marking
- Attendance Reporting

## Validation Rules

The application validates common user input before saving records:

- Required fields cannot be empty.
- Student and lecturer emails must use a valid email format.
- Phone numbers must contain 10 digits and start with `0`.
- Usernames must contain 3-30 letters, numbers, or underscores.
- Passwords must contain at least 6 characters.
- Class schedule dates must use `yyyy-MM-dd`.
- Class schedule times must use `HH:mm`.
- Class schedule end time must be after start time.
- Attendance report date filters must use valid date ranges.
- Students must be assigned to an existing course.

## Database Setup

1. Open MySQL Workbench or MySQL Command Line Client.
2. Import and run:

```text
sams_db.sql
```

The SQL script creates the `sams_db` database, required tables, relationships, and sample login data.

The application connects to MySQL using:

```text
Database: sams_db
Username: root
Password: root123
```

If your MySQL password is different, update:

```text
src/com/sams/util/DBConnection.java
```

## Run Instructions

1. Open NetBeans.
2. Select `File > Open Project`.
3. Open the `SAMS` project folder.
4. Make sure MySQL Server is running.
5. Confirm the MySQL Connector/J path is configured in project libraries.
6. Right-click the project and select `Run`.

The application starts from:

```text
com.sams.view.LoginForm
```

## Login Credentials

```text
Admin
Username: admin
Password: admin123

Lecturer
Username: lecturer
Password: lecturer123
```

## Notes

- Course IDs used in Student Management must already exist in the Course Management table.
- Attendance can be marked only after creating class schedules.
- Attendance reports can be filtered by student, course, subject, and date range.

## Screenshots

Add screenshots of these screens before final submission:

- Login screen
- Admin dashboard
- Student Management
- Course Management
- Lecturer Management
- User Management
- Class Scheduling
- Attendance Marking
- Attendance Reports
