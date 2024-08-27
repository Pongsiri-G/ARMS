package ku.cs.models;

public class FacultyStaff extends User {
    Faculty faculty;
    public FacultyStaff(String username, String password, String name, Faculty faculty) {
        super(username, password, name);
        this.faculty = faculty;
    }
    @Override
    public Faculty getFaculty() {
        return faculty;
    }

    @Override
    public String toString() {
        return name + " " + faculty.toString();
    }
}
