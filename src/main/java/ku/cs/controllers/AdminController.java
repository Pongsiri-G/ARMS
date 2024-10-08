package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.RequestList;
import ku.cs.models.Request;
import ku.cs.models.Advisor;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.models.FacultyOfficer;
import ku.cs.models.DepartmentOfficer;
import ku.cs.models.Student;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class AdminController {
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;
    @FXML private Label userLabel;
    @FXML private TableView<User> allUserTableView;
    private RequestList requestList;
    private UserList userList;
    private Datasource<UserList> datasource;
    private Datasource<RequestList> requestListDatasource;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        requestListDatasource = new RequestListFileDatasource("data/test", "all-request.csv");
        userList = datasource.readData();
        requestList = requestListDatasource.readData();
        showTable(userList);
        showRequest(requestList);
        showTotalUsers(userList);
    }

    @FXML
    private void showTable(UserList userList) {
        // Column for name
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Column for role
        TableColumn<User, String> roleColumn = new TableColumn<>("บทบาท");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Column for faculty
        TableColumn<User, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            String facultyName = "";

            // Get faculty based on user type
            if (user instanceof Student) {
                Student student = (Student) user;
                facultyName = student.getEnrolledFaculty().getFacultyName(); // Get faculty for student
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                facultyName = advisor.getFaculty().getFacultyName(); // Get faculty for advisor
            } else if (user instanceof FacultyOfficer) {
                FacultyOfficer facultyOfficer = (FacultyOfficer) user;
                facultyName = facultyOfficer.getFaculty().getFacultyName(); // Get faculty for faculty officer
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
                facultyName = departmentOfficer.getFaculty().getFacultyName(); // Get faculty for department officer
            }

            // Return the faculty name (or "-" if not available)
            return new SimpleStringProperty(facultyName.isEmpty() ? "-" : facultyName);
        });

        // Column for department (with logic to handle FacultyOfficer not having a department)
        TableColumn<User, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();

            if (user instanceof FacultyOfficer) {
                return new SimpleStringProperty("-");
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer officer = (DepartmentOfficer) user;
                return new SimpleStringProperty(officer.getDepartment().getDepartmentName());
            } else if (user instanceof Student) {
                Student student = (Student) user;
                return new SimpleStringProperty(student.getEnrolledDepartment().getDepartmentName());
            }
            return new SimpleStringProperty("-");
        });

        // Add columns to the table
        allUserTableView.getColumns().clear();
        allUserTableView.getColumns().add(nameColumn);
        allUserTableView.getColumns().add(roleColumn);
        allUserTableView.getColumns().add(facultyColumn);  // Add faculty column
        allUserTableView.getColumns().add(departmentColumn);

        // Add users to the table
        allUserTableView.getItems().clear();
        for (User user : userList.getAllUsers()) {
            allUserTableView.getItems().add(user);
        }
    }

    private void showRequest(RequestList requestList) {
        allRequestLabel.setText(String.format("%d", requestList.getAllRequestCount()));
        approvedLabel.setText(String.format("%d", requestList.getApprovedRequestsCount()));
    }

    private void showTotalUsers(UserList userList) {
        userLabel.setText(String.format("%d", userList.getAllUsers().size()));
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
    protected void onAllRequestClick() {
        try {
            FXRouter.goTo("all-request");
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
    protected void onStaffClick() {
        try {
            FXRouter.goTo("staff-advisor-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onApprovedClick() {
        try {
            FXRouter.goTo("approved-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
