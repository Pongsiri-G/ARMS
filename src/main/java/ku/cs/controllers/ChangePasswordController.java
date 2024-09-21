package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.models.User;
import ku.cs.services.FXRouter;
import java.io.IOException;

public class ChangePasswordController {
    @FXML private Label userLabel;
    //@FXML private TextField NewpasswordTextField;
    //@FXML private TextField ConfirmPasswordTextField;
    @FXML private Label errorLabel;

    private User user;
    private User userPassWord;

    @FXML
    public void initialize() {
//        User user = new User("advisor", "123", "Putt");
//        showUserInfo(user);
    }


    private void showUserInfo(User user) {
        userLabel.setText(user.getUsername());
    }

    @FXML
    protected void onToAdvisorButtonClick() {
        try {
            FXRouter.goTo("advisor");
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
