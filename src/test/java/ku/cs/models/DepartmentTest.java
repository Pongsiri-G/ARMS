package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {
    Department department;
    @BeforeEach
    void setUp() {
        department = new Department("Science", "01");
    }
    @Test
    void testIsDepartmentName() {
        assertTrue(department.isDepartmentName("Science"));
    }

    @Test
    void testIsDepartmentID() {
        assertTrue(department.isDepartmentID("01"));
    }

    @Test
    @DisplayName("Test Create Department with auto generate and display Object")
    void testGenerateDepartment() {
        Department engineering  = new Department("Engineering");
        System.out.println(engineering);
        assertNotNull(engineering);
    }


}