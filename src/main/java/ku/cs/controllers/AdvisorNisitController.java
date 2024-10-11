package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.ArrayList;

public class AdvisorNisitController {

    @FXML private Label departmentLabel;
    @FXML private Label facultyLabel;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
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
    @FXML private AnchorPane requestDetail;
    @FXML private TableColumn<Request, String> type;
    @FXML private TableColumn<Request, String> status;
    @FXML private TableColumn<Request, String> time;

    private User user;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;
    private Student student;

    public AdvisorNisitController() {
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        user = userList.findUserByUsername((String) FXRouter.getData());
        student = (Student) user;

        showTable(student.getRequestsByStudent(requestList));
        showStudent(student);
        requestDetail.setVisible(false);
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
                            setStyle("-fx-text-fill: #d7a700;");
                            break;
                        case "ปฏิเสธ":
                            setStyle("-fx-text-fill: #be0000;");
                            break;
                        case "เสร็จสิ้น":
                            setStyle("-fx-text-fill: #149100;");
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
        // Fill the detail pane with the request's data
        nameRequesterLabel.setText("ชื่อ-สกุล " + request.getRequester().getName());
        facultyRequesterLabel.setText("คณะ " + request.getRequester().getEnrolledFaculty().getFacultyName());
        departmentRequesterLabel.setText("ภาควิชา " + request.getRequester().getEnrolledDepartment().getDepartmentName());
        studentIdRequesterLabel.setText("รหัสประจำตัวนิสิต " + request.getRequester().getStudentID());
        emailRequesterLabel.setText("อีเมล " + request.getRequester().getEmail());
        phoneNumberRequesterLabel.setText("เบอร์มือถือ " + request.getNumberPhone());
        recentRequestLogLabel.setText(request.getRecentStatusLog());
        timestampLabel.setText("วันที่สร้างคำร้อง: " + request.getLastModifiedDateTime());

        StringBuilder logs = new StringBuilder();
        for (String log : request.getStatusLog()) {
            logs.append(log).append("\n");
        }
        requestLogTextArea.setText(logs.toString());

        requestDetailsLabel.setText(request.toString());
        requestDetail.setVisible(true);
    }
    private void showStudent(Student student) {
        nameLabel.setText(student.getName());
        idLabel.setText(student.getStudentID());
        facultyLabel.setText(student.getEnrolledFaculty().getFacultyName());
        departmentLabel.setText(student.getEnrolledDepartment().getDepartmentName());
        emailLabel.setText(student.getEmail());
    }


    @FXML
    void onButtonTBackAdvisor(ActionEvent event) {
        try {
            FXRouter.goTo("advisor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void closeRequestDetailClick(MouseEvent event) {
        requestDetail.setVisible(false);
    }

}



