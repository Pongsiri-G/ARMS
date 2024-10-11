package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.FacDepListFileDatascource;

import java.io.IOException;


import java.io.IOException;
import java.util.stream.Collectors;

public class DepartmentAndFacultyManagementController {
    @FXML private StackPane addStackPane;
    @FXML private StackPane editStackPane;
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
    private Datasource<FacultyList> datasource;

    @FXML
    public void initialize() {
        errorMessageLabel.setText("");
        editErrorMessageLabel.setText("");
        addStackPane.setVisible(false);
        editStackPane.setVisible(false);
        datasource = new FacDepListFileDatascource("data/test", "facdeplist.csv");
        facultyList = datasource.readData();
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

    private void showEditPane(Faculty faculty) {
        editStackPane.setVisible(true);
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
        editStackPane.setVisible(false);
        clearEditFields();
    }

    // คุณสามารถเพิ่มฟังก์ชันสำหรับการบันทึกการแก้ไขได้ที่นี่
    @FXML
    public void onEditClick() {
        String facultyName = editFacultyTextField.getText();
        String facultyId = editFacultyIdTextField.getText();
        String departmentName = editDepartmentTextField.getText();
        String departmentId = editDepartmentIdTextField.getText();

        if (facultyName.trim().isEmpty() || facultyId.trim().isEmpty() || departmentName.trim().isEmpty() || departmentId.trim().isEmpty() || facultyName == null || facultyId == null || departmentName == null || departmentId == null) {
            editErrorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
            return;
        }

        Faculty selectedFaculty = facDepTableView.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            // อัปเดตข้อมูลของ selectedFaculty
            selectedFaculty.setFacultyName(facultyName);
            selectedFaculty.setFacultyId(facultyId);

            // อัปเดตภาควิชา (ตามที่คุณออกแบบไว้ในโมเดล)
            // ในที่นี้สมมุติว่ามีแค่ภาควิชาเดียวที่สามารถแก้ไขได้
            if (!selectedFaculty.getDepartments().isEmpty()) {
                Department department = selectedFaculty.getDepartments().get(0); // สมมุติว่ามีแค่หนึ่งภาควิชา
                department.setDepartmentName(departmentName);
                department.setDepartmentID(departmentId);
            }

            // บันทึกข้อมูลลงไฟล์
            datasource.writeData(facultyList);

            // ซ่อน editStackPane
            editStackPane.setVisible(false);

            // อัปเดตตารางเพื่อแสดงข้อมูลที่อัปเดต
            showTable(facultyList);

            // ล้างข้อมูลใน TextField
            clearEditFields();
        } // แสดงตารางอีกครั้งเพื่อแสดงข้อมูลที่อัปเดต
    }

    @FXML public void onAddFacDepButtonClick() {
        addStackPane.setVisible(true);
    }

    @FXML public void onCancelButtonClick() {
        addStackPane.setVisible(false);
        clearAddFields();
    }

    @FXML public void onConfirmClick() {
        String facultyName = facultyTextField.getText();
        String facultyId = facultyIdTextField.getText();
        String departmentName = departmentTextField.getText();
        String departmentId = departmentIdTextField.getText();

        if(facultyName.trim().isEmpty() || facultyId.trim().isEmpty() || departmentName.trim().isEmpty() || departmentId.trim().isEmpty() || facultyName == null || facultyId == null || departmentName == null || departmentId == null) {
            errorMessageLabel.setText("กรุณากรอกข้อมูลให้ครบถ้วน");
        } else {
            facultyList.addFaculty(facultyName, facultyId, departmentName, departmentId);
            datasource.writeData(facultyList);
            addStackPane.setVisible(false);
            showTable(facultyList);
            clearAddFields();
        }
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
}