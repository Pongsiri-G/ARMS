package ku.cs.models;

import java.util.ArrayList;

public class FacultyOfficer extends User implements Officer {
    private Faculty faculty;

    // Begin Constructor
    public FacultyOfficer(String username, String password, String name, Faculty faculty, boolean isHashed) {
        super(username, password, name, isHashed);
        this.faculty = faculty;
    }
    // End Constructor

    @Override
    public void addRequestManager(String name, String position) {
        faculty.addRequestHandlingOfficer(new RequestHandlingOfficer(this.faculty.getFacultyName(), name, position));
    }

    @Override
    public void removeRequestManager(RequestHandlingOfficer officer) {
        faculty.removeRequestHandlingOfficer(officer);
    }


    @Override
    public void changeRequestManager(RequestHandlingOfficer officer, String name, String position) {
        removeRequestManager(officer);
        addRequestManager(name, position);
    }

    @Override
    public ArrayList<String> getAvailablePositions() {
        ArrayList<String> positions = new ArrayList<>();
        positions.add("คณบดีคณะ");
        positions.add("รองคณบดีฝ่ายบริหารคณะ");
        positions.add("รองคณบดีฝ่ายวิชาการรคณะ");
        positions.add("รักษาแทนการคณบดี");
        return  positions;
    }



    // End Handle Request Manager
    public Faculty getFaculty() {
        return faculty;
    }

    @Override
    public String getRole(){
        return "FacultyOfficer";
    }

    @Override
    public String toString() {
        return "FacultyOfficer: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName();
    }

}
