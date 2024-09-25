package ku.cs.models;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Advisor extends User{
    private Faculty faculty;
    private Department department;
    private String advisorID;
    private boolean status = false;
    private ArrayList<Student> students;


    public Advisor(String username, String password, String name, Faculty faculty, Department department, String advisorID, boolean isHashed) {
        super(username, password, name, isHashed);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        students = new ArrayList<>(); // will read datasource later

    }
    public Advisor(String username, String password, String name, Faculty faculty, Department department, ArrayList<Student> students, boolean isHashed) {
        super(username, password, name, isHashed);
        this.faculty = faculty;
        this.department = department;
        students = new ArrayList<>();
    }

    public void testAddStudents(Student student){
        students.add(student);
    } //เพิ่มนักเรียน

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) { this.students = students; }

//    public RequestHandlingOfficer fineRequestManager(RequestHandlingOfficer requestManager, Advisor advisor){
//        requestManager = new RequestHandlingOfficer("Sick", "Advisor");
//        if (advisor.getStatus())
//            return requestManager; // เช็คสถานะคำร้องหากเป็น true คือสถานะคำร้องผ่านการอนุมัติถึงจะส่งคำร้องต่อไปได้
//        return null;
//    }

    public boolean getStatus() {return status;}

    public String getAdvisorID() {
        return advisorID;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Department getDepartment () {
        return department;
    }

    public String getRole(){
        return "Advisor";
    }

    @Override
    public String toString() {
        return "Advisor: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName() + ", Department: " + getDepartment().getDepartmentName() + ", Advisor ID: " + getAdvisorID();
    }

}
