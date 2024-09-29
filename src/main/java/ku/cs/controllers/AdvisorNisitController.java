package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
import ku.cs.models.User;
import ku.cs.services.Datasource;
import ku.cs.services.FXRouter;
import ku.cs.services.StudentListFileDatasource;

import java.io.IOException;

public class AdvisorNisitController {

    @FXML private Label departmentLabel;
    @FXML private Label facultyLabel;
    @FXML private Label idLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private TableView<?> studentTable;

    private Datasource<StudentList> datasource;
    private StudentList studentList;
    private Student student;

    @FXML
    public void initialize() {
        datasource = new StudentListFileDatasource("data/test", "student-list.csv");
        studentList = datasource.readData();

        // รับข้อมูล studentId จากหน้าอื่น ผ่าน method FXRouter.getData()
        // โดยจำเป็นต้อง casting data type ให้ตรงกับหน้าที่ส่งข้อมูล
        String studentId = (String) FXRouter.getData();
        student = studentList.findStudentById(studentId);

        showStudent(student);

    }

    private void showStudent(Student student) {
        nameLabel.setText(student.getName());
        idLabel.setText(student.getStudentID());
        facultyLabel.setText(student.getEnrolledFaculty().getFacultyName());
        departmentLabel.setText(student.getEnrolledDepartment().getDepartmentName());
        emailLabel.setText(student.getEmail());
    }


    @FXML
    void onButtonTBackAdvisor(ActionEvent event) {
        try {
            FXRouter.goTo("advisor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}



