package ku.cs.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {
    static Faculty faculty;
    Department department;
    @BeforeAll
    static void setUpAll(){
        faculty = new Faculty("Science");
    }
    @BeforeEach
    void setUp() {
        department = new Department("Math", "01", faculty);
    }
    @Test
    void testIsDepartmentName() {
        assertTrue(department.isDepartmentName("Math"));
    }

    @Test
    void testIsDepartmentID() {
        assertTrue(department.isDepartmentID("01"));
    }

    @Test
    @DisplayName("Test Create Department with auto generate and display Object")
    void testGenerateDepartment() {
        Department engineering  = new Department("Engineering", faculty);
        System.out.println(engineering);
        assertNotNull(engineering);
        //g
        //
    }


}