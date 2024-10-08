package ku.cs.models;

import java.util.ArrayList;

public class FacultyOfficer extends User implements Officer {
    private Faculty faculty;

    // Begin Constructor
    public FacultyOfficer(String username, String password, String name, Faculty faculty, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
    }

    // ใข้ไปก่อนเดี๋ยวแก้ที่หลัง
    public FacultyOfficer(String username, String password, String name, String faculty, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = new Faculty(faculty);
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
        positions.add("คณบดี");
        positions.add("รองคณบดีฝ่ายบริหาร");
        positions.add("รองคณบดีฝ่ายวิชาการ");
        positions.add("รักษาแทนการคณบดี");
        return  positions;
    }

    @Override
    public ArrayList<RequestHandlingOfficer> getRequestManagers() {
        return faculty.getRequestHandlingOfficers();
    }

    //เรียกดูรายการคำร้องที่ต้องดำเนินการของเจ้าหน้าที่คณะ
    public ArrayList<Request> getRequestsByFaculty(RequestList requests) {
        ArrayList<Request> facultyRequests = new ArrayList<>();
        for (Request request : requests.getRequests()) {
            for (Department department : this.getFaculty().getDepartments()) {
                for (Student student : department.getStudents()) {
                    if (student.getUsername().equalsIgnoreCase(request.getRequester()) && request.getCurrentApprover().equals("เจ้าหน้าที่คณะ") && request.getStatus().equals("กำลังดำเนินการ")) {
                        facultyRequests.add(request);
                    }
                }
            }
        }
        return facultyRequests;
    }

    public void rejectRequest(Request request, String approver, String reason) {
        request.processRequest(approver, "ปฏิเสธ", reason);
    }
    public void acceptRequest(Request request, String approver) {
        request.processRequest(approver, "อนุมัติ", null);
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
        return "เจ้าหน้าที่คณะ";
    }

    @Override
    public String toString() {
        return "FacultyOfficer: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName();
    }

}
