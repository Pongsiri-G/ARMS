package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
    String username;
    String password;
    String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.name = name;
    }



    public boolean isUsername(String username) {
        return this.username.equals(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return null;
    }

    public Department getDepartment() {
        return null;
    }

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    @Override
    public String toString() {
        return name + " " + username;
    }
}
