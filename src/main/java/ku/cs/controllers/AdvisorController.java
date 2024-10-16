package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class AdvisorController extends BaseController {
    @FXML private BorderPane rootPane;
    @FXML private TableView<Student> studentListTable;
    @FXML private Label roleLabel;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label departmentLabel;
    @FXML private Label emailLabel;
    @FXML private Label facultyLabel;
    @FXML private Label advisorNameLabel;
    @FXML private TextField searchStudentField;
    @FXML private Circle profilePictureDisplay;
    @FXML private Circle advisorProfilePictureDisplay;

    @FXML
    private TableColumn<Student, String> departmentCol;
    @FXML
    private TableColumn<Student, String> emailCol;
    @FXML
    private TableColumn<Student, String> facultyCol;
    @FXML
    private TableColumn<Student, String> idCol;
    @FXML
    private TableColumn<Student, String> nameCol;

    private ArrayList<Student> studentList;
    private UserList userList;
    private UserListFileDatasource datasources;
    private Advisor advisor;
    ObservableList<Student> studentListObservable;

    @FXML
    public void initialize() {
        datasources = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv", "departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasources.readData();
        // เนื่องจากการส่งข้อมูลข้ามหน้าของเราเป็น การส่ง Username มาก็เลย Cast ให้มันเป็น String เเละหาใน UserList เเล้วให้ return Object นั้นมาเพื่อใช้ในการเเสดงข้อมูลขั้นต่อไป
        User user = userList.findUserByUsername((String) FXRouter.getData());
        showUserInfo(user);

        advisor = (Advisor) user;
        roleLabel.setText("อาจารย์ | ภาควิชา" + advisor.getDepartment().getDepartmentName());
        nameLabel.setText(advisor.getName());
        usernameLabel.setText(advisor.getUsername());
        applyThemeAndFont(rootPane);
        setProfilePicture(profilePictureDisplay, advisor.getProfilePicturePath());
        setProfilePicture(advisorProfilePictureDisplay, advisor.getProfilePicturePath());

        studentList = advisor.getDepartment().findStudentsByAdvisorName(advisor.getName());
        studentListObservable = FXCollections.observableArrayList(studentList);

        showTable(studentList);
        searchStudent();

        studentListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student oldStudent, Student newStudent) {
                try {
                    ArrayList<String> data = new ArrayList<>();
                    data.add(advisor.getUsername());
                    data.add(newStudent.getStudentID());
                    FXRouter.goTo("advisor-nisit", data);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void goToRequestNisit(MouseEvent event) throws IOException {
        FXRouter.goTo("advisor-request-nisit", advisor.getUsername());
    }

    private void showUserInfo(User user) {
        Advisor advisor = (Advisor) user;
        advisorNameLabel.setText("ชื่อ: " +advisor.getName());
        facultyLabel.setText("คณะ: " +advisor.getFaculty().getFacultyName());
        departmentLabel.setText("สาขาวิชา: " + advisor.getDepartment().getDepartmentName());
        emailLabel.setText("อีเมล: " +advisor.getAdvisorEmail());
    }

    private void showTable(ArrayList<Student> students) {
        studentListTable.getItems().clear();

        facultyCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledFaculty().getFacultyName())//ใช้ SimpleStringProperty ในการดึง method ที่่ return เป็น string
        );//ซึ่งตรงกับชนิด dataType ที่กำหนดไว้เพราะ contructor ที่เรารับมานั้น Faculty and Department มันรับเป็น object
        departmentCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledDepartment().getDepartmentName())
        );
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        studentListTable.getColumns().clear();
        studentListTable.getColumns().add(facultyCol);
        studentListTable.getColumns().add(departmentCol);
        studentListTable.getColumns().add(nameCol);
        studentListTable.getColumns().add(idCol);
        studentListTable.getColumns().add(emailCol);
        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        studentListTable.getItems().addAll(students);
    }

    @FXML
    void searchStudent() {
        facultyCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledFaculty().getFacultyName())//ใช้ SimpleStringProperty ในการดึง method ที่่ return เป็น string
        );
        departmentCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledDepartment().getDepartmentName())
        );
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        FilteredList<Student> filteredData = new FilteredList<>(studentListObservable, p -> true);
        searchStudentField.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = (newValue == null) ? "" : newValue.toLowerCase().trim();
            filteredData.setPredicate(student -> {
                if (lowerCaseFilter.isEmpty()) {
                    return true;
                }else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getStudentID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }else
                    return false;
            });
        });
        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(studentListTable.comparatorProperty());
        studentListTable.setItems(sortedData);
    }

    @FXML
    public void settingsPageClick(MouseEvent event) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        data.add("advisor");
        data.add(advisor.getUsername());
        FXRouter.goTo("settings", data);

    }

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }
    }



