package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.stream.Collectors;

public class AdminController {
    @FXML private ChoiceBox<String> selectedChoiceBox;
    @FXML private ChoiceBox<String> selectedFaculty;
    @FXML private ChoiceBox<String> selectedDepartment;
    private String[] choice = {"บทบาท", "คณะ/ภาควิชา"};
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;
    @FXML private Label userLabel;
    @FXML private Label countStudent;
    @FXML private Label countAdvisor;
    @FXML private Label countFacOff;
    @FXML private Label countDepOff;
    @FXML private Label facultyLabel;
    @FXML private Label departmentLabel;
    @FXML private Label totalLabel;
    @FXML private StackPane roleStackPane;
    @FXML private StackPane facDepStackPane;
    @FXML private TableView<User> allUserTableView;
    private RequestList requestList;
    private UserList userList;
    private FacultyList facultyList;
    private Datasource<UserList> datasource;
    private Datasource<RequestList> requestListDatasource;
    private Datasource<FacultyList> facultyListDatasource;

    @FXML
    public void initialize() {
        // ตั้งค่า ChoiceBox สำหรับการเลือกประเภทการแสดงผล
        selectedChoiceBox.getItems().addAll(choice);
        selectedChoiceBox.setValue(choice[0]);

        // ตั้งค่า StackPane เริ่มต้น
        roleStackPane.setVisible(true);
        facDepStackPane.setVisible(false);

        // โหลดข้อมูลจากไฟล์ CSV
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = requestListDatasource.readData();
        facultyListDatasource = new FacDepListFileDatascource("data/test", "facdeplist.csv"); // แก้ไขชื่อคลาสและไฟล์ให้ถูกต้อง
        facultyList = facultyListDatasource.readData();

        addChoiceBoxListeners();
        // อัปเดตจำนวนผู้ใช้ในคณะและสาขาเริ่มต้น
        updateFacultyUser(selectedFaculty.getValue()); // อัปเดตจำนวนผู้ใช้ในคณะ
        updateDepartmentUser(selectedDepartment.getValue()); // อัปเดตจำนวนผู้ใช้ในสาขา

        populateFacultyChoiceBox();
        populateDepartmentChoiceBox("All");

        // ตั้งค่าเลือกเริ่มต้น
        selectedFaculty.getSelectionModel().select("วิทยาศาสตร์");
        selectedDepartment.getSelectionModel().select("All");

        facultyLabel.setText(selectedFaculty.getValue()); // อัปเดต label คณะ
        departmentLabel.setText(selectedDepartment.getValue()); // อัปเดต label สาขา

        // นับจำนวนผู้ใช้ตามบทบาท
        countUserByRole();

        // แสดงข้อมูลคำร้องขอ
        showRequest(requestList);

        // แสดงจำนวนผู้ใช้ทั้งหมด
        showTotalUsers(userList);
    }

    private void populateFacultyChoiceBox() {
        // เพิ่มชื่อคณะจาก facultyList ลงใน selectedFaculty
        selectedFaculty.getItems().clear();
        selectedFaculty.getItems().addAll(
                facultyList.getFaculties().stream()
                        .map(Faculty::getFacultyName)
                        .collect(Collectors.toList())
        );
    }

    private void populateDepartmentChoiceBox(String facultyName) {
        // ล้างรายการสาขาเก่า
        selectedDepartment.getItems().clear();
        selectedDepartment.getItems().add("All"); // เพิ่มตัวเลือก All

        if (!facultyName.equals("All")) { // เนื่องจาก selectedFaculty ไม่มี "All"
            Faculty faculty = facultyList.findFacultyByName(facultyName);
            if (faculty != null) {
                selectedDepartment.getItems().addAll(
                        faculty.getDepartments().stream()
                                .map(Department::getDepartmentName)
                                .collect(Collectors.toList())
                );
            }
        }

        // ตั้งค่าเลือกเป็น "All" หลังจากเติมข้อมูล
        selectedDepartment.getSelectionModel().select("All");
    }


    private void countUserByRole() {
        int allStudent = 0;
        int allAdvisor = 0;
        int allFacOff = 0;
        int allDepOff = 0;

        for (User user : userList.getAllUsers()) {
            if (user instanceof Student) { allStudent++; }
            else if (user instanceof Advisor) { allAdvisor++; }
            else if (user instanceof FacultyOfficer) { allFacOff++; }
            else if (user instanceof DepartmentOfficer) { allDepOff++; }
        }

        countStudent.setText(String.valueOf(allStudent));
        countAdvisor.setText(String.valueOf(allAdvisor));
        countFacOff.setText(String.valueOf(allFacOff));
        countDepOff.setText(String.valueOf(allDepOff));
    }

    private void addChoiceBoxListeners() {
        // Listener สำหรับ selectedChoiceBox เพื่อแสดง/hide StackPane ตามการเลือก
        selectedChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if ("บทบาท".equals(newValue)) {
                        roleStackPane.setVisible(true);
                        facDepStackPane.setVisible(false);
                    } else if ("คณะ/ภาควิชา".equals(newValue)) {
                        roleStackPane.setVisible(false);
                        facDepStackPane.setVisible(true);
                    }
                }
        );

        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateDepartmentChoiceBox(newValue);
                        facultyLabel.setText(newValue); // อัปเดตป้ายชื่อคณะ
                    }
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        departmentLabel.setText(newValue); // อัปเดตป้ายชื่อสาขา
                        if ("All".equals(newValue)) {
                            updateFacultyUser(selectedFaculty.getSelectionModel().getSelectedItem()); // อัปเดตจำนวนผู้ใช้ในคณะที่เลือกทั้งหมด
                        } else {
                            updateDepartmentUser(newValue); // อัปเดตจำนวนผู้ใช้ในสาขาที่เลือก
                        }
                    }
                }
        );
    }

    private void updateFacultyUser(String facultyName) {
        int totalFacUser = 0;

        // นับจำนวนผู้ใช้ในคณะที่เลือกทั้งหมด
        for (User user : userList.getAllUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getEnrolledFaculty() != null && student.getEnrolledFaculty().getFacultyName().equals(facultyName)) {
                    totalFacUser++;
                }
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                if (advisor.getFaculty() != null && advisor.getFaculty().getFacultyName().equals(facultyName)) {
                    totalFacUser++;
                }
            } else if (user instanceof FacultyOfficer) {
                FacultyOfficer officer = (FacultyOfficer) user;
                if (officer.getFaculty() != null && officer.getFaculty().getFacultyName().equals(facultyName)) {
                    totalFacUser++;
                }
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer officer = (DepartmentOfficer) user;
                if (officer.getFaculty() != null && officer.getFaculty().getFacultyName().equals(facultyName)) {
                    totalFacUser++;
                }
            }
        }

        // อัปเดตจำนวนผู้ใช้ทั้งหมดใน Label
        totalLabel.setText(String.valueOf(totalFacUser));
    }

    private void updateDepartmentUser(String departmentName) {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        int totalDepUser = 0;

        // นับจำนวนผู้ใช้ในภาควิชาที่เลือกภายในคณะที่เลือก
        for (User user : userList.getAllUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getEnrolledFaculty() != null && student.getEnrolledFaculty().getFacultyName().equals(selectedFac) &&
                        student.getEnrolledDepartment() != null && student.getEnrolledDepartment().getDepartmentName().equals(departmentName)) {
                    totalDepUser++;
                }
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                if (advisor.getFaculty() != null && advisor.getFaculty().getFacultyName().equals(selectedFac) &&
                        advisor.getDepartment() != null && advisor.getDepartment().getDepartmentName().equals(departmentName)) {
                    totalDepUser++;
                }
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer officer = (DepartmentOfficer) user;
                if (officer.getFaculty() != null && officer.getFaculty().getFacultyName().equals(selectedFac) &&
                        officer.getDepartment() != null && officer.getDepartment().getDepartmentName().equals(departmentName)) {
                    totalDepUser++;
                }
            }
        }

        // อัปเดตจำนวนผู้ใช้ทั้งหมดใน Label
        totalLabel.setText(String.valueOf(totalDepUser));
    }

    private void showRequest(RequestList requestList) {
        allRequestLabel.setText(String.format("%d", requestList.getAllRequestCount()));
        approvedLabel.setText(String.format("%d", requestList.getApprovedRequestsCount()));
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
