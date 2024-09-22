package ku.cs.services;

import ku.cs.models.Department;
import ku.cs.models.DepartmentList;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;

import java.io.File;

public class FacDepListFileDatasourceTest {
    private static FacDepListFileDatascource datasource;
    private static String testDirectory = "data/test";
    private static String testFileName = "facdep.csv";

    public static void setup(){
        datasource = new FacDepListFileDatascource(testDirectory, testFileName);
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
        FacultyList facultyList = new FacultyList();
        // เพิ่มคณะ
        Faculty faculty1 = new Faculty("วิทยาศาสตร์", "01");
        Faculty faculty2 = new Faculty("วิศวกรรมศาตร์", "02");
        // เพิ่มสาขา
        Department department1 = new Department("วิทยาการคอมพิวเตอร์", "011", faculty1);
        Department department2 = new Department("วิศวกรรมคอมพิวเตอร์", "022", faculty2);

        faculty1.addDepartment(department1);
        faculty1.addDepartment(department2);

        facultyList.addFaculty(faculty1);
        facultyList.addFaculty(faculty2);

        datasource.writeData(facultyList);

        // ถ้าอ่านได้ = ทำงานได้
        FacultyList test = datasource.readData();
        for (Faculty faculty : test.getFaculties()) {
            for (Department department : faculty.getDepartments()) {
                System.out.println(faculty.getFacultyName() + ", " + faculty.getFacultyId() + ", " + department.getDepartmentName() + ", " + department.getDepartmentID());
            }
        }

    }
}
