package ku.cs.models;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Advisor extends User{
    private String faculty;
    private String department;
    private String advisorID;
    private String facultyAdvisor;
    private String departmentAdvisor; //
    private boolean status = false;
    private ArrayList<Student> students;

    public Advisor(String username, String password, String name, String faculty, String department, String advisorID) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        students = new ArrayList<>(); // will read datasource later

    }

    public Advisor(String username, String password, String name, String facultyAdvisor, String departmentAdvisor, ArrayList<Student> students) {
        super(username, password, name);
        this.facultyAdvisor = facultyAdvisor;
        this.departmentAdvisor = departmentAdvisor;
        students = new ArrayList<>();
    }

    public void testAddStudents(Student student){
        students.add(student);
    } //เพิ่มนักเรียน

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) { this.students = students; }

    public Student checkStudentByDepartment(Student student, String department){ //Table เเสดงรายชื่อนิสิตในที่ปรึกษาหาก สาขาเเละคณะตรงกันก็จะส่งค่าเเสดง
        for  (Student stud : students){
            if (student.getEnrolledDepartment().equals(department) && student.getEnrolledDepartment().equals(faculty)){
                return stud;
            }
        }
        return null;
    }

    public boolean getStatus() {return status;}

    public RequestHandlingOfficer fineRequestManager(RequestHandlingOfficer requestManager, Advisor advisor){
        requestManager = new RequestHandlingOfficer("Sick", "Advisor");
        if (advisor.getStatus())
            return requestManager; // เช็คสถานะคำร้องหากเป็น true คือสถานะคำร้องผ่านการอนุมัติถึงจะส่งคำร้องต่อไปได้
        return null;
    }

}
