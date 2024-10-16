package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ku.cs.services.FXRouter;

import ku.cs.models.*; //Add for testing only
import ku.cs.services.UserListFileDatasource;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class RegisterController {

    @FXML private VBox createUserPane;
    @FXML private TextField firstnameTextField;
    @FXML private TextField lastnameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField studentIdTextField;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label errorLabel;

    private UserList userList;
    private UserListFileDatasource datasource;

    @FXML
    private void initialize() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv", "departmentofficerlist.csv", "facdeplist.csv");
        errorLabel.setText("");
        createUserPane.setVisible(false);
        this.userList = datasource.readData();
    }

    public void checkRegister() {
        try {
            String firstName = firstnameTextField.getText().trim();
            String lastName = lastnameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String studentId = studentIdTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String password = passwordPasswordField.getText().trim();
            String confirmPassword = confirmPasswordPasswordField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || studentId.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                throw new NullPointerException("โปรดกรอกข้อมูลให้ครบถ้วน");
            }
            if (!password.equals(confirmPassword)) {
                errorLabel.setText("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
                return;
            }
            userList.registerStudent(username, password, confirmPassword, firstName + " " + lastName, studentId, email, false);
            datasource.writeData(userList);
            FXRouter.goTo("login");
        } catch (IOException e) {
            errorLabel.setText("โปรแกรมเกิดข้อผิดพลาด");
        } catch (NullPointerException e) {
            errorLabel.setText(e.getMessage());
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }
    public void toLoginPageClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
    public void toCreateUserClick(MouseEvent event) throws IOException {
        createUserPane.setVisible(true);
    }
    public void closeCreateUserClick(MouseEvent event) throws IOException {
        createUserPane.setVisible(false);
    }
}

