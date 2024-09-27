package ku.cs.models;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;
    private FacultyList faculties;

    public UserList() {
        this.users = new ArrayList<>();
        //readData();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    // Add FacultyOfficer
    public void addUser(String username, String password, String name, String faculty, boolean isHashed, boolean suspended) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                users.add(new FacultyOfficer(username, password, name, f, isHashed, suspended));
            }
        }
    }

    // Add DepartmentOfficer
    public void addUser(String username, String password, String name, String faculty, String department, boolean isHashed, boolean suspended) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new DepartmentOfficer(username, password, name, f, d, isHashed, suspended));
                }
            }
        }
    }

    // Add Advisor
    public void addUser(String username, String password, String name, String faculty, String department, String advisorID, boolean isHashed, boolean suspended) {
        Faculty f = faculties.findFacultyByName(faculty);
        if (f != null) {
            Department d = f.findDepartmentByName(department);
            if (d != null) {
                Advisor advisor = new Advisor(username, password, name, f, d, advisorID, isHashed, suspended);
                addUser(advisor);
            }
        }
    }

    // Add Student
    public void addUser(String username, String password, String name, String faculty, String department, String studentID, String email, boolean isHashed, boolean suspended) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new Student(username, password, name, f, d, studentID, email, isHashed, suspended));
                }
            }
        }
    }

    /*
    // ping : for dashboard user table view
    public void addTableUser(String imagePath, String username, String name, String role, String faculty, String department, String timeStamp) {
        users.add(new User(imagePath, username, name, role, faculty, department, timeStamp));
    }
     */

    public void setUsers(ArrayList<User> users) {
        this.users.clear();
        this.users.addAll(users);
    }


    public void setFaculties(FacultyList faculties) {
        this.faculties = faculties;
    }

    // Register student with hashed password support
    public void registerStudent(String username, String password, String confirmPassword, String fullName,
                                String studentId, String email, boolean isHashed) throws IllegalArgumentException {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
        }

        boolean foundStudent = false;
        Student matchedStudent = null;

        for (Faculty faculty : faculties.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                for (Student student : department.getStudentList()) {
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

        if (!foundStudent) {
            throw new IllegalArgumentException("ไม่พบข้อมูลนิสิตในฐานข้อมูลภาควิชา กรุณาตรวจสอบข้อมูลที่กรอกอีกครั้ง");
        }

        matchedStudent.setUsername(username);
        matchedStudent.setPassword(password, true); // Password is hashed inside the method if isHashed is false

        users.add(matchedStudent);
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.isUsername(username)) return user;
        }
        return null;
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }

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

        return user.getRole();
    }
}