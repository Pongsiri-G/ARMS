package ku.cs.models;

import java.util.ArrayList;

public class DepartmentOfficer extends User implements Officer {
    private Faculty faculty;
    private Department department;

    // Begin Constructor
    public DepartmentOfficer(String username, String password, String name, Faculty faculty, Department department, boolean isHashed) {
        super(username, password, name, isHashed);
        this.faculty = faculty;
        this.department = department;
    }
    // End Constructor

    @Override
    public void addRequestManager(String name, String position) {
        department.addRequestHandlingOfficer(new RequestHandlingOfficer(name, position + department.getDepartmentName()));
    }

    @Override
    public void removeRequestManager(RequestHandlingOfficer officer) {
        department.removeRequestHandlingOfficer(officer);
    }


    @Override
    public void changeRequestManager(RequestHandlingOfficer officer, String name, String position) {
        removeRequestManager(officer);
        addRequestManager(name, position);
    }
    @Override
    public ArrayList<String> getAvailablePositions() {
        ArrayList<String> positions = new ArrayList<>();
        positions.add("หัวหน้าภาควิชา");
        positions.add("รองหัวหน้าภาควิขา");
        positions.add("รักษาการแทนหัวหน้าภาควิชา");
        return  positions;
    }


    // Handle Student
    public void addStudent(String name, String studentID, String email) {
        Student student = new Student(name, studentID, email);
        department.addStudent(student);
    }

    public void removeStudent(String studentID) {
        Student student = department.findStudentByID(studentID);
        if (student != null) {
            department.removeStudent(student);
        }
    }


    public void assignAdvisor(Student student, Advisor advisor) {
        student.setStudentAdvisor(advisor);
    }

    public Faculty getFaculty() {
        return faculty;
    }
    public Department getDepartment() {
        return department;
    }
    @Override
    public String getRole(){
        return "DepartmentOfficer";
    }
    @Override
    public String toString() {
        return "DepartmentOfficer: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName() + ", Department: " + getDepartment().getDepartmentName();
    }
}
