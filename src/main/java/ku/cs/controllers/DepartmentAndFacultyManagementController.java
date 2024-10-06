package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacDepListFileDatascource;

import java.io.IOException;


import java.io.IOException;
import java.util.stream.Collectors;

public class DepartmentAndFacultyManagementController {
    @FXML private TableView<Faculty> facDepTableView; // ใช้ Object เนื่องจากจะมีทั้ง Faculty และ Department
    private FacultyList facultyList;
    private Datasource<FacultyList> datasource;

    @FXML
    public void initialize() {
        datasource = new FacDepListFileDatascource("data/test", "facdeplist.csv");
        facultyList = datasource.readData();
        showTable(facultyList);
    }

    private void showTable(FacultyList facultyList) {
        // สร้าง TableColumn สำหรับคณะและภาควิชา
        TableColumn<Faculty, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> facultyIdColumn = new TableColumn<>("รหัสคณะ");
        facultyIdColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));

        TableColumn<Faculty, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(cellData -> {
            Faculty faculty = cellData.getValue();
            String departments = faculty.getDepartments().stream()
                    .map(Department::getDepartmentName)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(departments);
        });

        TableColumn<Faculty, String> departmentIdColumn = new TableColumn<>("รหัสภาควิชา");
        departmentIdColumn.setCellValueFactory(cellData -> {
            Faculty faculty = cellData.getValue();
            String departmentIds = faculty.getDepartments().stream()
                    .map(Department::getDepartmentID)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(departmentIds);
        });

        // ล้างคอลัมน์เก่า
        facDepTableView.getColumns().clear();
        // เพิ่มคอลัมน์ใหม่ทั้งหมด
        facDepTableView.getColumns().addAll(facultyColumn, facultyIdColumn, departmentColumn, departmentIdColumn);

        // ตั้งค่าข้อมูลให้กับ TableView
        ObservableList<Faculty> data = FXCollections.observableArrayList(facultyList.getFaculties());
        facDepTableView.setItems(data);

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
