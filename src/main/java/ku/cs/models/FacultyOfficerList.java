package ku.cs.models;

import java.util.ArrayList;

public class FacultyOfficerList {
    private FacultyList facultyList; //ไม่มีไม่ได้ไม่งั้นจะหาภาควิชาหรือคณะที่มีอยู่แล้วยังไง (อาจเกิดความซ้ำซ้อนถามพุธ)
    private ArrayList<FacultyOfficer> officers;

    public FacultyOfficerList() {
        officers = new ArrayList<>();
    }

    public void add (String username, String password, String name, String faculty, boolean isHashed, boolean suspend) {
        Faculty fac = facultyList.findFacultyByName(faculty);
        officers.add(new FacultyOfficer(username, password, name, fac, isHashed, suspend));
    }

    public void add (FacultyOfficer officer) {
        officers.add(officer);
    }

    public void remove (FacultyOfficer officer) {
        officers.remove(officer);
    }

    //เอามาจาก FacDepDatasource ต้องเรียก Facdep ก่อน
    public void setFacDep(FacultyList facultyList) {
        this.facultyList = facultyList;
    }

    public ArrayList<FacultyOfficer> getOfficers() {
        return officers;
    }
}
