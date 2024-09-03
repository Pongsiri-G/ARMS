package ku.cs.models;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

public class Advisor extends User{
    private Faculty faculty;
    private Department department;
    private String advisorID;
    private ArrayList<Student> students;


    public Advisor(String username, String password, String name, Faculty faculty, Department department, String advisorID) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
        this.advisorID = advisorID;
        students = new ArrayList<>(); // will read datasource later

    }

    public void testAddStudents(Student student){
        students.add(student);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
/*
    // เพิ่มนิสิตใหม่เข้าสู่ลิสต์
    public void addNisit(String username, String password, String name, String faculty, String department, String studentID, String email) {
        advisorList.addNisit(username, password, name, faculty, department, studentID, email);
    }

    // รับนิสิตทั้งหมดที่ Advisor ดูแล
    public ArrayList<Student> getAllStudents() {
        return advisorList.getAllStudents();
    }

    // รับนิสิตจาก ID
    public Student getStudentById(String id) {
        return advisorList.getStudentById(id);
    }*/
}
