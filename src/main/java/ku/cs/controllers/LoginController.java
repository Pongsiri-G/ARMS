package ku.cs.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;

import javafx.fxml.FXML;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserListFileDatasourceTest;

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
    private UserListFileDatasource datasource;

    public LoginController() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisors.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
    }

    public void initialize() {
        errorLabel.setText("");
        System.out.println("Loaded users: " + this.userList.getAllUsers());
    }

    public void userLogin() throws IOException {
        try {
            //System.out.println("Loaded users: " + userList.getAllUsers()); // Debugging Only, will remove later

            if ((username.getText().trim().equals("Admin")) && (password.getText().trim().equals("0000"))) {
                FXRouter.goTo("dashboard");
            } // TEMPORARY LOGIN FOR TEST ONLY

            String role = userList.login(username.getText().trim(), password.getText().trim());

            if (role != null && role.equals("Student")) {
                redirect(role);
            }

            if (role != null && !role.equals("Student")) {
                // append
                User loggedinUser = userList.findUserByUsername(username.getText().trim());
                if (loggedinUser != null) {
                    FXRouter.goTo("change-password", loggedinUser.getUsername());
                }else {
                    //datasource.writeData(userList);
                    redirect(role);  // Redirect based on role
                }
                // เปลี่ยนบรรทัดมาไว้ตรงนี้ลองๆ
                datasource.writeData(userList);
            }
        }
        catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // Handle redirection based on the user role
    private void redirect(String role) throws IOException {
        String loggedInUser = userList.findUserByUsername(username.getText().trim()).getUsername();

        switch (role) {
            case "Admin":
                FXRouter.goTo("dashboard");
                break;
            case "Advisor":
                FXRouter.goTo("change-password", loggedInUser);
                break;
            case "DepartmentOfficer":
                FXRouter.goTo("department-request", loggedInUser);
                break;
            case "Student":
                FXRouter.goTo("student-create-request", loggedInUser);
                break;
//            case "FacultyOfficer":  // Handling FacultyOfficer role
//                //FXRouter.goTo("faculty-dashboard");  // Navigate to faculty dashboard (Wait for Putt Add fxml)
//                break;
            default:
                throw new NullPointerException("Unrecognized role: " + role);
        }
    }

    public void toRegisterPageClick() throws IOException {
        FXRouter.goTo("register-first");
    }
}