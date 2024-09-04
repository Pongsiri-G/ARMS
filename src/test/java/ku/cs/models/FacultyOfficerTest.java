package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyOfficerTest {
    private FacultyOfficer fs;
    @BeforeEach
    void setUp1() {
        fs = new FacultyOfficer("User", "1234", "Test", new Faculty("Science"));
    }
    @BeforeEach
    void setUp2() {
        fs.loadRequestManagers();
    }

    @Test
    void addRequestManager() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        assertEquals("Deputy Dean Science", fs.getRequestManagers().getFirst().getPosition());
    }

    @Test
    void removeRequestManager() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.removeRequestManager(fs.getRequestManagers().getFirst());
        assertEquals(0, fs.getRequestManagers().size());
    }

    @Test
    void changeRequestManagerName() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.changeRequestManagerName(fs.getRequestManagers().getFirst(), "Change");
        assertEquals("Change", fs.getRequestManagers().getFirst().getName());
    }

    @Test
    void changeRequestManagerPosition() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.changeRequestManagerPosition(fs.getRequestManagers().getFirst(), "Change");
        assertEquals("ChangeScience", fs.getRequestManagers().getFirst().getPosition());
    }
}