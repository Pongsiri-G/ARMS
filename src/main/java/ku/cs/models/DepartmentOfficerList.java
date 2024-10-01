package ku.cs.models;

import java.util.ArrayList;

public class DepartmentOfficerList {
    private ArrayList<DepartmentOfficer> officers;

    public DepartmentOfficerList() {
        officers = new ArrayList<>();
    }

    public void add (String username, String password, String name, String faculty, String department, boolean isHashed, boolean suspend) {
        Faculty fac = new Faculty(faculty);
        Department dep = new Department(department);
        officers.add(new DepartmentOfficer(username, password, name, fac, dep, isHashed, suspend));
    }

    public void add (DepartmentOfficer officer) {
        officers.add(officer);
    }

    public void remove (DepartmentOfficer officer) {
        officers.remove(officer);
    }

    //เอามาจาก FacDepDatasource ต้องเรียก Facdep ก่อน
//    public void setFacDep(FacultyList facultyList) {
//        this.facultyList = facultyList;
//    }

    public ArrayList<DepartmentOfficer> getOfficers() {
        return officers;
    }
}
