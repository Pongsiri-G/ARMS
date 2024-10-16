package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudentCreateRequestController extends BaseController {
    @FXML private BorderPane rootPane;
    @FXML private VBox createRequestPane;
    @FXML private Label roleLabel;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Circle profilePictureDisplay;
    @FXML private ChoiceBox<String> typeRequestChoice;
    @FXML private GridPane confirmationPane;
    @FXML private GridPane errorPane;
    @FXML private Label errorLabel;

    //For Request
    @FXML private TextField phoneTextField;
    @FXML private TextField reasonTextField;

    @FXML private RadioButton sickLeaveRadio;
    @FXML private RadioButton personalLeaveRadio;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private TextArea absentSubjectTextArea;

    @FXML private TextField currentSemesterTextField;
    @FXML private TextField currentAcademicYearTextField;
    @FXML private TextField fromSemesterTextField;
    @FXML private TextField fromAcademicYearTextField;
    @FXML private TextField toSemesterTextField;
    @FXML private TextField toAcademicYearTextField;
    @FXML private TextArea registeredCoursesTextArea;
    @FXML private TextField currentAddressTextField;

    private UserList userList;
    private RequestList requestList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private Student student;


    public StudentCreateRequestController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        setupChoiceBox();
        student = (Student) userList.findUserByUsername((String) FXRouter.getData());
        roleLabel.setText("นิสิต | ภาควิชา" + student.getEnrolledDepartment().getDepartmentName());
        nameLabel.setText(student.getName());
        usernameLabel.setText(student.getUsername());
        confirmationPane.setVisible(false);
        errorPane.setVisible(false);
        setProfilePicture(profilePictureDisplay, student.getProfilePicturePath());
        applyThemeAndFont(rootPane);
    }

    private void setupChoiceBox() {
        typeRequestChoice.getItems().addAll("ลาป่วยหรือลากิจ", "ลาพักการศึกษา", "ลาออก");
        typeRequestChoice.setValue("ลาป่วยหรือลากิจ");

        typeRequestChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "ลาป่วยหรือลากิจ":
                    showSickLeaveForm();
                    break;
                case "ลาพักการศึกษา":
                    showLeaveOfAbsenceForm();
                    break;
                case "ลาออก":
                    showResignationForm();
                    break;
            }
        });

        showSickLeaveForm();
    }


    private void showSickLeaveForm() {
        createRequestPane.getChildren().clear();
        phoneTextField = createTextField(150);
        HBox phoneBox = new HBox(30, createLabel("เบอร์โทรติดต่อ"), phoneTextField);
        ToggleGroup leaveTypeGroup = new ToggleGroup();

        // Create radio buttons and add them to the ToggleGroup
        sickLeaveRadio = createRadioButton("ลาป่วย", true);
        personalLeaveRadio = createRadioButton("ลากิจ", false);

        sickLeaveRadio.setToggleGroup(leaveTypeGroup);
        personalLeaveRadio.setToggleGroup(leaveTypeGroup);

        HBox purposeBox = new HBox(50, sickLeaveRadio, personalLeaveRadio);

        fromDatePicker = createDatePicker();
        toDatePicker = createDatePicker();
        HBox dateRangeBox = new HBox(30,
                createLabel("ตั้งแต่วันที่"), fromDatePicker,
                createLabel("ถึงวันที่"), toDatePicker
        );

        // Reason for leave TextArea
        reasonTextField = createTextField(500);

        // Absent subjects TextArea
        absentSubjectTextArea = createTextArea(3, 300);

        // Add all elements to the pane
        createRequestPane.getChildren().addAll(
                phoneBox,
                purposeBox,
                dateRangeBox,
                createLabel("เนื่องจาก"),
                reasonTextField,
                createLabel("โดยมีรายวิชาที่ขอหยุดเรียน ดังนี้ (ระบุรหัสวิชา ชื่อวิชา อาจารย์ผู้สอน)"),
                absentSubjectTextArea
        );
    }

    private void showLeaveOfAbsenceForm() {
        createRequestPane.getChildren().clear();

        phoneTextField = createTextField(150);
        HBox phoneBox = new HBox(30, createLabel("เบอร์โทรติดต่อ"), phoneTextField);

        currentSemesterTextField = createTextField(50);
        currentAcademicYearTextField = createTextField(100);
        HBox currentSemesterBox = new HBox(30,
                createLabel("ภาคการศึกษาปัจจุบัน"), currentSemesterTextField,
                createLabel("ปีการศึกษาปัจจุบัน"), currentAcademicYearTextField
        );

        fromSemesterTextField = createTextField(50);
        fromAcademicYearTextField = createTextField(100);
        toSemesterTextField = createTextField(50);
        toAcademicYearTextField = createTextField(100);

        HBox leaveDurationBox = new HBox(30,
                createLabel("ตั้งแต่ภาคการศึกษา"), fromSemesterTextField,
                createLabel("ปีการศึกษา"), fromAcademicYearTextField,
                createLabel("ถึงภาคการศึกษา"), toSemesterTextField,
                createLabel("ปีการศึกษา"), toAcademicYearTextField
        );

        reasonTextField = createTextField(500);

        registeredCoursesTextArea = createTextArea(3, 200);

        currentAddressTextField = createTextField(500);

        createRequestPane.getChildren().addAll(
                phoneBox,
                currentSemesterBox,
                leaveDurationBox,
                createLabel("เหตุผลที่ลา"),
                reasonTextField,
                createLabel("วิชาที่ลงทะเบียนเรียนไว้"),
                registeredCoursesTextArea,
                createLabel("ที่อยู่ปัจจุบัน"),
                currentAddressTextField
        );
    }

    private void showResignationForm() {
        createRequestPane.getChildren().clear();
        phoneTextField = createTextField(150);
        HBox phoneBox = new HBox(30, createLabel("เบอร์โทรติดต่อ"), phoneTextField);

        reasonTextField = createTextField(500);

        createRequestPane.getChildren().addAll(
                phoneBox,
                createLabel("เหตุผลที่ลาออก"),
                reasonTextField
        );
    }


    private RadioButton createRadioButton(String text, boolean selected) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setSelected(selected);
        radioButton.getStyleClass().add("radio-button");
        return radioButton;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label");
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
        return textArea;
    }

    private TextField createTextField(int width) {
        TextField textField = new TextField();
        textField.setPrefWidth(width);
        textField.getStyleClass().add("short-text-field");
        return textField;
    }

    @FXML void saveRequestClick(){
        confirmationPane.setVisible(true);
    }

    @FXML void cancelCreateRequestClick(){
        confirmationPane.setVisible(false);
    }

    @FXML
    void confirmCreateRequestClick() throws IOException {
        try {
            confirmationPane.setVisible(false);
            String selectedRequestType = typeRequestChoice.getValue();

            if (selectedRequestType.equals("ลาป่วยหรือลากิจ")) {
                // Handle Sick Leave or Personal Leave Request
                String phone = phoneTextField.getText();
                String typeLeave = sickLeaveRadio.isSelected() ? "ลาป่วย" : "ลากิจ";
                String reason = reasonTextField.getText();
                String fromDate = fromDatePicker.getValue() != null ? fromDatePicker.getValue().toString() : "";
                String toDate = toDatePicker.getValue() != null ? toDatePicker.getValue().toString() : "";
                String absentSubject = absentSubjectTextArea.getText();

                if (phone.isEmpty() || reason.isEmpty() || fromDate.isEmpty() || toDate.isEmpty() || absentSubject.isEmpty()) {
                    throw new NullPointerException("กรุณากรอกข้อมูลให้ครบถ้วน");
                }

                SickLeaveRequest sickLeaveRequest = new SickLeaveRequest(student, phone, typeLeave, reason, fromDate, toDate, absentSubject);
                student.createRequest(requestList, sickLeaveRequest);

            } else if (selectedRequestType.equals("ลาพักการศึกษา")) {
                String phone = phoneTextField.getText();
                String reason = reasonTextField.getText();
                String registeredCourses = registeredCoursesTextArea.getText();
                String currentAddress = currentAddressTextField.getText();

                if (phone.isEmpty() || reason.isEmpty() || registeredCourses.isEmpty() || currentAddress.isEmpty()) {
                    throw new NullPointerException("กรุณากรอกข้อมูลให้ครบถ้วน");
                }

                // Check if all semester and academic year fields are filled
                if (currentSemesterTextField.getText().isEmpty() || currentAcademicYearTextField.getText().isEmpty() ||
                        fromSemesterTextField.getText().isEmpty() || fromAcademicYearTextField.getText().isEmpty() ||
                        toSemesterTextField.getText().isEmpty() || toAcademicYearTextField.getText().isEmpty()) {
                    throw new NullPointerException("กรุณากรอกภาคการศึกษาและปีการศึกษาให้ครบถ้วน");
                }

                int currentSemester, currentAcademicYear, fromSemester, fromAcademicYear, toSemester, toAcademicYear;
                try {
                    currentSemester = Integer.parseInt(currentSemesterTextField.getText());
                    currentAcademicYear = Integer.parseInt(currentAcademicYearTextField.getText());
                    fromSemester = Integer.parseInt(fromSemesterTextField.getText());
                    fromAcademicYear = Integer.parseInt(fromAcademicYearTextField.getText());
                    toSemester = Integer.parseInt(toSemesterTextField.getText());
                    toAcademicYear = Integer.parseInt(toAcademicYearTextField.getText());
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("กรุณากรอกภาคการศึกษาและปีการศึกษาเป็นตัวเลข");
                }

                LeaveOfAbsenceRequest leaveOfAbsenceRequest = new LeaveOfAbsenceRequest(student, phone, reason, currentAddress, registeredCourses, currentSemester, currentAcademicYear, fromSemester, fromAcademicYear, toSemester, toAcademicYear);
                student.createRequest(requestList, leaveOfAbsenceRequest);

            } else if (selectedRequestType.equals("ลาออก")) {
                String phone = phoneTextField.getText();
                String reason = reasonTextField.getText();

                if (phone.isEmpty() || reason.isEmpty()) {
                    throw new NullPointerException("กรุณากรอกข้อมูลให้ครบถ้วน");
                }

                ResignationRequest resignationRequest = new ResignationRequest(student, phone, reason);
                student.createRequest(requestList, resignationRequest);
            }

            requestListDatasource.writeData(requestList);
            FXRouter.goTo("student-request-list-view", student.getUsername());

        } catch (NullPointerException | NumberFormatException e) {
            errorLabel.setText(e.getMessage());
            errorPane.setVisible(true);
        }
    }


    @FXML void closeErrorClick(){
        errorPane.setVisible(false);
    }

    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("student-create-request");
        data.add(student.getUsername());
        FXRouter.goTo("settings", data);

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