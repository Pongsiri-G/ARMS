package ku.cs.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    private ArrayList<User> users;
    private FacultyList faculties;

    public UserList() {
        this.users = new ArrayList<>();
        this.faculties = new FacultyList();
    }

    // Add FacultyOfficer
    public void addUser(FacultyOfficer facultyOfficer) {
        if (findUserByUsername(facultyOfficer.getUsername()) == null) {
            Faculty faculty = faculties.findFacultyByName(facultyOfficer.getFaculty().getFacultyName());
            if (faculty != null) {
                facultyOfficer.setFaculty(faculty);
                faculty.getFacultyOfficers().add(facultyOfficer);
                users.add(facultyOfficer);
            }
        }
    }

    // Add DepartmentOfficer
    public void addUser(DepartmentOfficer departmentOfficer) {
        if (findUserByUsername(departmentOfficer.getUsername()) == null) {
            Faculty faculty = faculties.findFacultyByName(departmentOfficer.getFaculty().getFacultyName());
            if (faculty != null) {
                Department department = faculty.findDepartmentByName(departmentOfficer.getDepartment().getDepartmentName());
                if (department != null) {
                    departmentOfficer.setFaculty(faculty);
                    departmentOfficer.setDepartment(department);
                    department.getDepartmentOfficers().add(departmentOfficer);
                    users.add(departmentOfficer);
                }
            }
        }
    }

    // Add Advisor
    public void addUser(Advisor advisor) {
        if (findUserByUsername(advisor.getUsername()) == null) {
            Faculty faculty = faculties.findFacultyByName(advisor.getFaculty());
            if (faculty != null) {
                Department department = faculty.findDepartmentByName(advisor.getDepartment());
                if (department != null) {
                    advisor.setFaculty(faculty);
                    advisor.setDepartment(department);
                    department.getAdvisors().add(advisor);
                    users.add(advisor);
                }
            }
        }
    }

    // Add Student
    public void addUser(Student student) {
        if (findUserByUsername(student.getUsername()) == null) {
            Faculty faculty = faculties.findFacultyByName(student.getEnrolledFaculty().getFacultyName());
            if (faculty != null) {
                Department department = faculty.findDepartmentByName(student.getEnrolledDepartment().getDepartmentName());
                if (department != null) {
                    student.setEnrolledFaculty(faculty);
                    student.setEnrolledDepartment(department);
                    if (student.getStudentAdvisor() != null) {
                        student.setStudentAdvisor(department.findAdvisorByName(student.getStudentAdvisor().getName()));
                    }
                    department.getStudents().add(student);
                    if (student.getUsername() != null && student.getPassword() != null) {
                        users.add(student);
                    }
                }
            }
        }
    }


    // Setters and Getters
    public void setUsers(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
    }

    public void setFacultyList(FacultyList faculties) {
        this.faculties = faculties;
    }

    public FacultyList getFacultyList() {
        return faculties;
    }

    // Register a student with hashed password support
    public void registerStudent(String username, String password, String confirmPassword, String fullName,
                                String studentId, String email, boolean isHashed) throws IllegalArgumentException {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
        }

        boolean foundStudent = false;
        Student matchedStudent = null;

        for (Faculty faculty : faculties.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                for (Student student : department.getStudents()) {
                    if (student.getStudentID().equals(studentId) &&
                            student.getName().equals(fullName) &&
                            student.getEmail().equals(email)) {
                        matchedStudent = student;
                        foundStudent = true;
                        break;
                    }
                }
                if (foundStudent) break;
            }
            if (foundStudent) break;
        }

        // If the student is not found, throw an exception
        if (!foundStudent) {
            throw new IllegalArgumentException("ไม่พบข้อมูลนิสิตในฐานข้อมูลภาควิชา\nกรุณาตรวจสอบข้อมูลที่กรอกอีกครั้ง");
        }

        if (matchedStudent.getUsername() != null && matchedStudent.getPassword() != null) {
            throw new IllegalArgumentException("ข้อมูลนิสิตนี้ได้ทำการลงทะเบียนในระบบคำร้องแล้ว");
        }

        if (findUserByUsername(username) != null) {
            throw new IllegalArgumentException("มีชื่อผู้ใช้นี้ในระบบแล้ว โปรดใช้ชื่อใหม่");
        }

        matchedStudent.setUsername(username);
        matchedStudent.setPassword(password, isHashed);

        users.add(matchedStudent);
    }

    // Find user by username
    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.isUsername(username)) return user;
        }
        return null;
    }

    // Remove user from list
    public void removeUser(User user) {
        this.users.remove(user);
    }

    // Get all users
    public ArrayList<User> getAllUsers() {
        return users;
    }

    // Login with hashed password verification
    public String login(String username, String password) throws IllegalArgumentException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("ชื่อบัญชีผู้ใช้หรือรหัสผ่านไม่สามารถเป็นค่าว่างได้");
        }

        User user = findUserByUsername(username);

        if (user == null || !user.validatePassword(password)) {
            throw new IllegalArgumentException("ชื่อบัญชีผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
        }

        if (user.getSuspended()) {
            throw new IllegalArgumentException("บัญชีผู้ใช้นี้ถูกระงับ ไม่สามารถเข้าสู่ระบบได้");
        }

        user.setLastLogin(LocalDateTime.now());
        return user.getRole();
    }
}
