package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;
    private int approvedDepartmentRequest;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    private ArrayList<DepartmentOfficer> departmentOfficers;
    private ArrayList<Advisor> advisors;
    private ArrayList<Student> students;

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
        this.students = new ArrayList<>();
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

    public ArrayList<Student> getStudents() {
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

    public void increaseApprovedDepartmentRequest() {
        this.approvedDepartmentRequest++;
        System.out.println("Approved requests increased for department: " + this.departmentName + ", new count: " + this.approvedDepartmentRequest);
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
    public ArrayList<Advisor> getAdvisors() { return advisors; }
    public String getDepartmentName(){
        return this.departmentName;
    }
    public String getDepartmentID(){
        return this.departmentID;
    }
    public int getApprovedDepartmentRequest() {return approvedDepartmentRequest;}
    // End Getter

    // หาอาจารย์ที่ปรึกษาในนิสิต
    public ArrayList<Student> findStudentsByAdvisorName(String advisorName) {
        ArrayList<Student> studentList = new ArrayList<>();
        for (Student student : students) {
            if (student.getStudentAdvisor() != null && student.getStudentAdvisor().getName().equals(advisorName)) {
                studentList.add(student);
                // ถ้าหากเจอชื่ออาจารย์ใน Student ก็ return Student ออกมาเเล้วไปเลือกเอาจะเลือกอะไรโชว์ Table บ้าง
            }
        }
        return studentList;
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
