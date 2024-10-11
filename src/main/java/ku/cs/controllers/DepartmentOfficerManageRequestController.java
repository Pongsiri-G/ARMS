package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import ku.cs.models.DepartmentOfficer;
import ku.cs.models.FacultyOfficer;
import ku.cs.models.Request;
import ku.cs.models.RequestHandlingOfficer;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentOfficerManageRequestController {
    // UI Components
    @FXML
    Label nameLabel;
    @FXML
    Label userNameLabel;
    @FXML
    Label roleLabel;
    @FXML
    Circle profilePicture;

    // detail scene
    @FXML
    VBox requestDetailScene;
    @FXML
    TextArea requestDetail;
    @FXML
    StackPane requestDetailButtons;
    @FXML
    MenuButton selectOfficerHandlingMenu;
    @FXML
    Label errorLabel;

    //หน้าปฏิเสธคำร้อง
    @FXML
    VBox rejectRequestScene;
    @FXML
    TextArea reasonForRejectTextArea;



    private Request request;
    private DepartmentOfficer officer;

    public void initialize() {
        errorLabel.setDisable(false);
        System.out.println("---------------------------------------");
        // Retrieve the passed data (List<Object>)
        List<Object> data = (List<Object>) FXRouter.getData();

        // Extract Request and officer from the list
        if (data != null && data.size() == 2) {
            request = (Request) data.get(0);  // Get the Request object
            officer = (DepartmentOfficer) data.get(1);  // Get the Officer object;
        }
        else {
            request = null;
            officer = null;
        }
        setupOfficerInfo();
        switchToDetailScence();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        userNameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่ภาควิชา" + officer.getFaculty().getFacultyName());
    }

    public void resetSecene(){
        requestDetailScene.setVisible(false);
        requestDetailButtons.setVisible(false);
        rejectRequestScene.setVisible(false);
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }

    public void switchToDetailScence(){
        resetSecene();
        requestDetailScene.setVisible(true);
        requestDetailButtons.setVisible(true);
        //requestDetail.setText(request.get());
        selectOfficerHandlingMenu.setVisible(true);
        selectOfficerHandlingMenu.setDisable(false);
        errorLabel.setVisible(true);
        fillSelectApproverMenuButtons();
    }

    public void switchToRejectScence(){
        resetSecene();
        rejectRequestScene.setVisible(true);
        rejectRequestScene.setDisable(false);
    }

    public void fillSelectApproverMenuButtons() {
        selectOfficerHandlingMenu.getItems().clear();
        ArrayList<RequestHandlingOfficer> approvers = officer.getRequestManagers();
        for (RequestHandlingOfficer approver : approvers) {
            MenuItem item = new MenuItem(approver.getFullPositoin());

            // Event handling when an item is clicked
            item.setOnAction(e -> {
                // Set the selected position
                String selectedPosition = item.getText();

                // Set the text of the roleSelectMenuButton to the selected position
                selectOfficerHandlingMenu.setText(selectedPosition);
            });

            // Add the item to the menu button
            selectOfficerHandlingMenu.getItems().add(item);
        }

    }

    public boolean checkValid(){
        String approver = selectOfficerHandlingMenu.getText();
        if (approver.equals("") || approver == null || approver.equals("เลือกผู้ดำเนินการ")) {
            errorLabel.setText("กรุณาเลือกผู้ดำเนินการ");
            return false;
        }
        return true;
    }

    @FXML
    public void onRejectRequestButtonClick(MouseEvent event) {
        if (checkValid()) {
            switchToRejectScence();
        }
    }

    @FXML
    public void onSendRequestButtonClick(MouseEvent event) throws IOException {
        if (checkValid()) {
            //officer.sendRequest(request, selectOfficerHandlingMenu.getText());
            FXRouter.goTo("department-officer");
        }
    }

    @FXML
    public void onApproveRequestButtonClick(MouseEvent event) throws IOException {
        if (checkValid()) {
            officer.acceptRequest(request, selectOfficerHandlingMenu.getText());
            FXRouter.goTo("department-officer");
        }
    }
    @FXML
    public void onBackToAllRequestButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("department-officer", officer.getUsername());
    }

    @FXML
    public void onBackToDetailButtonClick(MouseEvent event) {
        switchToDetailScence();
        selectOfficerHandlingMenu.setText("เลือกผู้ดำเนินการ");
    }

    @FXML
    public void onOkButtonClick(MouseEvent event) throws IOException {
        officer.rejectRequest(request, reasonForRejectTextArea.getText(), selectOfficerHandlingMenu.getText());
        FXRouter.goTo("department-officer", officer.getUsername());
    }





}
