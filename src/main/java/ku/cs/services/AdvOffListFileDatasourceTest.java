/*package ku.cs.services;

import ku.cs.models.AdvisorList;
import ku.cs.models.Advisor;
import java.io.File;

public class AdvOffListFileDatasourceTest {

    public static void main(String[] args) {
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource("data/test", "advisors.csv");

        // สร้าง AdvisorList พร้อมข้อมูลใหม่
        AdvisorList advisorList = new AdvisorList();
        advisorList.addNewAdvisor("advisor1", "pass1", "Advisor One", "Engineering", "Computer Science", "A12345", "advisor1@example.com", false, false, true);
        advisorList.addNewAdvisor("advisor2", "pass2", "Advisor Two", "Science", "Biology", "A12346", "advisor2@example.com", false, false, true);
        advisorList.addNewAdvisor("advisor3", "pass3", "Advisor Three", "Arts", "History", "A12347", "advisor3@example.com", false, false, true);
        advisorList.addNewAdvisor("advisor4", "pass4", "Advisor Four", "Com", "Comscience", "A12348", "advisor4@example.com", false, false, true);

        // เขียนข้อมูลลงไฟล์ CSV
        datasource.writeData(advisorList);

        // อ่านข้อมูลกลับจากไฟล์ CSV
        AdvisorList advisorListFromFile = datasource.readData();

        // แสดงข้อมูลที่อ่านจากไฟล์
        System.out.println("ข้อมูลที่อ่านจากไฟล์:");
        datasource.displayAdvisors(advisorListFromFile);

        // ตรวจสอบการสร้างไฟล์
        File file = new File("data/test", "advisors.csv");
        if (file.exists()) {
            System.out.println("\nไฟล์ถูกสร้างแล้ว: " + file.getAbsolutePath());
        } else {
            System.out.println("\nการสร้างไฟล์ล้มเหลว!");
        }


    }
}*/
