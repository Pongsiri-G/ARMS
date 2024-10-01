package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Faculty {
    private String facultyName;
    private String facultyId;
    private FacultyOfficerList facultyOfficers;
    private DepartmentList departments;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;

    // Begin Constructor
    public Faculty(String facultyName){
        // Auto Generate facultyID will implement later
        Random rand = new Random();
        this.facultyName = facultyName;
        this.facultyId = String.valueOf(rand.nextInt(90) + 10 +"");
        this.departments = new DepartmentList();
        this.requestHandlingOfficers = new ArrayList<>();
        this.facultyOfficers = new FacultyOfficerList();
    }

    public Faculty(String facultyName, String facultyId) {
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.departments = new DepartmentList();
        this.requestHandlingOfficers = new ArrayList<>();
        this.facultyOfficers = new FacultyOfficerList();
    }// End Constructor

    public boolean isFacultyName(String facultyName){
        return this.facultyName.equals(facultyName);
    }
    public boolean isFacultyId(String facultyId){
        return this.facultyId.equals(facultyId);
    }

    // Begin setter
    public void setFacultyName(String facultyName){
        this.facultyName = facultyName;
    }
    public void setFacultyId(String facultyId){
        this.facultyId = facultyId;
    }
    // End setter


    // Begin handle Departments
    public void addDepartment(Department department){
        this.departments.getDepartments().add(department);
    }

    public void addDepartment(String departmentName){
       this.departments.getDepartments().add(new Department(departmentName));
    }

    public void addDepartment(String departmentName, String departmentId){
        this.departments.getDepartments().add(new Department(departmentName, departmentId));
    }

    public void removeDepartment(Department department){
        this.departments.getDepartments().remove(department);
    }

    public Department findDepartmentByName(String departmentName){
        for(Department department : this.departments.getDepartments()){
            if(department.isDepartmentName(departmentName)){
                return department;
            }
        }
        return null;
    }

    public Department findDepartmentByID(String departmentId){
        for(Department department : this.departments.getDepartments()){
            if(department.isDepartmentID(departmentId)){
                return department;
            }
        }
        return null;
    }
    // End handle Department

    public ArrayList<RequestHandlingOfficer> getRequestHandlingOfficers() {
        return this.requestHandlingOfficers;
    }
    public void setRequestManagers(ArrayList<RequestHandlingOfficer> approvers){this.requestHandlingOfficers = approvers;}

    // Begin getter
    public DepartmentList getDepartmentList() { return this.departments; }
    public String getFacultyName(){
        return this.facultyName;
    }
    public String getFacultyId(){
        return this.facultyId;
    }
    public FacultyOfficerList getFacultyOfficerList(){
        return this.facultyOfficers;
    }

    // End getter

    @Override
    public String toString(){
        return this.facultyName + "\t" + this.facultyId;
    }

}
