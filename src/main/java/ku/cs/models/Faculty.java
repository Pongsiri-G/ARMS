package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Faculty {
    private String facultyName;
    private String facultyId;
    private ArrayList<FacultyOfficer> facultyOfficers;
    private ArrayList<Department> departments;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    
    public Faculty(String facultyName){
        
        Random rand = new Random();
        this.facultyName = facultyName;
        this.facultyId = rand.nextInt(90) + 10 + "";
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
    }
    
    public void setFacultyName(String facultyName){
        this.facultyName = facultyName;
    }

    public void setFacultyId(String facultyId){
        this.facultyId = facultyId;
    }
    
    public void addDepartment(Department department){
        this.departments.add(department);
    }


    public void addDepartment(String departmentName, String departmentId){
        this.departments.add(new Department(departmentName, departmentId));
    }

    public Department findDepartmentByName(String departmentName){
        for(Department department : this.departments){
            if(department.isDepartmentName(departmentName)){
                return department;
            }
        }
        return null;
    }

    public ArrayList<RequestHandlingOfficer> getRequestHandlingOfficers() {
        return this.requestHandlingOfficers;
    }
    public void setRequestManagers(ArrayList<RequestHandlingOfficer> approvers){this.requestHandlingOfficers = approvers;}

    
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

    @Override
    public String toString(){
        return this.facultyName + "\t" + this.facultyId;
    }

}
