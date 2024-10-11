package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FacultyOfficerController {

    // UI Components
    @FXML
    Label nameLabel;
    @FXML
    Label userNameLabel;
    @FXML
    Label roleLabel;
    @FXML
    Circle profilePicture;
    @FXML
    Rectangle currentBar1;
    @FXML
    Rectangle currentBar2;

    // Request Scene
    @FXML
    VBox requestListScene;
    @FXML
    TableView<Request> requestListTableView;

    // Approver Scene UI
    @FXML
    VBox approverScene;
    @FXML
    StackPane approverMainButtons;
    @FXML
    TextField searchBarApproverTextField;
    @FXML
    TableView<RequestHandlingOfficer> approverTableView;
    @FXML
    TableColumn<RequestHandlingOfficer, String> approverRoleTableColumn;
    @FXML
    TableColumn<RequestHandlingOfficer, String> approverNameTableColumn;
    @FXML
    TableColumn<RequestHandlingOfficer, String> approverLastUpdateTableColumn;

    // Manage Approver Scene UI
    @FXML
    VBox manageApproverScene;
    @FXML
    MenuButton roleSelectMenuButton;
    @FXML
    TextField nameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    Label errorManageApproverLabel;

    FacultyOfficer officer;
    RequestHandlingOfficersDataSource approverDatasource;
    UserListFileDatasource datasource;
    UserList userList;
    ArrayList<Request> requests;
    RequestListFileDatasource requestDatasource;
    RequestList requestList;
    RequestHandlingOfficer approverToEdit;



    @FXML
    public void initialize() {
        initializeDataSources();
        loadRequests();
        setupOfficerInfo();
        switchToRequestScene();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        userNameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่คณะ" + officer.getFaculty().getFacultyName());
        //profilePicture
        setProfilePicture(officer.getProfilePicturePath());
    }

    private void setProfilePicture(String profilePath) {
        try {
            // โหลดรูปจาก profilePath
            Image profileImage = new Image("file:" + profilePath);

            profilePicture.setFill(new ImagePattern(profileImage));

        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            profilePicture.setFill(Color.GRAY);
        }
    }

    private void initializeDataSources() {
        datasource = new UserListFileDatasource("data/test",
                                            "studentlist.csv",
                                            "advisorlist.csv",
                                        "facultyofficerlist.csv",
                                    "departmentofficerlist.csv",
                                            "facdeplist.csv");
        userList = datasource.readData();
        requestDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = requestDatasource.readData();
        officer = (FacultyOfficer) userList.findUserByUsername((String) FXRouter.getData());
        requests = officer.getRequestsByFaculty(requestList);
        approverDatasource = new RequestHandlingOfficersDataSource("data/approver", officer.getFaculty().getFacultyName() + "-approver.csv");
        loadApprovers();
    }


    public void loadRequests(){
        requestList = requestDatasource.readData();
        requests = officer.getRequestsByFaculty(requestList);
    }


    public void resetScene() {
        currentBar1.setVisible(false);
        currentBar2.setVisible(false);
        requestListScene.setVisible(false);
        approverScene.setVisible(false);
        approverScene.setManaged(false);
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(true);
    }

    public void switchToRequestScene() {
        resetScene();
        currentBar1.setVisible(true);
        requestListScene.setVisible(true);
        requestListScene.setManaged(true);
        updateRequestTableView();
    }

    public void switchToApproverScene() {
        resetScene();
        currentBar2.setVisible(true);
        approverScene.setVisible(true);
        approverScene.setManaged(true);
        approverMainButtons.setDisable(false);
        updateApproverTableView();
    }

    public void switchToManageApproverScene() {
        manageApproverScene.setVisible(true);
        manageApproverScene.setManaged(true);
        approverMainButtons.setDisable(true);
        setApproverPositionAvailable();
        errorManageApproverLabel.setText("");
        if (approverToEdit != null) {
            roleSelectMenuButton.setText(approverToEdit.getPosition());
            nameTextField.setText(approverToEdit.getName().split(" ")[0]);
            lastNameTextField.setText(approverToEdit.getName().split(" ")[1]);
        }
        else {
            roleSelectMenuButton.setText("เลือกตำแหน่ง");
            nameTextField.clear();
            lastNameTextField.clear();
        }
    }



    public void loadApprovers(){
        officer.loadRequestManage(approverDatasource.readData());
    }

    public void updateRequestTableView() {
        loadApprovers();

        // Set up the columns
        TableColumn<Request, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType")); // รับประเภทคำร้องจาก Request โดยตรง
        typeColumn.setMinWidth(290);

        TableColumn<Request, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(cellData -> {
            Student student = cellData.getValue().getRequester();
            return new SimpleStringProperty(student.getName()); // ดึงชื่อ-นามสกุลจากที่สิตที่สร้างคำร้อง
        });
        nameColumn.setMinWidth(290);

        TableColumn<Request, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(cellData -> {
            Student student = cellData.getValue().getRequester();
            return new SimpleStringProperty(student.getStudentID()); // ดึงรหสนิสิตจากที่สิตที่สร้างคำร้อง
        });
        idColumn.setMinWidth(290);

        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("recentStatusLog"));
        statusColumn.setMinWidth(290);
        statusColumn.setCellFactory(column -> new TableCell<Request, String>() {
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
        lastModifiedColumn.setMinWidth(290);



        // Clear previous columns and add the new ones
        requestListTableView.getColumns().clear();
        requestListTableView.getColumns().addAll(typeColumn, nameColumn, idColumn, lastModifiedColumn);

        // Clear the items in the table
        requestListTableView.getItems().clear();

        requests.sort(Comparator.comparing(Request::getLastModifiedDateTime).reversed()); // เรียง Request ตามวันเวลา

        // Populate the TableView with requests
        requestListTableView.getItems().addAll(requests);

        // Use the selection model for row selection
        requestListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Get the selected request directly
                Request selectedRequest = newValue;

                // Perform your actions with the selected request
                handleSelectedRequest(selectedRequest);
            }
        });
    }

    private void handleSelectedRequest(Request selectedRequest) {
        // Handle the selected request (e.g., navigate to the details scene)
        try {
            List<Object> dataToPass = new ArrayList<>();
            dataToPass.add(selectedRequest);  // Add the Request object
            dataToPass.add(requestList);// Add the Request object
            dataToPass.add(requestDatasource);
            dataToPass.add(officer);  // Add the RequestHandlingOfficer object
            FXRouter.goTo("faculty-officer-manage-request", dataToPass);  // Pass the list with both objects
        } catch (IOException e) {
            System.err.println("Route is null. Check if the route is registered properly.");
            e.printStackTrace();
        }
    }




    public void updateApproverTableView() {
        loadApprovers();
        approverRoleTableColumn = new TableColumn<>("ตำแหน่ง");
        approverRoleTableColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        approverRoleTableColumn.setMinWidth(360);
        approverNameTableColumn = new TableColumn<>("ขื่อ-นามสกุล");
        approverNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        approverNameTableColumn.setMinWidth(420);
        approverLastUpdateTableColumn = new TableColumn<>("วัน-เวลา แก้ไขล่าสุด");
        approverLastUpdateTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        approverLastUpdateTableColumn.setMinWidth(400);

        approverTableView.getColumns().clear();
        approverTableView.getColumns().add(approverRoleTableColumn);
        approverTableView.getColumns().add(approverNameTableColumn);
        approverTableView.getColumns().add(approverLastUpdateTableColumn);
        approverTableView.getItems().clear();

        for (RequestHandlingOfficer approver : officer.getRequestManagers()) {
            approverTableView.getItems().add(approver);
        }


    }

    public void setApproverPositionAvailable() {
        roleSelectMenuButton.getItems().clear(); // Clear existing items
        ArrayList<String> positions = officer.getAvailablePositions();

        for (String position : positions) {
            MenuItem item = new MenuItem(position);

            // Event handling when an item is clicked
            item.setOnAction(e -> {
                // Set the selected position
                String selectedPosition = item.getText();

                // Set the text of the roleSelectMenuButton to the selected position
                roleSelectMenuButton.setText(selectedPosition);
            });

            // Add the item to the menu button
            roleSelectMenuButton.getItems().add(item);
        }
    }


    @FXML
    public void onGoToRequestSceneButtonClick(MouseEvent mouseEvent) {
        switchToRequestScene();
    }

    @FXML
    public void onGoToApproverSceneButtonClick(MouseEvent mouseEvent) {
        switchToApproverScene();
    }

    @FXML
    public void onRemoveApproverButtonClick(MouseEvent mouseEvent) {
        RequestHandlingOfficer selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
        approverToEdit = selectedApprover;
        if (approverToEdit != null) {
            officer.removeRequestManager(approverToEdit);
            approverDatasource.writeData(officer.getRequestManagers());
            approverToEdit = null;
            switchToApproverScene();
        }
    }

    @FXML
    public void onEditApproverButtonClick(MouseEvent mouseEvent) {
        RequestHandlingOfficer selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
        approverToEdit = selectedApprover;
        if (approverToEdit != null) {
            switchToManageApproverScene();
        }
    }

    @FXML
    public void onAddApproverButtonClick(MouseEvent mouseEvent) {
        approverToEdit = null;
        switchToManageApproverScene();
    }

    @FXML
    public void onBackButtonClick(MouseEvent mouseEvent) {
        approverToEdit = null;
        switchToApproverScene();
    }

    @FXML
    public void onOkButtonClick(MouseEvent mouseEvent) {
        String position = roleSelectMenuButton.getText();
        String name = nameTextField.getText() + " " + lastNameTextField.getText();
        String test = name.replaceAll("\\s+", "");
        if (position == null || position.equals("") || position.equals("เลือกตำแหน่ง")) {
            errorManageApproverLabel.setText("กรุณาระบุตำแหน่งคนอนุมัติ");
        }
        else if (name == null || test.equals("")) {
            errorManageApproverLabel.setText("กรุณากรอกข้อมูลผู้อนุมัติ");
        }
        else {
            if (approverToEdit == null) {
                officer.addRequestManager(position, name);
                //approvers.add(new RequestHandlingOfficer(position, name));
            }
            else {
                officer.updateRequestManager(approverToEdit, position, name);
                //approverToEdit.update(position, name);
            }
            approverDatasource.writeData(officer.getRequestManagers());
            approverToEdit = null;
            switchToApproverScene();
        }


    }


    @FXML
    public void onLogoutButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
