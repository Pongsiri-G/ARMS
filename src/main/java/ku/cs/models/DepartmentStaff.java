package ku.cs.models;

import java.util.ArrayList;

public class DepartmentStaff extends User{
    private Faculty faculty;
    private Department department;
    private ArrayList<RequestManager> requestManagers;

    // Begin Constructor
    public DepartmentStaff(String username, String password, String name, Faculty faculty, Department department) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
    }
    // End Constructor

    // Begin handle Request Managers
    public void loadRequestManagers() {
        // Will load DataSource later
        requestManagers = new ArrayList<>();
    }
    public void writeRequestManagers() {
        return;
    }

    public void addRequestManager(String name, String position){
        requestManagers.add(new RequestManager(name, department.getDepartmentName() + position));
    }
    public void removeRequestManager(RequestManager requestManager){
        requestManagers.remove(requestManager);
    }
    public void changeRequestManagerName(RequestManager requestManager, String name){
        requestManager.setName(name);
    }
    public void changeRequestManagerPosition(RequestManager requestManager, String position){
        requestManager.setPosition(department.getDepartmentName() + position);
    }
    public ArrayList<RequestManager> getRequestManagers() {
        return requestManagers;
    }

    // End Handle Request Manager

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
