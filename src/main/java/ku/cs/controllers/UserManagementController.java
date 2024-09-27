package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class UserManagementController {
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;
    @FXML private TableView<User> userManagementTableView;
    private UserList userList;
    private Datasource<UserList> datasource;
    private Datasource<RequestList> requestListDatasource;

    @FXML
    public void initialize() {
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv");
        userList = datasource.readData();
        showTable(userList);
    }

    @FXML
    private void showTable(UserList userList) {
        // Column for name
        TableColumn<User, String> pictureColumn = new TableColumn<>("รูปภาพ");
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("profilePicturePath"));

        // Column for role
        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Column for faculty
        TableColumn<User, String> timeColumn = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        timeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user.getLastLogin() != null) {
                return new SimpleStringProperty(user.getLastLogin().toString());
            }
            return new SimpleStringProperty("never");
        });

        userManagementTableView.getColumns().clear();
        userManagementTableView.getColumns().add(pictureColumn);
        userManagementTableView.getColumns().add(usernameColumn);
        userManagementTableView.getColumns().add(timeColumn);  // Add faculty column

        // Add users to the table
        userManagementTableView.getItems().clear();
        for (User user : userList.getAllUsers()) {
            userManagementTableView.getItems().add(user);
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
}
