package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import ku.cs.models.*; //Add for testing only
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserListFileDatasourceTest;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class RegisterSecondController {

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label errorLabel;

    private String firstName;
    private String lastName;
    private String email;
    private String studentId;

    private UserList userList;
    private UserListFileDatasource datasource;

    @FXML
    private void initialize() {
        errorLabel.setText("");
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv", "departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();

        // Get the data from the first registration step
        List<String> data = (List<String>) FXRouter.getData();
        firstName = data.get(0);
        lastName = data.get(1);
        email = data.get(2);
        studentId = data.get(3);
    }

    @FXML
    public void userRegister(MouseEvent event) {
        registerStudent();
    }

    private void registerStudent() {
        String username = usernameTextField.getText().trim();
        String password = passwordPasswordField.getText().trim();
        String confirmPassword = confirmPasswordPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบถ้วน");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
            return;
        }

        try {
            userList.registerStudent(username, password, confirmPassword, firstName + " " + lastName, studentId, email, false);
            datasource.writeData(userList);
            FXRouter.goTo("login");
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            errorLabel.setText("โปรแกรมเกิดข้อผิดพลาด");
        }
    }

    @FXML
    public void backRegisterClick(MouseEvent event) throws IOException {
        FXRouter.goTo("register-first");
    }
}


