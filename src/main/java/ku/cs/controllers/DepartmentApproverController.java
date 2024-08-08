package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DepartmentApproverController {
    @FXML
    protected void onRequestClick() {
        try {
            FXRouter.goTo("department-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onStudentManagementClick() {
        try {
            FXRouter.goTo("department-student-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onLogoutClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAddButtonClick() {
        try {
            FXRouter.goTo("department-approver-add");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
