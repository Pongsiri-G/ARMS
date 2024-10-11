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

import java.io.IOException;
import java.util.ArrayList;

public class OfficerAndAdvisorManagementController {
    @FXML private StackPane addStackPane;
    @FXML private StackPane editStackPane;
    @FXML private Label editErrorMessageLabel;
    @FXML private Label addErrorMessageLabel;
    @FXML private TextField addNameTextField;
    @FXML private TextField addUsernameTextField;
    @FXML private TextField addPasswordTextField;
    @FXML private TextField addFacultyTextField;
    @FXML private TextField addDepartmentTextField;
    @FXML private TextField addIdTextField;
    @FXML private TextField editNameTextField;
    @FXML private TextField editUsernameTextField;
    @FXML private TextField editPasswordTextField;
    @FXML private TextField editFacultyTextField;
    @FXML private TextField editDepartmentTextField;
    @FXML private TextField editIdTextField;
    @FXML private TableView<User> officerAdvisorTableView;
    private UserList userList;
    private Datasource<UserList> datasource;

    @FXML
    public void initialize() {
        addErrorMessageLabel.setText("");
        editErrorMessageLabel.setText("");
        addStackPane.setVisible(false);
        editStackPane.setVisible(false);
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
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
            editNameTextField.setText(advisor.getName());
            editUsernameTextField.setText(advisor.getUsername());
            editPasswordTextField.setText(advisor.getPassword());
            editFacultyTextField.setText(advisor.getFaculty().getFacultyName());
            editDepartmentTextField.setText(advisor.getDepartment().getDepartmentName());
            editIdTextField.setText(advisor.getAdvisorID());
        } else if (user instanceof  FacultyOfficer) {
            FacultyOfficer facultyOfficer = (FacultyOfficer) user;
            editNameTextField.setText(facultyOfficer.getName());
            editUsernameTextField.setText(facultyOfficer.getUsername());
            editPasswordTextField.setText(facultyOfficer.getPassword());
            editFacultyTextField.setText(facultyOfficer.getFaculty().getFacultyName());
            editDepartmentTextField.setText("-");
            editIdTextField.setText("-");
        } else if (user instanceof  DepartmentOfficer) {
            DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
            editNameTextField.setText(departmentOfficer.getName());
            editUsernameTextField.setText(departmentOfficer.getUsername());
            editPasswordTextField.setText(departmentOfficer.getPassword());
            editFacultyTextField.setText(departmentOfficer.getFaculty().getFacultyName());
            editDepartmentTextField.setText(departmentOfficer.getDepartment().getDepartmentName());
            editIdTextField.setText("-");
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
                return new SimpleStringProperty(facOff.getPassword());
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer depOff = (DepartmentOfficer) user;
                return new SimpleStringProperty(depOff.getPassword());
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                return new SimpleStringProperty(advisor.getPassword());
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

        if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || id.trim().isEmpty() || name == null || username == null || password == null || faculty == null || department == null || id == null) {
            addErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        User selectedUser = officerAdvisorTableView.getSelectionModel().getSelectedItem();
        if (selectedUser instanceof FacultyOfficer) {
            FacultyOfficer facOff = (FacultyOfficer) selectedUser;
            facOff.setName(name);
            facOff.setUsername(username);
            facOff.setPassword(password, false);
            facOff.setFaculty(faculty);
        } else if (selectedUser instanceof DepartmentOfficer) {
            DepartmentOfficer depOff = (DepartmentOfficer) selectedUser;
            depOff.setName(name);
            depOff.setUsername(username);
            depOff.setPassword(password, false);
            depOff.setFaculty(faculty);
            depOff.setDepartment(department);
        } else if (selectedUser instanceof Advisor) {
            Advisor advisor = (Advisor) selectedUser;
            advisor.setName(name);
            advisor.setUsername(username);
            advisor.setPassword(password, false);
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
        String name = addNameTextField.getText();
        String username = addUsernameTextField.getText();
        String password = addPasswordTextField.getText();
        Faculty faculty = new Faculty(addFacultyTextField.getText().trim());
        Department department = new Department(addDepartmentTextField.getText().trim());
        String id = addIdTextField.getText();

        if (name.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || id.trim().isEmpty() || name == null || username == null || password == null || faculty == null || department == null || id == null) {
            addErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        boolean isAdvisor = !id.equals("-");
        boolean isFacultyOfficer = id.equals("-") && department.equals("-");
        boolean isDepartmentOfficer = id.equals("-") && !department.equals("-");

        if (userList.findUserByUsername(username) != null) {
            addErrorMessageLabel.setText("ชื่อผู้ใช้นี้มีอยู่ในระบบแล้ว โปรดใช้ชื่อใหม่");
            return;
        }

        User newUser = null;
        if (isAdvisor) {
            Advisor advisor = new Advisor(name, username, password, faculty, department, id);
            newUser = advisor;
        } else if (isFacultyOfficer) {
            FacultyOfficer facultyOfficer = new FacultyOfficer(username, password, name, faculty, false, false);
            newUser = facultyOfficer;
        } else if (isDepartmentOfficer) {
            DepartmentOfficer departmentOfficer = new DepartmentOfficer(username, password, name, faculty, department, false, false);
            newUser = departmentOfficer;
        }

        if (newUser instanceof Advisor) {
            userList.addUser((Advisor) newUser);
        } else if (newUser instanceof FacultyOfficer) {
            userList.addUser((FacultyOfficer) newUser);
        } else if (newUser instanceof DepartmentOfficer) {
            userList.addUser((DepartmentOfficer) newUser);
        }

        datasource.writeData(userList);
        addStackPane.setVisible(false);
        showTable(userList);
        clearAddFields();
    }

    private void clearAddFields() {
        addNameTextField.setText("");
        addUsernameTextField.setText("");
        addPasswordTextField.setText("");
        addFacultyTextField.setText("");
        addDepartmentTextField.setText("");
        addIdTextField.setText("");
        addErrorMessageLabel.setText("");
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
        addStackPane.setVisible(false);
        clearAddFields();
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
}