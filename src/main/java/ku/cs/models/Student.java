package ku.cs.models;

public class Student extends User {
    String studentID;
    String email;
    String enrolledFaculty;
    String enrolledDepartment;

    public Student(String username, String password, String name, String faculty, String department, String studentID, String email) {
        super(username, password, name);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public String getStudentID() { return studentID; }
    public String getEmail() { return email; }
    public String getEnrolledFaculty() { return enrolledFaculty; }
    public String getEnrolledDepartment() { return enrolledDepartment; }
}
