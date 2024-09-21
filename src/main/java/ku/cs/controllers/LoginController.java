package ku.cs.controllers;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;

import javafx.fxml.FXML;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;


    private UserList userList;

    public LoginController() {
        UserListFileDatasource datasource = new UserListFileDatasource("data/test", "userlist.csv");
        this.userList = new UserList();
        this.userList.setUsers((ArrayList<User>) datasource.readData());
    }

    public void userLogin() throws IOException {
        System.out.println("Loaded users: " + userList.getAllUsers());
        String role = userList.login(username.getText().trim(), password.getText().trim());

        if ((username.getText().trim().equals("Admin")) && (password.getText().trim().equals("0000"))) {
            FXRouter.goTo("dashboard");
        } // TEMPORARY LOGIN FOR TEST ONLY

        if (role != null) {
            redirect(role);  // Redirect based on role
        } else {
            System.out.println("Login failed. Invalid username or password.");
            // Debugging Only, will remove later
        }
    }

    // Handle redirection based on the user role
    private void redirect(String role) throws IOException {
        switch (role) {
            case "Admin":
                FXRouter.goTo("dashboard");
                break;
            case "Advisor":
                FXRouter.goTo("change-password");
                break;
            case "DepartmentOfficer":
                FXRouter.goTo("department-request");
                break;
            case "Student":
                FXRouter.goTo("student");
                break;
            case "FacultyOfficer":  // Handling FacultyOfficer role
                //FXRouter.goTo("faculty-dashboard");  // Navigate to faculty dashboard (Wait for Putt Add fxml)
                break;
            default:
                throw new NullPointerException("Unrecognized role: " + role);
        }
    }

    public void toRegisterPageClick() throws IOException {
        FXRouter.goTo("register-first");
    }
}
