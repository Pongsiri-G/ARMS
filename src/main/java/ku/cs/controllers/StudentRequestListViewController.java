package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.Request;
import ku.cs.models.RequestList;
import ku.cs.models.Student;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentRequestListViewController {


    @FXML
    private ImageView optionDropdown;

    @FXML private Label roleLabel;

    @FXML private Label nameLabel;

    @FXML private Label usernameLabel;

    @FXML private Label doneLabel;

    @FXML private Label waitLabel;

    @FXML private Label rejectLabel;

    @FXML private Rectangle currentBar1;

    @FXML private Rectangle currentBar2;

    @FXML private TableView<Request> requestListTableview;

    @FXML private Circle profilePictureDisplay;


    //Part of Request Detail Pane
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
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private ComboBox<String> typeFilterComboBox;
    private UserList userList;
    private RequestList requestList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private Student student;

    public StudentRequestListViewController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = userListDatasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        this.requestList = requestListDatasource.readData();
    }

    @FXML
    public void initialize() {
        student = (Student) userList.findUserByUsername((String) FXRouter.getData());
        roleLabel.setText("นิสิต | ภาควิชา" + student.getEnrolledDepartment().getDepartmentName());
        nameLabel.setText(student.getName());
        usernameLabel.setText(student.getUsername());
        currentBar1.setVisible(false);
        setProfilePicture(student.getProfilePicturePath());
        doneLabel.setText(String.format("%d", student.getStudentApprovedRequestCount(student.getRequestsByStudent(requestList))));
        waitLabel.setText(String.format("%d", student.getStudentPendingRequestCount(student.getRequestsByStudent(requestList))));
        rejectLabel.setText(String.format("%d", student.getStudentRejectedRequestCount(student.getRequestsByStudent(requestList))));
        initializeFilters();
        showTable(student.getRequestsByStudent(requestList));
        requestDetailPane.setVisible(false);
        setupTableClickListener();
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


    private void initializeFilters() {
        statusFilterComboBox.getItems().addAll("ทั้งหมด", "กำลังดำเนินการ", "ปฏิเสธ", "เสร็จสิ้น");
        statusFilterComboBox.setValue("ทั้งหมด");

        typeFilterComboBox.getItems().addAll("ทั้งหมด", "ลาป่วยหรือลากิจ", "ลาพักการศึกษา", "ลาออก");
        typeFilterComboBox.setValue("ทั้งหมด");

        statusFilterComboBox.setOnAction(event -> applyFilter());
        typeFilterComboBox.setOnAction(event -> applyFilter());
    }

    private void showTable(ArrayList<Request> requestList) {
        TableColumn<Request, String> requestTypeColumn = new TableColumn<>("ประเภทคำร้อง");
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<Request, String> requestStatusColumn = new TableColumn<>("สถานะคำร้อง");
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("recentStatusLog"));

        requestStatusColumn.setCellFactory(column -> new TableCell<Request, String>() {
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
                            setStyle("-fx-text-fill: #d7a700;"); // Yellow for "in progress"
                            break;
                        case "ปฏิเสธ":
                            setStyle("-fx-text-fill: #be0000;"); // Red for "rejected"
                            break;
                        case "เสร็จสิ้น":
                            setStyle("-fx-text-fill: #149100;"); // Green for "completed"
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });

        TableColumn<Request, LocalDateTime> lastModifiedColumn = new TableColumn<>("วันที่แก้ไขล่าสุด");
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModifiedDateTime"));

        requestList.sort(Comparator.comparing(Request::getLastModifiedDateTime).reversed());

        requestListTableview.getColumns().clear();
        requestListTableview.getColumns().addAll(requestTypeColumn, requestStatusColumn, lastModifiedColumn);

        FilteredList<Request> filteredData = new FilteredList<>(FXCollections.observableArrayList(requestList), p -> true);

        statusFilterComboBox.setOnAction(event -> filterTable(filteredData));
        typeFilterComboBox.setOnAction(event -> filterTable(filteredData));

        requestListTableview.setItems(filteredData);
    }

    private void filterTable(FilteredList<Request> filteredData) {
        String selectedStatus = statusFilterComboBox.getValue();
        String selectedType = typeFilterComboBox.getValue();

        filteredData.setPredicate(request -> {
            boolean statusMatches = selectedStatus.equals("ทั้งหมด") || request.getStatus().equals(selectedStatus);

            boolean typeMatches = selectedType.equals("ทั้งหมด") || request.getRequestType().equals(selectedType);

            return statusMatches && typeMatches;
        });
    }


    @FXML
    private void applyFilter() {
        FilteredList<Request> filteredData = (FilteredList<Request>) requestListTableview.getItems();

        String selectedStatus = statusFilterComboBox.getValue();
        String selectedType = typeFilterComboBox.getValue();

        filteredData.setPredicate(request -> {
            boolean statusMatches = selectedStatus.equals("ทั้งหมด") || request.getStatus().equals(selectedStatus);
            boolean typeMatches = selectedType.equals("ทั้งหมด") || request.getRequestType().equals(selectedType);
            return statusMatches && typeMatches;
        });
    }

    private void setupTableClickListener() {
        requestListTableview.setOnMouseClicked(event -> {
            Request selectedRequest = requestListTableview.getSelectionModel().getSelectedItem();
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



    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }

    @FXML
    public void createRequestPageClick(MouseEvent event) throws IOException {
        FXRouter.goTo("student-create-request", student.getUsername());
    }

    @FXML
    public void closeRequestDetailClick(MouseEvent event) {
        requestDetailPane.setVisible(false);
    }
}
