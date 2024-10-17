package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

public abstract class User {
    private String username;
    private String password;
    private String name;
    private boolean suspended;
    private LocalDateTime lastLogin;
    private String profilePicturePath;
    private UserPreferences preferences;

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
    }

    // Constructor for users loaded from a file (password is either hashed or plaintext)
    public User(String username, String password, String name, boolean isHashed, boolean suspended) {
        this.username = username;
        if (password != null) {
            if (isHashed) {
                this.password = password; // Use the hashed password as is
            } else {
                this.password = BCrypt.withDefaults().hashToString(12, password.toCharArray()); // Hash if not already hashed
            }
        } else {
            this.password = null; // Handle case where password is null
        }
        this.name = name;
        this.suspended = suspended;
        this.lastLogin = null;
        this.preferences = new UserPreferences();
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

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    public UserPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreferences preferences) {
        this.preferences = preferences;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return name + " (" + username + ")";
    }
}