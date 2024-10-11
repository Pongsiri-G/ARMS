package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;


public class AdvisorRequestController {
    @FXML private TableView<Request> tableRequest;
    @FXML private AnchorPane rejectPopupLabel;
    @FXML private AnchorPane popupApprove;
    @FXML private TextField reasonText;
    @FXML private TableColumn<Request, String> name;
    @FXML private TableColumn<Request, String> type;
    @FXML private TableColumn<Request, String> status;
    @FXML private TableColumn<Request, String> time;
    @FXML private Label errorLabel;
    private Advisor advisor;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private RequestList requestList;
    private Request request;

    public AdvisorRequestController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        User user = userList.findUserByUsername((String) FXRouter.getData());
        advisor = (Advisor) user;
        showTable(advisor.getRequestsByAdvisor(requestList));
        popupApprove.setVisible(false);
        rejectPopupLabel.setVisible(false);
        errorLabel.setText("");
        showTextRequest();
    }

    @FXML
    void acceptClick(ActionEvent event) {
        if (advisor != null) { advisor.acceptRequest(request); }
        userListDatasource.writeData(userList);
        requestListDatasource.writeData(requestList);
        showTable(advisor.getRequestsByAdvisor(requestList));
        popupApprove.setVisible(false);
    }

    @FXML
    void rejectClick(ActionEvent event) {
        popupApprove.setVisible(false);
        rejectPopupLabel.setVisible(true);
    }

    @FXML
    void reasonRejectToStudent(ActionEvent event) {
        try {
            if (advisor != null) {
                advisor.rejectRequest(request, reasonText.getText());
                reasonText.clear();
                errorLabel.setText("");
                userListDatasource.writeData(userList);
                requestListDatasource.writeData(requestList);
                showTable(advisor.getRequestsByAdvisor(requestList));
                rejectPopupLabel.setVisible(false);
            }else {
                errorLabel.setText("โปรดระบุเหตุหล");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void goToBackAdvisorClick(ActionEvent event) {
        popupApprove.setVisible(false);
    }

    private void showTextRequest() {
        tableRequest.setOnMouseClicked(event -> {
            Request selectedRequest = tableRequest.getSelectionModel().getSelectedItem();
            if (selectedRequest != null) {
                request = selectedRequest;
                showRequestDetails();
            }
        });
    }

    private void showRequestDetails() {
        popupApprove.setVisible(true);
    }

    @FXML
    public void goToAdvisor(MouseEvent event) throws IOException {
        FXRouter.goTo("advisor");
    }

    private void showTable(ArrayList<Request> requests){
        tableRequest.getItems().clear();
        name.setCellValueFactory(request ->
                new SimpleStringProperty(request.getValue().getRequester().getName())//ใช้ SimpleStringProperty ในการดึง method ที่่ return เป็น string
        );
        type.setCellValueFactory(new PropertyValueFactory<>("requestType"));
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
