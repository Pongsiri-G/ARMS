package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import ku.cs.models.*; //Add for testing only
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField studentIdTextField;
    @FXML private Label errorLabel;

    private String firstName;
    private String lastName;
    private String email;
    private String studentId;

    @FXML
    private void initialize() {
        errorLabel.setText("");
    }

    @FXML
    public void nextRegisterClick(MouseEvent event) {
        checkRegister();
    }

    private void checkRegister() {
        firstName = firstnameTextField.getText().trim();
        lastName = lastnameTextField.getText().trim();
        email = emailTextField.getText().trim();
        studentId = studentIdTextField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || studentId.isEmpty()) {
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบถ้วน");
            return;
        }

        try {
            FXRouter.goTo("register-second", List.of(firstName, lastName, email, studentId));
        } catch (IOException e) {
            errorLabel.setText("โปรแกรมเกิดข้อผิดพลาด");
            System.err.println(e.getMessage());
        }
    }
}

