package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.IOException;

public class ApprovedController extends BaseController {
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

    @FXML private BorderPane rootPane;
    @FXML private Circle profilePictureDisplay;

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

        applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, admin.getProfilePicturePath());

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
        selectedFaculty.getItems().clear(); 

        
        for (Faculty faculty : facultyList.getFaculties()) {
            selectedFaculty.getItems().add(faculty.getFacultyName()); 
        }

        
        selectedFaculty.getSelectionModel().select("เลือกคณะ");
        populateDepartmentChoiceBox(selectedFaculty.getSelectionModel().getSelectedItem());

        
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    populateDepartmentChoiceBox(newValue);
                    facultyLabel.setText(newValue); 
                }
        );
    }


    private void populateDepartmentChoiceBox(String selectedFacultyName) {
        selectedDepartment.getItems().clear(); 
        selectedDepartment.getItems().add("All"); 

        Faculty selectedFaculty = facultyList.findFacultyByName(selectedFacultyName);
        if (selectedFaculty != null) {
            
            for (Department department : selectedFaculty.getDepartments()) {
                selectedDepartment.getItems().add(department.getDepartmentName());
            }

            
            selectedDepartment.getSelectionModel().select("All");
        }
    }

    private void addChoiceBoxListeners() {
        selectedFaculty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.equals("All")) {
                        selectedDepartment.getSelectionModel().select("All"); 
                    } else {
                        populateDepartmentChoiceBox(newValue); 
                    }
                    facultyLabel.setText(newValue); 
                }
        );

        selectedDepartment.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    departmentLabel.setText(newValue); 
                    updateApprovedRequestCount(newValue);
                }
        );
    }

    private void updateApprovedRequestCount(String departmentName) {
        String selectedFac = selectedFaculty.getSelectionModel().getSelectedItem();
        int approvedCount = 0;

        
        if (departmentName.equals("All")) {
            Faculty selectedFacultyObj = facultyList.findFacultyByName(selectedFac);
            if (selectedFacultyObj != null) {
                
                for (Department department : selectedFacultyObj.getDepartments()) {
                    approvedCount += countApprovedRequestsByDepartment(department.getDepartmentName());
                }
            }
        } else {
            
            approvedCount = countApprovedRequestsByDepartment(departmentName);
        }

        
        approvedCountLabel.setText(String.format("%d", approvedCount));
    }

    private int countApprovedRequestsByDepartment(String departmentName) {
        int count = 0;

        for (Request request : requestList.getRequests()) {
            Department department = request.getRequester().getEnrolledDepartment();

            
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
            FXRouter.goTo("admin-settings", "approved-request");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
