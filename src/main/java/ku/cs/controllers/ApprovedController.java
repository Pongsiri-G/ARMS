package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

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
        userDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = datasource.readData();
        facdepDatasource = new FacDepListFileDatascource("data/test", "facdeplist.csv");
        facultyList = facdepDatasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/test", "admin.csv");
        admin = adminDatasource.readData();

        for (Request request : requestList.getRequests()) {
            admin.increaseRequestCount(request);
            String facultyName = request.getRequester().getEnrolledFaculty().getFacultyName();
            String departmentName = request.getRequester().getEnrolledDepartment().getDepartmentName();
            admin.increaseFacultyApprovedRequests(facultyName);
            admin.increaseDepartmentApprovedRequests(departmentName);
        }
        for (User user : userList.getAllUsers()) {
            admin.increaseUserCount(user);
        }

        addChoiceBoxListeners();
        populateFacultyChoiceBox();

        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        selectedDepartment.getSelectionModel().select("เลือกภาควิชา");

        showCounts();
    }

    private void populateFacultyChoiceBox() {
        selectedFaculty.getItems().clear(); // ล้างรายการคณะเก่า

        // เพิ่มชื่อคณะจาก FacultyList ลงใน ChoiceBox
        for (Faculty faculty : facultyList.getFaculties()) {
            selectedFaculty.getItems().add(faculty.getFacultyName()); // เพิ่มชื่อคณะ
        }

        // ตั้งค่าเริ่มต้นให้เลือกเป็น "เลือกคณะ"
        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        populateDepartmentChoiceBox(selectedFaculty.getSelectionModel().getSelectedItem());

        // เพิ่ม Listener เพื่ออัปเดตสาขาเมื่อมีการเลือกคณะ
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    populateDepartmentChoiceBox(newValue);
                    facultyLabel.setText(newValue); // อัปเดตป้ายชื่อคณะ
                    updateFacultyApprovedRequestCount(selectedDepartment.getSelectionModel().getSelectedItem());
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
                selectedDepartment.getItems().add(department.getDepartmentName());
            }

            // ตั้งค่าเริ่มต้นให้เลือกเป็น "All"
            selectedDepartment.getSelectionModel().select("All");
        }
    }

    private void addChoiceBoxListeners() {
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.equals("เลือกคณะ")) {
                        populateDepartmentChoiceBox(newValue);
                        facultyLabel.setText(newValue); // อัปเดตป้ายชื่อคณะ
                    } else {
                        facultyLabel.setText(""); // ล้างป้ายชื่อคณะถ้าเลือก "เลือกคณะ"
                    }
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (!newValue.equals("All")) {
                            departmentLabel.setText(newValue); // อัปเดตป้ายชื่อสาขา
                        } else {
                            departmentLabel.setText("ทั้งหมด"); // แสดง "ทั้งหมด" เมื่อเลือก "All"
                        }

                        if ("All".equals(newValue)) {
                            updateFacultyApprovedRequestCount(selectedFaculty.getSelectionModel().getSelectedItem()); // อัปเดตจำนวนผู้ใช้ในคณะที่เลือกทั้งหมด
                        } else {
                            updateDepartmentApproved(newValue);
                        }
                    }
                }
        );
    }

    private void updateFacultyApprovedRequestCount(String facultyName) {
        if (facultyName == null || facultyName.equals("เลือกคณะ")) {
            approvedCountLabel.setText("0");
            return;
        }
        int totalFacApproved = admin.getFacultyApprovedRequests(facultyName);
        approvedCountLabel.setText(String.valueOf(totalFacApproved));
    }

    private void updateDepartmentApproved(String departmentName) {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        if (selectedFac == null || selectedFac.equals("เลือกคณะ")) {
            approvedCountLabel.setText("0");
            return;
        }

        int totalFacApproved = admin.getDepartmentApprovedRequests(departmentName);
        approvedCountLabel.setText(String.valueOf(totalFacApproved));
    }

    private void showCounts() {
        allRequestLabel.setText(String.format("%d", admin.getAllRequests()));
        approvedLabel.setText(String.format("%d", admin.getAllApprovedRequests()));
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
