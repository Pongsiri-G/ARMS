package ku.cs.models;

public class Student extends User {
    String studentID;
    String email;
    Faculty enrolledFaculty;
    Department enrolledDepartment;

    public Student(String username, String password, String name, Faculty faculty, Department department, String studentID, String email) {
        super(username, password, name);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    /*
    public createRequest() {
        Request new
    }
    */

    public String getStudentID() { return studentID; }
    public String getEmail() { return email; }
    public String getEnrolledFaculty() { return enrolledFaculty.getFacultyName(); }
    public String getEnrolledDepartment() { return enrolledDepartment.getDepartmentName(); }

    public void register(String name, String studentID, String email, String username, String password, String confirmPassword){}
    public void createRequest(){};
    public void viewMyRequests(){};
}
