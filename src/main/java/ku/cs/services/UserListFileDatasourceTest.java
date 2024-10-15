package ku.cs.services;

import ku.cs.models.*;
import java.util.*;
import java.io.*;

public class UserListFileDatasourceTest {
    private UserListFileDatasource datasource;
    private String testDirectory = "data/test";
    private String testAdvisorFileName = "advisorlist.csv";
    private String testStudentFileName = "studentlist.csv";
    private String testFacultyOfficerFileName = "facultyofficerlist.csv";
    private String testDepartmentFileName = "departmentofficerlist.csv";
    private String testFacDepFileName = "facdeplist.csv";
    public static void main(String[] args) {
        UserListFileDatasourceTest test = new UserListFileDatasourceTest();
        test.runTests();
    }

    public void runTests() {
        setup(); //โปรดระวัง ถ้าใช้ runtest ไฟล์จะกลับเป็นค่าเริ่มต้น
        loadFacultyList();
        loadStudent();
        loadFacultyOfficer();
        loadAdvisorList();
        loadDepartmentOfficer();
    }

    private void setup() {
        datasource = new UserListFileDatasource(testDirectory, testStudentFileName, testAdvisorFileName, testFacultyOfficerFileName, testDepartmentFileName, testFacDepFileName);
        clearTestFile();
    }

    private void clearTestFile() {
        File file1 = new File(testDirectory + File.separator + testStudentFileName);
        if (file1.exists()) {
            file1.delete();
        }
        File file2 = new File(testDirectory + File.separator + testAdvisorFileName);
        if (file2.exists()) {
            file2.delete();
        }
    }

    public UserList loadUserList() {
        UserList users = new UserList();
        datasource.writeData(users);
        return users;
    }

    public void loadAdvisorList(){
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource("data/test", "advisorlist.csv");
        ArrayList<Advisor> advisorList = new ArrayList<>();
        advisorList.add(new Advisor("Jak", "vXuO637", "พรรษา จักรพันธ์ประดิษฐ์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "D12345", "advisor1@example.com", false, false));
        advisorList.add(new Advisor("Tan", "168K5Hl", "ปวิน จันทรเกียรติ", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "D12346", "advisor2@example.com", false, false));
        advisorList.add(new Advisor("Pang", "Ux5G63Y", "ปาณิตา พันธ์ภูผา", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "A12347", "advisor3@example.com", false, false));
        advisorList.add(new Advisor("Tai", "cN76A0S", "วันชัย เกียรติบวรสกุล", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "A12348", "advisor4@example.com", false, false));
        advisorList.add(new Advisor("Tar", "cN76A0S", "เตชิต จุลาอำพันธ์", "วิทยาศาสตร์", "เคมี", "D32142", "advisor5@example.com", false, false));


        datasource.writeData(advisorList);
    }

    public void loadStudent(){
        StudentListFileDatasource datasource = new StudentListFileDatasource("data/test", "studentlist.csv");
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student("อธิฐาน คมปราชญ์","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "b6620400000", "studentmail1@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("นิชกานต์ ประเสริฐโสม","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "b6620400001", "studentmail2@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("กรีฑา พิพัฒนกุล","วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "b6625500002", "studentmail3@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("ณัฐวดี ทรัพย์ธารา","วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "b6625500003", "studentmail4@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("ตะวัน พลแสน","วิทยาศาสตร์", "เคมี", "b6620400004", "studentmail5@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));


        datasource.writeData(studentList);
    }

    public void loadDepartmentOfficer(){
        DepartmentOfficerListFileDatasource datasource = new DepartmentOfficerListFileDatasource("data/test", "departmentofficerlist.csv");
        ArrayList<DepartmentOfficer> officers = new ArrayList<>();
        officers.add(new DepartmentOfficer("Rain", "cN76A0S", "อนันกร ปิติโอภาสพงศ์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("Patrick", "fORP833", "จักร จันทรเกียรติ", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("Pi", "2Ug6V7v", "กุลนิภา เมธากิจขจร", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("Tuptim", "5E2r4px", "รัญชน์ นิธิธรรมรงค์", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("First", "5E2r4px", "เฟสเอง ไงไง", "วิทยาศาสตร์", "เคมี", false, false));

        datasource.writeData(officers);
    }

    public void loadFacultyOfficer(){
        FacultyOfficerListFileDatasource datasource = new FacultyOfficerListFileDatasource("data/test", "facultyofficerlist.csv");
        ArrayList<FacultyOfficer> officers = new ArrayList<>();
        officers.add(new FacultyOfficer("Farm", "91gw9Wf", "โถมนะ สุพรรณภาคิน", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Tame", "48eEO6X", "ปวัตร เจริญผลวัฒนา", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Earth", "8Y65Dbt", "ธนภัทร อุดมเดชรักษา", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("At", "7V1v37r", "ตุลย์ กิตติชัยยากร", "วิศวกรรมศาสตร์", false, false));

        datasource.writeData(officers);
    }


    public void loadFacultyList(){
        FacDepListFileDatascource datasource = new FacDepListFileDatascource("data/test", "facdeplist.csv");
        FacultyList facultyList = new FacultyList();
        // เพิ่มคณะ
        Faculty faculty1 = new Faculty("วิทยาศาสตร์", "01");
        Faculty faculty2 = new Faculty("วิศวกรรมศาสตร์", "02");
        // เพิ่มสาขา
        Department department1 = new Department("วิทยาการคอมพิวเตอร์", "011");
        Department department3 = new Department("เคมี", "012");
        Department department2 = new Department("วิศวกรรมคอมพิวเตอร์", "022");

        faculty1.addDepartment(department1);
        faculty2.addDepartment(department2);
        faculty1.addDepartment(department3);

        facultyList.addFaculty(faculty1);
        facultyList.addFaculty(faculty2);

        datasource.writeData(facultyList);
    }
}
