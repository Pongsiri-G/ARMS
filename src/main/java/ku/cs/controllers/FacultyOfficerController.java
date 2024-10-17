package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FacultyOfficerController extends BaseController{

    // UI Components
    @FXML
    BorderPane rootPane;
    @FXML
    Label nameLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label roleLabel;
    @FXML
    Circle profilePictureDisplay;
    @FXML
    Pane currentMenu1;
    @FXML
    Pane currentMenu2;

    // Request Scene
    @FXML VBox requestListScene;
    @FXML TableView<Request> requestListTableView;
    @FXML VBox requestDetailPane;
    @FXML Label timestampLabel;
    @FXML TextArea requestLogTextArea;
    @FXML ScrollPane requestDetailScrollPane;
    @FXML MenuButton selectOfficerHandlingMenu;
    @FXML Label errorLabel;
    @FXML Label fileLabel;
    @FXML GridPane rejectPopupPane;
    @FXML Label rejectionErrorLabel;
    @FXML TextField reasonTextField;

    // Approver Scene UI
    @FXML
    VBox approverScene;
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
    GridPane manageApproverScene;
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
    private UserPreferencesListFileDatasource preferencesListFileDatasource;

    private String selectedApprover;
    private Request selectedRequest;
    private File selectedFile;

    @FXML
    public void initialize() {
        initializeDataSources();
        loadRequests();
        preferencesListFileDatasource = new UserPreferencesListFileDatasource("data/test", "preferences.csv", userList);
        this.preferencesListFileDatasource.readData();
        setupOfficerInfo();
        switchToRequestScene();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        usernameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่ | คณะ" + officer.getFaculty().getFacultyName());
        applyThemeAndFont(rootPane, officer.getPreferences().getTheme(), officer.getPreferences().getFontFamily(), officer.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, officer.getProfilePicturePath());
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
        currentMenu1.setVisible(false);
        currentMenu2.setVisible(false);
        requestListScene.setVisible(false);
        approverScene.setVisible(false);
        approverScene.setManaged(false);
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(true);
        rejectPopupPane.setVisible(false);
        requestDetailPane.setVisible(false);
    }

    public void switchToRequestScene() {
        resetScene();
        selectedRequest = null;
        currentMenu1.setVisible(true);
        requestListScene.setVisible(true);
        requestListScene.setManaged(true);
        requests = officer.getRequestsByFaculty(requestList);
        updateRequestTableView();
    }

    public void switchToManageRequestScene() {
        StringBuilder logs = new StringBuilder();
        List<String> statusLog = selectedRequest.getStatusLog();
        for (int i = statusLog.size() - 1; i >= 0; i--) {
            logs.append(statusLog.get(i)).append("\n");
        }
        requestLogTextArea.setText(logs.toString());
        timestampLabel.setText(("วันที่สร้างคำร้อง: " + selectedRequest.getTimestamp()));

        try {
            fillSelectApproverMenuButtons();
            setShowPDF(selectedRequest.getPdfFilePath(), requestDetailScrollPane);
            requestDetailPane.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void switchToApproverScene() {
        resetScene();
        currentMenu2.setVisible(true);
        approverScene.setVisible(true);
        approverScene.setManaged(true);
        updateApproverTableView();
    }


    public void switchToManageApproverScene() {
        manageApproverScene.setVisible(true);
        manageApproverScene.setManaged(true);
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
                selectedRequest = newValue;
                switchToManageRequestScene();
            }
        });
    }

    public void setShowPDF(String pdfFilePath, ScrollPane pdfScrollPane) throws IOException {
        PDDocument document = PDDocument.load(new File(pdfFilePath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-alignment: center;");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 150);

            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(1240);
            imageView.setPreserveRatio(true);

            vbox.getChildren().add(imageView);
        }

        pdfScrollPane.setContent(null);
        pdfScrollPane.setContent(vbox);

        document.close();
    }

    public void closeRequestDetailClick(){
        System.out.println("kuy");
        switchToRequestScene();
    }

    public void fillSelectApproverMenuButtons() {
        selectOfficerHandlingMenu.getItems().clear();
        ArrayList<RequestHandlingOfficer> approvers = officer.getRequestManagers();
        for (RequestHandlingOfficer approver : approvers) {
            System.out.println(approver.getFullPositoin());
            MenuItem item = new MenuItem(approver.getFullPositoin());

            item.setOnAction(e -> {
                String selectedPosition = item.getText();

                selectOfficerHandlingMenu.setText(selectedPosition);
            });

            selectOfficerHandlingMenu.getItems().add(item);
            selectOfficerHandlingMenu.setText("เลือกผู้ดำเนินการ");
        }
    }

    @FXML
    public void uploadButtonClick(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose PDF File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        selectedFile = fileChooser.showOpenDialog(requestDetailPane.getScene().getWindow());

        if (selectedFile != null) {
            selectedRequest.setPdfFilePath(selectedFile.getAbsolutePath());
            fileLabel.setText(selectedFile.getName());
            fileLabel.setVisible(true);

            setShowPDF(selectedFile.getAbsolutePath(), requestDetailScrollPane);
        }
    }

    @FXML
    public void downloadButtonClick(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");

        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);

        fileChooser.setInitialFileName(selectedRequest.getRequester().getStudentID() + selectedRequest.getRequestType() + ".pdf");

        File fileToSave = fileChooser.showSaveDialog(requestDetailPane.getScene().getWindow());

        if (fileToSave != null) {
            String filePath = fileToSave.getAbsolutePath();

            Path sourcePath = Path.of(selectedRequest.getPdfFilePath());
            Path destinationPath = Path.of(filePath);

            try {
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onRejectRequestButtonClick(MouseEvent event) {
        selectedApprover = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprover, false)) {
            rejectPopupPane.setVisible(true);

        }
    }

    @FXML void onReasonRejectToStudentClick(MouseEvent event) throws IOException {
        if (reasonTextField.getText().trim().isEmpty()){
            rejectionErrorLabel.setVisible(true);
        }
        else {
            officer.rejectRequest(selectedRequest, selectedApprover, reasonTextField.getText().trim());
            requestDatasource.writeData(requestList);
            switchToRequestScene();
        }
    }

    @FXML
    public void onApproveRequestButtonClick(MouseEvent event) throws IOException {
        selectedApprover = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprover, true)) {
            officer.acceptRequest(selectedRequest, selectedApprover);
            updateRequest();
            switchToRequestScene();
        }
    }

    public void updateRequest() throws IOException {
        // การอัพโหลด pdf
        fileLabel.setText(selectedFile.getName());
        String filePath = FileStorage.replaceFileWithTimestamp(selectedFile, selectedRequest.getPdfFilePath()); // เอาไฟล์ที่อัพโหลดไปใส่
        selectedRequest.setPdfFilePath(filePath); // เก็บที่อยู่ pdf ใน request

        // เขียนลง csv
        requestDatasource.writeData(requestList);
    }

    public boolean checkValid(String approver, boolean isUseFile){
        if (approver.equals("") || approver == null || approver.equals("เลือกผู้ดำเนินการ")) {
            errorLabel.setText("กรุณาเลือกผู้ดำเนินการ");
            errorLabel.setVisible(true);
            return false;
        }
        else if (isUseFile && selectedFile == null){
            errorLabel.setText("กรุณาแนบเอกสารไฟล์ PDF");
            errorLabel.setVisible(true);
            return false;
        }
        return true;
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
        String name = nameTextField.getText().trim() + " " + lastNameTextField.getText().trim();
        if (position == null || position.equals("") || position.equals("เลือกตำแหน่ง")) {
            errorManageApproverLabel.setText("กรุณาระบุตำแหน่งผู้อนุมัติ");
        }
        else if (nameTextField.getText().trim().isEmpty() || lastNameTextField.getText().trim().isEmpty()) {
            errorManageApproverLabel.setText("กรุณากรอกข้อมูลผู้อนุมัติ");
        }
        else {
            if (approverToEdit == null) {
                officer.addRequestManager(position, name);
            }
            else {
                officer.updateRequestManager(approverToEdit, position, name);
            }
            approverDatasource.writeData(officer.getRequestManagers());
            approverToEdit = null;
            switchToApproverScene();
        }
    }


    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("faculty-officer");
        data.add(officer.getUsername());
        FXRouter.goTo("settings", data);

    }

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
