package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.models.FacultyOfficer;
import ku.cs.models.Request;
import ku.cs.models.RequestHandlingOfficer;
import ku.cs.models.RequestList;
import ku.cs.services.FXRouter;
import javafx.scene.input.MouseEvent;
import ku.cs.services.RequestHandlingOfficersDataSource;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FacultyOfficerRequestController {
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



    private RequestListFileDatasource requestDatasource;
    private Request request;
    private RequestList requestList;
    private FacultyOfficer officer;
    private String selectedApprove;

    public void initialize() {
        errorLabel.setDisable(false);
        System.out.println("---------------------------------------");
        // Retrieve the passed data (List<Object>)
        List<Object> data = (List<Object>) FXRouter.getData();

        request = (Request) data.get(0);  // Get the Request object
        requestList = (RequestList) data.get(1);
        requestDatasource = (RequestListFileDatasource) data.get(2);
        officer = (FacultyOfficer) data.get(3);  // Get the Officer object;
        setupOfficerInfo();
        switchToDetailScence();
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
        //requestDetail.setText(request.getText());
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

    public void updateRequest(){
        requestDatasource.writeData(requestList);
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

    public boolean checkValid(String approver){;
        if (approver.equals("") || approver == null || approver.equals("เลือกผู้ดำเนินการ")) {
            errorLabel.setText("กรุณาเลือกผู้ดำเนินการ");
            return false;
        }
        return true;
    }

    @FXML
    public void onRejectRequestButtonClick(MouseEvent event) {
        selectedApprove = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprove)) {
            switchToRejectScence();
        }
    }
    @FXML
    public void onApproveRequestButtonClick(MouseEvent event) throws IOException {
        selectedApprove = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprove)) {
            officer.acceptRequest(request, selectedApprove);
            updateRequest();
            FXRouter.goTo("faculty-officer");
        }

    }
    @FXML
    public void onBackToAllRequestButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("faculty-officer", officer.getUsername());
    }

    @FXML
    public void onn(MouseEvent event) {
        System.out.println("8");
    }

    @FXML
    public void onBackToDetailButtonClick(MouseEvent event) {
        switchToDetailScence();
        selectOfficerHandlingMenu.setText("เลือกผู้ดำเนินการ");
    }

    @FXML
    public void onOkButtonClick(MouseEvent event) throws IOException {
        officer.rejectRequest(request,selectedApprove, reasonForRejectTextArea.getText());
        updateRequest();
        FXRouter.goTo("faculty-officer", officer.getUsername());
    }



}
