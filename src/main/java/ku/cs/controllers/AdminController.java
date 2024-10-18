package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;
import java.util.stream.Collectors;

public class AdminController extends BaseController {
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
    @FXML private VBox roleVBox;
    @FXML private VBox facDepVBox;
    @FXML private BorderPane rootPane;
    @FXML private Circle profilePictureDisplay;


    private RequestList requestList;
    private UserList userList;
    private FacultyList facultyList;
    private Admin admin; 
    private Datasource<UserList> datasource;
    private Datasource<RequestList> requestListDatasource;
    private Datasource<FacultyList> facultyListDatasource;
    private Datasource<Admin> adminDatasource; 

    @FXML
    public void initialize() {
        
        selectedChoiceBox.getItems().addAll(choice);
        selectedChoiceBox.setValue(choice[0]);

        
        roleVBox.setVisible(true);
        facDepVBox.setVisible(false);

        
        datasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = datasource.readData();
        requestListDatasource = new RequestListFileDatasource("data/students_requests", "requestlist.csv", userList);
        requestList = requestListDatasource.readData();
        facultyListDatasource = new FacDepListFileDatascource("data/csv_files", "facdeplist.csv");
        facultyList = facultyListDatasource.readData();
        adminDatasource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        admin = adminDatasource.readData();

        applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, admin.getProfilePicturePath());

        addChoiceBoxListeners();

        for (User user : userList.getAllUsers()) {
            admin.increaseUserCount(user);
            if (user instanceof Student) {
                Student student = (Student) user;
                if (student.getEnrolledFaculty() != null) {
                    admin.increaseFacultyUserCount(student.getEnrolledFaculty().getFacultyName());
                } if (student.getEnrolledDepartment() != null) {
                    admin.increaseDepartmentUserCount(student.getEnrolledDepartment().getDepartmentName());
                }
            } else if (user instanceof Advisor) {
                Advisor advisor = (Advisor) user;
                if (advisor.getFaculty() != null) {
                    admin.increaseFacultyUserCount(advisor.getFaculty().getFacultyName());
                }if (advisor.getDepartment() != null) {
                    admin.increaseDepartmentUserCount(advisor.getDepartment().getDepartmentName());
                }
            } else if (user instanceof FacultyOfficer) {
                FacultyOfficer facultyOfficer = (FacultyOfficer) user;
                if (facultyOfficer.getFaculty() != null) {
                    admin.increaseFacultyUserCount(facultyOfficer.getFaculty().getFacultyName());
                }
            } else if (user instanceof DepartmentOfficer) {
                DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
                if (departmentOfficer.getFaculty() != null) {
                    admin.increaseFacultyUserCount(departmentOfficer.getFaculty().getFacultyName());
                }if (departmentOfficer.getDepartment() != null) {
                    admin.increaseDepartmentUserCount(departmentOfficer.getDepartment().getDepartmentName());
                }
            }
        }

        for (Request request : requestList.getRequests()) {
            admin.increaseRequestCount(request);
        }

        
        populateFacultyChoiceBox();
        populateDepartmentChoiceBox("All");

        
        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        selectedDepartment.getSelectionModel().select("เลือกภาควิชา");

        facultyLabel.setText(selectedFaculty.getValue()); 
        departmentLabel.setText(selectedDepartment.getValue()); 

        
        countUserByRole();

        
        showRequest();

        
        showTotalUsers();
    }

    private void countUserByRole() {
        if (admin != null) {
            countStudent.setText(String.valueOf(admin.getAllStudents()));
            countAdvisor.setText(String.valueOf(admin.getAllAdvisors()));
            countFacOff.setText(String.valueOf(admin.getAllFacultyOfficers()));
            countDepOff.setText(String.valueOf(admin.getAllDepartmentOfficers()));
        } else {
            
            countStudent.setText("0");
            countAdvisor.setText("0");
            countFacOff.setText("0");
            countDepOff.setText("0");
        }
    }

    private void populateFacultyChoiceBox() {
        
        selectedFaculty.getItems().clear();
        selectedFaculty.getItems().addAll(
                facultyList.getFaculties().stream()
                        .map(Faculty::getFacultyName)
                        .collect(Collectors.toList())
        );
        selectedFaculty.getItems().add(0, "เลือกคณะ"); 
    }

    private void populateDepartmentChoiceBox(String facultyName) {
        
        selectedDepartment.getItems().clear();
        selectedDepartment.getItems().add("All"); 

        if (!facultyName.equals("All")) { 
            Faculty faculty = facultyList.findFacultyByName(facultyName);
            if (faculty != null) {
                selectedDepartment.getItems().addAll(
                        faculty.getDepartments().stream()
                                .map(Department::getDepartmentName)
                                .collect(Collectors.toList())
                );
            }
        }

        
        selectedDepartment.getSelectionModel().select("All");
    }

    private void addChoiceBoxListeners() {
        
        selectedChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if ("บทบาท".equals(newValue)) {
                        roleVBox.setVisible(true);
                        facDepVBox.setVisible(false);
                    } else if ("คณะ/ภาควิชา".equals(newValue)) {
                        roleVBox.setVisible(false);
                        facDepVBox.setVisible(true);
                    }
                }
        );

        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.equals("เลือกคณะ")) {
                        populateDepartmentChoiceBox(newValue);
                        facultyLabel.setText(newValue); 
                    } else {
                        facultyLabel.setText(""); 
                    }
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (!newValue.equals("All")) {
                            departmentLabel.setText(newValue); 
                        } else {
                            departmentLabel.setText("ทั้งหมด"); 
                        }

                        if ("All".equals(newValue)) {
                            updateFacultyUser(selectedFaculty.getSelectionModel().getSelectedItem()); 
                        } else {
                            updateDepartmentUser(newValue); 
                        }
                    }
                }
        );
    }

    private void updateFacultyUser(String facultyName) {
        if (facultyName == null || facultyName.equals("เลือกคณะ")) {
            totalLabel.setText("0");
            return;
        }
        int totalFacUser = admin.getAllFacultyUsers(facultyName);
        totalLabel.setText(String.valueOf(totalFacUser));
    }

    private void updateDepartmentUser(String departmentName) {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        if (selectedFac == null || selectedFac.equals("เลือกคณะ")) {
            totalLabel.setText("0");
            return;
        }

        int totalDepUser = admin.getAllDepartmentUsers(departmentName);
        totalLabel.setText(String.valueOf(totalDepUser));
    }

    private void showRequest() {
        allRequestLabel.setText(String.format("%d", admin.getAllRequests()));
        approvedLabel.setText(String.format("%d", admin.getAllApprovedRequests()));
    }

    private void showTotalUsers() {
        if (admin != null) {
            userLabel.setText(String.valueOf(admin.getTotalUsers()));
        } else {
            userLabel.setText("0");
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

    @FXML
    protected void onSettingButtonClick() {
        try {
            FXRouter.goTo("admin-settings", "dashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
