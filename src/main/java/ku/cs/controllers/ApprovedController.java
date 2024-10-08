package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Request;
import ku.cs.models.RequestList;
import ku.cs.models.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class ApprovedController {
    @FXML private Label userLabel;
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;

    @FXML private TableView<Request> approvedTableView;
    private RequestList requestList;
    private UserList userList;
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;

    @FXML
    public void initialize() {
        datasource = new RequestListFileDatasource("data/test", "all-request.csv");
        userDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        requestList = datasource.readData();
        userList = userDatasource.readData();
        showTable(requestList);
        showRequest(requestList);
        showTotalUsers(userList);
    }

    @FXML
    private void showTable(RequestList requestList) {
        //System.out.println("Showing table with " + requestList.getRequests().size() + " requests");
        TableColumn<Request, String> nameColumn = new TableColumn<>("ชื่อผู้ใช้");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Request, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<Request, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        approvedTableView.getColumns().clear();
        approvedTableView.getColumns().add(nameColumn);
        approvedTableView.getColumns().add(facultyColumn);
        approvedTableView.getColumns().add(departmentColumn);

        approvedTableView.getItems().clear();

        // เพิ่มเฉพาะ requests ที่มีสถานะ "คำร้องถูกอนุมัติ"
        for (Request request: requestList.getRequests()) {
            if (request.getStatus().equals("คำร้องถูกอนุมัติ")) {
                approvedTableView.getItems().add(request);
            }
        }
    }

    private void showRequest(RequestList requestList) {
        allRequestLabel.setText(String.format("%d", requestList.getAllRequestCount()));
        approvedLabel.setText(String.format("%d", requestList.getAllRequestCount()));
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
}
