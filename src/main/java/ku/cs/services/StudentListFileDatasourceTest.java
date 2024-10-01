package ku.cs.services;

import ku.cs.models.*;
import java.util.*;
import java.io.*;

public class StudentListFileDatasourceTest {
    private StudentListFileDatasource datasource;
    private String testDirectory = "data/test";
    private String testFileName = "studentlist.csv";

    public void runTests() {
        setup();
        loadStudentList();
    }

    private void setup() {
        datasource = new StudentListFileDatasource(testDirectory, testFileName);
        clearTestFile();
    }

    private void clearTestFile() {
        File file = new File(testDirectory + File.separator + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public StudentList loadStudentList() {
        FacultyList facultyList = this.loadFacultyList();

        StudentList students = new StudentList();
        Faculty science = facultyList.findFacultyByName("Science");
        Faculty engineering = facultyList.findFacultyByName("Engineering");

        Department computerScience = science.findDepartmentByName("Computer Science");
        Department math = science.findDepartmentByName("Math");
        Department civilEngineering = engineering.findDepartmentByName("Civil Engineering");
        Department computerEngineering = engineering.findDepartmentByName("Computer Engineering");

        Advisor advisor1 = new Advisor("advisor1", "password1", "Advisor One", science, computerScience, "A101", "xxxxxx@ku.th", false, false);
        Advisor advisor2 = new Advisor("advisor2", "password2", "Advisor Two", science, math, "A102", "xxxxxx@ku.th", false, false);

        // Creating test students
        students.addStudent(new Student("student1", "password1", "John Doe", science, computerScience, "S001", "johndoe@ku.th", false, false));
        students.addStudent(new Student("student2", "password2", "Jane Doe", science, math, "S002", "janedoe@ku.th", false, false));
        students.addStudent(new Student("student3", "password3", "Jim Beam", engineering, civilEngineering, "S003", "jimbeam@ku.th", false, false));
        students.addStudent(new Student("student4", "password4", "Jack Daniels", engineering, computerEngineering, "S004", "jackdaniels@ku.th", false, false));

        // Assign advisors to students
        students.get(0).setStudentAdvisor(advisor1);
        students.get(1).setStudentAdvisor(advisor2);

        datasource.writeData(students);
        return students;
    }

    public FacultyList loadFacultyList() {
        FacultyList faculties = new FacultyList();
        Faculty science = new Faculty("Science");
        science.addDepartment("Computer Science");
        science.addDepartment("Math");

        Faculty engineering = new Faculty("Engineering");
        engineering.addDepartment("Civil Engineering");
        engineering.addDepartment("Computer Engineering");

        faculties.addFaculty(science);
        faculties.addFaculty(engineering);
        return faculties;
    }
}