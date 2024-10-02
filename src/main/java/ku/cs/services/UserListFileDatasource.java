package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

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
        StudentList studentList = studentDatasource.readData();
        AdvisorList advisorList = advisorDatasource.readData();
        FacultyOfficerList facultyOfficerList = facultyOfficerDatasource.readData();
        DepartmentOfficerList departmentOfficerList = departmentOfficerDatasource.readData();

        users.setFacultyList(facultyList);



        // Add students from studentlist.csv
        for (Student student : studentList.getStudents()) {
            users.addUser(student);
        }

        // Add advisors from advisor.csv
        for (Advisor advisor : advisorList.getAdvisors()) {
            users.addUser(advisor);
        }

        for (FacultyOfficer facultyOfficer : facultyOfficerList.getOfficers()){
            users.addUser(facultyOfficer);
        }

        for (DepartmentOfficer departmentOfficer : departmentOfficerList.getOfficers()){
            users.addUser(departmentOfficer);
        }

        System.out.println("Loaded " + studentList.getStudents().size() + " students");
        System.out.println("Loaded " + advisorList.getAdvisors().size() + " advisors");
        System.out.println("Loaded " + facultyOfficerList.getOfficers().size() + " faculty officers");
        System.out.println("Loaded " + departmentOfficerList.getOfficers().size() + " department officers");

        return users;
    }

    @Override
    public void writeData(UserList users) {
        StudentList studentList = new StudentList();
        AdvisorList advisorList = new AdvisorList();
        DepartmentOfficerList departmentOfficerList = new DepartmentOfficerList();
        FacultyOfficerList facultyOfficerList = new FacultyOfficerList();
        FacultyList facultyList = users.getFacultyList();

        for (User user : users.getAllUsers()) {
            if (user instanceof Student) {
                System.out.println("Adding student to file: " + ((Student) user).getStudentID());
                studentList.addStudent((Student) user);
            } else if (user instanceof Advisor) {
                advisorList.addAdvisor((Advisor) user);
            }
            else if (user instanceof FacultyOfficer) {
                facultyOfficerList.add((FacultyOfficer) user);
            }
            else if (user instanceof DepartmentOfficer) {
                departmentOfficerList.add((DepartmentOfficer) user);
            }
        }

        for (Faculty faculty : users.getFacultyList().getFaculties()){
            for (Department department : faculty.getDepartmentList().getDepartments()) {
                for (Student student : department.getStudentList().getStudents()) {
                    if(student.getUsername() == null && student.getPassword() == null) studentList.addStudent(student);
                }
            }
        }

        facDepDatasource.writeData(facultyList);
        studentDatasource.writeData(studentList);
        advisorDatasource.writeData(advisorList);
        facultyOfficerDatasource.writeData(facultyOfficerList);
        departmentOfficerDatasource.writeData(departmentOfficerList);

    }
}
