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
        ArrayList<Advisor> advisorList = advisorDatasource.readData();
        ArrayList<Student> studentList = studentDatasource.readData();
        ArrayList<FacultyOfficer> facultyOfficers = facultyOfficerDatasource.readData();
        ArrayList<DepartmentOfficer> departmentOfficers = departmentOfficerDatasource.readData();

        users.setFacultyList(facultyList);

        // Add advisors from advisor.csv
        for (Advisor advisor : advisorList) {
            users.addUser(advisor);
        }

        // Add students from studentlist.csv
        for (Student student : studentList) {
            users.addUser(student);
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
