package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class ApprovedController {
    @FXML
    protected void onLogoutClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onAllRequestClick() {
        try {
            FXRouter.goTo("all-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onUserClick() {
        try {
            FXRouter.goTo("user-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onDepartmentAndFacultyClick() {
        try {
            FXRouter.goTo("department-faculty-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onStaffClick() {
        try {
            FXRouter.goTo("staff-advisor-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
