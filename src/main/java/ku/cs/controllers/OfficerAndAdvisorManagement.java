package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.AdvisorList;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;
import ku.cs.services.AdvOffListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacDepListFileDatascource;

import java.io.IOException;

public class OfficerAndAdvisorManagement {
    @FXML private TableView<Faculty> staffAdvisorTableView;
    private AdvisorList advisorList;
    private Datasource<AdvisorList> datasource;

    @FXML
    public void initialize() {
        datasource = new AdvOffListFileDatasource("data/test", "officer-advisor.csv");
        advisorList = datasource.readData();
        showTable(advisorList);
    }

    @FXML
    private void showTable(AdvisorList advisorList) {
        TableColumn<Faculty, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้ระบบ");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        TableColumn<Faculty, String> passwordColumn = new TableColumn<>("รหัสผ่านเริ่มต้น");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        TableColumn<Faculty, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        TableColumn<Faculty, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        TableColumn<Faculty, String> idColumn = new TableColumn<>("รหัสประจำตัวอาจารย์ที่ปรึกษา");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));

        staffAdvisorTableView.getColumns().clear();
        staffAdvisorTableView.getColumns().add(nameColumn);
        staffAdvisorTableView.getColumns().add(usernameColumn);
        staffAdvisorTableView.getColumns().add(passwordColumn);
        staffAdvisorTableView.getColumns().add(facultyColumn);
        staffAdvisorTableView.getColumns().add(departmentColumn);
        staffAdvisorTableView.getColumns().add(idColumn);

        staffAdvisorTableView.getItems().clear();

        for (AdvisorList advisor: advisorList.getFaculties()) {
            staffAdvisorTableView.getItems().add(advisor);
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
