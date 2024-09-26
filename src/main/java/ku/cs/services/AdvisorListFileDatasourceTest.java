package ku.cs.services;

import ku.cs.models.*;

import java.io.File;

public class AdvisorListFileDatasourceTest {
    private static AdvisorListFileDatasource datasource;
    private static String testDirectory = "data/test";
    private static String testFileName = "advisor-list.csv";

    public static void setup() {
        datasource = new AdvisorListFileDatasource(testDirectory, testFileName);
        clearTestFile();
    }

    private static void clearTestFile() {
        File file = new File(testDirectory + File.separator + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void main(String[] args) {
        setup();

        // สร้างคณะและแผนกเพื่อใช้ในการทดสอบ
        Faculty faculty1 = new Faculty("วิทยาศาสตร์");
        Faculty faculty2 = new Faculty("วิศวกรรมศาสตร์");

        Department department1 = new Department("วิทยาการคอมพิวเตอร์", faculty1);
        Department department2 = new Department("วิศวกรรมคอมพิวเตอร์", faculty2);

        faculty1.addDepartment(department1);
        faculty2.addDepartment(department2);

        // สร้างรายการที่ปรึกษา
        //AdvisorList advisorList = new AdvisorList();
        //advisorList.addAdvisor("jdoe", "password123", "John Doe", true, true, "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "12345", "A001", "jdoe@example.com");
        //advisorList.addAdvisor("jsmith", "pass456", "Jane Smith", true, true, "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "67890", "A002", "jsmith@example.com");

        // ทดสอบการเขียนข้อมูล
        //datasource.writeData(advisorList);

        // ทดสอบการอ่านข้อมูลหลังจากการเขียน
        AdvisorList readAdvisorList = datasource.readData();

        // แสดงผลข้อมูลที่อ่านได้
        System.out.println("ข้อมูลที่อ่านได้:");
        for (Advisor advisor : readAdvisorList.getAdvisors()) {
            System.out.println(advisor.getName() + " " + advisor.getUsername() + " " + advisor.getAdvisorEmail() + " " + advisor.getFaculty() + " " + advisor.getDepartment() + " " + advisor.getAdvisorID());
        }
    }
}
