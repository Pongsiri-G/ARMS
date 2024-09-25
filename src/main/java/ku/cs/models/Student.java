package ku.cs.models;

public class Student extends User {
    private String studentID;
    private String email;
    private Faculty enrolledFaculty;
    private Department enrolledDepartment;
    private Advisor studentAdvisor;


    public Student(String name, String studentID, String email) {
        super(null, null, name);
        this.studentID = studentID;
        this.email = email;
    }

    public Student(String username, String password, String name, Faculty faculty, Department department, String studentID, String email, boolean isHashed, boolean suspended) {
        super(username, password, name, isHashed, suspended);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public void createRequest() {
        //wait for Request finish
    }

    public void viewMyRequests(){
        //wait for request finish
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


    public String getStudentID() { return studentID; }
    public String getEmail() { return email; }
    public Faculty getEnrolledFaculty() { return enrolledFaculty; }
    public Department getEnrolledDepartment() { return enrolledDepartment; }
    public Advisor getStudentAdvisor() { return studentAdvisor; }

    @Override
    public String getRole(){
        return "Student";
    }

    @Override
    public String toString() {
        return "Student: " + getName() + " (" + getUsername() + "), Faculty: " + getEnrolledFaculty().getFacultyName() + ", Department: " + getEnrolledDepartment().getDepartmentName() + ", Student ID: " + getStudentID() + ", Email: " + getEmail();
    }

}