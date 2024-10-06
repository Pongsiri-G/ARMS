package ku.cs.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UserManagementController {
    @FXML private ChoiceBox<String> searchByRole;
    @FXML private Label roleLabel;
    @FXML private Label allRequestLabel;
    @FXML private Label approvedLabel;
    @FXML private TableView<User> userManagementTableView;
    private String[] role = {"All", "Students", "Advisors", "Faculty Officers", "Department Officers"};
    private UserList userList;
    private Datasource<UserList> datasource;
    private Datasource<RequestList> requestListDatasource;
    private UserListFileDatasource listDatasource;
    private String testDirectory = "data/test";
    private String testAdvisorFileName = "advisorlist.csv";
    private String testStudentFileName = "studentlist.csv";
    private String testFacultyOfficerFileName = "facultyofficerlist.csv";
    private String testDepartmentFileName = "departmentofficerlist.csv";
    private String testFacDepFileName = "facdeplist.csv";

    @FXML
    public void initialize() {
        searchByRole.getItems().addAll(role);
        searchByRole.setValue("All");
        searchByRole.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterTableByRole(newValue);
        });
        datasource = new UserListFileDatasource(testDirectory, testStudentFileName, testAdvisorFileName, testFacultyOfficerFileName, testDepartmentFileName, testFacDepFileName);
        userList = datasource.readData();
        showTable(userList);
    }

    private void filterTableByRole(String role) {
        userManagementTableView.getItems().clear(); // Clear current items

        for (User user : userList.getAllUsers()) {
            // Filter based on the selected role
            if (role.equals("Students") && user instanceof Student) {
                userManagementTableView.getItems().add(user);
            } else if (role.equals("Advisors") && user instanceof Advisor) {
                userManagementTableView.getItems().add(user);
            } else if (role.equals("Faculty Officers") && user instanceof FacultyOfficer) {
                userManagementTableView.getItems().add(user);
            } else if (role.equals("Department Officers") && user instanceof DepartmentOfficer) {
                userManagementTableView.getItems().add(user);
            } else if (role.equals("All")) {
                userManagementTableView.getItems().add(user);
            }
        }
    }

    @FXML
    private void showTable(UserList userList) {
        // Column for name
        TableColumn<User, Image> pictureColumn = new TableColumn<>("รูปภาพ");
        pictureColumn.setCellValueFactory(cellData -> {
            String imagePath = cellData.getValue().getProfilePicturePath();
            File file = new File(imagePath);
            Image image;
            if (file.exists()) {
                image = new Image(file.toURI().toString(), 50, 50, true, true); // กำหนดขนาดตามต้องการ
            } else {
                // ใช้รูปภาพเริ่มต้นถ้าไม่พบไฟล์
                image = new Image(getClass().getResourceAsStream("/images/default-profile.png"), 50, 50, true, true);
            }
            return new SimpleObjectProperty<>(image);
        });

        pictureColumn.setCellFactory(column -> new TableCell<User, Image>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    setGraphic(imageView);
                }
            }
        });


        // Column for role
        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Column for faculty
        TableColumn<User, String> timeColumn = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        timeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user.getLastLogin() != null) {
                return new SimpleStringProperty(user.getLastLogin().toString());
            }
            return new SimpleStringProperty("never");
        });

        userManagementTableView.getColumns().clear();
        userManagementTableView.getColumns().add(pictureColumn);
        userManagementTableView.getColumns().add(usernameColumn);
        userManagementTableView.getColumns().add(timeColumn);

        // Add users to the table
        userManagementTableView.getItems().clear();
        for (User user : userList.getAllUsers()) {
            userManagementTableView.getItems().add(user);
            //System.out.println("Added user to TableView: " + user.getUsername());
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
