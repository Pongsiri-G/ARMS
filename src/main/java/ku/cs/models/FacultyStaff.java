package ku.cs.models;

import java.util.ArrayList;

public class FacultyStaff extends User {
    private Faculty faculty;
    private ArrayList<RequestManager> requestManagers;

    // Begin Constructor
    public FacultyStaff(String username, String password, String name, Faculty faculty) {
        super(username, password, name);
        this.faculty = faculty;
    }
    // End Constructor

    // Begin Handle Request Managers
    public void loadRequestManagers() {
        // Will load DataSource later
        requestManagers = new ArrayList<>();
    }
    public void writeRequestManagers() {
        return;
    }

    public void addRequestManager(String name, String position){
        requestManagers.add(new RequestManager(name, position + faculty.getFacultyName()));
    }
    public void removeRequestManager(RequestManager requestManager){
        requestManagers.remove(requestManager);
    }
    public void changeRequestManagerName(RequestManager requestManager, String name){
        requestManager.setName(name);
    }
    public void changeRequestManagerPosition(RequestManager requestManager, String position){
        requestManager.setPosition(position + faculty.getFacultyName());
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
    public String toString() {
        return name + " " + faculty.toString();
    }
}
