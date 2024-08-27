package ku.cs.models;

public class DepartmentStaff extends User{
    Faculty faculty;
    Department department;
    public DepartmentStaff(String username, String password, String name, Faculty faculty, Department department) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
    }
    @Override
    public Faculty getFaculty() {
        return faculty;
    }
    @Override
    public Department getDepartment() {
        return department;
    }
    @Override
    public String toString() {
        return name + " " + faculty.toString() + " " + department.toString();
    }
}
