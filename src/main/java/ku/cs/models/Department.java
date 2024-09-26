package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;
    private Faculty faculty;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    private ArrayList<Advisor> advisors;
    private ArrayList<Student> students;

    // Begin Constructor
    public Department(String departmentName) {
        this(departmentName, (Faculty) null);
    }

    public Department(String departmentName, Faculty faculty) {
        this(departmentName, null, faculty);
        // Auto Generate departmentID will implement later
        Random rand = new Random();
        this.departmentID = rand.nextInt(90) + 10 + "";
    }

    public Department(String departmentName, String departmentID, Faculty faculty) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.faculty = faculty;
        this.requestHandlingOfficers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public Department(String departmentName, String departmentID) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
    } // ping : เพิ่ม constructor
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
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    public ArrayList<Student> getStudentList() {
        return this.students;
    }

    public Student findStudentByID(String studentID) {
        for (Student student : students) {
            if (student.getStudentID().equals(studentID)) {
                return student;
            }
        }
        return null;
    }


    // Begin Setter
    public void setFaculty(Faculty faculty) {this.faculty = faculty;}
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }
    public void setDepartmentID(String departmentID){
        this.departmentID = departmentID;
    }
    // End Setter

    // Begin Getter
    public Faculty getFaculty() { return faculty; }
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


    public Advisor findAdvisorByName(String advisorName) {
        for (Advisor advisor : advisors) {
            if (advisor.getName().equals(advisorName)) {
                return advisor;
            }
        }
        return null;
    }
}
