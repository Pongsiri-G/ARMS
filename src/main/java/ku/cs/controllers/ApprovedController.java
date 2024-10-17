package ku.cs.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ApprovedController {
    @FXML private ChoiceBox<String> selectedFaculty;
    @FXML private ChoiceBox<String> selectedDepartment;
    @FXML private Label userLabel;
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;
    @FXML private Label facultyLabel;
    @FXML private Label departmentLabel;
    @FXML private Label approvedCountLabel;
    private RequestList requestList;
    private FacultyList facultyList;
    private UserList userList;
    private Admin admin;
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;
    private Datasource<FacultyList> facdepDatasource;
    private Datasource<Admin> adminDatasource;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/students_requests", "requestlist.csv", userList);
        requestList = datasource.readData();
        facdepDatasource = new FacDepListFileDatascource("data/csv_files", "facdeplist.csv");
        facultyList = facdepDatasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        admin = adminDatasource.readData();
        for (Request request : requestList.getRequests()) {
            admin.increaseRequestCount(request);
        }
        for (User user : userList.getAllUsers()) {
            admin.increaseUserCount(user);
        }

        addChoiceBoxListeners();
        populateFacultyChoiceBox();

        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        selectedDepartment.getSelectionModel().select("All");

        showRequest();
        showTotalUsers();
    }

    private void populateFacultyChoiceBox() {
        selectedFaculty.getItems().clear(); // ล้างรายการคณะเก่า

        // เพิ่มชื่อคณะจาก FacultyList ลงใน ChoiceBox
        for (Faculty faculty : facultyList.getFaculties()) {
            selectedFaculty.getItems().add(faculty.getFacultyName()); // เพิ่มชื่อคณะ
        }

        // ตั้งค่าเริ่มต้นให้เลือกเป็น "คณะวิทยาศาสตร์"
        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        populateDepartmentChoiceBox(selectedFaculty.getSelectionModel().getSelectedItem());

        // เพิ่ม Listener เพื่ออัปเดตสาขาเมื่อมีการเลือกคณะ
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    populateDepartmentChoiceBox(newValue);
                    facultyLabel.setText(newValue); // อัปเดตป้ายชื่อคณะ
                }
        );
    }


    private void populateDepartmentChoiceBox(String selectedFacultyName) {
        selectedDepartment.getItems().clear(); // ล้างรายการสาขาเก่า
        selectedDepartment.getItems().add("All"); // เพิ่มตัวเลือก "All"

        Faculty selectedFaculty = facultyList.findFacultyByName(selectedFacultyName);
        if (selectedFaculty != null) {
            // เพิ่มสาขาทั้งหมดในคณะที่เลือกลงใน ChoiceBox
            for (Department department : selectedFaculty.getDepartments()) {
                System.out.println("Adding department: " + department.getDepartmentName()); // Debugging
                selectedDepartment.getItems().add(department.getDepartmentName());
            }

            // ตั้งค่าเริ่มต้นให้เลือกเป็น "All"
            selectedDepartment.getSelectionModel().select("All");
        } else {
            System.out.println("Faculty not found: " + selectedFacultyName); // Debugging
        }
    }

    private void addChoiceBoxListeners() {
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.equals("All")) {
                        selectedDepartment.getSelectionModel().select("All"); // ตั้งค่า selectedDepartment เป็น "All"
                    } else {
                        populateDepartmentChoiceBox(newValue); // ปรับอัปเดตสาขา
                    }
                    facultyLabel.setText(newValue); // อัปเดตป้ายชื่อคณะ
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    departmentLabel.setText(newValue); // อัปเดตป้ายชื่อสาขา
                    updateApprovedRequestCount(newValue);
                }
        );
    }

    private void updateApprovedRequestCount(String departmentName) {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        int approvedCount = 0;

        // ตรวจสอบว่าถ้า selectedDepartment เป็น "All"
        if (departmentName.equals("All")) {
            Faculty selectedFacultyObj = facultyList.findFacultyByName(selectedFac);
            if (selectedFacultyObj != null) {
                // วนลูปเช็คคำร้องของทุกสาขาในคณะที่เลือก
                for (Department department : selectedFacultyObj.getDepartments()) {
                    approvedCount += countApprovedRequestsByDepartment(department.getDepartmentName());
                }
            }
        } else {
            // ถ้าเลือกสาขาเฉพาะก็ให้คำนวณเฉพาะสาขานั้น
            approvedCount = countApprovedRequestsByDepartment(departmentName);
        }

        // อัปเดต approvedCountLabel ด้วยจำนวนคำร้องที่ได้รับการอนุมัติ
        approvedCountLabel.setText(String.format("%d", approvedCount));
    }

    private int countApprovedRequestsByDepartment(String departmentName) {
        int count = 0;

        for (Request request : requestList.getRequests()) {
            Department department = request.getRequester().getEnrolledDepartment();

            // ตรวจสอบว่าคำร้องมาจากสาขาที่เลือก และมีสถานะเป็น "เสร็จสิ้น"
            if (department != null && department.getDepartmentName().equals(departmentName)
                    && request.getStatus().equals("เสร็จสิ้น")) {
                count++;
            }
        }

        return count;
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

    @FXML
    protected void onSettingButtonClick() {
        try {
            FXRouter.goTo("settings");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
