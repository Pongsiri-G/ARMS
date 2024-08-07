package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DepartmentStudentManageController {
    @FXML
    protected void onRequestClick() {
        try {
            FXRouter.goTo("department-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onApproverManagementClick() {
        try {
            FXRouter.goTo("department-approver");
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
            FXRouter.goTo("department-student-add");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
