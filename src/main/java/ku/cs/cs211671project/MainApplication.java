package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.services.*;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "CS211 Project", 1440, 1024);
        configRoutes();
        FXRouter.goTo("login");
        //UserListFileDatasourceTest u = new UserListFileDatasourceTest(); //FOR TESTING ONLY
        //u.runTests(); //FOR TESTING ONLY
        //RequestListFileDatasourceTest r = new RequestListFileDatasourceTest(); //FOR TESING ONLY
        //r.runTests();
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("login", viewPath + "login-view.fxml");
        FXRouter.when("creator", viewPath + "creator-view.fxml");
        FXRouter.when("settings", viewPath + "settings-view.fxml");
        FXRouter.when("dashboard", viewPath + "admin-view.fxml");
        FXRouter.when("advisor", viewPath + "advisor-view.fxml");
        FXRouter.when("advisor-nisit", viewPath + "advisor-nisit-view.fxml");
        FXRouter.when("advisor-request-nisit", viewPath + "advisor-request-nisit-view.fxml");
        FXRouter.when("student-create-request", viewPath + "student-create-request.fxml");
        FXRouter.when("student-request-list-view", viewPath + "student-request-list-view.fxml");
        FXRouter.when("register", viewPath + "register-view.fxml");;
        FXRouter.when("user-management", viewPath + "user-management.fxml");
        FXRouter.when("staff-advisor-management", viewPath + "staff-advisor-management.fxml");
        FXRouter.when("department-faculty-management", viewPath + "department-faculty-management.fxml");
        FXRouter.when("all-request", viewPath + "all-request.fxml");
        FXRouter.when("approved-request", viewPath + "approved-request.fxml");
        FXRouter.when("change-password", viewPath + "change-password-view.fxml");
        FXRouter.when("faculty-officer", viewPath + "faculty-officer-view.fxml");
        FXRouter.when("faculty-officer-manage-request", viewPath + "faculty-officer-manage-request-view.fxml");
        FXRouter.when("department-officer", viewPath + "department-officer-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}