package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Faculty {
    private String facultyName;
    private String facultyId;
    private String departmentName;
    private String departmentId;
    //ping : add department data
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
    }

    public Faculty(String facultyName, String facultyId, String departmentName, String departmentId){
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.departmentName = departmentName;
        this.departmentId = departmentId;
    }

    public Faculty(String facultyName, String facultyId) {
        this.facultyName = facultyName;
        this.facultyId = facultyId;
        this.departments = new ArrayList<>();
        this.requestHandlingOfficers = new ArrayList<>();
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
        if (department.getFaculty() == null){
            department.setFaculty(this);
        }
        this.departments.add(department);
    }

    public void addDepartment(String departmentName){
        this.departments.add(new Department(departmentName, this));
    }

    public void addDepartment(String departmentName, String departmentId){
        this.departments.add(new Department(departmentName, departmentId, this));
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
    public ArrayList<Department> getDepartments(){
        return this.departments;
    }

    public String getFacultyName(){
        return this.facultyName;
    }
    public String getFacultyId(){
        return this.facultyId;
    }
    public String getDepartmentName(){return this.departmentName;}
    public String getDepartmentId(){return this.departmentId;}
    //ping : add department getter

    // End getter

    @Override
    public String toString(){
        return this.facultyName + "\t" + this.facultyId;
    }

}
