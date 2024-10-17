package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.models.User;
import ku.cs.services.AdvOffListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.FacDepListFileDatascource;

import java.io.IOException;
import java.util.ArrayList;

public class OfficerAndAdvisorManagementController {
    @FXML private ChoiceBox<String> roleChoiceBox;
    private String[] role = {"อาจารย์ที่ปรึกษา", "เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา"};
    @FXML private StackPane addStackPane;
    @FXML private StackPane editStackPane;
    @FXML private StackPane advisorStackPane;
    @FXML private StackPane facultyOfficerStackPane;
    @FXML private StackPane departmentOfficerStackPane;
    @FXML private Label editErrorMessageLabel;
    @FXML private Label advisorErrorMessageLabel;
    @FXML private Label facultyOfficerErrorMessageLabel;
    @FXML private Label departmentOfficerErrorMessageLabel;
    @FXML private Label editIdLabel;
    @FXML private Label editDepartmentLabel;
    @FXML private Label editRoleLabel;
    @FXML private TextField advisorNameTextField;
    @FXML private TextField advisorUsernameTextField;
    @FXML private TextField advisorPasswordTextField;
    @FXML private TextField advisorFacultyTextField;
    @FXML private TextField advisorDepartmentTextField;
    @FXML private TextField advisorIdTextField;
    @FXML private TextField facultyOfficerNameTextField;
    @FXML private TextField facultyOfficerUsernameTextField;
    @FXML private TextField facultyOfficerPasswordTextField;
    @FXML private TextField facultyOfficerFacultyTextField;
    @FXML private TextField departmentOfficerNameTextField;
    @FXML private TextField departmentOfficerUsernameTextField;
    @FXML private TextField departmentOfficerPasswordTextField;
    @FXML private TextField departmentOfficerFacultyTextField;
    @FXML private TextField departmentOfficerDepartmentTextField;
    @FXML private TextField editNameTextField;
    @FXML private TextField editUsernameTextField;
    @FXML private TextField editPasswordTextField;
    @FXML private TextField editFacultyTextField;
    @FXML private TextField editDepartmentTextField;
    @FXML private TextField editIdTextField;
    @FXML private TableView<User> officerAdvisorTableView;
    private FacultyList facultyList;
    private UserList userList;
    private Datasource<UserList> datasource;
    private Datasource<FacultyList> facultyDatasource;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll(role);
        roleChoiceBox.setValue("อาจารย์ที่ปรึกษา");
        advisorStackPane.setVisible(true);
        roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectRole(newValue);
        });
        advisorErrorMessageLabel.setText("");
        facultyOfficerErrorMessageLabel.setText("");
        departmentOfficerErrorMessageLabel.setText("");
        editErrorMessageLabel.setText("");
        addStackPane.setVisible(false);
        facultyOfficerStackPane.setVisible(false);
        departmentOfficerStackPane.setVisible(false);
        editStackPane.setVisible(false);
        datasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
        facultyDatasource = new FacDepListFileDatascource("data/csv_files", "facdeplist.csv");
        facultyList = facultyDatasource.readData();
        showTable(userList);

        officerAdvisorTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                User user = officerAdvisorTableView.getSelectionModel().getSelectedItem();
                if (user != null) {
                    showEditPane(user);
                }
            }
        });
    }

    private void showEditPane(User user) {
        editStackPane.setVisible(true);
        if (user instanceof Advisor) {
            Advisor advisor = (Advisor) user;
            editRoleLabel.setText("แก้ไขอาจารย์ที่ปรึกษา");
            editNameTextField.setText(advisor.getName());
            editUsernameTextField.setText(advisor.getUsername());
            editPasswordTextField.setText(advisor.getDefaultPassword());
            editDepartmentTextField.setVisible(true);
            editIdTextField.setVisible(true);
            editDepartmentLabel.setVisible(true);
            editIdLabel.setVisible(true);
            editFacultyTextField.setText(advisor.getFaculty().getFacultyName());
            editDepartmentTextField.setText(advisor.getDepartment().getDepartmentName());
            editIdTextField.setText(advisor.getAdvisorID());
        } else if (user instanceof  FacultyOfficer) {
            FacultyOfficer facultyOfficer = (FacultyOfficer) user;
            editRoleLabel.setText("แก้ไขเจ้าหน้าที่คณะ");
            editNameTextField.setText(facultyOfficer.getName());
            editUsernameTextField.setText(facultyOfficer.getUsername());
            editPasswordTextField.setText(facultyOfficer.getDefaultPassword());
            editFacultyTextField.setText(facultyOfficer.getFaculty().getFacultyName());
            editDepartmentTextField.setVisible(false);
            editIdTextField.setVisible(false);
            editDepartmentLabel.setVisible(false);
            editIdLabel.setVisible(false);
        } else if (user instanceof  DepartmentOfficer) {
            DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
            editRoleLabel.setText("แก้ไขเจ้าหน้าที่ภาควิชา");
            editNameTextField.setText(departmentOfficer.getName());
            editUsernameTextField.setText(departmentOfficer.getUsername());
            editPasswordTextField.setText(departmentOfficer.getDefaultPassword());
            editFacultyTextField.setText(departmentOfficer.getFaculty().getFacultyName());
            editDepartmentTextField.setText(departmentOfficer.getDepartment().getDepartmentName());
            editDepartmentTextField.setVisible(true);
            editIdTextField.setVisible(false);
            editDepartmentLabel.setVisible(true);
            editIdLabel.setVisible(false);
        }
    }

    @FXML
    private void showTable(UserList userList) {
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                FacultyOfficer facOff = (FacultyOfficer) user;
                return new SimpleStringProperty(facOff.getName());
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                return new SimpleStringProperty(depOff.getName());
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getName());
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้ระบบ");
        usernameColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                FacultyOfficer facOff = (FacultyOfficer) user;
                return new SimpleStringProperty(facOff.getUsername());
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                return new SimpleStringProperty(depOff.getUsername());
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getUsername());
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> passwordColumn = new TableColumn<>("รหัสผ่านเริ่มต้น");
        passwordColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                FacultyOfficer facOff = (FacultyOfficer) user;
                if (facOff.validatePassword(facOff.getDefaultPassword())) {
                    return new SimpleStringProperty(facOff.getDefaultPassword());
                } else {
                    return new SimpleStringProperty("ผู้ใช้เปลี่ยนรหัสผ่านแล้ว");
                }
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                if (depOff.validatePassword(depOff.getDefaultPassword())) {
                    return new SimpleStringProperty(depOff.getDefaultPassword());
                } else {
                    return new SimpleStringProperty("ผู้ใช้เปลี่ยนรหัสผ่านแล้ว");
                }
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                if (advisor.validatePassword(advisor.getDefaultPassword())) {
                    return new SimpleStringProperty(advisor.getDefaultPassword());
                } else {
                    return new SimpleStringProperty("ผู้ใช้เปลี่ยนรหัสผ่านแล้ว");
                }
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                FacultyOfficer facOff = (FacultyOfficer) user;
                return new SimpleStringProperty(facOff.getFaculty().getFacultyName());
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                return new SimpleStringProperty(depOff.getFaculty().getFacultyName());
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getFaculty().getFacultyName());
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                return new SimpleStringProperty("-");
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                return new SimpleStringProperty(depOff.getDepartment().getDepartmentName());
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getDepartment().getDepartmentName());
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> idColumn = new TableColumn<>("รหัสประจำตัว");
        idColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                return new SimpleStringProperty("-");
            } else if (user instanceof DepartmentOfficer) {
                return new SimpleStringProperty("-");
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getAdvisorID());
            }
            return new SimpleStringProperty("-");
        });

        officerAdvisorTableView.getColumns().clear();
        officerAdvisorTableView.getColumns().add(nameColumn);
        officerAdvisorTableView.getColumns().add(usernameColumn);
        officerAdvisorTableView.getColumns().add(passwordColumn);
        officerAdvisorTableView.getColumns().add(facultyColumn);
        officerAdvisorTableView.getColumns().add(departmentColumn);
        officerAdvisorTableView.getColumns().add(idColumn);

        officerAdvisorTableView.getItems().clear();

        officerAdvisorTableView.getItems().clear();
        for (User user : userList.getAllUsers()) {
            if (user instanceof Advisor || user instanceof FacultyOfficer || user instanceof DepartmentOfficer) {
                officerAdvisorTableView.getItems().add(user);
            }
        }

        nameColumn.setPrefWidth(180);
        usernameColumn.setPrefWidth(180);
        passwordColumn.setPrefWidth(180);
        facultyColumn.setPrefWidth(180);
        departmentColumn.setPrefWidth(180);
        idColumn.setPrefWidth(180);
    }

    public void selectRole(String role) {
        if (role.equals("อาจารย์ที่ปรึกษา")) {
            advisorStackPane.setVisible(true);
            facultyOfficerStackPane.setVisible(false);
            departmentOfficerStackPane.setVisible(false);
        } else if (role.equals("เจ้าหน้าที่คณะ")) {
            facultyOfficerStackPane.setVisible(true);
            advisorStackPane.setVisible(false);
            departmentOfficerStackPane.setVisible(false);
        } else if (role.equals("เจ้าหน้าที่ภาควิชา")) {
            departmentOfficerStackPane.setVisible(true);
            advisorStackPane.setVisible(false);
            facultyOfficerStackPane.setVisible(false);
        }
    }

    @FXML
    public void onAddButtonClick() {
        addStackPane.setVisible(true);
    }

    @FXML
    public void onEditConfirmClick() {
        String name = editNameTextField.getText();
        String username = editUsernameTextField.getText();
        String password = editPasswordTextField.getText();
        Faculty faculty = new Faculty(editFacultyTextField.getText().trim());
        Department department = new Department(editDepartmentTextField.getText().trim());
        String id = editIdTextField.getText();

        User selectedUser = officerAdvisorTableView.getSelectionModel().getSelectedItem();
        if (selectedUser instanceof FacultyOfficer) {
            FacultyOfficer facOff = (FacultyOfficer) selectedUser;
            if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || faculty.getFacultyName().trim().isEmpty() || name == null || username == null || password == null || faculty == null) {
                editErrorMessageLabel.setLayoutY(440);
                editErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null && !(username.equals(facOff.getUsername()))) {
                editErrorMessageLabel.setLayoutY(440);
                editErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty validateFaculty = facultyList.findFacultyByName(faculty.getFacultyName());
            if (validateFaculty == null) {
                editErrorMessageLabel.setLayoutY(440);
                editErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            facOff.setName(name);
            facOff.setUsername(username);
            facOff.setDefaultPassword(password);
            facOff.setFaculty(faculty);
        } else if (selectedUser instanceof DepartmentOfficer) {
            DepartmentOfficer depOff = (DepartmentOfficer) selectedUser;
            if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || faculty.getFacultyName().trim().isEmpty() || department.getDepartmentName().trim().isEmpty() || name == null || username == null || password == null || faculty == null || department == null) {
                editErrorMessageLabel.setLayoutY(505);
                editErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null && !(username.equals(depOff.getUsername()))) {
                editErrorMessageLabel.setLayoutY(505);
                editErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty validateFaculty = facultyList.findFacultyByName(faculty.getFacultyName());
            if (validateFaculty == null) {
                editErrorMessageLabel.setLayoutY(505);
                editErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            Department validateDepartment = facultyList.findFacultyByName(validateFaculty.getFacultyName()).findDepartmentByName(department.getDepartmentName());
            if (validateDepartment == null) {
                editErrorMessageLabel.setLayoutY(505);
                editErrorMessageLabel.setText("โปรดกรอกคภาควิชาที่มีอยู่ในระบบ");
                return;
            }

            depOff.setName(name);
            depOff.setUsername(username);
            depOff.setDefaultPassword(password);
            depOff.setFaculty(faculty);
            depOff.setDepartment(department);
        } else if (selectedUser instanceof Advisor) {
            Advisor advisor = (Advisor) selectedUser;
            if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || faculty.getFacultyName().trim().isEmpty() || department.getDepartmentName().trim().isEmpty() || id.trim().isEmpty() || name == null || username == null || password == null || faculty == null || department == null || id == null) {
                editErrorMessageLabel.setLayoutY(580);
                editErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null && !(username.equals(advisor.getUsername()))) {
                editErrorMessageLabel.setLayoutY(580);
                editErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty validateFaculty = facultyList.findFacultyByName(faculty.getFacultyName());
            if (validateFaculty == null) {
                editErrorMessageLabel.setLayoutY(580);
                editErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            Department validateDepartment = facultyList.findFacultyByName(validateFaculty.getFacultyName()).findDepartmentByName(department.getDepartmentName());
            if (validateDepartment == null) {
                editErrorMessageLabel.setText("โปรดกรอกคภาควิชาที่มีอยู่ในระบบ");
                return;
            }

            advisor.setName(name);
            advisor.setUsername(username);
            advisor.setDefaultPassword(password);
            advisor.setFaculty(faculty);
            advisor.setDepartment(department);
            advisor.setAdvisorID(id);
        }
        datasource.writeData(userList);
        editStackPane.setVisible(false);
        showTable(userList);
        clearEditFields();
    }

    @FXML
    public void onAddConfirmClick() {
        String selectedRole = roleChoiceBox.getValue();
        if (selectedRole.equals("อาจารย์ที่ปรึกษา")) {
            String name = advisorNameTextField.getText().trim();
            String username = advisorUsernameTextField.getText().trim();
            String password = advisorPasswordTextField.getText().trim();
            String facultyName = advisorFacultyTextField.getText().trim();
            String departmentName = advisorDepartmentTextField.getText().trim();
            String id = advisorIdTextField.getText().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || id.isEmpty() || facultyName.isEmpty() || departmentName.isEmpty()) {
                advisorErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null) {
                advisorErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty faculty = facultyList.findFacultyByName(facultyName);
            if (faculty == null) {
                advisorErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            Department department = facultyList.findFacultyByName(faculty.getFacultyName()).findDepartmentByName(departmentName);
            if (department == null) {
                advisorErrorMessageLabel.setText("โปรดกรอกภาควิชาที่มีอยู่ในระบบ");
                return;
            }

            Advisor advisor = new Advisor(name, username, password, faculty, department, id);
            userList.addUser(advisor);
            advisorNameTextField.setText("");
            advisorUsernameTextField.setText("");
            advisorPasswordTextField.setText("");
            advisorFacultyTextField.setText("");
            advisorDepartmentTextField.setText("");
            advisorIdTextField.setText("");
            advisorErrorMessageLabel.setText(""); // ล้างข้อความผิดพลาดหลังจากเพิ่มสำเร็จ
        } else if (selectedRole.equals("เจ้าหน้าที่คณะ")) {
            String name = facultyOfficerNameTextField.getText().trim();
            String username = facultyOfficerUsernameTextField.getText().trim();
            String password = facultyOfficerPasswordTextField.getText().trim();
            String facultyName = facultyOfficerFacultyTextField.getText().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || facultyName.isEmpty()) {
                facultyOfficerErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null) {
                facultyOfficerErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty faculty = facultyList.findFacultyByName(facultyName);
            if (faculty == null) {
                facultyOfficerErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            FacultyOfficer facultyOfficer = new FacultyOfficer(username, password, name, faculty, false, false);
            userList.addUser(facultyOfficer);
            facultyOfficerNameTextField.setText("");
            facultyOfficerUsernameTextField.setText("");
            facultyOfficerPasswordTextField.setText("");
            facultyOfficerFacultyTextField.setText("");
            facultyOfficerErrorMessageLabel.setText("");
        } else if (selectedRole.equals("เจ้าหน้าที่ภาควิชา")) {
            String name = departmentOfficerNameTextField.getText().trim();
            String username = departmentOfficerUsernameTextField.getText().trim();
            String password = departmentOfficerPasswordTextField.getText().trim();
            String facultyName = departmentOfficerFacultyTextField.getText().trim();
            String departmentName = departmentOfficerDepartmentTextField.getText().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || facultyName.isEmpty() || departmentName.isEmpty()) {
                departmentOfficerErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
                return;
            }

            if (userList.findUserByUsername(username) != null) {
                departmentOfficerErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
                return;
            }

            Faculty faculty = facultyList.findFacultyByName(facultyName);
            if (faculty == null) {
                departmentOfficerErrorMessageLabel.setText("โปรดกรอกคณะที่มีอยู่ในระบบ");
                return;
            }

            Department department = facultyList.findFacultyByName(faculty.getFacultyName()).findDepartmentByName(departmentName);
            if (department == null) {
                departmentOfficerErrorMessageLabel.setText("โปรดกรอกภาควิชาที่มีอยู่ในระบบ");
                return;
            }

            DepartmentOfficer departmentOfficer = new DepartmentOfficer(username, password, name, faculty, department, false, false);
            userList.addUser(departmentOfficer);
            departmentOfficerNameTextField.setText("");
            departmentOfficerUsernameTextField.setText("");
            departmentOfficerPasswordTextField.setText("");
            departmentOfficerFacultyTextField.setText("");
            departmentOfficerDepartmentTextField.setText("");
            departmentOfficerErrorMessageLabel.setText(""); // ล้างข้อความผิดพลาดหลังจากเพิ่มสำเร็จ
        }

        datasource.writeData(userList);
        addStackPane.setVisible(false);
        showTable(userList);
    }

    private void clearEditFields() {
        editNameTextField.setText("");
        editUsernameTextField.setText("");
        editPasswordTextField.setText("");
        editFacultyTextField.setText("");
        editDepartmentTextField.setText("");
        editIdTextField.setText("");
        editErrorMessageLabel.setText("");
    }

    @FXML
    public void onEditCancelButtonClick() {
        editStackPane.setVisible(false);
        clearEditFields();
    }

    @FXML
    public void onAddCancelButtonClick() {
        advisorNameTextField.setText("");
        advisorUsernameTextField.setText("");
        advisorPasswordTextField.setText("");
        advisorFacultyTextField.setText("");
        advisorDepartmentTextField.setText("");
        advisorIdTextField.setText("");
        advisorErrorMessageLabel.setText("");
        facultyOfficerNameTextField.setText("");
        facultyOfficerUsernameTextField.setText("");
        facultyOfficerPasswordTextField.setText("");
        facultyOfficerFacultyTextField.setText("");
        facultyOfficerErrorMessageLabel.setText("");
        departmentOfficerNameTextField.setText("");
        departmentOfficerUsernameTextField.setText("");
        departmentOfficerPasswordTextField.setText("");
        departmentOfficerFacultyTextField.setText("");
        departmentOfficerDepartmentTextField.setText("");
        departmentOfficerErrorMessageLabel.setText("");
        addStackPane.setVisible(false);
    }

    @FXML
    protected void onLogoutClick() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onDashboardClick() {
        try {
            FXRouter.goTo("dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onUserClick() {
        try {
            FXRouter.goTo("user-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onDepartmentAndFacultyClick() {
        try {
            FXRouter.goTo("department-faculty-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSettingButtonClick() {
        try {
            FXRouter.goTo("settings");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}