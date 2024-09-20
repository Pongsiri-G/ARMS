package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {
    private String username;
    private String password;
    private String name;
    private boolean suspended;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.name = name;
        suspended = false;
    }

    public User(String name){
        this.username = null;
        this.password = null;
        this.name = name;
        suspended = false;
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

    public void setSuspended(boolean suspended) { this.suspended = suspended; }

    public boolean getSuspended() { return suspended; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
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
