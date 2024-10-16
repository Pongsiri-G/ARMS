package ku.cs.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Request;
import ku.cs.models.RequestList;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

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
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = datasource.readData();
        showRequestStatusCount(requestList);
        showRequest(requestList);
        showTotalUsers(userList);
    }

    private void showRequestStatusCount(RequestList requestList) {
        successLabel.setText(requestList.getApprovedRequestsCount() + "");
        pendingLabel.setText(requestList.getPendingRequestCount() + "");
        deniedLabel.setText(requestList.getRejectedRequestsCount() + "");
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
