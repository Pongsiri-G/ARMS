package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RegisterController {

    @FXML
    public void userRegister(ActionEvent event) throws IOException {
        checkRegister();
    }

    private void checkRegister() throws IOException {
        FXRouter.goTo("login"); // not finish
    }

    @FXML
    public void nextRegisterClick(MouseEvent event) throws IOException {
        FXRouter.goTo("register-second");
    }

    @FXML
    public void backRegisterClick(MouseEvent event) throws IOException {
        FXRouter.goTo("register-first");
    }
}
