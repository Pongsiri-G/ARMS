package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    }

    private void showTable(ArrayList<Request> requests) {
        studentTable.getItems().clear();
        TableColumn<Request, String> type = new TableColumn<>("ประเภทคำร้อง");
        type.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<Request, String> time = new TableColumn<>("วันที่");
        time.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        studentTable.getColumns().clear();
        studentTable.getColumns().add(type);
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



