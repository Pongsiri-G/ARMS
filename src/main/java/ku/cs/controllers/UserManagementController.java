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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.util.Callback;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class UserManagementController {
    @FXML private ChoiceBox<String> searchByRole;
    @FXML private TextField searchTextField;
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

        // Print users who are suspended when the page is loaded (Debug)
        printSuspendedUsers(userList);
        showTable(userList);

        // เพิ่ม Listener ให้กับ TextField เพื่อค้นหาทันทีที่มีการเปลี่ยนแปลงข้อความ
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterUsersBySearch(newValue);
            }
        });
    }

    // เมธอดสำหรับพิมพ์รายชื่อผู้ใช้ที่ถูกระงับ (Debug)
    private void printSuspendedUsers(UserList userList) {
        for (User user : userList.getAllUsers()) {
            System.out.println("Checking user: " + user.getName() + " (" + user.getUsername() + ") - Suspended: " + user.getSuspended());
            if (user.getSuspended()) {
                System.out.println(user.getName() + " (" + user.getUsername() + ") ถูกระงับสิทธิ");
            }
        }
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

    private void filterUsersBySearch(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        if (searchQuery.isEmpty()) {
            showTable(userList);  // ถ้าไม่มีข้อความค้นหาให้แสดงผู้ใช้ทั้งหมด
            return;
        }

        // สร้าง UserList ใหม่เพื่อเก็บผลลัพธ์ที่กรอง
        UserList filteredUserList = new UserList();

        for (User user : userList.getAllUsers()) {
            // ตรวจสอบชื่อหรือชื่อผู้ใช้ที่ตรงกับคำค้นหา
            if (user.getName().toLowerCase().contains(searchQuery) ||
                    user.getUsername().toLowerCase().contains(searchQuery)) {

                // เพิ่มผู้ใช้โดยตรงลงใน List โดยไม่ต้องผ่าน addUser
                filteredUserList.getAllUsers().add(user);
            }
        }

        // แสดงผลผู้ใช้ที่กรองแล้วใน TableView
        showTable(filteredUserList);
    }

    @FXML
    private void showTable(UserList userList) {
        // Column for image
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

        // Column for username
        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้ระบบ");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Column for username
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Column for login time
        TableColumn<User, String> timeColumn = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        timeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user.getLastLogin() != null) {
                return new SimpleStringProperty(user.getLastLogin().toString());
            }
            return new SimpleStringProperty("never");
        });

        TableColumn<User, Void> suspendColumn = new TableColumn<>("สถานะการระงับสิทธิ");
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                final TableCell<User, Void> cell = new TableCell<User, Void>() {
                    private final Button btnSuspend = new Button();

                    {
                        btnSuspend.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            System.out.println("Before suspend: " + user.getUsername() + " - Suspended: " + user.getSuspended());
                            user.setSuspended(!user.getSuspended());
                            System.out.println("After suspend: " + user.getUsername() + " - Suspended: " + user.getSuspended());
                            updateButtonStyle(btnSuspend, user);
                            datasource.writeData(userList);
                            getTableView().refresh();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            User user = getTableView().getItems().get(getIndex());
                            updateButtonStyle(btnSuspend, user); // เรียกใช้ updateButtonStyle เพื่ออัปเดตปุ่มตามสถานะ
                            setGraphic(btnSuspend); // แสดงปุ่ม
                        }
                    }

                    private void updateButtonStyle(Button btn, User user) {
                        if (user.getSuspended()) {
                            btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                            btn.setText("ถูกระงับสิทธิ");
                        } else {
                            btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                            btn.setText("ปกติ");
                        }
                    }
                };
                return cell;
            }
        };
        suspendColumn.setCellFactory(cellFactory);

        userManagementTableView.getColumns().clear();
        userManagementTableView.getColumns().add(pictureColumn);
        userManagementTableView.getColumns().add(usernameColumn);
        userManagementTableView.getColumns().add(nameColumn);
        userManagementTableView.getColumns().add(timeColumn);
        userManagementTableView.getColumns().add(suspendColumn);

        // Add users to the table
        userManagementTableView.getItems().clear();
        for (User user : userList.getAllUsers()) {
            userManagementTableView.getItems().add(user);
        }

        pictureColumn.setPrefWidth(220);
        usernameColumn.setPrefWidth(220);
        nameColumn.setPrefWidth(220);
        timeColumn.setPrefWidth(220);
        suspendColumn.setPrefWidth(220);
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

