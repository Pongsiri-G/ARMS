package ku.cs.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class AdvisorController{
    @FXML private TableView<Student> studentListTable;
    @FXML private Label departmentLabel;
    @FXML private Label emailLabel;
    @FXML private Label facultyLabel;
    @FXML private Label nameLabel;

    private ArrayList<Student> students;
    private Datasource<ArrayList<Student>> datasource;
    private User user;

    @FXML
    protected void onButtonToAdvisor() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onButtonTBackAdvisor() {
        try {
            FXRouter.goTo("advisor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void initialize() {

        Advisor advisor = (Advisor) FXRouter.getData();
        showUserInfo(advisor);
        datasource = new StudentListFileDatasource("data/test", "studentlist.csv");
        students = datasource.readData();
        showTable(students);

        studentListTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student oldStudent, Student newStudent) {
                try {
                    FXRouter.goTo("advisor-nisit", newStudent.getStudentID());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showUserInfo(Advisor advisor) {
        nameLabel.setText(advisor.getName());
        facultyLabel.setText(advisor.getFaculty());
        departmentLabel.setText(advisor.getDepartment());
        emailLabel.setText(advisor.getAdvisorEmail());
    }
    /*
    private void showTable(StudentList students) {
        studentListTable.getItems().clear();
        studentListTable.getItems().addAll(students.getStudents());
    }*/

    private void showTable(ArrayList<Student> students) {
        TableColumn<Student, String> facultyCol = new TableColumn<>("คณะ");
        facultyCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledFaculty().getFacultyName())//ใช้ SimpleStringProperty ในการดึง method ที่่ return เป็น string
        );//ซึ่งตรงกับชนิด dataType ที่กำหนดไว้เพราะ contructor ที่เรารับมานั้น Faculty and Department มันรับเป็น object

        TableColumn<Student, String> departmentCol = new TableColumn<>("สาขา");
        departmentCol.setCellValueFactory(student ->
                new SimpleStringProperty(student.getValue().getEnrolledDepartment().getDepartmentName())
        );

        TableColumn<Student, String> nameCol = new TableColumn<>("ชื่อ-นามสกุล");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> idCol = new TableColumn<>("รหัสนิสิต");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentID"));

        TableColumn<Student, String> emailCol = new TableColumn<>("อีเมล");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        studentListTable.getColumns().clear();
        studentListTable.getColumns().add(facultyCol);
        studentListTable.getColumns().add(departmentCol);
        studentListTable.getColumns().add(nameCol);
        studentListTable.getColumns().add(idCol);
        studentListTable.getColumns().add(emailCol);

        //studentListTable.getItems().addAll(students.getStudents());



        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Student student: students) {
            studentListTable.getItems().add(student);
        }
    }
    }



