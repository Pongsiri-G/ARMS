package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentController {
    @FXML
    private VBox navigation;

    @FXML
    private VBox pane;

    @FXML
    private ImageView optionDropdown;

    @FXML
    public void initialize() {
    }

    @FXML
    public void createRequestMenu(MouseEvent event) {
        pane.getChildren().clear();

        Label title = new Label("เพิ่มคำร้องใหม่");
        title.getStyleClass().add("title-label");
        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        Label typeRequestLabel = new Label("ประเภทคำร้อง");
        typeRequestLabel.setFont(new Font("System", 18));

        ChoiceBox<String> typeRequestChoice = new ChoiceBox<>();
        typeRequestChoice.getItems().addAll("ลาป่วย หรือ ลากิจ", "ของดเรียนบางรายวิชาล่าช้า", "ลาออก");
        typeRequestChoice.setValue("ลาป่วย หรือ ลากิจ");
        typeRequestChoice.getStyleClass().add("choice-box");

        ImageView defaultProfile = new ImageView(new Image(getClass().getResourceAsStream("/images/profile.jpg")));
        defaultProfile.setFitWidth(500);
        defaultProfile.setFitHeight(500);

        pane.getChildren().addAll(title, separator, typeRequestLabel , typeRequestChoice, defaultProfile);
    }

    @FXML
    public void listRequestsMenu(MouseEvent event) {
        pane.getChildren().clear();
        Label title = new Label("ติดตามคำร้อง");
        title.getStyleClass().add("title-label");
        TableView<Object> tableView = new TableView<>();

        pane.getChildren().addAll(title, tableView);
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

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }

}
