package ku.cs.models;

import java.util.ArrayList;

public class FacultyOfficer extends User implements Officer {
    private Faculty faculty;

    // Begin Constructor
    public FacultyOfficer(String username, String password, String name, Faculty faculty, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
    }
    // End Constructor


    @Override
    public void loadRequestManage(ArrayList<RequestHandlingOfficer> approvers) {
        faculty.setRequestManagers(approvers);
    }

    @Override
    public void addRequestManager(String position, String name) {
        faculty.getRequestHandlingOfficers().add(new RequestHandlingOfficer(faculty.getFacultyName(), position, name));
    }

    @Override
    public void removeRequestManager(RequestHandlingOfficer officer) {
        faculty.getRequestHandlingOfficers().remove(officer);
    }


    @Override
    public void updateRequestManager(RequestHandlingOfficer officer, String position, String name) {
        removeRequestManager(officer);
        addRequestManager(position, name);
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

    @Override
    public ArrayList<RequestHandlingOfficer> getRequestManagers() {
        return faculty.getRequestHandlingOfficers();
    }

    public void rejectRequest(Request request, String reason, String approver) {
        request.setApproveName(approver);
        request.changeStatus("rejected");
        request.setTimeStamp();
    }
    public void acceptRequest(Request request,String approver) {
        request.setApproveName(approver);
        request.changeStatus("accepted");
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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
