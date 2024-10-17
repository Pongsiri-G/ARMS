package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;


import java.io.IOException;
import java.util.stream.Collectors;

public class DepartmentAndFacultyManagementController {
    @FXML private GridPane addGridPane;
    @FXML private GridPane editGridPane;
    @FXML private TextField facultyTextField;
    @FXML private TextField facultyIdTextField;
    @FXML private TextField departmentTextField;
    @FXML private TextField departmentIdTextField;
    @FXML private TextField editFacultyTextField;
    @FXML private TextField editFacultyIdTextField;
    @FXML private TextField editDepartmentTextField;
    @FXML private TextField editDepartmentIdTextField;
    @FXML private Label errorMessageLabel;
    @FXML private Label editErrorMessageLabel;
    @FXML private TableView<Faculty> facDepTableView; // ใช้ Object เนื่องจากจะมีทั้ง Faculty และ Department
    private FacultyList facultyList;
    private UserList userList;
    private Admin admin;
    private Datasource<Admin> adminDatasource;
    private Datasource<UserList> datasource;

    @FXML
    public void initialize() {
        errorMessageLabel.setText("");
        editErrorMessageLabel.setText("");
        addGridPane.setVisible(false);
        editGridPane.setVisible(false);
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/test", "admin.csv");
        admin = adminDatasource.readData();
        facultyList = userList.getFacultyList();
        showTable(facultyList);

        facDepTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // ตรวจสอบการคลิก 2 ครั้ง
                Faculty selectedFaculty = facDepTableView.getSelectionModel().getSelectedItem();
                if (selectedFaculty != null) {
                    showEditPane(selectedFaculty);
                }
            }
        });
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
            // คืนค่าชื่อภาควิชา
            return new SimpleStringProperty(faculty.getDepartments().get(0).getDepartmentName());
        });

        TableColumn<Faculty, String> departmentIdColumn = new TableColumn<>("รหัสภาควิชา");
        departmentIdColumn.setCellValueFactory(cellData -> {
            Faculty faculty = cellData.getValue();
            // คืนค่ารหัสภาควิชา
            return new SimpleStringProperty(faculty.getDepartments().get(0).getDepartmentID());
        });

        // ล้างคอลัมน์เก่า
        facDepTableView.getColumns().clear();
        // เพิ่มคอลัมน์ใหม่ทั้งหมด
        facDepTableView.getColumns().addAll(facultyColumn, facultyIdColumn, departmentColumn, departmentIdColumn);

        // สร้าง ObservableList ใหม่สำหรับแสดงผล
        ObservableList<Faculty> data = FXCollections.observableArrayList();

        // เพิ่มข้อมูลใหม่โดยการแยกแถวสำหรับแต่ละภาควิชา
        for (Faculty faculty : facultyList.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                Faculty newFaculty = new Faculty(faculty.getFacultyName(), faculty.getFacultyId());
                newFaculty.addDepartment(department.getDepartmentName(), department.getDepartmentID());
                data.add(newFaculty);
            }
        }

        // ตั้งค่าข้อมูลให้กับ TableView
        facDepTableView.setItems(data);
    }

    private void showEditPane(Faculty faculty) {
        editGridPane.setVisible(true);
        // ตั้งค่าให้ TextField แสดงค่าที่เลือก
        editFacultyTextField.setText(faculty.getFacultyName());
        editFacultyIdTextField.setText(faculty.getFacultyId());

        // สมมุติว่ามีแค่หนึ่งภาควิชาที่สามารถแก้ไขได้
        if (!faculty.getDepartments().isEmpty()) {
            Department department = faculty.getDepartments().get(0); // แสดงข้อมูลภาควิชาแรก
            editDepartmentTextField.setText(department.getDepartmentName());
            editDepartmentIdTextField.setText(department.getDepartmentID());
        }
    }

    // ฟังก์ชันที่ใช้ซ่อน editStackPane
    @FXML
    public void onCancelEditButtonClick() {
        editGridPane.setVisible(false);
        clearEditFields();
    }

    @FXML
    public void onEditClick() {
        String facultyName = editFacultyTextField.getText();
        String facultyId = editFacultyIdTextField.getText();
        String departmentName = editDepartmentTextField.getText();
        String departmentId = editDepartmentIdTextField.getText();

        if (facultyName.trim().isEmpty() || facultyId.trim().isEmpty() || departmentName.trim().isEmpty() || departmentId.trim().isEmpty()) {
            editErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        Faculty selectedFaculty = facDepTableView.getSelectionModel().getSelectedItem();
        Faculty originFaculty = facultyList.findFacultyByName(selectedFaculty.getFacultyName());

        for (Faculty faculty : facultyList.getFaculties()) {
            if (faculty.getFacultyName().equals(facultyName) && !(facultyName.equals(selectedFaculty.getFacultyName()))) {
                editErrorMessageLabel.setText("คณะนี้มีอยู่แล้วในระบบ");
                return;
            }
        }

        for (Faculty faculty : facultyList.getFaculties()) {
            if (!faculty.getFacultyName().equals(selectedFaculty.getFacultyName()) && facultyId.equals(faculty.getFacultyId())) {
                editErrorMessageLabel.setText("รหัสคณะนี้มีอยู่แล้วในระบบ");
                return;
            }
        }

        for (Faculty faculty : facultyList.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                if (department.getDepartmentName().equals(departmentName) && !(departmentName.equals(selectedFaculty.getDepartments().get(0).getDepartmentName()))) {
                    editErrorMessageLabel.setText("ภาควิชานี้มีอยู่แล้วในระบบ");
                    return;
                }
            }
        }

        // ตรวจสอบว่ารหัสภาควิชาซ้ำหรือไม่
        for (Faculty faculty : facultyList.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                if (!department.getDepartmentName().equals(selectedFaculty.getDepartments().get(0).getDepartmentName())
                        && departmentId.equals(department.getDepartmentID())) {
                    editErrorMessageLabel.setText("รหัสภาควิชานี้มีอยู่แล้วในระบบ");
                    return;
                }
            }
        }

        if (selectedFaculty != null) {
            Department selectedDepartment = selectedFaculty.getDepartments().get(0); // เนื่องจากแต่ละแถวมีหนึ่งภาควิชา
            Department originDepartment = originFaculty.findDepartmentByName(selectedDepartment.getDepartmentName());
            originDepartment.setDepartmentName(departmentName);
            originDepartment.setDepartmentID(departmentId);
            originFaculty.setFacultyName(facultyName);
            originFaculty.setFacultyId(facultyId);
            // บันทึกข้อมูลลงไฟล์
            datasource.writeData(userList);
            showTable(facultyList);
            // ซ่อน pane และรีเฟรชตาราง
            editGridPane.setVisible(false);
            //showTable(facultyList);
            clearEditFields();
        }
    }

    @FXML public void onAddFacDepButtonClick() {
        addGridPane.setVisible(true);
    }

    @FXML public void onCancelButtonClick() {
        addGridPane.setVisible(false);
        clearAddFields();
    }

    @FXML
    public void onConfirmClick() {
        String facultyName = facultyTextField.getText();
        String facultyId = facultyIdTextField.getText();
        String departmentName = departmentTextField.getText();
        String departmentId = departmentIdTextField.getText();

        // ตรวจสอบข้อมูล
        if (facultyName.trim().isEmpty() || facultyId.trim().isEmpty() || departmentName.trim().isEmpty() || departmentId.trim().isEmpty()) {
            errorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            errorMessageLabel.setVisible(true); // แสดง errorMessageLabel
            return; // หยุดการทำงาน
        } else {
            errorMessageLabel.setVisible(false); // ซ่อน errorMessageLabel เมื่อข้อมูลถูกต้อง
        }

        if (facultyList.findFacultyByName(facultyName) != null && (!facultyList.findFacultyByName(facultyName).getFacultyId().equals(facultyId))) {
            errorMessageLabel.setText("โปรดกรอกรหัสคณะให้ถูกต้อง");
            errorMessageLabel.setVisible(true);
            return;
        }

        for (Faculty faculty : facultyList.getFaculties()) {
            if (facultyId.equals(faculty.getFacultyId())) {
                errorMessageLabel.setText("รหัสคณะนี้มีอยู่แล้วในระบบ");
                errorMessageLabel.setVisible(true);
                return;
            }
        }
        // ตรวจสอบว่ารหัสภาควิชาซ้ำหรือไม่
        for (Faculty faculty : facultyList.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                if (departmentId.equals(department.getDepartmentID())) {
                    errorMessageLabel.setText("รหัสภาควิชานี้มีอยู่แล้วในระบบ");
                    errorMessageLabel.setVisible(true);
                    return;
                }
            }
        }

        // เพิ่มคณะหรือภาควิชาใหม่ (ถ้ามีการตรวจสอบใน FacultyList แล้ว)
        boolean added = facultyList.addFaculty(facultyName, facultyId, departmentName, departmentId);
        if (!added) {
            errorMessageLabel.setText("ภาควิชานี้มีอยู่แล้วในคณะนี้");
            errorMessageLabel.setVisible(true);
            return;
        }

        // บันทึกข้อมูลลงไฟล์
        datasource.writeData(userList);
        addGridPane.setVisible(false);
        showTable(facultyList);
        clearAddFields();
    }

    private void clearAddFields() {
        facultyTextField.clear();
        facultyIdTextField.clear();
        departmentTextField.clear();
        departmentIdTextField.clear();
        errorMessageLabel.setText("");
    }

    private void clearEditFields() {
        editFacultyTextField.clear();
        editFacultyIdTextField.clear();
        editDepartmentTextField.clear();
        editDepartmentIdTextField.clear();
        editErrorMessageLabel.setText("");
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

    @FXML
    protected void onSettingButtonClick() {
        try {
            FXRouter.goTo("settings");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}