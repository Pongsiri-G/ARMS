package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentRequestListViewController {

    @FXML
    private ImageView optionDropdown;

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }

    @FXML
    public void createRequestPageClick(MouseEvent event) throws IOException {
        FXRouter.goTo("student-create-request");
    }

    @FXML
    public void optionDropdown(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Option 1");
        MenuItem item2 = new MenuItem("Option 2");
        MenuItem item3 = new MenuItem("Option 3");


        contextMenu.getItems().addAll(item1, item2, item3);

        if (!contextMenu.isShowing()) {
            Bounds optionBounds = optionDropdown.localToScreen(optionDropdown.getBoundsInLocal());
            contextMenu.show(optionDropdown, optionBounds.getMinX(), optionBounds.getMaxY());
        } else {
            contextMenu.hide();
        }

    }
}
