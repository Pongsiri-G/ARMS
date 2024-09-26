package ku.cs.services;

import ku.cs.models.Department;
import ku.cs.models.DepartmentOfficer;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;

import java.util.ArrayList;

public class OfficerDatasourceTest {
    static FacDepListFileDatascource facDepDatascource;
    public static void main(String[] args) {
        facDepDatascource = new FacDepListFileDatascource("data/test", "facdep.csv");
        // ถ้าอ่านได้ = ทำงานได้
        FacultyList facultyList = facDepDatascource.readData();
        ArrayList<DepartmentOfficer> officers = new ArrayList<>();
        officers.add(new DepartmentOfficer("test1", "1234", "test1", facultyList.findFacultyByName("วิทยาศาสตร์"), facultyList.findFacultyByName("วิทยาศาสตร์").findDepartmentByName("วิทยาการคอมพิวเตอร์"), false, false));
        officers.add(new DepartmentOfficer("test2", "1234", "test2", facultyList.findFacultyByName("วิทยาศาสตร์"), facultyList.findFacultyByName("วิทยาศาสตร์").findDepartmentByName("วิทยาการคอมพิวเตอร์"), false, false));
        officers.add(new DepartmentOfficer("test3", "1234", "test3", facultyList.findFacultyByName("วิศวกรรมศาตร์"), facultyList.findFacultyByName("วิศวกรรมศาตร์").findDepartmentByName("วิศวกรรมคอมพิวเตอร์"), false, false));
        for (DepartmentOfficer officer : officers) {
            System.out.println(officer.toString());
        }

        DepartmentOfficerDatasource depDatasource = new DepartmentOfficerDatasource("data/test", "department-officer.csv");
        depDatasource.writeData(officers);

        ArrayList<DepartmentOfficer> officersTest = depDatasource.readData();
        for (DepartmentOfficer officer : officersTest) {
            System.out.println(officer.toString());
        }
    }
}
