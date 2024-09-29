package ku.cs.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;

import javafx.fxml.FXML;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;

    private UserList userList;

    public LoginController() {
        UserListFileDatasource datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv");
        this.userList = datasource.readData();
        System.out.println("Loaded users: " + this.userList.getAllUsers());
    }

    public void initialize() {
        errorLabel.setText("");
    }

    public void userLogin() throws IOException {
        try {
            // test firstLogin to changepassword
            User loggedInUser = userList.findUserByUsername(username.getText().trim());
            //System.out.println("Loaded users: " + userList.getAllUsers()); // Debugging Only, will remove later

            if ((username.getText().trim().equals("Admin")) && (password.getText().trim().equals("0000"))) {
                FXRouter.goTo("dashboard");
            } // TEMPORARY LOGIN FOR TEST ONLY

            else if ((username.getText().trim().equals("Advisor")) && (password.getText().trim().equals("0000"))) {
                FXRouter.goTo("change-password", loggedInUser);
                // อันเดิมคือ advisor เปลี่ยนการส่งข้ามหน้าเพื่อเปลี่ยนรหัสผ่านหากเป็นการเข้าใช้งานครั้งเเรก
            } // TEMPORARY LOGIN FOR TEST ONLY

            else if ((username.getText().trim().equals("Department")) && (password.getText().trim().equals("0000"))) {
                FXRouter.goTo("department-request");
            } // TEMPORARY LOGIN FOR TEST ONLY

            String role = userList.login(username.getText().trim(), password.getText().trim());

            if (role != null) {
                redirect(role);  // Redirect based on role
            } else {
                System.out.println("Login failed. Invalid username or password.");
                // Debugging Only, will remove later
            }
        }
        catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
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
                FXRouter.goTo("student-create-request");
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