package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.models.User;
import ku.cs.services.AdvOffListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class OfficerAndAdvisorManagementController {
    @FXML private TableView<User> officerAdvisorTableView;
    private UserList userList;
    private Datasource<UserList> datasource;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
        showTable(userList);
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
                return new SimpleStringProperty(advisor.getFaculty());
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
                return new SimpleStringProperty(advisor.getDepartment());
            }
            return new SimpleStringProperty("-");
        });

        TableColumn<User, String> idColumn = new TableColumn<>("รหัสประจำตัวอาจารย์ที่ปรึกษา");
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
