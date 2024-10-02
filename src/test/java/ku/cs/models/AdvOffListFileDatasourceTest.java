package ku.cs.models;

import ku.cs.models.*;
import ku.cs.services.AdvOffListFileDatasource;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdvOffListFileDatasourceTest {

    private static final String TEST_DIRECTORY = "data/test";
    private static final String TEST_FILE = "test_advisors.csv";
    private AdvOffListFileDatasource datasource;
    private ArrayList<Advisor> advisorList;

    @BeforeEach
    void setUp() {
        // Initialize datasource with test directory and file
        datasource = new AdvOffListFileDatasource(TEST_DIRECTORY, TEST_FILE);

        // Create a new AdvisorList with sample data
        advisorList = new ArrayList<>();
        advisorList.add(new Advisor("advisor1", "pass1", "Advisor One", "Engineering", "Computer Science", "A12345", "advisor1@example.com", false, false, true));
        advisorList.add(new Advisor("advisor2", "pass2", "Advisor Two", "Science", "Biology", "A12346", "advisor2@example.com", false, false, true));
        advisorList.add(new Advisor("advisor3", "pass3", "Advisor Three", "Arts", "History", "A12347", "advisor3@example.com", false, false, true));
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clean up: delete the test file after every test
        Files.deleteIfExists(Paths.get(TEST_DIRECTORY, TEST_FILE));
    }

    @Test
    void testWriteAndReadData() {
        // Write data to the file
        datasource.writeData(advisorList);

        // Read data back from the file
        ArrayList<Advisor> readAdvisorList = datasource.readData();

        // Check if the data read matches the data written
        assertEquals(advisorList.size(), readAdvisorList.size(), "Number of advisors should match");

        for (int i = 0; i < advisorList.size(); i++) {
            Advisor expected = advisorList.get(i);
            Advisor actual = readAdvisorList.get(i);

            assertEquals(expected.getUsername(), actual.getUsername(), "Usernames should match");
            assertEquals(expected.getPassword(), actual.getPassword(), "Passwords should match");
            assertEquals(expected.getName(), actual.getName(), "Names should match");
            assertEquals(expected.getFaculty(), actual.getFaculty(), "Faculties should match");
            assertEquals(expected.getDepartment(), actual.getDepartment(), "Departments should match");
            assertEquals(expected.getAdvisorID(), actual.getAdvisorID(), "Advisor IDs should match");
            assertEquals(expected.getAdvisorEmail(), actual.getAdvisorEmail(), "Advisor emails should match");
        }
    }

    @Test
    void testFileCreated() {
        // Write data to the file
        datasource.writeData(advisorList);

        // Check if the file is created in the correct directory
        File file = new File(TEST_DIRECTORY, TEST_FILE);
        assertTrue(file.exists(), "Test file should be created");
    }
}
