package ku.cs.services;

import ku.cs.models.*;
import java.util.*;
import java.io.*;

public class UserListFileDatasourceTest {
    private UserListFileDatasource datasource;
    private String testDirectory = "data/csv_files";
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
        loadAdmin();
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
        AdvOffListFileDatasource datasource = new AdvOffListFileDatasource("data/csv_files", "advisorlist.csv");
        ArrayList<Advisor> advisorList = new ArrayList<>();
        advisorList.add(new Advisor("Jak", "vXuO637", "พรรษา จักรพันธ์ประดิษฐ์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "D12345", false, false));
        advisorList.add(new Advisor("Tan", "168K5Hl", "ปวิน จันทรเกียรติ", "วิทยาศาสตร์", "เคมี", "D12346", false, false));
        advisorList.add(new Advisor("Pang", "Ux5G63Y", "ปาณิตา พันธ์ภูผา", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "A12347", false, false));
        advisorList.add(new Advisor("Tai", "cN76A0S", "วันชัย เกียรติบวรสกุล", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "A12348", false, false));
        advisorList.add(new Advisor("Tar", "cN76A0S", "เตชิต จุลาอำพันธ์", "เกษตร", "พืชสวน", "D32142", false, false));
        advisorList.add(new Advisor("Prasert", "yHk73Bf", "ประเสริฐ ธรรมเกษม", "เกษตร", "ปฐพีวิทยา", "A12450", false, false));
        advisorList.add(new Advisor("Chalerm", "pT6r9Gj", "เฉลิมชัย จงรักษ์", "บริหารธุรกิจ", "การตลาด", "D45873", false, false));
        advisorList.add(new Advisor("Wichai", "kX6S4Gh", "วิชัย กิติศรี", "บริหารธุรกิจ", "บัญชี", "A12102", false, false));
        advisorList.add(new Advisor("Somchai", "qJ5R2Lz", "สมชาย ศรีสุชาติ", "ประมง", "การจัดการประมง", "D12367", false, false));
        advisorList.add(new Advisor("Anan", "abc123", "อนันต์ สมบัติทวี", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "A12349", false, false));
        /*
        advisorList.add(new Advisor("Ratana", "efg456", "รัตนา ภูริรักษ์", "วิทยาศาสตร์", "เคมี", "B12350", false, false));
        advisorList.add(new Advisor("Sakda", "Jf9G3Vh", "ศักดา ประเสริฐสุข", "วิทยาศาสตร์", "ฟิสิกส์", "A12310", false, false));
        advisorList.add(new Advisor("Surin", "tN83P7v", "สุรินทร์ ธนกิจพงษ์", "วิทยาศาสตร์", "เคมี", "A12311", false, false));
        advisorList.add(new Advisor("Tanasak", "1H82D7a", "ธนศักดิ์ เมธาสิทธิ์", "วิทยาศาสตร์", "ชีววิทยา", "A12312", false, false));
        advisorList.add(new Advisor("Chutima", "2L76Y8t", "จุฑิมา อภิรักษ์ธนากร", "วิทยาศาสตร์", "ชีววิทยา", "A12313", false, false));
        advisorList.add(new Advisor("Nuttaporn", "lH9X7Er", "ณัฐพร สุวรรณทวี", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "A12314", false, false));
        advisorList.add(new Advisor("Jira", "vY6J72r", "จิรา ทองสุข", "วิศวกรรมศาสตร์", "วิศวกรรมเคมี", "A12315", false, false));
        advisorList.add(new Advisor("Chanin", "mT6N2R8", "ชานินทร์ จันทร์แก้ว", "วิทยาศาสตร์", "วิทยาศาสตร์การอาหาร", "A12316", false, false));
        advisorList.add(new Advisor("Sukanya", "pJ8U1Wl", "สุกัญญา นนทศิลป์", "วิศวกรรมศาสตร์", "วิศวกรรมอุตสาหการ", "A12317", false, false));
        advisorList.add(new Advisor("Chakkraphat", "eQ3V7P9", "จักรพรรดิ์ รัตนดิลก", "วิทยาศาสตร์", "ฟิสิกส์", "A12318", false, false));
        advisorList.add(new Advisor("Wirat", "bO2M6Rz", "วิรัตน์ แซ่ลี้", "วิทยาศาสตร์", "เคมี", "A12319", false, false));
        advisorList.add(new Advisor("Pornchai", "vN5S7G6", "พรชัย วัฒนากิจ", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "A12320", false, false));
        advisorList.add(new Advisor("Anucha", "oB6F4J8", "อนุชา สุริยะวงศ์", "วิศวกรรมศาสตร์", "วิศวกรรมเคมี", "A12321", false, false));
        advisorList.add(new Advisor("Ratana", "yD8T5M9", "รัตนา พันธ์งาม", "วิทยาศาสตร์", "ชีววิทยา", "A12322", false, false));
        advisorList.add(new Advisor("Somjit", "xP2L4J7", "สมจิตร ภูมิรัตน์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "A12323", false, false));
        advisorList.add(new Advisor("Chaiyong", "kE6H1G3", "ชัยยงค์ มหาศิริกุล", "วิศวกรรมศาสตร์", "วิศวกรรมเคมี", "A12324", false, false));
        advisorList.add(new Advisor("Prapai", "nF7T8J2", "ประไพ พงษ์ประเสริฐ", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "A12325", false, false));
        advisorList.add(new Advisor("Nopparat", "rU6J3P9", "นพรัตน์ ศิริบูรณ์", "วิทยาศาสตร์", "ฟิสิกส์", "A12326", false, false));
        advisorList.add(new Advisor("Patama", "qT9N2E4", "ปัทมา ภักดีศรี", "วิทยาศาสตร์", "เคมี", "A12327", false, false));
        advisorList.add(new Advisor("Tassanee", "gF2R7M5", "ทัศนีย์ ชูชาติ", "วิทยาศาสตร์", "ชีววิทยา", "A12328", false, false));
        advisorList.add(new Advisor("Sawat", "hB5Y8L1", "สวัสดิ์ เจียรนัย", "วิทยาศาสตร์", "วิทยาศาสตร์การอาหาร", "A12329", false, false));
*/
        datasource.writeData(advisorList);
    }

    public void loadStudent(){
        StudentListFileDatasource datasource = new StudentListFileDatasource("data/csv_files", "studentlist.csv");
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student("อธิฐาน คมปราชญ์","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6620400000", "studentmail1@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("นิชกานต์ ประเสริฐโสม","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6620400001", "studentmail2@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("กรีฑา พิพัฒนกุล","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6625500002", "studentmail3@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("ณัฐวดี ทรัพย์ธารา","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6625500003", "studentmail4@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("ตะวัน พลแสน","วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", "6620400004", "studentmail5@ku.th", new Advisor("พรรษา จักรพันธ์ประดิษฐ์")));
        studentList.add(new Student("นพดล สุขแสง", "วิทยาศาสตร์", "เคมี", "6625500006", "nopadol.s@ku.th", new Advisor("ปวิน จันทรเกียรติ")));
        studentList.add(new Student("พิชญา วงศ์พิบูลย์", "วิทยาศาสตร์", "เคมี", "6620400007", "pichaya.v@ku.th", new Advisor("ปวิน จันทรเกียรติ")));
        studentList.add(new Student("ชาญณรงค์ ชินวงษ์", "วิทยาศาสตร์", "เคมี", "6625500008", "channarong.c@ku.th", new Advisor("ปวิน จันทรเกียรติ")));
        studentList.add(new Student("ศิริวัฒน์ ธนกิตติพงศ์", "วิทยาศาสตร์", "เคมี", "6620400009", "siriwat.t@ku.th", new Advisor("ปวิน จันทรเกียรติ")));
        studentList.add(new Student("รัตพล อินทนี", "วิทยาศาสตร์", "เคมี", "6620400005", "rattaphon@ku.th", new Advisor("ปวิน จันทรเกียรติ")));
        studentList.add(new Student("สมฤทัย ดำรงรักษ์", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "6625500006", "somruthai@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("กัญญารัตน์ โพธิ์ศรี", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "6620400007", "kanyarat@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("ศุภชัย ดำรงค์พงษ์", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "6620400010", "supachai@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("ปรียานันท์ แก้วสุวรรณ", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "6620400011", "preeyanan@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("จิรศักดิ์ ศรีพงศ์", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", "6620400012", "jirasak@ku.th", new Advisor("ปาณิตา พันธ์ภูผา")));
        studentList.add(new Student("สิริกร สมบัติสุข", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "6625500013", "sirikon@ku.th", new Advisor("วันชัย เกียรติบวรสกุล")));
        studentList.add(new Student("นพดล แสงทอง", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "6625500014", "nopadol@ku.th", new Advisor("วันชัย เกียรติบวรสกุล")));
        studentList.add(new Student("ดวงใจ นภาโสภณ", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "6620400015", "duangjai@ku.th", new Advisor("วันชัย เกียรติบวรสกุล")));
        studentList.add(new Student("สมฤทัย พูนสวัสดิ์", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "6620400016", "somruthai@ku.th", new Advisor("วันชัย เกียรติบวรสกุล")));
        studentList.add(new Student("ภคมน อินทราวาส", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", "6625500017", "pakkamon@ku.th", new Advisor("วันชัย เกียรติบวรสกุล")));
        studentList.add(new Student("อนงค์ แสงจันทร์", "เกษตร", "พืชสวน", "6625500018", "anong@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));
        studentList.add(new Student("อภินันท์ ประสงค์วิทยา", "เกษตร", "พืชสวน", "6620400019", "apinan@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));
        studentList.add(new Student("ณัฐวุฒิ พิพัฒนกุล", "เกษตร", "พืชสวน", "6625500020", "natthawut@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));
        studentList.add(new Student("สมปอง ชัยรุ่งเรือง", "เกษตร", "พืชสวน", "6620400021", "sompong@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));
        studentList.add(new Student("เกศรา พงศ์ภักดี", "เกษตร", "พืชสวน", "6620400022", "kasara@ku.th", new Advisor("เตชิต จุลาอำพันธ์")));
        studentList.add(new Student("กิตติพงษ์ ทองไพบูลย์", "เกษตร", "ปฐพีวิทยา", "6625500023", "kittipong@ku.th", new Advisor("ประเสริฐ ธรรมเกษม")));
        studentList.add(new Student("วิชาญ ชลิตศิริ", "เกษตร", "ปฐพีวิทยา", "6625500024", "wichan@ku.th", new Advisor("ประเสริฐ ธรรมเกษม")));
        studentList.add(new Student("จันทร์จิรา สุทธิวรรณ", "เกษตร", "ปฐพีวิทยา", "6620400025", "chanjira@ku.th", new Advisor("ประเสริฐ ธรรมเกษม")));
        studentList.add(new Student("ชาลิสา ปรีชาพงษ์", "เกษตร", "ปฐพีวิทยา", "6625500026", "chalisa@ku.th", new Advisor("ประเสริฐ ธรรมเกษม")));
        studentList.add(new Student("กมล สาครวิทยา", "เกษตร", "ปฐพีวิทยา", "6625500027", "kamon@ku.th", new Advisor("ประเสริฐ ธรรมเกษม")));
        studentList.add(new Student("ณัฐฐา กิจวัฒนา", "บริหารธุรกิจ", "การตลาด", "6620400028", "nattha@ku.th", new Advisor("เฉลิมชัย จงรักษ์")));
        studentList.add(new Student("สรวิชญ์ เพชรวิจิตร", "บริหารธุรกิจ", "การตลาด", "6625500029", "sarawit@ku.th", new Advisor("เฉลิมชัย จงรักษ์")));
        studentList.add(new Student("ภาสกร พัฒนศักดิ์", "บริหารธุรกิจ", "การตลาด", "6620400021", "phasakorn@ku.th", new Advisor("เฉลิมชัย จงรักษ์")));
        studentList.add(new Student("กฤษณา วิจิตร", "บริหารธุรกิจ", "การตลาด", "6620400022", "krisana@ku.th", new Advisor("เฉลิมชัย จงรักษ์")));
        studentList.add(new Student("นันทวัฒน์ ทองคำ", "บริหารธุรกิจ", "การตลาด", "6620400023", "nantawat@ku.th", new Advisor("เฉลิมชัย จงรักษ์")));
        studentList.add(new Student("ดารินทร์ พิพัฒน์", "บริหารธุรกิจ", "บัญชี", "6620400024", "darinn@ku.th", new Advisor("วิชัย กิติศรี")));
        studentList.add(new Student("ศุภกิตต์ มั่นคง", "บริหารธุรกิจ", "บัญชี", "6625500025", "supakit@ku.th", new Advisor("วิชัย กิติศรี")));
        studentList.add(new Student("กิตติยา รัตนชัย", "บริหารธุรกิจ", "บัญชี", "6625500026", "kittiya@ku.th", new Advisor("วิชัย กิติศรี")));
        studentList.add(new Student("ปวริศา ชัยมงคล", "บริหารธุรกิจ", "บัญชี", "6620400027", "pawarisa@ku.th", new Advisor("วิชัย กิติศรี")));
        studentList.add(new Student("ปฏิพัทธ์ ศรีสุข", "บริหารธุรกิจ", "บัญชี", "6620400028", "patipat@ku.th", new Advisor("วิชัย กิติศรี")));
        studentList.add(new Student("นิรชา ศิริวัฒน์", "ประมง", "การจัดการประมง", "6620400029", "niracha@ku.th", new Advisor("สมชาย ศรีสุชาติ")));
        studentList.add(new Student("ปรางทิพย์ ยอดรัก", "ประมง", "การจัดการประมง", "6620400030", "prangthip@ku.th", new Advisor("สมชาย ศรีสุชาติ")));
        studentList.add(new Student("ณัฐชยา ทิพยรัตน์", "ประมง", "การจัดการประมง", "6625500031", "natchaya@ku.th", new Advisor("สมชาย ศรีสุชาติ")));
        studentList.add(new Student("ธีรภัทร์ กุลสวัสดิ์", "ประมง", "การจัดการประมง", "6625500032", "teerapat@ku.th", new Advisor("สมชาย ศรีสุชาติ")));
        studentList.add(new Student("มณีรัตน์ ลักษณ์ชัย", "ประมง", "การจัดการประมง", "6620400033", "maneerat@ku.th", new Advisor("สมชาย ศรีสุชาติ")));
        studentList.add(new Student("รณชัย อภิวัฒนกุล", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6620400034", "ronachai@ku.th", new Advisor("อนันต์ สมบัติทวี")));
        studentList.add(new Student("อภิวัฒน์ ศักดิ์สิทธิ์", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6620400035", "apiwat@ku.th", new Advisor("อนันต์ สมบัติทวี")));
        studentList.add(new Student("อรนุช ชัยวัฒน์", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6620400036", "ornnuch@ku.th", new Advisor("อนันต์ สมบัติทวี")));
        studentList.add(new Student("ศศิวิมล สุขเกษม", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6625500037", "sasiwimon@ku.th", new Advisor("อนันต์ สมบัติทวี")));
        studentList.add(new Student("ชยพล ธีรธรรม", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6625500038", "chayapol@ku.th", new Advisor("อนันต์ สมบัติทวี")));
        studentList.add(new Student("อรอุมา แก้วประเสริฐ", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", "6620400039", "onuma@ku.th", new Advisor("อนันต์ สมบัติทวี")));


        datasource.writeData(studentList);
    }

    public void loadDepartmentOfficer(){
        DepartmentOfficerListFileDatasource datasource = new DepartmentOfficerListFileDatasource("data/csv_files", "departmentofficerlist.csv");
        ArrayList<DepartmentOfficer> officers = new ArrayList<>();
        officers.add(new DepartmentOfficer("Rain", "cN76A0S", "อนันกร ปิติโอภาสพงศ์", "วิทยาศาสตร์", "วิทยาการคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("Patrick", "fORP833", "จักร จันทรเกียรติ", "วิทยาศาสตร์", "ฟิสิกส์", false, false));
        officers.add(new DepartmentOfficer("Pi", "2Ug6V7v", "กุลนิภา เมธากิจขจร", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("Tuptim", "5E2r4px", "รัญชน์ นิธิธรรมรงค์", "วิศวกรรมศาสตร์", "วิศวกรรมคอมพิวเตอร์", false, false));
        officers.add(new DepartmentOfficer("First", "5E2r4px", "เฟสเอง ไงไง", "วิทยาศาสตร์", "จุลชีววิทยา", false, false));
        officers.add(new DepartmentOfficer("Siriwat", "YjR43Ff", "ศิริวัฒน์ วัฒนกุล", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", false, false));
        officers.add(new DepartmentOfficer("Krit", "zPdA123", "กฤษฎา แก้วทวี", "วิทยาศาสตร์", "เคมี", false, false));
        officers.add(new DepartmentOfficer("Somboon", "Tp6D7Qm", "สมบูรณ์ อธิรักษ์", "วิทยาศาสตร์", "ชีววิทยา", false, false));
        officers.add(new DepartmentOfficer("Chai", "abc987", "ชัยชนะ จันทร์นิ่ม", "วิศวกรรมศาสตร์", "วิศวกรรมไฟฟ้า", false, false));
        officers.add(new DepartmentOfficer("Somchai", "def654", "สมชาย เกียรติศักดิ์", "วิทยาศาสตร์", "ฟิสิกส์", false, false));
        officers.add(new DepartmentOfficer("Sunee", "ghi321", "สุนีย์ วิชัย", "วิทยาศาสตร์", "เคมี", false, false));
        officers.add(new DepartmentOfficer("Somchit", "gJ5K4T9", "สมจิตร สุขเกษม", "เกษตร", "พืชสวน", false, false));
        officers.add(new DepartmentOfficer("Nirut", "sV8R3L6", "นิรุต พิพัฒนาธรรม", "ประมง", "การจัดการประมง", false, false));
        officers.add(new DepartmentOfficer("Warunee", "oL6P9G4", "วารุณี ทองคำ", "เกษตร", "พืชสวน", false, false));
        officers.add(new DepartmentOfficer("Nittaya", "fP7D5N8", "นิตยา สมบัติ", "เกษตร", "โรคพืช", false, false));
        officers.add(new DepartmentOfficer("Supot", "kS3G2F7", "สุพจน์ วงศ์ชัย", "เกษตร", "ปฐพีวิทยา", false, false));
        officers.add(new DepartmentOfficer("Anan", "rK6T8L4", "อนันต์ มหาชัย", "เกษตร", "ปฐพีวิทยา", false, false));
        officers.add(new DepartmentOfficer("Ratchanee", "vM9F3P5", "รัชนี สงวนทรัพย์", "สถาปัตยกรรมศาสตร์", "วิชานวัตกรรมอาคาร", false, false));
        officers.add(new DepartmentOfficer("Siriwan", "pJ5T9K6", "สิริวรรณ ทองพูล", "สถาปัตยกรรมศาสตร์", "ภูมิสถาปัตยกรรม", false, false));
        officers.add(new DepartmentOfficer("Chinnakorn", "bR8N6G3", "ชินกร สวัสดิ์ชัย", "สถาปัตยกรรมศาสตร์", "ภูมิสถาปัตยกรรม", false, false));
        officers.add(new DepartmentOfficer("Watcharapong", "nV4F7D2", "วัชรพงษ์ อินทวิเชียร", "ประมง", "ชีววิทยาประมง", false, false));
        officers.add(new DepartmentOfficer("Wichian", "sK9T6R3", "วิเชียร รุ่งเรือง", "สถาปัตยกรรมศาสตร์", "วิชานวัตกรรมอาคาร", false, false));
        officers.add(new DepartmentOfficer("Somchai", "oF6L9J5", "สมชาย ศรีสุข", "สถาปัตยกรรมศาสตร์", "สถาปัตยกรรม", false, false));
        officers.add(new DepartmentOfficer("Kittipong", "fR2M8P4", "กิตติพงษ์ อิศรานนท์", "ประมง", "วิทยาศาสตร์ทางทะเล", false, false));
        officers.add(new DepartmentOfficer("Rattanaporn", "kT3G5N7", "รัตนพร จงเจริญ", "บริหารธุรกิจ", "การจัดการ", false, false));
        officers.add(new DepartmentOfficer("Somsri", "rV7P9J6", "สมศรี วงศ์วาน", "บริหารธุรกิจ", "การตลาด", false, false));
        officers.add(new DepartmentOfficer("Amorn", "vN4T6K2", "อมร ศรีวรพงษ์", "ประมง", "เพาะเลี้ยงสัตว์น้ำ", false, false));
        officers.add(new DepartmentOfficer("Yuwadee", "pG3M7F8", "ยุวดี อินทรวิจิตร", "บริหารธุรกิจ", "บัญชี", false, false));
        officers.add(new DepartmentOfficer("Akkharadet", "bF9K2T3", "อัครเดช สงวนศรี", "ประมง", "ผลิตภัณฑ์ประมง", false, false));



                datasource.writeData(officers);
    }

    public void loadFacultyOfficer(){
        FacultyOfficerListFileDatasource datasource = new FacultyOfficerListFileDatasource("data/csv_files", "facultyofficerlist.csv");
        ArrayList<FacultyOfficer> officers = new ArrayList<>();
        officers.add(new FacultyOfficer("Farm", "91gw9Wf", "โถมนะ สุพรรณภาคิน", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Tame", "48eEO6X", "ปวัตร เจริญผลวัฒนา", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Earth", "8Y65Dbt", "ธนภัทร อุดมเดชรักษา", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("At", "7V1v37r", "ตุลย์ กิตติชัยยากร", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Anucha", "tH7F5Dv", "อนุชา สุริยา", "วิทยาศาสตร์", false, false));
        officers.add(new FacultyOfficer("Pisut", "rL9X2Kd", "พิสุทธิ์ เกริกไกร", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Chai", "jM6Y3Lf", "ชัยชนะ จันทร์นิ่ม", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("John", "password123", "จอห์น สมิธ", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Jane", "password456", "เจน สมิธ", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Somsak", "password789", "สมศักดิ์ ใจดี", "วิศวกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Somsak", "bK9N8F7", "สมศักดิ์ กิตติรัตน", "เกษตร", false, false));
        officers.add(new FacultyOfficer("Pradit", "rH6F3T9", "ประดิษฐ์ สุขเกษม", "เกษตร", false, false));
        officers.add(new FacultyOfficer("Suriya", "vL3G9K4", "สุริยา เมฆทอง", "เกษตร", false, false));
        officers.add(new FacultyOfficer("Piyaporn", "fT9H2D6", "ปิยพร พรหมศร", "เกษตร", false, false));
        officers.add(new FacultyOfficer("Jiraphat", "kR7M5T8", "จิรภัทร จงเจริญกุล", "เกษตร", false, false));
        officers.add(new FacultyOfficer("Kirati", "pL4S8R9", "กีรติ แสงจันทร์", "บริหารธุรกิจ", false, false));
        officers.add(new FacultyOfficer("Narong", "nE6T4F3", "ณรงค์ แสงชัย", "บริหารธุรกิจ", false, false));
        officers.add(new FacultyOfficer("Thitipong", "uP3R9J7", "ฐิติพงศ์ ธนสุวรรณ", "บริหารธุรกิจ", false, false));
        officers.add(new FacultyOfficer("Wanida", "mV6B5L2", "วนิดา กาญจนพร", "บริหารธุรกิจ", false, false));
        officers.add(new FacultyOfficer("Nathapon", "sD7K2T5", "ณัฐพล เจริญสุข", "บริหารธุรกิจ", false, false));
        officers.add(new FacultyOfficer("Sutthipong", "gR9N6F4", "สุทธิพงศ์ ศรีประเสริฐ", "ประมง", false, false));
        officers.add(new FacultyOfficer("Wirawan", "oP6L9S3", "วิราวรรณ ธนาคาร", "ประมง", false, false));
        officers.add(new FacultyOfficer("Nuchanart", "xV3T2M5", "นุชนาถ วงศ์เจริญ", "ประมง", false, false));
        officers.add(new FacultyOfficer("Phonchai", "tD5G8P7", "พลชัย สุขสมบัติ", "ประมง", false, false));
        officers.add(new FacultyOfficer("Sakorn", "bK4F9J2", "สาคร พูลสุข", "ประมง", false, false));
        officers.add(new FacultyOfficer("Suchada", "rM9L7D6", "สุชาดา รัตนาวดี", "สถาปัตยกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Chariya", "vP6T2F8", "จริยา ศรีสุข", "สถาปัตยกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Somrak", "fN3G9K7", "สมรักษ์ วงศ์สุวรรณ", "สถาปัตยกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Thawatchai", "kL8S5P2", "ธวัชชัย จงเจริญ", "สถาปัตยกรรมศาสตร์", false, false));
        officers.add(new FacultyOfficer("Suchat", "pT9F6D3", "สุชาติ ประเสริฐวงศ์", "สถาปัตยกรรมศาสตร์", false, false));


        datasource.writeData(officers);
    }


    public void loadFacultyList(){
        FacDepListFileDatascource datasource = new FacDepListFileDatascource("data/csv_files", "facdeplist.csv");
        FacultyList facultyList = new FacultyList();
        // เพิ่มคณะ
        Faculty faculty1 = new Faculty("วิทยาศาสตร์", "01");
        Faculty faculty2 = new Faculty("วิศวกรรมศาสตร์", "02");
        Faculty faculty3 = new Faculty("เกษตร", "03");
        Faculty faculty4 = new Faculty("บริหารธุรกิจ", "04");
        Faculty faculty5 = new Faculty("ประมง", "05");
        Faculty faculty6 = new Faculty("สถาปัตยกรรมศาสตร์", "06");
        // เพิ่มสาขา
        Department department1 = new Department("วิทยาการคอมพิวเตอร์", "011");
        Department department3 = new Department("เคมี", "012");
        Department department6 = new Department("ฟิสิกส์", "013");
        Department department7 = new Department("ชีววิทยา", "014");
        Department department20 = new Department("จุลชีววิทยา", "015");
        Department department2 = new Department("วิศวกรรมคอมพิวเตอร์", "021");
        Department department8 = new Department("วิศวกรรมเครื่องกล", "022");
        Department department9 = new Department("วิศวกรรมไฟฟ้า", "023");
        Department department4 = new Department("พืชสวน", "031");
        Department department10 = new Department("โรคพืช", "032");
        Department department11 = new Department("ปฐพีวิทยา", "033");
        Department department5 = new Department("การตลาด", "041");
        Department department12 = new Department("การจัดการ", "042");
        Department department13 = new Department("บัญชี", "043");
        Department department14 = new Department("การจัดการประมง", "051");
        Department department15 = new Department("ชีววิทยาประมง", "052");
        Department department16 = new Department("ผลิตภัณฑ์ประมง", "053");
        Department department21 = new Department("เพาะเลี้ยงสัตว์น้ำ", "054");
        Department department22 = new Department("วิทยาศาสตร์ทางทะเล", "055");
        Department department17 = new Department("สถาปัตยกรรม", "061");
        Department department18 = new Department("ภูมิสถาปัตยกรรม", "062");
        Department department19 = new Department("นวัตกรรมอาคาร", "063");


        faculty1.addDepartment(department1);
        faculty2.addDepartment(department2);
        faculty1.addDepartment(department3);
        faculty3.addDepartment(department4);
        faculty4.addDepartment(department5);
        faculty3.addDepartment(department10);
        faculty3.addDepartment(department11);
        faculty4.addDepartment(department12);
        faculty4.addDepartment(department13);
        faculty2.addDepartment(department8);
        faculty2.addDepartment(department9);
        faculty1.addDepartment(department6);
        faculty1.addDepartment(department7);
        faculty5.addDepartment(department14);
        faculty5.addDepartment(department15);
        faculty5.addDepartment(department16);
        faculty6.addDepartment(department17);
        faculty6.addDepartment(department18);
        faculty6.addDepartment(department19);
        faculty1.addDepartment(department20);
        faculty5.addDepartment(department21);
        faculty5.addDepartment(department22);

        facultyList.addFaculty(faculty1);
        facultyList.addFaculty(faculty2);
        facultyList.addFaculty(faculty3);
        facultyList.addFaculty(faculty4);
        facultyList.addFaculty(faculty5);
        facultyList.addFaculty(faculty6);

        datasource.writeData(facultyList);
    }

    public void loadAdmin() {
        AdminPasswordFileDataSource adminPasswordFileDataSource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        Admin admin = new Admin("1111", false, false);
        adminPasswordFileDataSource.writeData(admin);
    }
}
