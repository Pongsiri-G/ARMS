package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.Student;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class StudentCreateRequestController {
    @FXML private VBox navigation;

    @FXML private VBox pane;

    @FXML private VBox createRequestPane;

    @FXML private Label roleLabel;

    @FXML private Label nameLabel;

    @FXML private Label usernameLabel;

    @FXML private Circle profilePictureDisplay;

    @FXML private ImageView optionDropdown;

    @FXML private Rectangle currentBar1;

    @FXML private Rectangle currentBar2;

    @FXML private ChoiceBox<String> typeRequestChoice;

    private UserList userList;
    private UserListFileDatasource datasource;
    private Student student;

    public StudentCreateRequestController(){
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
    }

    @FXML
    public void initialize() {
        setupChoiceBox();
        student = (Student) userList.findUserByUsername((String) FXRouter.getData());
        roleLabel.setText("นิสิต | ภาควิชา" + student.getEnrolledDepartment().getDepartmentName());
        nameLabel.setText(student.getName());
        usernameLabel.setText(student.getUsername());
        currentBar2.setVisible(false);
        setProfilePicture(student.getProfilePicturePath());
        System.out.println("[" + student.getName() + " " + student.getUsername() + "]");
    }

    private void setupChoiceBox() {
        typeRequestChoice.getItems().addAll("ลาป่วย หรือ ลากิจ", "ลาพักการศึกษา", "ลาออก");
        typeRequestChoice.setValue("ลาป่วย หรือ ลากิจ");

        typeRequestChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("ลาป่วย หรือ ลากิจ")) {
                showSickLeaveForm();
            } else if (newValue.equals("ลาพักการศึกษา")) {
                showLeaveOfAbsenceForm();
            } else if (newValue.equals("ลาออก")) {
                showResignationForm();
            }
        });

        showSickLeaveForm();
    }

    private void setProfilePicture(String profilePath) {
        try {
            // โหลดรูปจาก profilePath
            Image profileImage = new Image("file:" + profilePath);

            profilePictureDisplay.setFill(new ImagePattern(profileImage));

        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            profilePictureDisplay.setFill(Color.GRAY);
        }
    }


    private void showSickLeaveForm() {
        createRequestPane.getChildren().clear();

        RadioButton sickLeaveRadio = createRadioButton("ลาป่วย", true);
        RadioButton personalLeaveRadio = createRadioButton("ลากิจ", false);

        HBox purposeBox = new HBox(50, sickLeaveRadio, personalLeaveRadio);

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

    private void showLeaveOfAbsenceForm() {
        createRequestPane.getChildren().clear();

        // Current semester and academic year
        HBox currentSemesterBox = new HBox(30,
                createLabel("ภาคการศึกษาปัจจุบัน"), createTextField(50),
                createLabel("ปีการศึกษาปัจจุบัน"), createTextField(50)
        );

        // Leave duration (from semester/year to semester/year)
        HBox leaveDurationBox = new HBox(30,
                createLabel("ตั้งแต่ภาคการศึกษา"), createTextField(50),
                createLabel("ปีการศึกษา"), createTextField(50),
                createLabel("ถึงภาคการศึกษา"), createTextField(50),
                createLabel("ปีการศึกษา"), createTextField(50)
        );

        // Reason for leave
        TextArea reasonTextArea = createTextArea(3, 300);

        // Registered courses TextArea
        TextArea registeredCoursesTextArea = createTextArea(3, 300);

        // Current address
        TextArea currentAddressTextArea = createTextArea(2, 300);

        // Add all elements to the pane
        createRequestPane.getChildren().addAll(
                currentSemesterBox,
                leaveDurationBox,
                createLabel("เหตุผลที่ลา"),
                reasonTextArea,
                createLabel("วิชาที่ลงทะเบียนเรียนไว้"),
                registeredCoursesTextArea,
                createLabel("ที่อยู่ปัจจุบัน"),
                currentAddressTextArea
        );
    }

    private void showResignationForm() {
        createRequestPane.getChildren().clear();

        // Reason for resignation
        TextArea reasonTextArea = createTextArea(3, 300);

        // Add all elements to the pane
        createRequestPane.getChildren().addAll(
                createLabel("เหตุผลที่ลาออก"),
                reasonTextArea
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
        textArea.getStyleClass().add("text-area");
        textArea.setFont(new Font("System", 18));
        return textArea;
    }

    private TextField createTextField(int width) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.getStyleClass().add("short-text-field");
        textField.setFont(new Font("System", 18));
        return textField;
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
        FXRouter.goTo("student-request-list-view", student.getUsername());
    }

}