package ku.cs.services;

import ku.cs.models.DepartmentOfficer;
import ku.cs.models.FacultyList;
import ku.cs.models.FacultyOfficer;

import java.util.ArrayList;

public class FacultyOfficerDatasourceTest {
    static FacDepListFileDatascource facDepDatascource;
    public static void main(String[] args) {
        facDepDatascource = new FacDepListFileDatascource("data/test", "facdep.csv");
        // ถ้าอ่านได้ = ทำงานได้
        FacultyList facultyList = facDepDatascource.readData();
        ArrayList<FacultyOfficer> officers = new ArrayList<>();
        officers.add(new FacultyOfficer("test1", "1234", "test1", facultyList.findFacultyByName("วิทยาศาสตร์"), false));
        officers.add(new FacultyOfficer("test2", "1234", "test2", facultyList.findFacultyByName("วิทยาศาสตร์"),  false));
        officers.add(new FacultyOfficer("test3", "1234", "test3", facultyList.findFacultyByName("วิศวกรรมศาตร์"),false));
        for (FacultyOfficer officer : officers) {
            System.out.println(officer.toString());
        }

        FacultyOfficerDatasource facDatasource = new FacultyOfficerDatasource("data/test", "faculty-officer.csv");
        facDatasource.writeData(officers);

        ArrayList<FacultyOfficer> officersTest = facDatasource.readData();
        for (FacultyOfficer officer : officersTest) {
            System.out.println(officer.toString());
        }
    }
}
