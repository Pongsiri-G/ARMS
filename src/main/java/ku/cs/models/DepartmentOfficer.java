package ku.cs.models;

import java.util.ArrayList;

public class DepartmentOfficer extends User{
    private Faculty faculty;
    private Department department;
    private UserList student;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;

    // Begin Constructor
    public DepartmentOfficer(String username, String password, String name, Faculty faculty, Department department) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
    }
    // End Constructor

    // Begin handle Request Managers
    public void loadRequestManagers() {
        // Will load DataSource later
        requestHandlingOfficers = new ArrayList<>();
    }
    public void writeRequestManagers() {
        return;
    }

    public void addRequestManager(String name, String position){
        requestHandlingOfficers.add(new RequestHandlingOfficer(name, department.getDepartmentName() + position));
    }
    public void removeRequestManager(RequestHandlingOfficer requestHandlingOfficer){
        requestHandlingOfficers.remove(requestHandlingOfficer);
    }
    public void changeRequestManagerName(RequestHandlingOfficer requestHandlingOfficer, String name){
        requestHandlingOfficer.setName(name);
    }
    public void changeRequestManagerPosition(RequestHandlingOfficer requestHandlingOfficer, String position){
        requestHandlingOfficer.setPosition(department.getDepartmentName() + position);
    }
    public ArrayList<RequestHandlingOfficer> getRequestManagers() {
        return requestHandlingOfficers;
    }

    // End Handle Request Manager

    public void addStudent(String name, String email){

        student.addUser(null, null, name, email, faculty.getFacultyName(), department.getDepartmentName());
    }

    @Override
    public Faculty getFaculty() {
        return faculty;
    }
    @Override
    public Department getDepartment() {
        return department;
    }
    @Override
    public String toString() {
        return name + " " + faculty.toString() + " " + department.toString();
    }
}
