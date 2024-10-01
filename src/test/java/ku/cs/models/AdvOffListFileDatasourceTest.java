package ku.cs.models;

import ku.cs.models.*;
import ku.cs.services.AdvOffListFileDatasource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdvOffListFileDatasourceTest {
    @Test
    void testReadData() {
        // สร้างแหล่งข้อมูล (datasource)
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource("data", "advisorlist.csv");

        // อ่านข้อมูลจากไฟล์ CSV
        AdvisorList advisors = datasource.readData();

        // ตรวจสอบว่ามีข้อมูลครบ 10 บรรทัด
        assertEquals(10, advisors.getAdvisors().size());

        // ตรวจสอบข้อมูลในบรรทัดแรก (index = 0)
        Advisor advisor = advisors.getAdvisors().get(0);
        assertEquals("Jeff Smith", advisor.getName());
        assertEquals("Engineering", advisor.getFaculty());
        assertEquals("Computer Science", advisor.getDepartment());
        assertEquals("jeff.smith@university.edu", advisor.getAdvisorEmail());
        assertEquals("A001", advisor.getAdvisorID());

        // ตรวจสอบข้อมูลในบรรทัดสุดท้าย (index = 9)
        Advisor lastAdvisor = advisors.getAdvisors().get(9);
        assertEquals("William Garcia", lastAdvisor.getName());
        assertEquals("Science", lastAdvisor.getFaculty());
        assertEquals("Chemistry", lastAdvisor.getDepartment());
        assertEquals("william.garcia@university.edu", lastAdvisor.getAdvisorEmail());
        assertEquals("A010", lastAdvisor.getAdvisorID());
    }
}
