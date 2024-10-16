package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserPreferencesListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdvisorRequestController extends BaseController {
    @FXML private TableView<Request> tableRequest;
    @FXML private GridPane rejectPopupPane;
    @FXML private TextField reasonTextField;
    @FXML private TableColumn<Request, String> name;
    @FXML private TableColumn<Request, String> type;
    @FXML private TableColumn<Request, String> status;
    @FXML private TableColumn<Request, String> time;

    @FXML private Circle profilePictureDisplay;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private BorderPane rootPane;

    @FXML private VBox requestDetailPane;
    @FXML private Label typeRequestLabel;
    @FXML private Label nameRequesterLabel;
    @FXML private Label facultyRequesterLabel;
    @FXML private Label departmentRequesterLabel;
    @FXML private Label studentIdRequesterLabel;
    @FXML private Label emailRequesterLabel;
    @FXML private Label phoneNumberRequesterLabel;
    @FXML private TextArea requestLogTextArea;
    @FXML private Label recentRequestLogLabel;
    @FXML private Label rejectReasonLabel;
    @FXML private Label timestampLabel;
    @FXML private Label requestDetailsLabel;

    @FXML private Label errorLabel;
    private Advisor advisor;
    private UserList userList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private UserPreferencesListFileDatasource preferencesListFileDatasource;
    private RequestList requestList;
    private Request request;

    public AdvisorRequestController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
        preferencesListFileDatasource = new UserPreferencesListFileDatasource("data/test", "preferences.csv", userList);
        this.preferencesListFileDatasource.readData();
    }

    @FXML
    public void initialize() {
        User user = userList.findUserByUsername((String) FXRouter.getData());
        advisor = (Advisor) user;
        roleLabel.setText("อาจารย์ | ภาควิชา" + advisor.getDepartment().getDepartmentName());
        nameLabel.setText(advisor.getName());
        usernameLabel.setText(advisor.getUsername());
        applyThemeAndFont(rootPane, advisor.getPreferences().getTheme(), advisor.getPreferences().getFontFamily(), advisor.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, advisor.getProfilePicturePath());

        showTable(advisor.getRequestsByAdvisor(requestList));
        requestDetailPane.setVisible(false);
        rejectPopupPane.setVisible(false);
        errorLabel.setText("");
        showTextRequest();
    }

    @FXML
    void acceptClick(ActionEvent event) throws Exception {
        if (advisor != null) { advisor.acceptRequest(request); }
        userListDatasource.writeData(userList);
        request.createRequest();

        requestListDatasource.writeData(requestList);
        showTable(advisor.getRequestsByAdvisor(requestList));
        requestDetailPane.setVisible(false);
    }

    @FXML
    void rejectClick(ActionEvent event) {
        rejectPopupPane.setVisible(true);
    }

    @FXML
    void reasonRejectToStudent(ActionEvent event) {
        try {
            if (advisor != null) {
                advisor.rejectRequest(request, reasonTextField.getText());
                reasonTextField.clear();
                errorLabel.setText("");
                userListDatasource.writeData(userList);
                requestListDatasource.writeData(requestList);
                showTable(advisor.getRequestsByAdvisor(requestList));
                rejectPopupPane.setVisible(false);
                requestDetailPane.setVisible(false);
            }else {
                errorLabel.setText("โปรดระบุเหตุหล");
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    void goToBackAdvisorClick(ActionEvent event) {
        requestDetailPane.setVisible(false);
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

    @FXML
    public void closeRequestDetailClick(MouseEvent event) {
        requestDetailPane.setVisible(false);
    }

    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("advisor-request-nisit");
        data.add(advisor.getUsername());
        FXRouter.goTo("settings", data);

    }

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
