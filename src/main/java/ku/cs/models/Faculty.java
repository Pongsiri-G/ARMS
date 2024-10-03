package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Faculty {
    private String facultyName;
    private String facultyId;
    private ArrayList<FacultyOfficer> facultyOfficers;
    private ArrayList<Department> departments;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;

    // Begin Constructor
    public Faculty(String facultyName){
        // Auto Generate facultyID will implement later
        Random rand = new Random();
        this.facultyName = facultyName;
        this.facultyId = String.valueOf(rand.nextInt(90) + 10 +"");
        this.departments = new ArrayList<>();
        this.requestHandlingOfficers = new ArrayList<>();
        this.facultyOfficers = new ArrayList<>();
    }

    public Faculty(String facultyName, String facultyId) {
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.departments = new ArrayList<>();
        this.requestHandlingOfficers = new ArrayList<>();
        this.facultyOfficers = new ArrayList<>();
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
        this.departments.add(department);
    }

    public void addDepartment(String departmentName){
       this.departments.add(new Department(departmentName));
    }

    public void addDepartment(String departmentName, String departmentId){
        this.departments.add(new Department(departmentName, departmentId));
    }

    public void removeDepartment(Department department){
        this.departments.remove(department);
    }

    public Department findDepartmentByName(String departmentName){
        for(Department department : this.departments){
            if(department.isDepartmentName(departmentName)){
                return department;
            }
        }
        return null;
    }

    public Department findDepartmentByID(String departmentId){
        for(Department department : this.departments){
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
    public ArrayList<Department> getDepartments() { return this.departments; }
    public String getFacultyName(){
        return this.facultyName;
    }
    public String getFacultyId(){
        return this.facultyId;
    }
    public ArrayList<FacultyOfficer> getFacultyOfficers(){
        return this.facultyOfficers;
    }

    // End getter

    @Override
    public String toString(){
        return this.facultyName + "\t" + this.facultyId;
    }

}
