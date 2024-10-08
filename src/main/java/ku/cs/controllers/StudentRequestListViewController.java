package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
import java.util.ArrayList;

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

    private UserList userList;
    private RequestList requestList;
    private UserListFileDatasource userListDatasource;
    private RequestListFileDatasource requestListDatasource;
    private Student student;

    public StudentRequestListViewController(){
        userListDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv");
        this.userList = userListDatasource.readData();
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
        showTable(student.getRequestsByStudent(requestList));
        System.out.println("[" + student.getName() + " " + student.getUsername() + "]");
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

        TableColumn<Request, String> lastModifiedColumn = new TableColumn<>("วันที่แก้ไขล่าสุด");
        lastModifiedColumn.setCellValueFactory(new PropertyValueFactory<>("lastModifiedDateTime"));

        // Clear previous columns and add the new ones
        requestListTableview.getColumns().clear();
        requestListTableview.getColumns().add(requestTypeColumn);
        requestListTableview.getColumns().add(requestStatusColumn);
        requestListTableview.getColumns().add(lastModifiedColumn);

        // Add the request list to the TableView
        requestListTableview.getItems().clear();
        requestListTableview.getItems().addAll(requestList);
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
    public void optionDropdown(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Option 1");
        MenuItem item2 = new MenuItem("Option 2");
        MenuItem item3 = new MenuItem("Option 3");


        contextMenu.getItems().addAll(item1, item2, item3);

        if (!contextMenu.isShowing()) {
            Bounds optionBounds = optionDropdown.localToScreen(optionDropdown.getBoundsInLocal());
            contextMenu.show(optionDropdown, optionBounds.getMinX(), optionBounds.getMaxY());
        } else {
            contextMenu.hide();
        }

    }
}
