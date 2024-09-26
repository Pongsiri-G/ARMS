package ku.cs.models;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Advisor extends User{
    private Faculty faculty;
    private Department department;
    private String advisorID;
    private String advisorEmail;
    private User user;
    private ArrayList<Student> students;

    public Advisor(String name, String username, String password, Faculty faculty, Department department, String advisorID) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
    }

    public Advisor(String username, String password, String name, Faculty faculty, Department department, String advisorID, String email, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        this.advisorEmail = email;
        students = new ArrayList<>(); // will read datasource later

    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    //public void setStudents(ArrayList<Student> students) { this.students = students; }

    public String getAdvisorEmail() { return advisorEmail; }


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
