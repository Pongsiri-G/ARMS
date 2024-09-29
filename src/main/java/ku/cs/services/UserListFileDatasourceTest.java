package ku.cs.services;

import ku.cs.models.*;
import java.util.*;
import java.io.*;

public class UserListFileDatasourceTest {
    private UserListFileDatasource datasource;
    private String testDirectory = "data/test";
    private String testAdvisorFileName = "advisors.csv";
    private String testStudentFileName = "studentlist.csv";

    public void runTests() {
        setup();
        loadUserList();
    }

    private void setup() {
        datasource = new UserListFileDatasource(testDirectory, testStudentFileName, testAdvisorFileName);
        clearTestFile();
    }

    private void clearTestFile() {
        File file1 = new File(testDirectory + File.separator + testStudentFileName);
        if (file1.exists()) {
            file1.delete();
        }
        File file2 = new File(testDirectory + File.separator + testAdvisorFileName);
        if (file2.exists()) {
            file2.delete();
        }
    }

    public UserList loadUserList() {
        UserList users = new UserList();
        FacultyList facultyList = this.loadFacultyList();
        users.setFaculties(facultyList);
        users.addUser("sample1", "1234", "John Doe", "Science", false, false); //Faculty Officer1
        users.addUser("sample2", "5678", "Jane Doe", "Science", false, false); //Faculty Officer2
        users.addUser("sample3", "1234", "Jim Doe", "Architecture", "Thai Architecture", false, false); //Department Officer1
        users.addUser("sample4", "5678", "Jack Doe", "Architecture", "Urban Architecture", false, false); //Department Officer2
        users.addUser("sample5", "1234", "Jeff Doe", "Science", "Computer Science", "D1411","xxxxxxx@ku.th", false, false); //Advisor1
        users.addUser("sample6", "5678", "Job Doe", "Science", "Math", "D1015", "aaaa@ku.th",  false, false); //Advisor2
        users.addUser("sample7", "1234", "Jerk Doe", "Engineering", "Civil Engineering", "b6620400000", "studentmail1@ku.th", false, false); //Student1
        users.addUser("sample8", "5678", "Josh Doe", "Engineering", "Computer Engineering", "b6620400001", "studentmail2@ku.th", false, false); //Student2
        users.findUserByUsername("sample8").setSuspended(true);
        datasource.writeData(users);
        return users;
    }

    public FacultyList loadFacultyList(){
        FacultyList faculties = new FacultyList();
        Faculty science = new Faculty("Science");
        science.addDepartment("Computer Science");
        science.addDepartment("Math");
        science.addDepartment("Food Science");

        Faculty architecture = new Faculty("Architecture");
        architecture.addDepartment("Thai Architecture");
        architecture.addDepartment("Interior Architecture");
        architecture.addDepartment("Urban Architecture");


        Faculty agriculture = new Faculty("Agriculture");
        agriculture.addDepartment("Agricultural Management");

        Faculty engineering = new Faculty("Engineering");
        engineering.addDepartment("Civil Engineering");
        engineering.addDepartment("Computer Engineering");
        engineering.addDepartment("Electrical Engineering");

        Faculty arts = new Faculty("Arts");
        arts.addDepartment("Graphic Arts");

        Faculty economics = new Faculty("Economics");
        economics.addDepartment("Civil Economics");

        faculties.addFaculty(science);
        faculties.addFaculty(architecture);
        faculties.addFaculty(engineering);
        faculties.addFaculty(arts);
        faculties.addFaculty(economics);
        return faculties;
    }
}
