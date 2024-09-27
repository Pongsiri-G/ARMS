package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private StudentListFileDatasource studentDatasource;
    private AdvOffListFileDatasource advisorDatasource;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserListFileDatasource(String directoryName, String studentListFileName, String advisorListFileName) {
        this.directoryName = directoryName;
        this.studentDatasource = new StudentListFileDatasource(directoryName, studentListFileName);
        this.advisorDatasource = new AdvOffListFileDatasource(directoryName, advisorListFileName);
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public UserList readData() {
        UserList users = new UserList();
        StudentList studentList = studentDatasource.readData();
        AdvisorList advisorList = advisorDatasource.readData();

        // Add students from studentlist.csv
        for (Student student : studentList.getStudents()) {
            users.addUser(student);
        }

        // Add advisors from advisor.csv
        for (Advisor advisor : advisorList.getAdvisors()) {
            users.addUser(advisor);
        }

        return users;
    }

    @Override
    public void writeData(UserList users) {
        StudentList studentList = new StudentList();
        AdvisorList advisorList = new AdvisorList();

        for (User user : users.getAllUsers()) {
            if (user instanceof Student) {
                studentList.addStudent((Student) user);
            } else if (user instanceof Advisor) {
                advisorList.addAdvisor((Advisor) user);
            }
        }

        studentDatasource.writeData(studentList);
        advisorDatasource.writeData(advisorList);
        //NOTE : AdvisorListFile Datasource is not finish now, waiting boom to write...
    }
}
