package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;

public class ChangePasswordController {
    @FXML private Label userLabel;
    @FXML private PasswordField confirmYourPasswordField;
    @FXML private Label errorPasswordLabel;
    @FXML private PasswordField newPasswordField;
    private User user;
    private UserList userList;
    private UserListFileDatasource datasource;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv", "departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
        user = userList.findUserByUsername((String) FXRouter.getData());
        errorPasswordLabel.setText("");
        userLabel.setText("");
        showUserInfo(user);
    }

    private void showUserInfo(User user) {
        userLabel.setText("ผู้ใช้: " + user.getUsername());
    }

    @FXML
    protected void onChangePasswordButtonClick() {
        try {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmYourPasswordField.getText();

            if (confirmPassword.equals(newPassword)) {
                user.setPassword(newPassword, false);
                user.setLastLogin(LocalDateTime.now());
                datasource.writeData(userList);
                String loggedInUser = user.getUsername();


                if (user instanceof Advisor) {
                    FXRouter.goTo("advisor", loggedInUser);
                } else if (user instanceof DepartmentOfficer) {
                    FXRouter.goTo("department-officer", loggedInUser);
                } else if (user instanceof FacultyOfficer) {
                    FXRouter.goTo("faculty-officer", loggedInUser);
                }
            }else {
                errorPasswordLabel.setText("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
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
