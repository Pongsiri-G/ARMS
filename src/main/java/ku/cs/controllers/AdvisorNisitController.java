package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvisorNisitController extends BaseController {
    @FXML BorderPane rootPane;
    @FXML private Circle profilePictureDisplay;
    @FXML private Circle studentProfilePictureDisplay;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label departmentLabel;
    @FXML private Label facultyLabel;
    @FXML private Label idLabel;
    @FXML private Label emailLabel;
    @FXML private Label departmentRequesterLabel;
    @FXML private Label emailRequesterLabel;
    @FXML private Label facultyRequesterLabel;
    @FXML private Label nameRequesterLabel;
    @FXML private Label phoneNumberRequesterLabel;
    @FXML private Label recentRequestLogLabel;
    @FXML private Label requestDetailsLabel;
    @FXML private TextArea requestLogTextArea;
    @FXML private Label studentIdRequesterLabel;
    @FXML private TableView<Request> studentTable;
    @FXML private Label timestampLabel;
    @FXML private Label typeRequestLabel;
    @FXML private VBox requestDetailPane;
    @FXML private Label rejectReasonLabel;
    @FXML private TableColumn<Request, String> type;
    @FXML private TableColumn<Request, String> status;
    @FXML private TableColumn<Request, String> time;

    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;
    private Student student;
    private Advisor advisor;

    public AdvisorNisitController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        ArrayList<String> data = (ArrayList<String>) FXRouter.getData();
        advisor = (Advisor) userList.findUserByUsername(data.get(0));
        student = advisor.getDepartment().findStudentByID(data.get(1));

        roleLabel.setText("อาจารย์ | ภาควิชา" + advisor.getDepartment().getDepartmentName());
        nameLabel.setText(advisor.getName());
        usernameLabel.setText(advisor.getUsername());
        applyThemeAndFont(rootPane);
        setProfilePicture(profilePictureDisplay, advisor.getProfilePicturePath());

        showTable(student.getRequestsByStudent(requestList));
        showStudent(student);
        requestDetailPane.setVisible(false);
        setupTableClickListener();
    }

    private void showTable(ArrayList<Request> requests) {
        studentTable.getItems().clear();
        type.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        time.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        status.setCellValueFactory(new PropertyValueFactory<>("recentStatusLog"));
        status.setCellFactory(column -> new TableCell<Request, String>() {
            @Override
            protected void updateItem(String statusLog, boolean empty) {
                super.updateItem(statusLog, empty);
                if (empty || statusLog == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(statusLog);

                    Request request = getTableView().getItems().get(getIndex());
                    String status = request.getStatus();

                    switch (status) {
                        case "กำลังดำเนินการ":
                            setStyle("-fx-text-fill: #d7a700; -fx-font-weight: bold;");
                            break;
                        case "ปฏิเสธ":
                            setStyle("-fx-text-fill: #be0000; -fx-font-weight: bold;");
                            break;
                        case "เสร็จสิ้น":
                            setStyle("-fx-text-fill: #149100; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        studentTable.getColumns().clear();
        studentTable.getColumns().add(type);
        studentTable.getColumns().add(status);
        studentTable.getColumns().add(time);


        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        studentTable.getItems().addAll(requests);
    }

    private void setupTableClickListener() {
        studentTable.setOnMouseClicked(event -> {
            Request selectedRequest = studentTable.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                showRequestDetails(selectedRequest);
            }
        });
    }

    private void showRequestDetails(Request request) {
        rejectReasonLabel.setVisible(false);
        typeRequestLabel.setText(request.getRequestType());
        nameRequesterLabel.setText("ชื่อ-สกุล " + request.getRequester().getName());
        facultyRequesterLabel.setText("คณะ " + request.getRequester().getEnrolledFaculty().getFacultyName());
        departmentRequesterLabel.setText("ภาควิชา " + request.getRequester().getEnrolledDepartment().getDepartmentName());
        studentIdRequesterLabel.setText("รหัสประจำตัวนิสิต " + request.getRequester().getStudentID());
        emailRequesterLabel.setText("อีเมล " + request.getRequester().getEmail());
        phoneNumberRequesterLabel.setText("เบอร์มือถือ " + request.getNumberPhone());
        recentRequestLogLabel.setText(request.getRecentStatusLog());

        timestampLabel.setText("วันที่สร้างคำร้อง: " + request.getLastModifiedDateTime());

        StringBuilder logs = new StringBuilder();
        List<String> statusLog = request.getStatusLog();
        for (int i = statusLog.size() - 1; i >= 0; i--) {
            logs.append(statusLog.get(i)).append("\n");
        }
        requestLogTextArea.setText(logs.toString());

        if (request.getStatus().equals("ปฏิเสธ")) {
            rejectReasonLabel.setText("บันทึกเหตุผล: " + request.getRejectionReason());
            rejectReasonLabel.setVisible(true);
        }

        requestDetailsLabel.setText(request.toString());
        requestDetailPane.setVisible(true);
    }
    private void showStudent(Student student) {
        studentNameLabel.setText("ชื่อ: " + student.getName());
        idLabel.setText("รหัสประจำตัวนิสิต: " + student.getStudentID());
        facultyLabel.setText("คณะ: " + student.getEnrolledFaculty().getFacultyName());
        departmentLabel.setText("สาขาวิชา: " + student.getEnrolledDepartment().getDepartmentName());
        emailLabel.setText("อีเมล: " + student.getEmail());
        setProfilePicture(studentProfilePictureDisplay, advisor.getProfilePicturePath());
    }

    @FXML
    void closeRequestDetailClick(MouseEvent event) {
        requestDetailPane.setVisible(false);
    }

    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("advisor-nisit");
        data.add(advisor.getUsername());
        FXRouter.goTo("settings", data);

    }

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }

    @FXML
    void goToRequestNisit(MouseEvent event) throws IOException {
        FXRouter.goTo("advisor-request-nisit", advisor.getUsername());
    }

    @FXML
    void backToAdvisor(MouseEvent event) throws IOException {
        FXRouter.goTo("advisor", advisor.getUsername());
    }
}



