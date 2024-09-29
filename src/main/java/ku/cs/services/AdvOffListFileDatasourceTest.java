package ku.cs.services;

import ku.cs.models.AdvisorList;
import ku.cs.models.Advisor;

import java.io.File;

public class AdvOffListFileDatasourceTest {
    public static void main(String[] args) {
        // กำหนดเส้นทางไปยังโฟลเดอร์ที่ใช้เก็บไฟล์
        String directoryName = "data"; // เปลี่ยนเป็นเส้นทางที่ต้องการ
        String advisorListFileName = "advisors.csv";

        // สร้าง instance ของ AdvOffListFileDatasource
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource(directoryName, advisorListFileName);

        // สร้าง AdvisorList ตัวอย่างเพื่อทดสอบการเขียน
        AdvisorList advisorsToWrite = new AdvisorList();
        advisorsToWrite.addNewAdvisor("John Doe", "Computer Science", "Software Engineering", "john@example.com", "ID123");
        advisorsToWrite.addNewAdvisor("Jane Smith", "Mathematics", "Statistics", "jane@example.com", "ID456");

        // เขียนข้อมูลลงไฟล์
        datasource.writeData(advisorsToWrite);

        // อ่านข้อมูลจากไฟล์
        AdvisorList advisorsRead = datasource.readData();
        datasource.displayAdvisors(advisorsRead);
    }
}
