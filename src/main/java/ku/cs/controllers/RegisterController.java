package ku.cs.controllers;

import javafx.event.ActionEvent;
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
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordPasswordField;
    @FXML private PasswordField confirmPasswordPasswordField;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {
    }

    @FXML
    public void userRegister(MouseEvent event) {
        checkRegister();
    }

    private void checkRegister() {
        //NOT FINISH
//        try {
//            String firstname = firstnameTextField.getText().trim();
//            String lastname = lastnameTextField.getText().trim();
//            String email = emailTextField.getText().trim();
//            String studentId = studentIdTextField.getText().trim();
//
//            String fullName = firstname + " " + lastname;
//
//            String username = usernameTextField.getText().trim();
//            String password = passwordPasswordField.getText().trim();
//            String confirmPassword = confirmPasswordPasswordField.getText().trim();
//
//            FacultyList data = new FacultyList();
//            data.addFaculty("Science");
//            data.findFacultyByName("Science").addDepartment("Computer");
//
//            Student existingStudent = new Student("John Doe", "6610400000", "john.d@ku.th");
//            data.findFacultyByName("Science")
//                    .findDepartmentByName("Computer")
//                    .addStudent(existingStudent);
//
//            UserList userList = new UserList();
//
//            userList.registerStudent(username, password, confirmPassword, fullName, studentId, email);
//
//            FXRouter.goTo("login");
//
//        } catch (IllegalArgumentException e) {
//            errorLabel.setText(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    @FXML
    public void nextRegisterClick(MouseEvent event) {
        try {
            // Wait for implement
            FXRouter.goTo("register-second");
        }
        catch (Exception e) {
            //Wait for implement
        }
    }

    @FXML
    public void backRegisterClick(MouseEvent event) throws IOException {
        FXRouter.goTo("register-first");
    }
}
