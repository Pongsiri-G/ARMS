package ku.cs.models;

import java.util.ArrayList;

public class Student extends User {
    private String studentID;
    private String email;
    private Faculty enrolledFaculty;
    private Department enrolledDepartment;
    private Advisor studentAdvisor;
    public String advisorName;
    
    public Student(String name,  Faculty faculty, Department department, String studentID, String email) {
        super(null, null, name);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public Student(String username, String password, String name, Faculty faculty, Department department, String studentID, String email, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public void createRequest(RequestList requestList, Request newRequest) throws NullPointerException {
        if (this.getStudentAdvisor() == null){
            throw new NullPointerException("ไม่สามารถสร้างคำร้องได้\nเนื่องจากคุณยังไม่มีอาจารย์ที่ปรึกษา");
        }
        requestList.addRequest(newRequest);
    }

    public ArrayList<Request> getRequestsByStudent(RequestList requests) {
        ArrayList<Request> studentRequests = new ArrayList<>();
        for (Request request : requests.getRequests()) {
            if (this.getUsername() != null && this.getUsername().equalsIgnoreCase(request.getRequester().getUsername())) {
                studentRequests.add(request);
            }
        }
        return studentRequests;
    }

    public int getStudentPendingRequestCount(ArrayList<Request> requests){
        int count = 0;
        for (Request request : requests) {
            if (request.getStatus().equals("กำลังดำเนินการ")) {
                count++;
            }
        }
        return count;
    }

    public int getStudentRejectedRequestCount(ArrayList<Request> requests){
        int count = 0;
        for (Request request : requests) {
            if (request.getStatus().equals("ปฏิเสธ")) {
                count++;
            }
        }
        return count;
    }

    public int getStudentApprovedRequestCount(ArrayList<Request> requests){
        int count = 0;
        for (Request request : requests) {
            if (request.getStatus().equals("เสร็จสิ้น")) {
                count++;
            }
        }
        return count;
    }
    
    public Student(String name, String faculty, String department, String studentID, String email) {
        this(name,new Faculty(faculty),new Department(department),studentID,email);
    }
    
    public Student(String name, String faculty, String department, String studentID, String email, Advisor studentAdvisor) {
        this(name,new Faculty(faculty),new Department(department),studentID,email);
        this.studentAdvisor = studentAdvisor;
    }

    public void setStudentAdvisor(Advisor studentAdvisor) {
        this.studentAdvisor = studentAdvisor;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setEnrolledFaculty(Faculty faculty) { this.enrolledFaculty = faculty; }
    public void setEnrolledDepartment(Department department) { this.enrolledDepartment = department; }


    public String getStudentID() { return studentID; }
    public String getEmail() { return email; }
    public Faculty getEnrolledFaculty() { return enrolledFaculty; }
    public Department getEnrolledDepartment() { return enrolledDepartment; }
    public Advisor getStudentAdvisor() { return studentAdvisor; }
    public String getAdvisorName() { return advisorName; }

    @Override
    public String getRole(){
        return "นิสิต";
    }

    @Override
    public String toString() {
        return "Student: " + getName() + " (" + getUsername() + "), Faculty: " + getEnrolledFaculty().getFacultyName() + ", Department: " + getEnrolledDepartment().getDepartmentName() + ", Student ID: " + getStudentID() + ", Email: " + getEmail() + "AdvisorName : " + getAdvisorName();
    }

}