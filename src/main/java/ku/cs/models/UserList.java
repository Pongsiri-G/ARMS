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
                users.add(new FacultyStaff(username, password, name, f));
            }
        }
    }
    public void addUser(String username, String password, String name, String faculty, String department) {
        if (findUserByUsername(username) == null) {
            Faculty f = faculties.findFacultyByName(faculty);
            if (f != null) {
                Department d = f.findDepartmentByName(department);
                if (d != null) {
                    users.add(new DepartmentStaff(username, password, name, f, d));
                }

            }
        }
    }
    public void addUser(String username, String password, String name, String faculty, String department, String advisorID) {

    }
    public void addUser(String username, String password, String name, String faculty, String department, String studentID, String email) {
        Student newUser = new Student(username, password, name, faculty, department, studentID, email);
        User exist = findUserByUsername(username);
        if (exist == null) users.add(newUser);
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
            if (user.validatePassword(password)) return user;
        }
        return null;
    }
}
