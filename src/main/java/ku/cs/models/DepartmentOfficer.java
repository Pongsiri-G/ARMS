package ku.cs.models;

import java.util.ArrayList;

public class DepartmentOfficer extends User implements Officer {
    private Faculty faculty;
    private Department department;

    // Begin Constructor
    public DepartmentOfficer(String username, String password, String name, Faculty faculty, Department department, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
        this.department = department;
    }
    // End Constructor

    @Override
    public void loadRequestManage(ArrayList<RequestHandlingOfficer> approvers) {
        department.setRequestManagers(approvers);
    }

    @Override
    public void addRequestManager(String position, String name) {
        department.getRequestHandlingOfficers().add(new RequestHandlingOfficer(department.getDepartmentName(), position, name));
    }

    @Override
    public void removeRequestManager(RequestHandlingOfficer officer) {
        department.getRequestHandlingOfficers().remove(officer);
    }


    @Override
    public void updateRequestManager(RequestHandlingOfficer officer, String position, String name) {
        removeRequestManager(officer);
        addRequestManager(position, name);
    }

    @Override
    public ArrayList<String> getAvailablePositions() {
        ArrayList<String> positions = new ArrayList<>();
        positions.add("หัวหน้าภาควิชา");
        positions.add("รองหัวหน้าภาควิขา");
        positions.add("รักษาการแทนหัวหน้าภาควิชา");
        return  positions;
    }

    @Override
    public ArrayList<RequestHandlingOfficer> getRequestManagers() {
        return faculty.getRequestHandlingOfficers();
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
