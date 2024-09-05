package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.models.*;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdvisorController{
    @FXML private TableView<Student> studentTable;
    private Advisor advisor;
    private FacultyList data;
    private Datasource<Advisor> datasource;

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

    public void readData() {
        data = new FacultyList();
        data.addFaculty("Science");
        data.getFaculties().getFirst().addDepartment("Computer");
    }


    @FXML
    public void initialize() {
        readData();
        advisor = new Advisor("user1", "1234", "Boom", "Science", "Computer", "D14");
        advisor.testAddStudents(new Student("student1", "1234", "temp01", "Science", "Computer", "001", "temp01@email"));
        advisor.testAddStudents(new Student("student2", "1234", "temp02", "Science", "Computer", "002", "temp02@email"));
        advisor.testAddStudents(new Student("student3", "1234", "temp003", "Science", "Computer", "003", "temp03@email"));
        ;
        //datasource = (Datasource<Advisor>) new Advisor("66104022xxxx", "Jason Army", "SCI", "CS"); // ไม่ใช่ๆ
        showTable();

        studentTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observableValue, Student student, Student t1) {
                try {
                    FXRouter.goTo("advisor-nisit");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void showTable() {
        studentTable.getItems().clear();
        studentTable.getItems().addAll(advisor.getStudents());
    }

    private void showTable(ArrayList<Student> students) {
        // กำหนด column ให้มี title ว่า ID และใช้ค่าจาก attribute id ของ object Student
        TableColumn<Student, String> idColumn = new TableColumn<>("Student ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // กำหนด column ให้มี title ว่า Name และใช้ค่าจาก attribute name ของ object Student
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // กำหนด column ให้มี title ว่า Score และใช้ค่าจาก attribute score ของ object Student
        TableColumn<Student, String> factulyColumn = new TableColumn<>("Score");
        factulyColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));

        TableColumn<Student, String> departmentColumn = new TableColumn<>("Score");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // ล้าง column เดิมทั้งหมดที่มีอยู่ใน table แล้วเพิ่ม column ใหม่
        //studentTable.getColumns().clear();
        studentTable.getColumns().add(idColumn);
        studentTable.getColumns().add(nameColumn);
        studentTable.getColumns().add(factulyColumn);
        studentTable.getColumns().add(departmentColumn);

        //studentTable.getItems().clear();

        // ใส่ข้อมูล Student ทั้งหมดจาก studentList ไปแสดงใน TableView
        for (Student student: students) {
            studentTable.getItems().add(student);
        }
    }


}
