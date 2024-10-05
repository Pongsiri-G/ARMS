package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;

public class ChangePasswordController {
    @FXML private Label userLabel;
    @FXML private TextField confirmYourPasswordField;
    @FXML private Label errorPasswordLabel;
    @FXML private TextField newPasswordField;
    private User user;
    private UserList userList;
    private UserListFileDatasource datasource;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData();
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv", "departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
        errorPasswordLabel.setText("");
        userLabel.setText("");
        showUserInfo(user);
    }

    private void showUserInfo(User user) {
        if (user instanceof DepartmentOfficer) {
            DepartmentOfficer officer = (DepartmentOfficer) user;
            userLabel.setText(officer.getUsername());
        } else if (user instanceof Advisor) {
            Advisor advisor = (Advisor) user;
            userLabel.setText(advisor.getUsername());
        } else if (user instanceof FacultyOfficer) {
            FacultyOfficer officer = (FacultyOfficer) user;
            userLabel.setText(officer.getUsername());
        }
    }

    @FXML
    protected void onChangePasswordButtonClick() {
        try {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmYourPasswordField.getText();

            if (confirmPassword.equals(newPassword)) {
                User user = userList.findUserByUsername(userLabel.getText());
                user.setPassword(newPassword, false);
                user.setLastLogin(LocalDateTime.now());
                datasource.writeData(userList);
                String loggedInUser = userList.findUserByUsername(userLabel.getText().trim()).getUsername();


                if (user instanceof Advisor) {
                    FXRouter.goTo("advisor", loggedInUser);
                } else if (user instanceof DepartmentOfficer) {
                    FXRouter.goTo("department-request", loggedInUser);
                } else if (user instanceof FacultyOfficer) {
                    FXRouter.goTo("faculty-officer", loggedInUser);
                }
            }else {
                errorPasswordLabel.setText("Passwords do not match");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void clickExitTologin() {
        try {
            FXRouter.goTo("login");
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
