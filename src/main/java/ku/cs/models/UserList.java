package ku.cs.models;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;
    private FacultyList faculties;


    public UserList() {
        this.users = new ArrayList<>();
        readData();
    }
    public void readData() {
        faculties = new FacultyList();
        faculties.addFaculty("Science");
        faculties.addFaculty("Engineer");
        faculties.findFacultyByName("Science").addDepartment("Computer");
        faculties.findFacultyByName("Science").addDepartment("Math");
        faculties.findFacultyByName("Engineer").addDepartment("Electronics");
        faculties.findFacultyByName("Engineer").addDepartment("Mechatronic");
    }

    public void addUser(User user) { this.users.add(user); }
    public void addUser(String username, String password, String name, String faculty) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                users.add(new FacultyOfficer(username, password, name, f));
            }
        }
    }
    public void addUser(String username, String password, String name, String faculty, String department) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new DepartmentOfficer(username, password, name, f, d));
                }

            }
        }
    }
    public void addUser(String username, String password, String name, Faculty faculty, Department department, String advisorID) {
        Advisor advisor = new Advisor(username, password, name, faculty, department, advisorID);// เพิ่มอาจารย์ที่ปรึกษา
        addUser(advisor);

    }

    public void addUser(String username, String password, String name, String faculty, String department, String studentID, String email) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new Student(username, password, name, f, d, studentID, email));
                }

            }
        }
    }

    public void registerstudent(String username, String password, String confirmPassword, String fullName,
                                String studentId, String email) throws IllegalArgumentException {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
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
            throw new IllegalArgumentException("Student data not found in any department.");
        }

        matchedStudent.setUsername(username);
        matchedStudent.setPassword(password);

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

    public ArrayList<User> getAllUsers() { return users; }

    public User login(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null) {
            if (user instanceof FacultyOfficer fs) {
                if (user.validatePassword(password) && !user.getSuspended()) return fs;
                else return null;
            }
            else if (user instanceof DepartmentOfficer ds) {
                if (user.validatePassword(password) && !user.getSuspended()) return ds;
                else return null;
            }
            else if (user instanceof Student student) {
                if (user.validatePassword(password) && !user.getSuspended()) return student;
                else return null;
            }
        }
        return null;
    }
}