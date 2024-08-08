package ku.cs.controllers;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ku.cs.services.FXRouter;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public void userLogin() throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        if (username.getText().toString().equals("admin") && password.getText().toString().equals("123")) {
            FXRouter.goTo("dashboard");
        } else if (username.getText().toString().equals("advisor") && password.getText().toString().equals("123")) {
            FXRouter.goTo("advisor");
        } else if (username.getText().toString().equals("staff") && password.getText().toString().equals("123")) {
            FXRouter.goTo("department-request");
        } else if (username.getText().toString().equals("student") && password.getText().toString().equals("123")) {
            FXRouter.goTo("student");
        }
    }

    public void toRegisterPageClick() throws IOException {
        FXRouter.goTo("register-first");
    }
}