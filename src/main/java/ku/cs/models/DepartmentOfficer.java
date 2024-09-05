package ku.cs.models;

public class DepartmentOfficer extends User{
    private Faculty faculty;
    private Department department;
    private UserList students;

    // Begin Constructor
    public DepartmentOfficer(String username, String password, String name, Faculty faculty, Department department) {
        super(username, password, name);
        this.faculty = faculty;
        this.department = department;
    }
    // End Constructor

    // Begin handle Request Managers
    public void addRequestManager(String name, String position) {
        department.addRequestHandlingOfficer(new RequestHandlingOfficer(name, position + department.getDepartmentName()));
    }

    public void removeRequestManager(RequestHandlingOfficer officer) {
        department.removeRequestHandlingOfficer(officer);
    }

    public void changeRequestManagerName(RequestHandlingOfficer officer, String name) {
        officer.setName(name);
    }

    public void changeRequestManagerPosition(RequestHandlingOfficer officer, String position) {
        officer.setPosition(position + department.getDepartmentName());
    }

    // End Handle Request Manager

    public void addStudent(String name, String email){
        String id = "66" + faculty.getFacultyId() + department.getDepartmentID();
        students.addUser(new Student(name, id, email, faculty, department));
    }

    public void addStudent(String name, String email, Advisor advisor){
        String id = "66" + faculty.getFacultyId() + department.getDepartmentID();
        students.addUser(new Student(name, id, email, advisor, faculty, department));
    }


    public void removeStudent(Student student){
        students.removeUser(student);
    }

    public void assignAdvisor(Student student, Advisor advisor){
        //
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
