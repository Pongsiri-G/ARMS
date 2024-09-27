package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

public abstract class User {
    private String username;
    private String password;
    private String name;
    private String role;
    private String faculty;
    private String department;
    private boolean suspended;

    // Constructor for new users (password is hashed)
    public User(String username, String password, String name) {
        this(username, password, name, false, false);
    }

    // Constructor for cases when only the name is provided (when creating a temporary user)
    public User(String name) {
        this.username = null;
        this.password = null;
        this.name = name;
        this.suspended = false;
    }

    // Constructor for users loaded from a file (password is either hashed or plaintext)
    public User(String username, String password, String name, boolean isHashed, boolean suspended) {
        this.username = username;
        if (isHashed) {
            this.password = password; // Use the hashed password as is
        } else {
            this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray()); // Hash if not already hashed
        }
        this.name = name;
        this.suspended = suspended;
    }

    // Check if the username matches
    public boolean isUsername(String username) {
        return this.username != null && this.username.equals(username);
    }

    // Setters for fields
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password, boolean isHashed) {
        if (isHashed) {
            this.password = password; // Use the hashed password directly
        } else {
            this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray()); // Hash the password
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String firstName, String lastName) {
        this.name = firstName + " " + lastName;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    // Getters for fields
    public boolean getSuspended() {
        return suspended;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // ping : สร้าง getter เพื่อ นำไปใช้ใน user table view
    public String getFacultyName() {
        return faculty;
    }

    public String getDepartmentName() {
        return department;
    }

    public String getName() {
        return name;
    }

    public String getRoleName() {
        return role;
    }
    // ping : สร้าง getter เพื่อ นำไปใช้ใน user table view

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + " " + name + " (" + username + ")";
    }
}
