package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

public class AllRequestController extends BaseController {
    @FXML private Label allRequestLabel;
    @FXML private Label userLabel;
    @FXML private Label approvedLabel;
    @FXML private Label successLabel;
    @FXML private Label pendingLabel;
    @FXML private Label deniedLabel;
    private RequestList requestList;
    private UserList userList;
    private Admin admin;
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;
    private Datasource<Admin> adminDatasource;

    @FXML private BorderPane rootPane;
    @FXML private Circle profilePictureDisplay;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/students_requests", "requestlist.csv", userList);
        requestList = datasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        admin = adminDatasource.readData();
        applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, admin.getProfilePicturePath());

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
            FXRouter.goTo("admin-settings", "all-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
