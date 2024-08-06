package ku.cs.cs211671project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "CS211 Project", 1440, 1024);
        configRoutes();
        FXRouter.goTo("login");
    }

    private void configRoutes() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("login", viewPath + "login-view.fxml");
        FXRouter.when("dashboard", viewPath + "admin-view.fxml");
        FXRouter.when("advisor", viewPath + "advisor-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}