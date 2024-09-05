package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    private ArrayList<Student> studentList; // student database

    // Begin Constructor
    Department(String departmentName){
        // Auto Generate departmentID will implement later
        Random rand = new Random();
        this.departmentName = departmentName;
        this.departmentID = rand.nextInt(90) + 10 + "";
        this.requestHandlingOfficers = new ArrayList<>();
    }

    Department(String departmentName, String departmentID) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.requestHandlingOfficers = new ArrayList<>();
    }
    // End Constructor

    public boolean isDepartmentName(String departmentName){
        return this.departmentName.equals(departmentName);
    }

    public boolean isDepartmentID(String departmentID){
        return this.departmentID.equals(departmentID);
    }

    // Methods to handle RequestHandlingOfficers
    public void addRequestHandlingOfficer(RequestHandlingOfficer officer) {
        this.requestHandlingOfficers.add(officer);
    }

    public void removeRequestHandlingOfficer(RequestHandlingOfficer officer) {
        this.requestHandlingOfficers.remove(officer);
    }

    public ArrayList<RequestHandlingOfficer> getRequestHandlingOfficers() {
        return this.requestHandlingOfficers;
    }

    public void addStudent(Student student) {
        this.studentList.add(student);
    }

    public void removeStudent(Student student) {
        this.studentList.remove(student);
    }

    public ArrayList<Student> getStudentList() {
        return this.studentList;
    }

    public Student findStudentByID(String studentID) {
        for (Student student : studentList) {
            if (student.getStudentID().equals(studentID)) {
                return student;
            }
        }
        return null;
    }


    // Begin Setter
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }
    public void setDepartmentID(String departmentID){
        this.departmentID = departmentID;
    }
    // End Setter

    // Begin Getter
    public String getDepartmentName(){
        return this.departmentName;
    }
    public String getDepartmentID(){
        return this.departmentID;
    }
    // End Getter

    @Override
    public String toString(){
        return departmentName + "\t" + departmentID;
    }



}
