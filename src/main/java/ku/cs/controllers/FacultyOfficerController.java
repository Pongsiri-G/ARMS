package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import ku.cs.services.FXRouter;
import ku.cs.services.FacultyOfficerDatasource;
import ku.cs.services.RequestHandlingOfficersDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class FacultyOfficerController {
    FacultyOfficerDatasource facOfficerDatasource;
    FacultyOfficer officer;
    RequestHandlingOfficersDataSource approverDatasource;
    ArrayList<RequestHandlingOfficer> approvers;

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

    RequestHandlingOfficer approverToEdit;



    @FXML
    public void initialize() {
        facOfficerDatasource = new FacultyOfficerDatasource("data/test", "faculty-officer.csv");
        officer = facOfficerDatasource.readData().getFirst();
        approverDatasource = new RequestHandlingOfficersDataSource("data/approver", officer.getFaculty().getFacultyName() + "-approver.csv");
        start();
    }

    public void start() {
        nameLabel.setText(officer.getName());
        userNameLabel.setText(officer.getUsername());
        roleLabel.setText(officer.getRole());
        requestScene();
    }

    public void loadApprovers(){
        approvers = approverDatasource.readData();
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

        for (RequestHandlingOfficer approver : approvers) {
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



    public void resetScene() {
        currentBar1.setVisible(false);
        currentBar2.setVisible(false);
        requestListScene.setVisible(false);
        approverScene.setVisible(false);
        approverScene.setManaged(false);
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(true);
    }

    public void requestScene() {
        resetScene();
        currentBar1.setVisible(true);
        requestListScene.setVisible(true);
        requestListScene.setManaged(true);
    }

    public void approverScene() {
        resetScene();
        currentBar2.setVisible(true);
        approverScene.setVisible(true);
        approverScene.setManaged(true);
        approverMainButtons.setDisable(false);
        updateApproverTableView();
    }

    public void manageApproverScene() {
        manageApproverScene.setVisible(true);
        manageApproverScene.setManaged(true);
        approverMainButtons.setDisable(true);
        setApproverPositionAvailable();
        if (approverToEdit != null) {
            System.out.println(approverToEdit.getPosition()  + approverToEdit.getName() + approverToEdit.getLastUpdate());
            roleSelectMenuButton.setText(approverToEdit.getPosition());
            nameTextField.setText(approverToEdit.getName().split(" ")[0]);
            lastNameTextField.setText(approverToEdit.getName().split(" ")[1]);
        }
        else {
            nameTextField.clear();
            lastNameTextField.clear();
        }
    }

    @FXML
    public void onGoToRequsetSceneButtonClick(MouseEvent mouseEvent) {
        requestScene();
    }

    @FXML
    public void onGoToApproverSceneButtonClick(MouseEvent mouseEvent) {
        approverScene();
    }

    @FXML
    public void onRemoveApproverButtonClick(MouseEvent mouseEvent) {
        // Logic to remove an approver
    }

    @FXML
    public void onEditApproverButtonClick(MouseEvent mouseEvent) {
        RequestHandlingOfficer selectedApprover = approverTableView.getSelectionModel().getSelectedItem();
        approverToEdit = selectedApprover;
        manageApproverScene();
    }

    @FXML
    public void onAddApproverButtonClick(MouseEvent mouseEvent) {
        approverToEdit = null;
        manageApproverScene();
    }

    @FXML
    public void onBackButtonClick(MouseEvent mouseEvent) {
        approverScene();
    }

    @FXML
    public void onOkButtonClick(MouseEvent mouseEvent) {
        String position = roleSelectMenuButton.getText();
        String name = nameTextField.getText() + " " + lastNameTextField.getText();
        String test = name.replaceAll("\\s+", "");
        if (position == null || position.equals("")) {
            errorManageApproverLabel.setText("กรุณาระบุตำแหน่งคนอนุมัติ");
        }
        else if (name == null || test.equals("")) {
            errorManageApproverLabel.setText("กรุณากรอกข้อมูลผู้อนุมัติ");
        }
        else {
            if (approverToEdit == null) {
                approvers.add(new RequestHandlingOfficer(position, name));
            }
            else {
                approverToEdit.update(position, name);
            }
            approverDatasource.writeData(approvers);
            approverScene();
        }


    }


    @FXML
    public void onLogoutButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
