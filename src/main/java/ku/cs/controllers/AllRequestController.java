package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Request;
import ku.cs.models.RequestList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;

public class AllRequestController {
    @FXML private TableView<Request> allRequestTableView;
    private RequestList requestList;
    private Datasource<RequestList> datasource;

    @FXML
    public void initialize() {
        datasource = new RequestListFileDatasource("data", "all-request.csv");
        requestList = datasource.readData();
        showTable(requestList);
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

        TableColumn<Request, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        allRequestTableView.getColumns().clear();
        allRequestTableView.getColumns().add(nameColumn);
        allRequestTableView.getColumns().add(facultyColumn);
        allRequestTableView.getColumns().add(departmentColumn);
        allRequestTableView.getColumns().add(statusColumn);

        allRequestTableView.getItems().clear();

        for (Request request: requestList.getRequests()) {
            //System.out.println("Adding request: " + request.getName());
            allRequestTableView.getItems().add(request);
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
