package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestHandlingOfficersDataSource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentOfficerController {


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
    @FXML
    Rectangle currentBar3;

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

    // all nisit scene
    @FXML
    VBox studentListScene;
    @FXML
    StackPane studentMainButtons;
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
    VBox manageStudentScene;
    @FXML
    ImageView studentImage;
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
    RequestHandlingOfficer approverToEdit;
    ArrayList<Advisor> advisors;
    ArrayList<Student> students;
    Student studentToEdit;



    @FXML
    public void initialize() {
        initializeDataSources();
        loadData();
        loadApprovers();
        setupOfficerInfo();
        switchToRequestScene();
    }

    public void setupOfficerInfo() {
        nameLabel.setText(officer.getName());
        userNameLabel.setText(officer.getUsername());
        roleLabel.setText("เจ้าหน้าที่ภาควิชา" + officer.getDepartment().getDepartmentName());
    }

    private void initializeDataSources() {
        datasource = new UserListFileDatasource("data/test",
                "studentlist.csv",
                "advisorlist.csv",
                "facultyofficerlist.csv",
                "departmentofficerlist.csv",
                "facdeplist.csv");
        userList = datasource.readData();
        officer = (DepartmentOfficer) userList.findUserByUsername((String) FXRouter.getData());
        //officer = userList.test();
        advisors = officer.getDepartment().getAdvisors();
        students = officer.getDepartment().getStudents();
        approverDatasource = new RequestHandlingOfficersDataSource("data/approver", officer.getDepartment().getDepartmentName() + "-approver.csv");
    }

    public void loadData() {
        userList = datasource.readData();
        officer = (DepartmentOfficer) userList.findUserByUsername(officer.getUsername());
        //officer = userList.test();
        advisors = officer.getDepartment().getAdvisors();
        students = officer.getDepartment().getStudents();
    }


    public void resetScene() {
        currentBar1.setVisible(false);
        currentBar2.setVisible(false);
        currentBar3.setVisible(false);
        requestListScene.setVisible(false);
        approverScene.setVisible(false);
        approverScene.setManaged(false);
        manageApproverScene.setVisible(false);
        manageApproverScene.setManaged(true);
        manageStudentScene.setVisible(false);
        studentsTableView.setVisible(false);
        studentListScene.setVisible(false);

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

    public void switchToStudentsScene(){
        resetScene();
        currentBar3.setVisible(true);
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
            //studentImage.setImage(new Image(studentToEdit.getProfilePicturePath()));
            studentIDTextField.setText(studentToEdit.getStudentID());
            studentNameTextField.setText(studentToEdit.getName().split(" ")[0]);
            studentLastNameTextField.setText(studentToEdit.getName().split(" ")[1]);
            studentEmailTextField.setText(studentToEdit.getEmail());
            studentFacultyLabel.setText("คณะ: "  + studentToEdit.getEnrolledFaculty().getFacultyName());
            studentDepartmentLabel.setText("ภาควิชา: " + studentToEdit.getEnrolledDepartment().getDepartmentName());
            if (studentToEdit.getStudentAdvisor() != null){
                studentSelectAdvisorMenuBar.setText(studentToEdit.getStudentAdvisor().getName());
                studentAdvisorLabel.setText(studentToEdit.getStudentAdvisor().getName());
            } else {
                studentSelectAdvisorMenuBar.setText("เลือกอาจารย์ที่ปรึกษา");
                studentAdvisorLabel.setText("ยังไม่กำหนดอาจารย์ที่ปรึกษา");
            }
        } else{
            studentSelectAdvisorMenuBar.setText("เลือกอาจารย์ที่ปรึกษา");
            //studentImage.setImage(null);
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
            FXRouter.goTo("department-officer-manage-request", dataToPass);  // Pass the list with both objects
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
        studentIDTableColumn = new TableColumn<>("ID");
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

        // Clear previous data
        studentsTableView.getItems().clear();

        // Populate table with student data
        for (Student student : students) {
            studentsTableView.getItems().add(student);
        }
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


    public void onGoToStudentListSceneButtonClick(MouseEvent mouseEvent) {switchToStudentsScene();}

    public void onRemoveStudentButtonClick(MouseEvent mouseEvent) {
        studentToEdit = studentsTableView.getSelectionModel().getSelectedItem();
        if (studentToEdit != null) {
            // Remove from the students
           // students.remove(studentToEdit);
            // Remove from userList
            officer.getDepartment().getStudents().remove(studentToEdit);
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
                studentToEdit.setStudentAdvisor(officer.getDepartment().findAdvisorByName(advisor));
            }
        } else {
            Student student;
            if (advisor == null || advisor.equals("เลือกอาจารย์ที่ปรึกษา") || advisor.equals("ไม่ระบุ")) {
                student = new Student(name, officer.getFaculty(), officer.getDepartment(), id, email);
            }
            else {
                student = new Student(name,officer.getFaculty().getFacultyName(), officer.getDepartment().getDepartmentName(), id, email, officer.getDepartment().findAdvisorByName(advisor));
            }
            officer.getDepartment().getStudents().add(student);
        }
        datasource.writeData(userList);
        loadData();
        studentToEdit = null;
        switchToStudentsScene();
    }

    @FXML
    public void onLogoutButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
}
