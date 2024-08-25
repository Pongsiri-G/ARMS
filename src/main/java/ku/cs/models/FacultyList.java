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
    public void addFaculty(String facultyName, String facultyID) {
        faculties.add(new Faculty(facultyName, facultyID));
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
