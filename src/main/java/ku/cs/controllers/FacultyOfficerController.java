package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.FacultyOfficer;
import ku.cs.models.Request;
import ku.cs.models.RequestHandlingOfficer;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyOfficerListFileDatasource;
import ku.cs.services.RequestHandlingOfficersDataSource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
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
    RequestHandlingOfficer approverToEdit;



    @FXML
    public void initialize() {
        initializeDataSources();
        setupOfficerInfo();
        switchToRequestScene();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        userNameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่คณะ" + officer.getFaculty().getFacultyName());
    }

    private void initializeDataSources() {
        datasource = new UserListFileDatasource("data/test",
                                            "studentlist.csv",
                                            "advisorlist.csv",
                                        "facultyofficerlist.csv",
                                    "departmentofficerlist.csv",
                                            "facdeplist.csv");
        userList = datasource.readData();
        System.out.println((String) FXRouter.getData());
        officer = (FacultyOfficer) userList.findUserByUsername((String) FXRouter.getData());
        approverDatasource = new RequestHandlingOfficersDataSource("data/approver", officer.getFaculty().getFacultyName() + "-approver.csv");
        loadApprovers();
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

    public void loadRequests(){
        requests = new ArrayList<>();
        Request req1 = new Request("2024-09-26 10:15", "John Doe", "Pending", "Leave Request", "Requesting 2 weeks of vacation", "REQ001", "555-1234");
        Request req2 = new Request("2024-09-25 09:45", "Jane Smith", "Pending", "Equipment Request", "Requesting new laptop for work", "REQ002", "555-5678");
        Request req3 = new Request("2024-09-24 11:30", "Michael Brown", "Pending", "Expense Reimbursement", "Reimbursement for travel expenses", "REQ003", "555-2345");
        Request req4 = new Request("2024-09-23 08:00", "Emily White", "Pending", "Training Request", "Requesting attendance at a conference", "REQ004", "555-8765");
        Request req5 = new Request("2024-09-22 14:20", "Chris Green", "Pending", "Leave Request", "Requesting personal leave for medical reasons", "REQ005", "555-3456");
        Request req6 = new Request("2024-09-21 16:45", "Anna Blue", "Pending", "Project Proposal", "Proposal for a new marketing campaign", "REQ006", "555-9876");
        Request req7 = new Request("2024-09-20 13:10", "David Black", "Pending", "Budget Increase", "Requesting an increase in project budget", "REQ007", "555-4567");
        Request req8 = new Request("2024-09-19 12:05", "Lisa Red", "Pending", "Salary Adjustment", "Request for salary review and adjustment", "REQ008", "555-5432");
        Request req9 = new Request("2024-09-18 17:00", "Robert Silver", "Pending", "Job Transfer", "Request for transfer to a new department", "REQ009", "555-6789");
        Request req10 = new Request("2024-09-17 15:30", "Sophia Gold", "Pending", "Remote Work Request", "Requesting remote work for 3 days a week", "REQ010", "555-7654");

        requests.add(req1);
        requests.add(req2);
        requests.add(req3);
        requests.add(req4);
        requests.add(req5);
        requests.add(req6);
        requests.add(req7);
        requests.add(req8);
        requests.add(req9);
        requests.add(req10);
    }

    public void loadApprovers(){
        officer.loadRequestManage(approverDatasource.readData());
    }

    public void updateRequestTableView() {
        loadRequests();

        // Set up the columns
        TableColumn<Request, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Request, String> approverColumn = new TableColumn<>("Approver By");
        approverColumn.setCellValueFactory(new PropertyValueFactory<>("approveName"));

        TableColumn<Request, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Request, String> textColumn = new TableColumn<>("Text");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        TableColumn<Request, String> timeStampColumn = new TableColumn<>("TimeStamp");
        timeStampColumn.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));

        // Clear previous columns and add the new ones
        requestListTableView.getColumns().clear();
        requestListTableView.getColumns().addAll(typeColumn, approverColumn, idColumn, textColumn, timeStampColumn);

        // Clear the items in the table
        requestListTableView.getItems().clear();

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
