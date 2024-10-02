package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    private ArrayList<DepartmentOfficer> departmentOfficers;
    private ArrayList<Advisor> advisors;
    private StudentList students;

    // Begin Constructor
    public Department(String departmentName) {
        this(departmentName, null);
        // Auto Generate departmentID will implement later
        Random rand = new Random();
        this.departmentID = rand.nextInt(90) + 10 + "";
    }

    public Department(String departmentName, String departmentID) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.requestHandlingOfficers = new ArrayList<>();
        this.students = new StudentList();
        this.advisors = new ArrayList<>();
        this.departmentOfficers = new ArrayList<>();
    }

    public boolean isDepartmentName(String departmentName){
        return this.departmentName.equals(departmentName);
    }

    public boolean isDepartmentID(String departmentID){
        return this.departmentID.equals(departmentID);
    }

    public ArrayList<RequestHandlingOfficer> getRequestHandlingOfficers() {
        return this.requestHandlingOfficers;
    }
    public void setRequestManagers(ArrayList<RequestHandlingOfficer> approvers){this.requestHandlingOfficers = approvers;}

//    public void addStudent(Student student) {
//        this.students.add(student);
//    }
//
//    public void removeStudent(Student student) {
//        this.students.remove(student);
//    }

    public StudentList getStudentList() {
        return this.students;
    }

    public Student findStudentByID(String studentID) {
        for (Student student : students.getStudents()) {
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
    public ArrayList<DepartmentOfficer> getDepartmentOfficers() { return departmentOfficers; }
    public ArrayList<Advisor> getAdvisorList() { return advisors; }
    public String getDepartmentName(){
        return this.departmentName;
    }
    public String getDepartmentID(){
        return this.departmentID;
    }
    // End Getter

    public void rejectRequest(Request request, String reason) {
        request.changeStatus("rejected");
        request.setTimeStamp();
    }
    public void acceptRequest(Request request, String reason) {
        request.changeStatus("accepted");
    }

    public void sendRequest(Request request) {
        request.changeStatus("sent");
    }

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
