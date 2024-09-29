package ku.cs.services;

import ku.cs.models.*;

public class AdvOffListFileDatasourceTest {
    public static void main(String[] args) {
        // สร้าง object ของ AdvOffListFileDatasource
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource("data/test", "advisors.csv");

        // สร้าง AdvisorList ใหม่
        AdvisorList advisorList = new AdvisorList();

        // ข้อมูลที่ต้องการเพิ่ม
        String[][] advisorData = {
                {"advisor1", "pass1", "Advisor One", "Engineering", "Computer Science", "A12345", "advisor1@example.com"},
                {"advisor2", "pass2", "Advisor Two", "Science", "Biology", "A12346", "advisor2@example.com"},
                {"advisor3", "pass3", "Advisor Three", "Arts", "History", "A12347", "advisor3@example.com"},
                {"advisor4", "pass4", "Advisor Four", "Engineering", "Mechanical", "A12348", "advisor4@example.com"},
                {"advisor5", "pass5", "Advisor Five", "Science", "Chemistry", "A12349", "advisor5@example.com"}
        };

        // เพิ่ม Advisor ใหม่เข้าไปใน list
        for (String[] data : advisorData) {
            advisorList.addNewAdvisor(data[0], data[1], data[2], data[3], data[4], data[5], data[6], false, false, true);
        }

        // เขียนข้อมูลใหม่ลงในไฟล์ CSV
        datasource.writeData(advisorList);

        // แสดงข้อมูล Advisor หลังจากเพิ่ม
        AdvisorList advisorRead = datasource.readData();
        System.out.println("\nข้อมูล Advisor หลังเพิ่มข้อมูลใหม่:");
        datasource.displayAdvisors(advisorRead);
    }
}


