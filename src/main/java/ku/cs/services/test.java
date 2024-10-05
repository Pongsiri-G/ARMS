package ku.cs.services;

import ku.cs.models.*;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        String testDirectory = "data/test";
        String testAdvisorFileName = "advisorlist.csv";
        String testStudentFileName = "studentlist.csv";
        String testFacultyOfficerFileName = "facultyofficerlist.csv";
        String testDepartmentFileName = "departmentofficerlist.csv";
        String testFacDepFileName = "facdeplist.csv";
        UserListFileDatasource datasource = new UserListFileDatasource(testDirectory, testStudentFileName, testAdvisorFileName, testFacultyOfficerFileName, testDepartmentFileName, testFacDepFileName);
        FacultyOfficerListFileDatasource facOffDatasource = new FacultyOfficerListFileDatasource("data/test", "facultyofficerlist.csv");
        UserList userList = datasource.readData();
        ArrayList<FacultyOfficer> facOffList = facOffDatasource.readData();
        System.out.println("UserList");
        for (User user : userList.getAllUsers()) {
            System.out.println(user);
        }
        FacultyList  facList =  userList.getFacultyList();
        for (Faculty faculty : facList.getFaculties()){
            System.out.println(faculty);
            for (Department department : faculty.getDepartments()){
                System.out.println(department);
            }
            System.out.println("-----------------");
        }
        ArrayList<Student> students = facList.getFaculties().getFirst().getDepartments().getFirst().getStudents();
        for (Student student : students){
            System.out.println(student);
        }
        students.add(new Student("test", facList.getFaculties().getFirst(), facList.getFaculties().getFirst().getDepartments().getFirst(), "1234", "sdaf"));
        for (Student student : students){
            System.out.println(student);
        }

        System.out.println("------------------------------------------------------");

        datasource.writeData(userList);
        userList = datasource.readData();
        facOffList = facOffDatasource.readData();
        System.out.println("UserList");
        for (User user : userList.getAllUsers()) {
            System.out.println(user);
        }
        facList =  userList.getFacultyList();
        for (Faculty faculty : facList.getFaculties()){
            System.out.println(faculty);
            for (Department department : faculty.getDepartments()){
                System.out.println(department);
            }
            System.out.println("-----------------");
        }
        students = facList.getFaculties().getFirst().getDepartments().getFirst().getStudents();
        for (Student student : students){
            System.out.println(student);
        }
        students.add(new Student("test", facList.getFaculties().getFirst(), facList.getFaculties().getFirst().getDepartments().getFirst(), "1234", "sdaf"));
        for (Student student : students){
            System.out.println(student);
        }

    }
}
