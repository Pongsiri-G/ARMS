package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class AdvisorRequestController {
    @FXML private TableView<Request> tableRequest;
    @FXML private AnchorPane ApprovePopupLabel;
    @FXML private Label requestTypeLabel;
    @FXML private Label requesterLabel;
    @FXML private Label timeLabel;
    @FXML private AnchorPane popupReject;
    @FXML private TextField reasonText;
    private Advisor advisor;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;
    private Request request;

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
        ApprovePopupLabel.setVisible(false);
        popupReject.setVisible(false);
        showTextRequest();
    }

    @FXML
    void acceptClick(ActionEvent event) {
        if (advisor != null) { advisor.acceptRequest(request); }
        userListDatasource.writeData(userList);
        requestListDatasource.writeData(requestList);
        showTable(advisor.getRequestsByAdvisor(requestList));
        ApprovePopupLabel.setVisible(false);
    }

    @FXML
    void rejectClick(ActionEvent event) {
        ApprovePopupLabel.setVisible(false);
        popupReject.setVisible(true);
    }

    @FXML
    void reasonRejectToStudent(ActionEvent event) {
        if (advisor != null) { advisor.rejectRequest(request, reasonText.getText()); }
        userListDatasource.writeData(userList);
        requestListDatasource.writeData(requestList);
        showTable(advisor.getRequestsByAdvisor(requestList));
        popupReject.setVisible(false);
    }

    @FXML
    void goToBackAdvisorClick(ActionEvent event) {
        ApprovePopupLabel.setVisible(false);
    }

    private void showTextRequest() {
        tableRequest.setOnMouseClicked(event -> {
            Request selectedRequest = tableRequest.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                request = selectedRequest;
                showRequestDetails(selectedRequest);
            }
        });
    }

    private void showRequestDetails(Request selectedRequest) {
        requestTypeLabel.setText(selectedRequest.getRequestType());
        requesterLabel.setText(selectedRequest.getRequester());
        timeLabel.setText(selectedRequest.getTimestamp());
        ApprovePopupLabel.setVisible(true);
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

        TableColumn<Request, String> status = new TableColumn<>("สถานะคำร้อง");
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

        TableColumn<Request, String> time = new TableColumn<>("วันที่");
        time.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        tableRequest.getColumns().clear();
        tableRequest.getColumns().add(name);
        tableRequest.getColumns().add(type);
        tableRequest.getColumns().add(status);
        tableRequest.getColumns().add(time);


        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        tableRequest.getItems().addAll(requests);


    }

}
