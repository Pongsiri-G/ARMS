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

    @FXML private TableView<Faculty> approvedTableView;
    private RequestList requestList;
    private FacultyList facultyList;
    private UserList userList;
    private Datasource<RequestList> datasource;
    private Datasource<UserList> userDatasource;
    private Datasource<FacultyList> facdepDatasource;

    @FXML
    public void initialize() {
        userDatasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource("data/test", "requestlist.csv", userList);
        requestList = datasource.readData();
        facdepDatasource = new FacDepListFileDatascource("data/test", "facdeplist.csv");
        facultyList = facdepDatasource.readData();

        populateFacultyChoiceBox();
        addChoiceBoxListeners();
        selectedDepartment.getSelectionModel().select("All");

        showTable(facultyList);
        showRequest(requestList);
        showTotalUsers(userList);
    }

    private void populateFacultyChoiceBox() {
        selectedFaculty.getItems().clear();
        Set<String> facultyNames = new HashSet<>(); // ใช้ Set เพื่อเก็บชื่อคณะที่ไม่ซ้ำกัน

        for (Faculty faculty : facultyList.getFaculties()) {
            facultyNames.add(faculty.getFacultyName()); // เพิ่มชื่อคณะลงใน Set
        }

        // เพิ่ม "All" ลงใน ChoiceBox
        selectedFaculty.getItems().add("All");

        // เพิ่มชื่อคณะจาก Set ลงใน ChoiceBox
        selectedFaculty.getItems().addAll(facultyNames);

        // ตั้งค่าเริ่มต้นให้เลือกเป็น "All"
        selectedFaculty.getSelectionModel().selectFirst();
        populateDepartmentChoiceBox(selectedFaculty.getSelectionModel().getSelectedItem());

        // เพิ่ม Listener เพื่ออัปเดตสาขาเมื่อมีการเลือกคณะ
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateDepartmentChoiceBox(newValue)
        );
    }

    private void populateDepartmentChoiceBox(String selectedFacultyName) {
        selectedDepartment.getItems().clear(); // ล้างรายการสาขาเก่า
        selectedDepartment.getItems().add("All"); // เพิ่มตัวเลือก "All"

        if (selectedFacultyName.equals("All")) {
            selectedDepartment.getSelectionModel().select("All");
            return; // ออกจากฟังก์ชัน
        }

        Faculty selectedFaculty = facultyList.findFacultyByName(selectedFacultyName);
        if (selectedFaculty != null) {
            // เพิ่มสาขาทั้งหมดที่อยู่ในคณะเดียวกันลงใน ChoiceBox
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
                    filterTable(); // เรียกใช้ฟังก์ชันกรองตาราง
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTable()
        );
    }


    private void filterTable() {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        String selectedDep = selectedDepartment.getSelectionModel().getSelectedItem();

        ObservableList<Faculty> filteredData = FXCollections.observableArrayList();

        for (Faculty faculty : facultyList.getFaculties()) {
            boolean facultyMatches = (selectedFac == null || selectedFac.equals("All")) || faculty.getFacultyName().equals(selectedFac);
            if (facultyMatches) {
                if (selectedDep == null || selectedDep.equals("All")) {
                    // ถ้า selectedDep เป็น "All" ให้เพิ่ม faculty ทั้งหมด
                    filteredData.add(faculty);
                } else {
                    // ตรวจสอบว่ามีสาขาใดที่ตรงกับตัวเลือกหรือไม่
                    boolean departmentMatches = faculty.getDepartments().stream()
                            .anyMatch(dept -> dept.getDepartmentName().equals(selectedDep));
                    if (departmentMatches) {
                        filteredData.add(faculty);
                    }
                }
            }
        }

        approvedTableView.setItems(filteredData);
    }

    @FXML
    private void showTable(FacultyList facultyList) {
        //System.out.println("Showing table with " + requestList.getRequests().size() + " requests");
        TableColumn<Faculty, String> facultyColumn = new TableColumn<>("คณะ");
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));

        TableColumn<Faculty, String> departmentColumn = new TableColumn<>("ภาควิชา");
        departmentColumn.setCellValueFactory(cellData -> {
            Faculty faculty = cellData.getValue();
            String departments = faculty.getDepartments().stream()
                    .map(Department::getDepartmentName)
                    .collect(Collectors.joining("\n"));
            return new SimpleStringProperty(departments);
        });

        TableColumn<Faculty, Integer> approvedRequestColumn = new TableColumn<>("จำนวนคำร้องที่อนุมัติ");
        approvedRequestColumn.setCellValueFactory(cellData -> {
            int approvedCount = 0;
            for (Department department : cellData.getValue().getDepartments()) {
                approvedCount += department.getApprovedDepartmentRequest();
            }
            return new SimpleIntegerProperty(approvedCount).asObject();
        });

        approvedTableView.getColumns().clear();
        approvedTableView.getColumns().addAll(facultyColumn, departmentColumn, approvedRequestColumn);
        ObservableList<Faculty> data = FXCollections.observableArrayList(facultyList.getFaculties());
        approvedTableView.setItems(data);
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
}
