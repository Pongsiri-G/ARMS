package ku.cs.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.Comparator;

public class UserManagementController {
    @FXML private ChoiceBox<String> searchByRole;
    @FXML private TextField searchTextField;
    @FXML private TableView<User> userManagementTableView;
    private String[] role = {"ทั้งหมด", "นิสิต", "อาจารย์", "เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา"};
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
        searchByRole.setValue("ทั้งหมด");
        searchByRole.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterTableByRole(newValue);
        });

        datasource = new UserListFileDatasource(testDirectory, testStudentFileName, testAdvisorFileName, testFacultyOfficerFileName, testDepartmentFileName, testFacDepFileName);
        userList = datasource.readData();

        showTable(userList);

        // เพิ่ม Listener ให้กับ TextField เพื่อค้นหาทันทีที่มีการเปลี่ยนแปลงข้อความ
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterUsersBySearch(newValue);
            }
        });
    }

    private void filterTableByRole(String role) {
        // Create a FilteredList from the original ObservableList
        ObservableList<User> observableUserList = FXCollections.observableArrayList(userList.getAllUsers());
        FilteredList<User> filteredList = new FilteredList<>(observableUserList, user -> {
            // Filter based on the selected role
            if (role.equals("ทั้งหมด")) {
                try {
                    FXRouter.goTo("user-management");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true; // Show all users
            } else if (role.equals("นิสิต") && user instanceof Student) {
                return true;
            } else if (role.equals("อาจารย์") && user instanceof Advisor) {
                return true;
            } else if (role.equals("เจ้าหน้าที่ภาควิชา") && user instanceof FacultyOfficer) {
                return true;
            } else if (role.equals("เจ้าหน้าที่คณะ") && user instanceof DepartmentOfficer) {
                return true;
            }
            return false;
        });

        // Wrap FilteredList with a SortedList and bind the comparator to the TableView's comparator
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userManagementTableView.comparatorProperty());

        // Set the items of the TableView to the SortedList
        userManagementTableView.setItems(sortedList);
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
            Image image;
            if (imagePath != null) {
                image = new Image("file:" + imagePath, 120, 160, true, true); // กำหนดขนาดตามต้องการ
            } else {
                // ใช้รูปภาพเริ่มต้นถ้าไม่พบไฟล์
                image = new Image(getClass().getResourceAsStream("/images/profile.jpg"), 120, 160, true, true);
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

        // Column for name
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Column for login time
        TableColumn<User, String> timeColumn = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        timeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user.getLastLogin() != null) {
                return new SimpleStringProperty(user.getLastLogin().toString());
            }
            return new SimpleStringProperty("ไม่เคยเข้าใช้งาน");
        });

        // Column for suspend status
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
        userManagementTableView.getColumns().addAll(pictureColumn, usernameColumn, nameColumn, timeColumn, suspendColumn);

        // สร้าง ObservableList จาก userList
        ObservableList<User> observableUserList = FXCollections.observableArrayList(userList.getAllUsers());

        // สร้าง SortedList และกำหนด Comparator
        SortedList<User> sortedList = new SortedList<>(observableUserList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.getLastLogin() == null && u2.getLastLogin() == null) {
                    return 0;
                } else if (u1.getLastLogin() == null) {
                    return 1; // u1 ไม่มีการเข้าสู่ระบบล่าสุด, จัดหลัง
                } else if (u2.getLastLogin() == null) {
                    return -1; // u2 ไม่มีการเข้าสู่ระบบล่าสุด, จัดหลัง
                } else {
                    return u2.getLastLogin().compareTo(u1.getLastLogin()); // เรียง descending
                }
            }
        });

        // ตั้งค่า SortedList ให้กับ TableView
        userManagementTableView.setItems(sortedList);

        // ตั้งค่าการเรียงลำดับเริ่มต้น
        userManagementTableView.getSortOrder().clear();
        userManagementTableView.getSortOrder().add(timeColumn);
        timeColumn.setSortType(TableColumn.SortType.DESCENDING);
        userManagementTableView.sort();

        // กำหนดความกว้างคอลัมน์
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

    @FXML
    protected void onSettingButtonClick() {
        try {
            FXRouter.goTo("settings");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

