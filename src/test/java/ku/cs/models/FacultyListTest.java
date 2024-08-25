package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyListTest {
    FacultyList faculties;
    Faculty faculty;
    @BeforeEach
    void setUp() {
        faculties = new FacultyList();
    }

    @Test
    void addFaculty() {
        Faculty science = new Faculty("Science");
        faculties.addFaculty(science);
        assertSame(science,faculties.getFaculties().getFirst());
    }

    @Test
    void removeFaculty() {
        Faculty science = new Faculty("Science");
        faculties.addFaculty(science);
        faculties.removeFaculty(science);
        System.out.println(faculties.getFaculties());
        assertTrue(faculties.getFaculties().isEmpty());
    }

    @Test
    void findFacultyByName() {
        faculties.addFaculty(new Faculty("A"));
        faculties.addFaculty(new Faculty("B"));
        faculties.addFaculty(new Faculty("C"));
        faculties.addFaculty(new Faculty("D"));

        Faculty found = faculties.findFacultyByName("C");
        assertSame(found, faculties.getFaculties().get(2));
    }

    @Test
    void findFacultyByID() {
        faculties.addFaculty(new Faculty("A", "01"));
        faculties.addFaculty(new Faculty("B", "02"));
        faculties.addFaculty(new Faculty("C", "03"));
        faculties.addFaculty(new Faculty("D", "04"));

        Faculty found = faculties.findFacultyByID("02");
        assertSame(found, faculties.getFaculties().get(1));
    }

    @Test
    void getFacultyList() {
        faculties.addFaculty(new Faculty("A", "01"));
        faculties.addFaculty(new Faculty("B", "02"));

        faculties.findFacultyByID("01").addDepartment("a");
        faculties.findFacultyByID("01").addDepartment("b");
        faculties.findFacultyByID("01").addDepartment("c");

        faculties.findFacultyByID("02").addDepartment("a");
        faculties.findFacultyByID("02").addDepartment("b");
        faculties.findFacultyByID("02").addDepartment("c");

        for (Faculty faculty : faculties.getFaculties()) {
            System.out.print("Faculty: "+ faculty + "--> ");
            for (Department department : faculty.getDepartments()) {
                System.out.print(department + " ");
            }
            System.out.println();
        }
    }
}