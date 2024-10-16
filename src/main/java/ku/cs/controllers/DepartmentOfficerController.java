package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
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

public class DepartmentOfficerController extends BaseController {
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
    @FXML
    Pane currentMenu3;

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

    // all nisit scene
    @FXML
    VBox studentListScene;
    @FXML
    TextField searchBarStudentTextField;
    @FXML
    TableView<Student> studentsTableView;
    @FXML
    TableColumn<Student, String> studentIDTableColumn;
    @FXML
    TableColumn<Student, String> studenttNameTableColumn;
    @FXML
    TableColumn<Student, String> studentEmailTableColumn;
    @FXML
    TableColumn<Student, String> studentAdvisorTableColumn;

    // manage nisit scene
    @FXML
    GridPane manageStudentScene;
    @FXML
    TextField studentNameTextField;
    @FXML
    TextField studentLastNameTextField;
    @FXML
    TextField studentIDTextField;
    @FXML
    TextField studentEmailTextField;
    @FXML
    Label studentFacultyLabel;
    @FXML
    Label studentDepartmentLabel;
    @FXML
    Label studentAdvisorLabel;
    @FXML
    MenuButton studentSelectAdvisorMenuBar;
    @FXML
    Label errorManageStudentLabel;


    // Handle Data
    DepartmentOfficer officer;
    RequestHandlingOfficersDataSource approverDatasource;
    UserListFileDatasource datasource;
    UserList userList;
    ArrayList<Request> requests;
    RequestListFileDatasource  requestDatasource;
    RequestList requestList;
    RequestHandlingOfficer approverToEdit;
    ArrayList<Advisor> advisors;
    ArrayList<Student> students;
    Student studentToEdit;

    private String selectedApprover;
    private Request selectedRequest;
    private File selectedFile;

    @FXML
    public void initialize() {
        initializeDataSources();
        loadData();
        loadRequests();
        loadApprovers();
        setupOfficerInfo();
        switchToRequestScene();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        usernameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่ | ภาควิชา" + officer.getDepartment().getDepartmentName());
        applyThemeAndFont(rootPane);
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
        officer = (DepartmentOfficer) userList.findUserByUsername((String) FXRouter.getData());
        requests = officer.getRequestsByDepartment(requestList);
        advisors = officer.getDepartment().getAdvisors();
        students = officer.getDepartment().getStudents();
        approverDatasource = new RequestHandlingOfficersDataSource("data/approver", officer.getDepartment().getDepartmentName() + "-approver.csv");
    }

    public void loadData() {
        userList = datasource.readData();
        officer = (DepartmentOfficer) userList.findUserByUsername(officer.getUsername());
        advisors = officer.getDepartment().getAdvisors();
        students = officer.getDepartment().getStudents();
    }

    public void loadRequests(){
        requestList = requestDatasource.readData();
        requests = officer.getRequestsByDepartment(requestList);
    }


    public void resetScene() {
        currentMenu1.setVisible(false);
        currentMenu2.setVisible(false);
        currentMenu3.setVisible(false);
        requestListScene.setVisible(false);
        approverScene.setVisible(false);
        approverScene.setManaged(false);
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(true);
        manageStudentScene.setVisible(false);
        studentsTableView.setVisible(false);
        studentListScene.setVisible(false);
        requestDetailPane.setVisible(false);

    }

    public void switchToRequestScene() {
        resetScene();
        currentMenu1.setVisible(true);
        requestListScene.setVisible(true);
        requestListScene.setManaged(true);
        updateRequestTableView();
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

    public void switchToStudentsScene(){
        resetScene();
        currentMenu3.setVisible(true);
        studentListScene.setVisible(true);
        studentsTableView.setVisible(true);
        //studentMainButtons.setDisable(false);
        updateStudentTableView();
    }

    public void switchToManageStudentScene(){
        manageStudentScene.setVisible(true);
        setAdvisorAvailable();
        errorManageStudentLabel.setText("");
        if (studentToEdit != null){
            studentIDTextField.setText(studentToEdit.getStudentID());
            studentNameTextField.setText(studentToEdit.getName().split(" ")[0]);
            studentLastNameTextField.setText(studentToEdit.getName().split(" ")[1]);
            studentEmailTextField.setText(studentToEdit.getEmail());
            studentFacultyLabel.setText("คณะ: "  + studentToEdit.getEnrolledFaculty().getFacultyName());
            studentDepartmentLabel.setText("ภาควิชา: " + studentToEdit.getEnrolledDepartment().getDepartmentName());
            if (studentToEdit.getStudentAdvisor() != null){
                studentSelectAdvisorMenuBar.setText(studentToEdit.getStudentAdvisor().getName());
                studentAdvisorLabel.setText("อาจารย์ที่ปรึกษา: " + studentToEdit.getStudentAdvisor().getName());
            } else {
                studentSelectAdvisorMenuBar.setText("เลือกอาจารย์ที่ปรึกษา");
                studentAdvisorLabel.setText("ยังไม่กำหนดอาจารย์ที่ปรึกษา");
            }
        } else{
            studentSelectAdvisorMenuBar.setText("เลือกอาจารย์ที่ปรึกษา");
            studentIDTextField.clear();
            studentNameTextField.clear();
            studentLastNameTextField.clear();
            studentEmailTextField.clear();
            studentFacultyLabel.setText("");
            studentDepartmentLabel.setText("");
            studentAdvisorLabel.setText("");

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

        requestListTableView.getColumns().clear();
        requestListTableView.getColumns().addAll(typeColumn, nameColumn, idColumn, lastModifiedColumn);

        requestListTableView.getItems().clear();
        requests.sort(Comparator.comparing(Request::getLastModifiedDateTime).reversed());
        requestListTableView.getItems().addAll(requests);

        requestListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedRequest = newValue;

                handleSelectedRequest(selectedRequest);
            }
        });
    }

    private void handleSelectedRequest(Request selectedRequest) {
        StringBuilder logs = new StringBuilder();
        List<String> statusLog = selectedRequest.getStatusLog();
        for (int i = statusLog.size() - 1; i >= 0; i--) {
            logs.append(statusLog.get(i)).append("\n");
        }
        requestLogTextArea.setText(logs.toString());
        timestampLabel.setText("วันที่สร้างคำร้อง: " + selectedRequest.getTimestamp());

        try {
            fillSelectApproverMenuButtons();
            setShowPDF(selectedRequest.getPdfFilePath(), requestDetailScrollPane);
            requestDetailPane.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeRequestDetailClick(){
        requestDetailPane.setVisible(false);
        rejectPopupPane.setVisible(false);
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
            //updateRequest();
            rejectPopupPane.setVisible(false);
            requestDetailPane.setVisible(false);
        }
    }

    @FXML
    public void onSendRequestButtonClick(MouseEvent event) throws IOException {
        selectedApprover = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprover, true)) {
            officer.acceptRequest(selectedRequest, selectedApprover);
            updateRequest();
            requestDetailPane.setVisible(false);
        }
    }

    @FXML
    public void onApproveRequestButtonClick(MouseEvent event) throws IOException {
        selectedApprover = selectOfficerHandlingMenu.getText();
        if (checkValid(selectedApprover, true)) {
            officer.acceptRequest(selectedRequest, selectedApprover);
            updateRequest();
            requestDetailPane.setVisible(false);
        }
    }

    public void updateRequest() throws IOException {
        // การอัพโหลด pdf
        fileLabel.setText(selectedFile.getName());
        String filePath = FileStorage.replaceFileWithTimestamp(selectedFile, selectedRequest.getPdfFilePath()); // เอาไฟล์ที่อัพโหลดไปใส่
        selectedRequest.setPdfFilePath(filePath); // เก็บที่อยู่ pdf ใน request

        // เขียนลง csv
        requestDatasource.writeData(requestList);
        loadRequests();
        updateRequestTableView();
    }

    public boolean checkValid(String approver, boolean isUseFile){
        if (approver.equals("") || approver == null || approver.equals("เลือกผู้ดำเนินการ")) {
            errorLabel.setText("กรุณาเลือกผู้ดำเนินการ");
            errorLabel.setVisible(true);
            return false;
        }
        else if (isUseFile && selectedFile == null){
            errorLabel.setText("กรุณาอัพโหลดไฟล์ PDF");
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
        approverNameTableColumn = new TableColumn<>("ชื่อ-นามสกุล");
        approverNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        approverNameTableColumn.setMinWidth(420);
        approverLastUpdateTableColumn = new TableColumn<>("วันที่แก้ไขล่าสุด");
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
        System.out.println(positions);

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

    public void updateStudentTableView() {
        // Column for student's ID
        studentIDTableColumn = new TableColumn<>("รหัสประจำตัวนิสิต");
        studentIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        studentIDTableColumn.setMinWidth(300);
        studentIDTableColumn.setMaxWidth(300);

        // Column for student's name
        studenttNameTableColumn = new TableColumn<>("ชื่อ-นามสกุล");
        studenttNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studenttNameTableColumn.setMinWidth(300);
        studenttNameTableColumn.setMaxWidth(300);

        // Column for student's email
        studentEmailTableColumn = new TableColumn<>("อีเมล");
        studentEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentEmailTableColumn.setMinWidth(300);
        studentEmailTableColumn.setMaxWidth(300);

        // Column for student's advisor (custom CellValueFactory to access Advisor's name)
        studentAdvisorTableColumn = new TableColumn<>("อาจารย์ที่ปรึกษา");
        studentAdvisorTableColumn.setCellValueFactory(cellData -> {
            Advisor advisor = cellData.getValue().getStudentAdvisor(); // Access the Advisor object
            return new SimpleStringProperty(advisor != null ? advisor.getName() : "-"); // Return advisor's name or empty string if null
        });
        studentAdvisorTableColumn.setMinWidth(300);
        studentAdvisorTableColumn.setMaxWidth(300);


        // Clear and set columns in the TableView
        studentsTableView.getColumns().clear();
        studentsTableView.getColumns().add(studentIDTableColumn);
        studentsTableView.getColumns().add(studenttNameTableColumn);
        studentsTableView.getColumns().add(studentEmailTableColumn);
        studentsTableView.getColumns().add(studentAdvisorTableColumn);

        // Create a FilteredList to allow filtering the students based on the search text
        FilteredList<Student> filteredData = new FilteredList<>(FXCollections.observableArrayList(students), p -> true);

        // Add a listener to the searchBarStudentTextField to filter the list
        searchBarStudentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                // If search field is empty, display all students
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Filter by student name, email, or advisor's name
                if (student.getStudentID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches student name
                } else if (student.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches student email
                } else if (student.getStudentAdvisor() != null && student.getStudentAdvisor().getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches advisor's name
                }
                return false; // No match
            });
        });

        // Use a SortedList to bind sorting to the TableView
        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentsTableView.comparatorProperty());

        // Set the sorted and filtered data to the TableView
        studentsTableView.setItems(sortedData);
    }

    public void setAdvisorAvailable() {
        studentSelectAdvisorMenuBar.getItems().clear();
        MenuItem noAdvisorItem = new MenuItem("ไม่ระบุ");

        // Event handling for "ไม่ระบุ"
        noAdvisorItem.setOnAction(e -> {
            // Set the text of the menu button to "ไม่ระบุ" when clicked
            studentSelectAdvisorMenuBar.setText("ไม่ระบุ");
        });
        studentSelectAdvisorMenuBar.getItems().add(noAdvisorItem);
        for (Advisor advisor : advisors) {
            MenuItem item = new MenuItem(advisor.getName());
            // Event handling when an item is clicked
            item.setOnAction(e -> {
                // Set the selected position
                String selectedPosition = item.getText();

                // Set the text of the roleSelectMenuButton to the selected position
                studentSelectAdvisorMenuBar.setText(selectedPosition);
            });

            // Add the item to the menu button
            studentSelectAdvisorMenuBar.getItems().add(item);
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
        } else if (nameTextField.getText().trim().isEmpty() || lastNameTextField.getText().trim().isEmpty()) {
            errorManageApproverLabel.setText("กรุณากรอกข้อมูลผู้อนุมัติ");
        } else {
            if (approverToEdit == null) {
                officer.addRequestManager(position, name);
            } else {
                officer.updateRequestManager(approverToEdit, position, name);
            }
            approverDatasource.writeData(officer.getRequestManagers());
            approverToEdit = null;
            switchToApproverScene();
        }
    }


    public void onGoToStudentListSceneButtonClick(MouseEvent mouseEvent) {switchToStudentsScene();}

    public void onSearchBarStudentTextFieldClick(MouseEvent mouseEvent) {switchToStudentsScene();}

    public void onRemoveStudentButtonClick(MouseEvent mouseEvent) {
        studentToEdit = studentsTableView.getSelectionModel().getSelectedItem();
        if (studentToEdit != null) {
            // Remove from the students
           // students.remove(studentToEdit);
            // Remove from userList
            officer.removeStudentInDep(studentToEdit);
            //officer.getDepartment().getStudents().remove(studentToEdit);
            datasource.writeData(userList);
            loadData();
            studentToEdit = null;
            switchToStudentsScene();
        }
    }


    public void onAddStudentButtonClick(MouseEvent mouseEvent) {
        studentToEdit = null;
        switchToManageStudentScene();
    }

    public void onEditStudentButtonClick(MouseEvent mouseEvent) {
        studentToEdit = studentsTableView.getSelectionModel().getSelectedItem();
        if (studentToEdit != null) {
            switchToManageStudentScene();
        }
    }

    public void onStudentBackButtonClick(MouseEvent mouseEvent) {
        studentToEdit = null;
        switchToStudentsScene();
    }

    public void onStudentOkButtonClick(MouseEvent mouseEvent) {
        String id = studentIDTextField.getText();
        String name = studentNameTextField.getText() + " " + studentLastNameTextField.getText();
        String email = studentEmailTextField.getText();
        String advisor = studentSelectAdvisorMenuBar.getText();

        // Validate ID: Check if it is null, blank, or not an integer
        if (id == null || id.trim().isEmpty()) {
            errorManageStudentLabel.setText("กรุณากรอกรหัสนิสิต");
            return;
        }

        // Validate Name: Check if it is null or blank
        if (name == null || name.trim().isEmpty()) {
            errorManageStudentLabel.setText("กรุณากรอกชื่อ-นามสกุลนิสิต");
            return;
        }


        // Validate Email: Check if it contains '@'
        if (email == null || !email.contains("@")) {
            errorManageStudentLabel.setText("กรุณาระบุอีเมลให้ถูกต้อง");
            return;
        }

        // If all validations pass, proceed with saving the data
        if (studentToEdit != null) {
            studentToEdit.setStudentID(id);
            studentToEdit.setName(name);
            studentToEdit.setEmail(email);
            if (advisor.equals("ไม่ระบุ")){
                studentToEdit.setStudentAdvisor(null);
            } else {
                officer.assignAdvisor(studentToEdit, advisor);
                //studentToEdit.setStudentAdvisor(officer.getDepartment().findAdvisorByName(advisor));
            }
        } else {
            //Student student;
            if (advisor == null || advisor.equals("เลือกอาจารย์ที่ปรึกษา") || advisor.equals("ไม่ระบุ")) {
                officer.addStudentToDep(name, id, email);
                //student = new Student(name, officer.getFaculty(), officer.getDepartment(), id, email);
            }
            else {
                officer.addStudentToDep(name, id, email, advisor);
                //student = new Student(name,officer.getFaculty().getFacultyName(), officer.getDepartment().getDepartmentName(), id, email, officer.getDepartment().findAdvisorByName(advisor));
            }
        }
        datasource.writeData(userList);
        loadData();
        studentToEdit = null;
        switchToStudentsScene();
    }

    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("department-officer");
        data.add(officer.getUsername());
        FXRouter.goTo("settings", data);

    }

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
