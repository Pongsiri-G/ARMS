package ku.cs.models;

import ku.cs.services.AdvOffListFileDatasource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AdvisorListFileDatasourceTest {

     private AdvOffListFileDatasource datasource;
     private String testDirectory = "data/test";
     private String testFileName = "advisor-list.csv";

     @BeforeEach
     public void setup() {
          // ลบไฟล์ทดสอบเดิมก่อนทำการทดสอบใหม่ทุกครั้ง
          File testFile = new File(testDirectory + File.separator + testFileName);
          if (testFile.exists()) {
               testFile.delete();
          }

          // สร้าง datasource เพื่อทดสอบ
          datasource = new AdvOffListFileDatasource(testDirectory, testFileName);
     }

     @Test
     public void testWriteDataAndReadData() {
          // สร้างข้อมูลที่ต้องการทดสอบ
          AdvisorList advisors = new AdvisorList();
          advisors.addNewAdvisor("John Doe", "jdoe", "password123", "Engineering", "Computer Science", "A001");
          advisors.addNewAdvisor("Jane Smith", "jsmith", "password456", "Science", "Biology", "A002");

          // เขียนข้อมูลลงในไฟล์
          datasource.writeData(advisors);

          // อ่านข้อมูลจากไฟล์
          AdvisorList readAdvisors = datasource.readData();

          // ทดสอบว่ามีจำนวนที่ปรึกษาถูกต้อง
          assertEquals(2, readAdvisors.getAdvisors().size());

          // ทดสอบที่ปรึกษาคนแรก
          Advisor advisor1 = readAdvisors.getAdvisors().get(0);
          assertEquals("John Doe", advisor1.getName());
          assertEquals("jdoe", advisor1.getUsername());
          //assertTrue(advisor1.validatePassword("password123"), "Password validation failed for John Doe");
          assertEquals("Engineering", advisor1.getFaculty());
          assertEquals("Computer Science", advisor1.getDepartment());
          assertEquals("A001", advisor1.getAdvisorID());

          // ทดสอบที่ปรึกษาคนที่สอง
          Advisor advisor2 = readAdvisors.getAdvisors().get(1);
          assertEquals("Jane Smith", advisor2.getName());
          assertEquals("jsmith", advisor2.getUsername());
          //assertTrue(advisor2.validatePassword("password456"), "Password validation failed for Jane Smith");
          assertEquals("Science", advisor2.getFaculty());
          assertEquals("Biology", advisor2.getDepartment());
          assertEquals("A002", advisor2.getAdvisorID());
     }
}
