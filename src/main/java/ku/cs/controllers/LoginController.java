package ku.cs.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.Advisor;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;

import javafx.fxml.FXML;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserListFileDatasourceTest;

import java.io.IOException;
import java.time.LocalDateTime;
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
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
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

            if (role != null) {
                datasource.writeData(userList);
                redirect(role);  // Redirect based on role
            }
        }
        catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // Handle redirection based on the user role
    private void redirect(String role) throws IOException {
        String loggedInUser = userList.findUserByUsername(username.getText().trim()).getUsername();
        User user = userList.findUserByUsername(username.getText().trim());

        switch (role) {
            case "Admin":
                FXRouter.goTo("dashboard");
                break;
            case "เจ้าหน้าที่คณะ":
                if (user.getLastLogin() == null) { FXRouter.goTo("change-password", loggedInUser); }
                else { FXRouter.goTo("faculty-officer", loggedInUser); }
                break;
            case "เจ้าหน้าที่ภาควิชา":
                if (user.getLastLogin() == null) { FXRouter.goTo("change-password", loggedInUser); }
                else { FXRouter.goTo("department-officer", loggedInUser); }
                break;
            case "อาจารย์":
                if (user.getLastLogin() == null) { FXRouter.goTo("change-password", loggedInUser); }
                else { FXRouter.goTo("advisor", loggedInUser); }
                break;
            case "นิสิต":
                FXRouter.goTo("student-create-request", loggedInUser);
                break;
            default:
                throw new NullPointerException("Unrecognized role: " + role);
        }
    }

    public void toRegisterPageClick() throws IOException {
        FXRouter.goTo("register-first");
    }
}