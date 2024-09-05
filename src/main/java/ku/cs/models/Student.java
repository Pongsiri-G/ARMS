package ku.cs.models;

public class Student extends User {
    String studentID;
    String email;
    Faculty enrolledFaculty;
    Department enrolledDepartment;
    Advisor studentAdvisor;


    public Student(String name, String studentID, String email) {
        super(null, null, name);
        this.studentID = studentID;
        this.email = email;
    }

    public Student(String username, String password, String name, Faculty faculty, Department department, String studentID, String email) {
        super(username, password, name);
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

    public String getStudentID() { return studentID; }
    public String getEmail() { return email; }
    public String getEnrolledFaculty() { return enrolledFaculty.getFacultyName(); }
    public String getEnrolledDepartment() { return enrolledDepartment.getDepartmentName(); }

}