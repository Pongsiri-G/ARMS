package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Advisor;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import java.io.IOException;

public class ChangePasswordController {
    @FXML private Label userLabel;
    @FXML private TextField confirmYourPasswordField;
    @FXML private Label errorPasswordLabel;
    @FXML private TextField newPasswordField;
    @FXML private Label errorLabel;
    private User user;
    private UserList userList;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData(); // รับข้อมูลผุ้ใช้ที่ส่งมาจากหน้า login
        showUserInfo(user); // เเสดงชื่อผู้ช้ในหน้า change-password
        errorLabel.setText("");
    }


    private void showUserInfo(User user) {
        userLabel.setText(user.getUsername());
    }

    @FXML
    protected void onToAdvisorButtonClick() {
        try {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmYourPasswordField.getText();
            if (confirmPassword.equals(newPassword)) {
                user.setPassword(newPassword, false);
                user.setLastLogin(java.time.LocalDateTime.now());
                String role = userLabel.getText().trim();
                String loggedInUser = userList.findUserByUsername(userLabel.getText().trim()).getUsername();
                switch (role) {
                    case "Advisor":
                        FXRouter.goTo("advisor", loggedInUser);
                        break;
                    case "DepartmentOfficer":
                        FXRouter.goTo("department-request", loggedInUser);
                        break;
                    default:
                        throw new NullPointerException("Unrecognized role: " + role);
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
/*
    private void clearForm() {
        errorLabel.setText(""); // error label
        toppingTextField.setText(""); // text field
    }*/
}
