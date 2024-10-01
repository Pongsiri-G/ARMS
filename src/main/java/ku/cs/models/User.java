package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public abstract class User {
    private String username;
    private String password;
    private String name;
    private String role;
    private boolean suspended;
    private LocalDateTime lastLogin;
    private String profilePicturePath;

    public static final String DEFAULT_PROFILE_PICTURE_PATH = "src/main/resources/images/profile.jpg";

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
        this.lastLogin = null;
        this.profilePicturePath = DEFAULT_PROFILE_PICTURE_PATH;
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
        this.lastLogin = null;
        this.profilePicturePath = DEFAULT_PROFILE_PICTURE_PATH;
    }
    /*
    //ping
    public User(String profilePicturePath, String username, String name, String role, String faculty, String department, String timeStamp) {
        this.profilePicturePath = profilePicturePath;
        this.username = username;
        this.name = name;
        this.role = role;
        this.faculty = faculty;
        this.department = department;
        this.timeStamp = timeStamp;
    }
     */

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

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public String getProfilePicturePath(){
        return profilePicturePath;
    }

    public boolean getSuspended() {
        return suspended;
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

    public String getRole() {return role;}
    // ping : สร้าง getter เพื่อ นำไปใช้ใน user table view

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    @Override
    public String toString() {
        return getRole() + " " + name + " (" + username + ")";
    }
}