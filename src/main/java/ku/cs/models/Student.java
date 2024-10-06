package ku.cs.models;

public class Student extends User {
    private String studentID;
    private String email;
    private Faculty enrolledFaculty;
    private Department enrolledDepartment;
    private Advisor studentAdvisor;
    // test หรือควรเก็บ AdvisorName เป็น String เเล้วเอาชชื่อไปหาด้วย method findUserByUseranme ใน userList จะได้ object ของ Advisor มาเพราะพอเก็บเป็น Object พอกุปลี่ยนรหัสผ่านเเล้วข้อมูลในไฟล์มันหาย
    public String advisorName;


    //Add New Student (No Username and password
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

    public Student(Faculty faculty, Department department, String name, String studentID, String email) {
        super(null, null, name);
        this.studentID = studentID;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
        this.email = email;
    }

    // เดี๋ยวแก้ที่หลังใช้ไปก่อน
    public Student(String name, String faculty, String department, String studentID, String email) {
        this(name,new Faculty(faculty),new Department(department),studentID,email);
    }

    public Student(String name, String faculty, String department, String studentID, String email, Advisor studentAdvisor) {
        this(name,new Faculty(faculty),new Department(department),studentID,email);
        this.studentAdvisor = studentAdvisor;
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
        return "Student";
    }

    @Override
    public String toString() {
        return "Student: " + getName() + " (" + getUsername() + "), Faculty: " + getEnrolledFaculty().getFacultyName() + ", Department: " + getEnrolledDepartment().getDepartmentName() + ", Student ID: " + getStudentID() + ", Email: " + getEmail() + "AdvisorName : " + getAdvisorName();
    }

}