package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.Advisor;
import ku.cs.models.User;
import ku.cs.services.FXRouter;
import java.io.IOException;

public class ChangePasswordController {
    @FXML private Label userLabel;
    @FXML
    private TextField confirmYourPasswordField;

    @FXML
    private Label errorPasswordLabel;

    @FXML
    private TextField newPasswordField;

    @FXML private Label errorLabel;

    private User user;

    @FXML
    public void initialize() {
        user = (User) FXRouter.getData(); // รับข้อมูลผุ้ใช้ที่ส่งมาจากหน้า login
        showUserInfo(user); // เเสดงชื่อผู้ช้ในหน้า change-password
        //errorLabel.setText("");
    }


    private void showUserInfo(User user) {
        //userLabel.setText(user.getUsername());
    }

    @FXML
    protected void onToAdvisorButtonClick() {
        try {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmYourPasswordField.getText();
            if (confirmPassword.equals(newPassword)) {
                //user.setPassword(newPassword, false); // Hash รหัสผ่านใหม่
                // เปลี่ยนสถานะการเข้าสู่ระบบครั้งแรกให้เป็น false
                //user.setFirstLogin(false);
                // ส่งต่อข้อมูลไปยังหน้า advisor-view
                //Advisor advisor = (Advisor) user;
                FXRouter.goTo("advisor-view");
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
