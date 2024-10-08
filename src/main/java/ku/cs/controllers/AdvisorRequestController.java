package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class AdvisorRequestController {
    @FXML private TableView<Request> tableRequest;
    private Advisor advisor;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;

    public AdvisorRequestController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv");
        this.userList = userListDatasource.readData();
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        User user = userList.findUserByUsername((String) FXRouter.getData());
        advisor = (Advisor) user;
        showTable(advisor.getRequestsByAdvisor(requestList));
    }

    @FXML
    public void goToAdvisor(MouseEvent event) throws IOException {
        FXRouter.goTo("advisor");
    }

    private void showTable(ArrayList<Request> requests){
        tableRequest.getItems().clear();
        TableColumn<Request, String> name = new TableColumn<>("ชื่อ-นามสกุล");
        name.setCellValueFactory(new PropertyValueFactory<>("requester"));

        TableColumn<Request, String> type = new TableColumn<>("ประเภทคำร้อง");
        type.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<Request, String> time = new TableColumn<>("วันที่");
        time.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        tableRequest.getColumns().clear();
        tableRequest.getColumns().add(name);
        tableRequest.getColumns().add(type);
        tableRequest.getColumns().add(time);


        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        tableRequest.getItems().addAll(requests);


    }

}
