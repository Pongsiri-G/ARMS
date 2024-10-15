package ku.cs.models;

import java.util.ArrayList;

public class Advisor extends User{
    private Faculty faculty;
    private Department department;
    private String advisorID;
    private String advisorEmail;
    private String defaultPassword;

    public Advisor(String username, String password, String name, Faculty faculty, Department department, String advisorID, String advisorEmail, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        this.advisorEmail = advisorEmail;
        this.defaultPassword = password;
    }

    // ใช้้ไปก่อนเดี๋ยวแก้ที่หลัง
    public Advisor(String username, String password, String name, String faculty, String department, String advisorID, String advisorEmail, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = new Faculty(faculty);
        this.department = new Department(department);
        this.advisorID = advisorID;
        this.advisorEmail = advisorEmail;
        this.defaultPassword = password;
    }

    public Advisor(String name, String username, String password, Faculty faculty, Department department, String advisorID) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        this.defaultPassword = password;
    }

    //ใช้สำหรับอ่านข้อมูลจากไฟล์เก็บเป็น object ชั่วคราวเท่านั้น
    public Advisor(String name) {
        super(null, null, name);
    }

    //เรียกดูรายการคำร้องที่ต้องดำเนินการอาจารย์
    public ArrayList<Request> getRequestsByAdvisor(RequestList requests) {
        ArrayList<Request> advisorRequests = new ArrayList<>();
        for (Request request : requests.getRequests()) {
            for (Student student : this.getDepartment().getStudents()) {
                if (student.getUsername() != null && student.getUsername().equalsIgnoreCase(request.getRequester().getUsername()) && student.getStudentAdvisor() != null) {
                    if (student.getStudentAdvisor().equals(this) && request.getCurrentApprover().equals("อาจารย์ที่ปรึกษา") && request.getStatus().equals("กำลังดำเนินการ")) {
                        advisorRequests.add(request);
                    }
                }
            }
        }
        return advisorRequests;
    }

    public void rejectRequest(Request request, String reason) {
        request.processRequest( "อาจารย์ที่ปรึกษา","ปฏิเสธ", reason);
    }
    public void acceptRequest(Request request) {
        request.processRequest("อาจารย์ที่ปรึกษา", "อนุมัติ", null);
    }

    public String getAdvisorEmail() { return advisorEmail; }


    public String getAdvisorID() {
        return advisorID;
    }

    public String getDefaultPassword() {return defaultPassword;}

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public Faculty getFaculty() {return faculty;}

    public void setAdvisorID(String newAdvisorID) { this.advisorID = newAdvisorID; }

    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public void setDepartment(Department department) { this.department = department; }

    public Department getDepartment () { return department; }


    public String getRole(){
        return "อาจารย์";
    }

    @Override
    public String toString() {
        return "Advisor: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty() + ", Department: " + getDepartment() + ", Advisor ID: " + getAdvisorID();
    }

}
