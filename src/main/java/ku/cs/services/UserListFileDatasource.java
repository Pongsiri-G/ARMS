package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;
    private StudentListFileDatasource studentDatasource;
    private AdvOffListFileDatasource advisorDatasource;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserListFileDatasource(String directoryName, String studentListFileName, String advisorListFileName) {
        this.directoryName = directoryName;
        this.studentDatasource = new StudentListFileDatasource(directoryName, studentListFileName);
        this.advisorDatasource = new AdvOffListFileDatasource(directoryName, advisorListFileName);
        checkFileIsExisted();
    }
    // ping : all user table view
    public UserListFileDatasource(String directoryName, String userListFileName) {
        this.directoryName = directoryName;
        this.fileName = userListFileName;
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

    /*ping
    @Override
    public UserList readData() {
        UserList users = new UserList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream    fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ( (line = buffer.readLine()) != null ){
                if (line.equals("")) continue;

                String[] data = line.split(", ");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String image = data[0].trim();
                String username = data[1].trim();
                String name = data[2].trim();
                String role = data[3].trim();
                String faculty = data[4].trim();
                String department = data[5].trim();
                String timeStamp = data[6].trim();

                // เพิ่มข้อมูลลงใน list
                users.addTableUser(image, username, name, role, faculty, department, timeStamp);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }
     */

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
