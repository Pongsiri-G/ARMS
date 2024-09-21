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
    public void addUser(String username, String password, String name, String faculty, boolean isHashed) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                users.add(new FacultyOfficer(username, password, name, f, isHashed));
            }
        }
    }

    // Add DepartmentOfficer
    public void addUser(String username, String password, String name, String faculty, String department, boolean isHashed) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new DepartmentOfficer(username, password, name, f, d, isHashed));
                }
            }
        }
    }

    // Add Advisor
    public void addUser(String username, String password, String name, String faculty, String department, String advisorID, boolean isHashed) {
        Faculty f = faculties.findFacultyByName(faculty);
        if (f != null) {
            Department d = f.findDepartmentByName(department);
            if (d != null) {
                Advisor advisor = new Advisor(username, password, name, f, d, advisorID, isHashed);
                addUser(advisor);
            }
        }
    }

    // Add Student
    public void addUser(String username, String password, String name, String faculty, String department, String studentID, String email, boolean isHashed) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new Student(username, password, name, f, d, studentID, email, isHashed));
                }
            }
        }
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


    public void setFaculties(FacultyList faculties) {
        this.faculties = faculties;
    }

    // Register student with hashed password support
    public void registerStudent(String username, String password, String confirmPassword, String fullName,
                                String studentId, String email, boolean isHashed) throws IllegalArgumentException {
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
        matchedStudent.setPassword(password); // Password is hashed inside the method if isHashed is false

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
    public String login(String username, String password) {
        User user = findUserByUsername(username);

        if (user != null && user.validatePassword(password) && !user.getSuspended()) {
            return user.getRole();
        }

        return null;
    }
}
