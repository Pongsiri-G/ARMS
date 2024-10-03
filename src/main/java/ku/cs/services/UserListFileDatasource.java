package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;
    private StudentListFileDatasource studentDatasource;
    private AdvOffListFileDatasource advisorDatasource;
    private FacultyOfficerListFileDatasource facultyOfficerDatasource;
    private DepartmentOfficerListFileDatasource departmentOfficerDatasource;
    private FacDepListFileDatascource facDepDatasource;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserListFileDatasource(String directoryName, String studentListFileName, String advisorListFileName, String facultyOfficerListFileName, String departmentOfficerListFileName, String facDepListFileName) {
        this.directoryName = directoryName;
        this.facDepDatasource = new FacDepListFileDatascource(directoryName, facDepListFileName);
        this.studentDatasource = new StudentListFileDatasource(directoryName, studentListFileName);
        this.advisorDatasource = new AdvOffListFileDatasource(directoryName, advisorListFileName);
        this.facultyOfficerDatasource = new FacultyOfficerListFileDatasource(directoryName, facultyOfficerListFileName);
        this.departmentOfficerDatasource = new DepartmentOfficerListFileDatasource(directoryName, departmentOfficerListFileName);
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
        FacultyList facultyList = facDepDatasource.readData();
        ArrayList<Student> studentList = studentDatasource.readData();
        ArrayList<Advisor> advisorList = advisorDatasource.readData();
        ArrayList<FacultyOfficer> facultyOfficers = facultyOfficerDatasource.readData();
        ArrayList<DepartmentOfficer> departmentOfficers = departmentOfficerDatasource.readData();

        users.setFacultyList(facultyList);



        // Add students from studentlist.csv
        for (Student student : studentList) {
            users.addUser(student);
        }

        // Add advisors from advisor.csv
        for (Advisor advisor : advisorList) {
            users.addUser(advisor);
        }

        for (FacultyOfficer facultyOfficer : facultyOfficers){
            users.addUser(facultyOfficer);
        }

        for (DepartmentOfficer departmentOfficer : departmentOfficers){
            users.addUser(departmentOfficer);
        }

        System.out.println("Loaded " + studentList.size() + " students");
        System.out.println("Loaded " + advisorList.size() + " advisors");
        System.out.println("Loaded " + facultyOfficers.size() + " faculty officers");
        System.out.println("Loaded " + departmentOfficers.size() + " department officers");

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
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<Advisor> advisorList = new ArrayList<>();
        ArrayList<DepartmentOfficer> departmentOfficers = new ArrayList<>();
        ArrayList<FacultyOfficer> facultyOfficers = new ArrayList<>();
        FacultyList facultyList = users.getFacultyList();

        for (User user : users.getAllUsers()) {
            if (user instanceof Student) {
                System.out.println("Adding student to file: " + ((Student) user).getStudentID());
                studentList.add((Student) user);
            } else if (user instanceof Advisor) {
                advisorList.add((Advisor) user);
            }
            else if (user instanceof FacultyOfficer) {
                facultyOfficers.add((FacultyOfficer) user);
            }
            else if (user instanceof DepartmentOfficer) {
                departmentOfficers.add((DepartmentOfficer) user);
            }
        }

        for (Faculty faculty : users.getFacultyList().getFaculties()){
            for (Department department : faculty.getDepartments()) {
                for (Student student : department.getStudents()) {
                    if(student.getUsername() == null && student.getPassword() == null) studentList.add(student);
                }
            }
        }

        facDepDatasource.writeData(facultyList);
        studentDatasource.writeData(studentList);
        advisorDatasource.writeData(advisorList);
        facultyOfficerDatasource.writeData(facultyOfficers);
        departmentOfficerDatasource.writeData(departmentOfficers);

    }
}
