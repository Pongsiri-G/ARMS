package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.Request;
import ku.cs.models.RequestHandlingOfficer;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class FacultyOfficerController {
    // ของทุกหน้า
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

    //หน้าคำร้อง
    @FXML
    VBox requestListScene;
    @FXML
    TableView<Request> requestListTableView;
    @FXML
    TableColumn<Request, String> requestTypeTableColumn;
    @FXML
    TableColumn<Request, String> requestStdDepartmentTableColumn;
    @FXML
    TableColumn<Request, String> requestStdNameTableColumn;
    @FXML
    TableColumn<Request, String> requestStdIDTableColumn;
    @FXML
    TableColumn<Request, String> requestDetailTableColumn;
    @FXML
    TableColumn<Request, String> requestApprovedDateTableColumn;


    //หน้าจัดการคนอนุมัติ
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


    //หน้าจัดการคนอนุมัต เพิ่ม/ลบ/แก้ไข
    @FXML
    VBox manageApproverScene;
    @FXML
    MenuButton roleSelectMenuButton;
    @FXML
    TextField nameTitleTextField;
    @FXML
    TextField nameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    Label errorManageApproverLabel;


    @FXML
    public void initialize() {
        start();
    }

    public void start() {
        // for test
        nameLabel.setText("name");
        userNameLabel.setText("Username");
        roleLabel.setText("role");
        //
        requestScene();
    }

    public void makeData(){

    }

    public void resetScene(){
        //side Bar
        currentBar1.setVisible(false);
        currentBar2.setVisible(false);

        //หน้าคำร้อง
        requestListScene.setVisible(false);
        requestListScene.setManaged(false);

        //หน้าจัดการคนอนุมัติ
        approverScene.setVisible(false);
        approverScene.setManaged(false);

        //หน้าจัดการคนอนุมัต เพิ่ม/ลบ/แก้ไข
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(false);

    }

    public void requestScene(){
        resetScene();
        currentBar1.setVisible(true);
        requestListScene.setVisible(true);
        requestListScene.setManaged(true);
    }

    public void approverScene(){
        resetScene();
        currentBar2.setVisible(true);
        approverScene.setVisible(true);
        approverScene.setManaged(true);
    }

    public void manageApproverScene(){
        manageApproverScene.setVisible(true);
        manageApproverScene.setManaged(true);

    }

    @FXML
    public void onGoToRequsetSceneButtonClick (MouseEvent mouseEvent) {
        requestScene();
    }
    @FXML
    public void onGoToApproverSceneButtonClick (MouseEvent mouseEvent) {
        approverScene();
    }
    @FXML
    public void onRemoveApproverButtonClick (MouseEvent mouseEvent) {
        ;;
    }
    @FXML
    public void onEditApproverButtonClick (MouseEvent mouseEvent) {
        manageApproverScene();
    }
    @FXML
    public void onAddApproverButtonClick (MouseEvent mouseEvent) {
        manageApproverScene();
    }
    @FXML
    public void onBackButtonClick (MouseEvent mouseEvent) {
        approverScene();
    }
    @FXML
    public void onOkButtonClick (MouseEvent mouseEvent) {
        approverScene();
    }



    @FXML
    public void onLogoutButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }



}
