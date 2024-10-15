package ku.cs.models;

import java.util.ArrayList;

public class FacultyList {
    private ArrayList<Faculty> faculties;


    public FacultyList() {
        // Will Read Data From CSV File Later
        faculties = new ArrayList<>();
    }

    public void addFaculty(Faculty faculty) {
        faculties.add(faculty);
    }
    public void addFaculty(String facultyName) {
        faculties.add(new Faculty(facultyName));
    }
    public boolean addFaculty(String facultyName, String facultyID, String departmentName, String departmentID) {
        Faculty existingFaculty = findFacultyByName(facultyName);
        if (existingFaculty == null) {
            Faculty newFaculty = new Faculty(facultyName, facultyID);
            newFaculty.addDepartment(departmentName, departmentID);
            faculties.add(newFaculty);
            return true; // เพิ่มสำเร็จ
        } else {
            // ตรวจสอบว่าภาควิชานี้มีอยู่แล้วหรือไม่
            boolean departmentExists = existingFaculty.getDepartments().stream()
                    .anyMatch(department -> department.getDepartmentName().equals(departmentName) && department.getDepartmentID().equals(departmentID));
            if (!departmentExists) {
                existingFaculty.addDepartment(departmentName, departmentID); // เพิ่มภาควิชาใหม่
                return true; // เพิ่มสำเร็จ
            } else {
                return false; // ภาควิชานี้มีอยู่แล้ว
            }
        }
    }


    public void removeFaculty(Faculty faculty) {
        faculties.remove(faculty);
    }

    public Faculty findFacultyByName(String facultyName) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyName().equals(facultyName)) {
                return faculty;
            }
        }
        return null;
    }

    public Faculty findFacultyByID(String facultyID) {
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyId().equals(facultyID)) {
                return faculty;
            }
        }
        return null;
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }
}
