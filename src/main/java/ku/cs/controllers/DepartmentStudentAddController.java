package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class DepartmentStudentAddController {
    @FXML
    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("department-student-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onApplyButtonClick() {
        try {
            FXRouter.goTo("department-student-manage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
