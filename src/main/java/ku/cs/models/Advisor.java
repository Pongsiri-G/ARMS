package ku.cs.models;

import java.util.ArrayList;

public class Advisor extends User{
    private Faculty faculty;
    private Department department;
    private String advisorID;
    private String advisorEmail;
    private boolean firstLogin;

    public Advisor(String username, String password, String name, Faculty faculty, Department department, String advisorID, String advisorEmail, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        this.advisorEmail = advisorEmail;
        this.firstLogin = true;
    }

    // ใช้้ไปก่อนเดี๋ยวแก้ที่หลัง
    public Advisor(String username, String password, String name, String faculty, String department, String advisorID, String advisorEmail, boolean isHashed, boolean suspended, boolean firstLogin) {
        super(username, password, name, isHashed, suspended);
        this.faculty = new Faculty(faculty);
        this.department = new Department(department);
        this.advisorID = advisorID;
        this.advisorEmail = advisorEmail;
        this.firstLogin = firstLogin;
    }

    public Advisor(String name, String username, String password, Faculty faculty, Department department, String advisorID) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
    }



    //public void setStudents(ArrayList<Student> students) { this.students = students; }

    public String getAdvisorEmail() { return advisorEmail; }


    public String getAdvisorID() {
        return advisorID;
    }

    public String getFaculty() {
        return faculty.getFacultyName();
    }

    public boolean isFirstLogin() { return firstLogin; }

    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }

    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public void setDepartment(Department department) { this.department = department; }

    public String getDepartment () {
        return department.getDepartmentName();
    }


    public String getRole(){
        return "Advisor";
    }

    @Override
    public String toString() {
        return "Advisor: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty() + ", Department: " + getDepartment() + ", Advisor ID: " + getAdvisorID();
    }

}
