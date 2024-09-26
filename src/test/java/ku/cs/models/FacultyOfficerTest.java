/*package ku.cs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FacultyOfficerTest {
    private FacultyOfficer fs;
    private Faculty f;
    @BeforeEach
    void setUp1() {
        fs = new FacultyOfficer("User", "1234", "Test", new Faculty("Science"), false, false);
        f = fs.getFaculty();
    }

    @Test
    void addRequestManager() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        assertEquals("Deputy Dean Science", f.getRequestHandlingOfficers().getFirst().getPosition());
    }

    @Test
    void removeRequestManager() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.removeRequestManager(f.getRequestHandlingOfficers().getFirst());
        assertEquals(0, f.getRequestHandlingOfficers().size());
    }

    @Test
    void updateRequestManagerName() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.changeRequestManagerName(f.getRequestHandlingOfficers().getFirst(), "Change");
        assertEquals("Change", f.getRequestHandlingOfficers().getFirst().getName());
    }

    @Test
    void updateRequestManagerPosition() {
        fs.addRequestManager("staff1", "Deputy Dean ");
        fs.changeRequestManagerPosition(f.getRequestHandlingOfficers().getFirst(), "Change");
        assertEquals("ChangeScience", f.getRequestHandlingOfficers().getFirst().getPosition());
    }
}*/