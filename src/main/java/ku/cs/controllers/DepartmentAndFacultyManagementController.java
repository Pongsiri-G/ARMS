package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacDepListFileDatascource;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;

public class DepartmentAndFacultyManagementController {
    @FXML private TableView<Faculty> facDepTableView;
    private FacultyList facultyList;
    private Datasource<FacultyList> datasource;

    @FXML
    public void initialize() {
        datasource = new FacDepListFileDatascource("data/test", "department-faculty.csv");
        facultyList = datasource.readData();
        showTable(facultyList);
    }

    @FXML
    private void showTable(FacultyList facultyList) {
        TableColumn<Faculty, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> facultyIdColumn = new TableColumn<>("รหัสของคณะ");
        facultyIdColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        TableColumn<Faculty, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        TableColumn<Faculty, String> departmentIdColumn = new TableColumn<>("รหัสของภาควิชา");
        departmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        facDepTableView.getColumns().clear();
        facDepTableView.getColumns().add(facultyColumn);
        facDepTableView.getColumns().add(facultyIdColumn);
        facDepTableView.getColumns().add(departmentColumn);
        facDepTableView.getColumns().add(departmentIdColumn);

        facDepTableView.getItems().clear();

        for (Faculty faculty: facultyList.getFaculties()) {
            facDepTableView.getItems().add(faculty);
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
    protected void onStaffClick() {
        try {
            FXRouter.goTo("staff-advisor-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
