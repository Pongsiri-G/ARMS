package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.Advisor;
import ku.cs.models.AdvisorList;
import ku.cs.models.Faculty;
import ku.cs.services.AdvOffListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class OfficerAndAdvisorManagementController {
    @FXML private TableView<Advisor> officerAdvisorTableView;
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
        TableColumn<Advisor, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Advisor, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้ระบบ");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Advisor, String> passwordColumn = new TableColumn<>("รหัสผ่านเริ่มต้น");
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Advisor, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("faculty"));

        TableColumn<Advisor, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Advisor, String> idColumn = new TableColumn<>("รหัสประจำตัวอาจารย์ที่ปรึกษา");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("advisorID"));

        officerAdvisorTableView.getColumns().clear();
        officerAdvisorTableView.getColumns().add(nameColumn);
        officerAdvisorTableView.getColumns().add(usernameColumn);
        officerAdvisorTableView.getColumns().add(passwordColumn);
        officerAdvisorTableView.getColumns().add(facultyColumn);
        officerAdvisorTableView.getColumns().add(departmentColumn);
        officerAdvisorTableView.getColumns().add(idColumn);

        officerAdvisorTableView.getItems().clear();

        for (Advisor advisor: advisorList.getAdvisors()) {
            officerAdvisorTableView.getItems().add(advisor);
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
