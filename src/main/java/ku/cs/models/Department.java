package ku.cs.models;

import java.util.Random;

public class Department {
    private String departmentName;
    private String departmentID;

    // Begin Constructor
    Department(String departmentName){
        // Auto Generate departmentID will implement later
        Random rand = new Random();
        this.departmentName = departmentName;
        this.departmentID = rand.nextInt(90) + 10 + "";
    }

    Department(String departmentName, String departmentID) {
        this.departmentName = departmentName;
        this.departmentID = departmentID;
    }
    // End Constructor

    public boolean isDepartmentName(String departmentName){
        return this.departmentName.equals(departmentName);
    }

    public boolean isDepartmentID(String departmentID){
        return this.departmentID.equals(departmentID);
    }

    // Begin Setter
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }
    public void setDepartmentID(String departmentID){
        this.departmentID = departmentID;
    }
    // End Setter

    // Begin Getter
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
