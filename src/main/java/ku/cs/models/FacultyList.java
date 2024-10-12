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
    public void addFaculty(String facultyName, String facultyID, String departmentName, String departmentID) {
        //faculties.add(new Faculty(facultyName, facultyID)); อันเดิม
        //faculties.add(new Faculty(facultyName, facultyID, departmentName, departmentID));

        Faculty faculty = new Faculty(facultyName, facultyID);
        Department department = new Department(departmentName, departmentID);
        faculty.addDepartment(department); // สร้างอ็อบเจก department เก็บใน faculty
        faculties.add(faculty); // faculty เก็บใน facultyList
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

    //ping
    public Department findDepartmentByName(String departmentName) {
        for (Faculty faculty : faculties) {
            for (Department department : faculty.getDepartments()) {
                if (department.getDepartmentName().equals(departmentName)) {
                    return department;
                }
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
