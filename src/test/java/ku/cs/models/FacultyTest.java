package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyTest {
    Faculty science;
    Faculty engineering;
    Department computerScience;
    Department computerEngineer;
    @BeforeEach
    void setUp() {
        science = new Faculty("Science");
        engineering = new Faculty("Engineering");
        computerScience = new Department("Computer Science",  science);
        computerEngineer = new Department("Computer Engineering", engineering);
    }

    @Test
    void testAddDepartmentByObject() {
        science.addDepartment(computerScience);
        assertTrue(science.getDepartments().contains(computerScience));
    }

    @Test
    void testAddDepartmentByNameAndID() {
        science.addDepartment("test", "01");
        assertEquals("01", science.getDepartments().getFirst().getDepartmentID());
    }

    @Test
    void testRemoveDepartment() {
        science.addDepartment(computerScience);
        science.removeDepartment(computerScience);
        assertTrue(science.getDepartments().isEmpty());
    }

    @Test
    void testFindDepartmentByName() {
        science.addDepartment(computerScience);
        science.addDepartment("math", "02");
        science.addDepartment(computerEngineer);
        assertSame(computerEngineer, science.findDepartmentByName("Computer Engineering"));

    }

    @Test
    void testFindDepartmentID() {
        science.addDepartment(computerScience);
        science.addDepartment("math", "02");
        science.addDepartment(computerEngineer);
        assertEquals("02", science.findDepartmentByID("02").getDepartmentID());

    }

    @Test
    void testToString(){
        science.addDepartment(computerScience);
        science.addDepartment("math", "02");
        science.addDepartment(computerEngineer);
        science.addDepartment("eng", "03");
        System.out.println(science.getDepartments());

    }

    @Test
    void testSetter(){
        System.out.println(science.getFacultyName() + " " + science.getFacultyId());
        science.setFacultyName("c");
        System.out.println(science.getFacultyName());
        science.setFacultyId("12");
        System.out.println(science.getFacultyId());
    }


}