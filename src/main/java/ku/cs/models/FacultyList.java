package ku.cs.models;

import java.util.ArrayList;

public class FacultyList {
    private ArrayList<Faculty> faculties;

    public FacultyList() {
        faculties = new ArrayList<>();
    }

    public void addFaculty(Faculty faculty) {
        faculties.add(faculty);
    }
    public boolean addFaculty(String facultyName, String facultyID, String departmentName, String departmentID) {
        Faculty existingFaculty = findFacultyByName(facultyName);
        if (existingFaculty == null) {
            Faculty newFaculty = new Faculty(facultyName, facultyID);
            newFaculty.addDepartment(departmentName, departmentID);
            faculties.add(newFaculty);
            return true; 
        } else {
            
            boolean departmentExists = existingFaculty.getDepartments().stream()
                    .anyMatch(department -> department.getDepartmentName().equals(departmentName) && department.getDepartmentID().equals(departmentID));
            if (!departmentExists) {
                existingFaculty.addDepartment(departmentName, departmentID); 
                return true; 
            } else {
                return false; 
            }
        }
    }

    public Faculty findFacultyByName(String facultyName) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyName().equals(facultyName)) {
                return faculty;
            }
        }
        return null;
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }
}
