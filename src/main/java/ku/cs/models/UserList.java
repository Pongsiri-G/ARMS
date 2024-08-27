package ku.cs.models;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;

    public UserList() { this.users = new ArrayList<>(); }

    public void addUser(User user) { this.users.add(user); }
    public void addUser(String username, String password, String name, String faculty) {}
    public void addUser(String username, String password, String name, String faculty, String department) {}
    public void addUser(String username, String password, String name, String faculty, String department, String advisorID) {}
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
