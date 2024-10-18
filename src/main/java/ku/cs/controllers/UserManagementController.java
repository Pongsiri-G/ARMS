package ku.cs.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.util.Callback;
import java.io.IOException;
import java.util.Comparator;

public class UserManagementController extends BaseController{
    @FXML private ChoiceBox<String> searchByRole;
    @FXML private TextField searchTextField;
    @FXML private TableView<User> userManagementTableView;
    @FXML private BorderPane rootPane;
    @FXML private Circle profilePictureDisplay;
    private String[] role = {"ทั้งหมด", "นิสิต", "อาจารย์", "เจ้าหน้าที่คณะ", "เจ้าหน้าที่ภาควิชา"};
    private UserList userList;
    private Admin admin;
    private Datasource<UserList> datasource;
    private Datasource<Admin> adminDatasource;
    private Datasource<RequestList> requestListDatasource;
    private UserListFileDatasource listDatasource;
    private String testDirectory = "data/csv_files";
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
        adminDatasource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        admin = adminDatasource.readData();

        applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
        setProfilePicture(profilePictureDisplay, admin.getProfilePicturePath());

        showTable(userList);

        
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterUsersBySearch(newValue);
            }
        });
    }

    private void filterTableByRole(String role) {
        
        ObservableList<User> observableUserList = FXCollections.observableArrayList(userList.getAllUsers());
        FilteredList<User> filteredList = new FilteredList<>(observableUserList, user -> {
            
            if (role.equals("ทั้งหมด")) {
                try {
                    FXRouter.goTo("user-management");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true; 
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

        
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(userManagementTableView.comparatorProperty());

        
        userManagementTableView.setItems(sortedList);
    }

    private void filterUsersBySearch(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();
        if (searchQuery.isEmpty()) {
            showTable(userList);  
            return;
        }

        
        UserList filteredUserList = new UserList();

        for (User user : userList.getAllUsers()) {
            
            if (user.getName().toLowerCase().contains(searchQuery) ||
                    user.getUsername().toLowerCase().contains(searchQuery)) {

                
                filteredUserList.getAllUsers().add(user);
            }
        }

        
        showTable(filteredUserList);
    }

    @FXML
    private void showTable(UserList filteredUserList) {
        
        TableColumn<User, Image> pictureColumn = new TableColumn<>("รูปภาพ");
        pictureColumn.setCellValueFactory(cellData -> {
            String imagePath = cellData.getValue().getProfilePicturePath();
            Image image;
            if (imagePath != null) {
                image = new Image("file:" + imagePath, 120, 160, true, true); 
            } else {
                
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

        
        TableColumn<User, String> usernameColumn = new TableColumn<>("ชื่อผู้ใช้ระบบ");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        
        TableColumn<User, String> timeColumn = new TableColumn<>("เวลาที่เข้าใช้ล่าสุด");
        timeColumn.setCellValueFactory(cellData -> {
            User user = cellData.getValue();
            if (user.getLastLogin() != null) {
                return new SimpleStringProperty(user.getLastLogin().toString());
            }
            return new SimpleStringProperty("ไม่เคยเข้าใช้งาน");
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
                            updateButtonStyle(btnSuspend, user); 
                            setGraphic(btnSuspend); 
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

        
        ObservableList<User> observableUserList = FXCollections.observableArrayList(filteredUserList.getAllUsers());

        
        SortedList<User> sortedList = new SortedList<>(observableUserList, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.getLastLogin() == null && u2.getLastLogin() == null) {
                    return 0;
                } else if (u1.getLastLogin() == null) {
                    return 1; 
                } else if (u2.getLastLogin() == null) {
                    return -1; 
                } else {
                    return u2.getLastLogin().compareTo(u1.getLastLogin()); 
                }
            }
        });

        
        userManagementTableView.setItems(sortedList);

        
        userManagementTableView.getSortOrder().clear();
        userManagementTableView.getSortOrder().add(timeColumn);
        timeColumn.setSortType(TableColumn.SortType.DESCENDING);
        userManagementTableView.sort();

        
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
            FXRouter.goTo("admin-settings", "user-management");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

