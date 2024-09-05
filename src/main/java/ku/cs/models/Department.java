package ku.cs.models;

import java.util.ArrayList;
import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;
    private Faculty faculty;
    private ArrayList<RequestHandlingOfficer> requestHandlingOfficers;
    private ArrayList<Student> students;

    // Begin Constructor
    Department(String departmentName, Faculty faculty) {
        // Auto Generate departmentID will implement later
        Random rand = new Random();
        this.departmentName = departmentName;
        this.departmentID = rand.nextInt(90) + 10 + "";
        this.requestHandlingOfficers = new ArrayList<>();
    }

    Department(String departmentName, String departmentID, Faculty faculty) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.requestHandlingOfficers = new ArrayList<>();
    }
    // End Constructor

    public void loadData(){

    }

    public boolean isDepartmentName(String departmentName){
        return this.departmentName.equals(departmentName);
    }

    public boolean isDepartmentID(String departmentID){
        return this.departmentID.equals(departmentID);
    }

    // Methods to handle RequestHandlingOfficers
    public void addRequestHandlingOfficer(RequestHandlingOfficer officer) {
        this.requestHandlingOfficers.add(officer);
    }

    public void removeRequestHandlingOfficer(RequestHandlingOfficer officer) {
        this.requestHandlingOfficers.remove(officer);
    }

    public ArrayList<RequestHandlingOfficer> getRequestHandlingOfficers() {
        return this.requestHandlingOfficers;
    }

    // Begin Setter
    public void setFaculty(Faculty faculty) {this.faculty = faculty;}
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }
    public void setDepartmentID(String departmentID){
        this.departmentID = departmentID;
    }
    // End Setter

    // Begin Getter
    public Faculty getFaculty() { return faculty; }
    public String getDepartmentName(){
        return this.departmentName;
    }
    public String getDepartmentID(){
        return this.departmentID;
    }
    // End Getter

    @Override
    public String toString(){
        return departmentName + "\t" + departmentID;
    }



}
