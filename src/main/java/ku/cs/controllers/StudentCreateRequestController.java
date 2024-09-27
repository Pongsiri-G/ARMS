package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class StudentCreateRequestController {
    @FXML
    private VBox navigation;

    @FXML
    private VBox pane;

    @FXML
    private VBox createRequestPane;

    @FXML
    private ImageView optionDropdown;

    @FXML
    private ChoiceBox<String> typeRequestChoice;

    @FXML
    public void initialize() {
        setupChoiceBox();
    }

    private void setupChoiceBox() {
        typeRequestChoice.getItems().addAll("ลาป่วย หรือ ลากิจ", "ของดเรียนบางรายวิชาล่าช้า", "ลาออก");
        typeRequestChoice.setValue("ลาป่วย หรือ ลากิจ");

        typeRequestChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("ลาป่วย หรือ ลากิจ")) {
                showSickLeaveForm();
            }
        });

        showSickLeaveForm();
    }


    private void showSickLeaveForm() {
        createRequestPane.getChildren().clear();

        RadioButton sickLeaveRadio = createRadioButton("ลาป่วย", true);
        RadioButton personalLeaveRadio = createRadioButton("ลากิจ", false);

        HBox purposeBox = new HBox(30, sickLeaveRadio, personalLeaveRadio);

        HBox dateRangeBox = new HBox(30,
                createLabel("ตั้งแต่วันที่"), createDatePicker(),
                createLabel("ถึงวันที่"), createDatePicker()
        );

        // Reason for leave TextArea
        TextArea reasonTextArea = createTextArea(3, 300);

        // Absent subjects TextArea
        TextArea absentSubjectTextArea = createTextArea(3, 300);

        // Add all elements to the pane
        createRequestPane.getChildren().addAll(
                purposeBox,
                dateRangeBox,
                createLabel("เนื่องจาก"),
                reasonTextArea,
                createLabel("โดยมีรายวิชาที่ขอหยุดเรียน ดังนี้ (ระบุรหัสวิชา ชื่อวิชา อาจารย์ผู้สอน)"),
                absentSubjectTextArea
        );
    }

    private RadioButton createRadioButton(String text, boolean selected) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setSelected(selected);
        radioButton.getStyleClass().add("radio-button");
        radioButton.setFont(new Font("System", 18));
        return radioButton;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label");
        label.setFont(new Font("System", 18));
        return label;
    }

    private DatePicker createDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.getStyleClass().add("date-picker");
        return datePicker;
    }

    private TextArea createTextArea(int rowCount, int width) {
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(rowCount);
        textArea.setPrefWidth(width);
        textArea.getStyleClass().add("text-area-reason");
        textArea.setFont(new Font("System", 18));
        return textArea;
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

    @FXML
    public void requestListViewPageClick(MouseEvent event) throws IOException {
        FXRouter.goTo("student-request-list-view");
    }

}