package ku.cs.models;

import java.util.ArrayList;

public class FacultyOfficer extends User {
    private Faculty faculty;

    // Begin Constructor
    public FacultyOfficer(String username, String password, String name, Faculty faculty) {
        super(username, password, name);
        this.faculty = faculty;
    }
    // End Constructor

    public void addRequestManager(String name, String position) {
        faculty.addRequestHandlingOfficer(new RequestHandlingOfficer(name, position + faculty.getFacultyName()));
    }

    public void removeRequestManager(RequestHandlingOfficer officer) {
        faculty.removeRequestHandlingOfficer(officer);
    }

    public void changeRequestManagerName(RequestHandlingOfficer officer, String name) {
        officer.setName(name);
    }

    public void changeRequestManagerPosition(RequestHandlingOfficer officer, String position) {
        officer.setPosition(position + faculty.getFacultyName());
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
