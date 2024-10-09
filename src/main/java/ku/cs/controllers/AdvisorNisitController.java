package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private TableView<Request> studentTable;

    private User user;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;
    private Student student;

    public AdvisorNisitController() {
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv");
        this.userList = userListDatasource.readData();
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        user = userList.findUserByUsername((String) FXRouter.getData());
        student = (Student) user;

        showTable(student.getRequestsByStudent(requestList));
        showStudent(student);
    }

    private void showTable(ArrayList<Request> requests) {
        studentTable.getItems().clear();
        TableColumn<Request, String> type = new TableColumn<>("ประเภทคำร้อง");
        type.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<Request, String> time = new TableColumn<>("วันที่");
        time.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        TableColumn<Request, String> status = new TableColumn<>("สถานะ");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
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

}



