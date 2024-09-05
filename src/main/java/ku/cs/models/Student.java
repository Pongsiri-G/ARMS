package ku.cs.models;

import javax.print.attribute.standard.RequestingUserName;

public class Student extends User {
    String studentID;
    String email;
    Faculty enrolledFaculty;
    Department enrolledDepartment;
    Advisor studentAdvisor;

    public Student(String name, String studentID, String email, Faculty faculty, Department department) {
        super(name);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public Student(String name, String studentID, String email, Advisor advisor, Faculty faculty, Department department) {
        super(name);
        this.studentID = studentID;
        this.email = email;
        this.studentAdvisor = advisor;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }

    public Student(String username, String password, String name, Faculty faculty, Department department, String studentID, String email) {
        super(username, password, name);
        this.studentID = studentID;
        this.email = email;
        this.enrolledFaculty = faculty;
        this.enrolledDepartment = department;
    }
    // ทำมาให้หน่อย 26.ระบบของนิสิต > การลงทะเบียนเข้าใช้งาน
    /*
    public setRegister() {
        เซ็ตค่าต่างๆ จาก Register ใน UserList
    }
    */

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
