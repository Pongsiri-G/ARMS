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

    // ใข้ไปก่อนเดี๋ยวแก้ที่หลัง
    public DepartmentOfficer(String username, String password, String name, String faculty, String department, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.faculty = new Faculty(faculty);
        this.department = new Department(department);
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
        return department.getRequestHandlingOfficers();
    }

    //เรียกดูรายการคำร้องที่ต้องดำเนินการของเจ้าหน้าภาควิชา
    public ArrayList<Request> getRequestsByDepartment(RequestList requests) {
        ArrayList<Request> departmentRequests = new ArrayList<>();
        for (Request request : requests.getRequests()) {
            for (Student student : this.getDepartment().getStudents()) {
                if (student.getUsername().equalsIgnoreCase(request.getRequester().getUsername()) && request.getCurrentApprover().equals("เจ้าหน้าที่ภาควิชา") && request.getStatus().equals("กำลังดำเนินการ")) {
                    departmentRequests.add(request);
                }
            }
        }
        return departmentRequests;
    }

    public void rejectRequest(Request request, String approver, String reason) {
        request.processRequest(approver, "ปฏิเสธ", reason);
    }
    public void acceptRequest(Request request, String approver) {
        request.processRequest(approver, "อนุมัติ", null);
    }
    public void finishRequest(Request request, String approver) {
        request.processRequest(approver,"สิ้นสุด", null);
    }

    //Handle Student
    public void addStudentToDep(String name, String studentID, String email) {
        Student student = new Student(name, this.getFaculty(), this.getDepartment(), studentID, email);
        department.getStudents().add(student);
    }

    public void addStudentToDep(String name, String studentID, String email, String advisor) {
        Student student = new Student(name, faculty.getFacultyName(), department.getDepartmentName(), studentID, email, department.findAdvisorByName(advisor));
        department.getStudents().add(student);
    }

    public void removeStudentInDep(Student student) {
        if (student != null) {
            department.getStudents().remove(student);
        }
    }


    public void assignAdvisor(Student student, Advisor advisor) {
        student.setStudentAdvisor(advisor);
    }


    public void assignAdvisor(Student student, String Advisor){
        assignAdvisor(student, this.department.findAdvisorByName(Advisor));
    }

    public void setFaculty(Faculty faculty){
        this.faculty = faculty;
    }
    public void setDepartment(Department department){
        this.department = department;
    }
    public Faculty getFaculty() {
        return faculty;
    }
    public Department getDepartment() {
        return department;
    }

    @Override
    public String getRole(){
        return "เจ้าหน้าที่ภาควิชา";
    }
    @Override
    public String toString() {
        return "DepartmentOfficer: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName() + ", Department: " + getDepartment().getDepartmentName();
    }
}
