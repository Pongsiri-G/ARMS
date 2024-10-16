package ku.cs.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

public class AllRequestController {
    @FXML private Label allRequestLabel;
    @FXML private Label userLabel;
    @FXML private Label approvedLabel;
    @FXML private Label successLabel;
    @FXML private Label pendingLabel;
    @FXML private Label deniedLabel;
    @FXML private TableView<Request> allRequestTableView;
    private RequestList requestList;
    private UserList userList;
    private Admin admin;
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;
    private Datasource<Admin> adminDatasource;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = datasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/test", "admin.csv");
        admin = adminDatasource.readData();
        for (Request request : requestList.getRequests()) {
            admin.increaseRequestCount(request);
        }
        for (User user : userList.getAllUsers()) {
            admin.increaseUserCount(user);
        }
        showRequestStatusCount(admin);
        showRequest();
        showTotalUsers();
    }

    private void showRequestStatusCount(Admin admin) {
        successLabel.setText(admin.getAllApprovedRequests() + "");
        pendingLabel.setText(admin.getAllPendingRequests() + "");
        deniedLabel.setText(admin.getAllRejectRequests() + "");
    }

    private void showRequest() {
        allRequestLabel.setText(String.format("%d", admin.getAllRequests()));
        approvedLabel.setText(String.format("%d", admin.getAllApprovedRequests()));
    }

    private void showTotalUsers() {
        userLabel.setText(String.format("%d", admin.getTotalUsers()));
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
    protected void onUserManagementClick() {
        try {
            FXRouter.goTo("user-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onUserClick() {
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

    @FXML
    protected void onApprovedClick() {
        try {
            FXRouter.goTo("approved-request");
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
