package ku.cs.models;

public class DepartmentOfficer extends User{
    private Faculty faculty;
    private Department department;

    // Begin Constructor
    public DepartmentOfficer(String username, String password, String name, Faculty faculty, Department department, boolean isHashed) {
        super(username, password, name, isHashed);
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


    // Handle Student
    public void addStudent(String name, String studentID, String email) {
        Student student = new Student(name, studentID, email);
        department.addStudent(student);
    }

    public void removeStudent(String studentID) {
        Student student = department.findStudentByID(studentID);
        if (student != null) {
            department.removeStudent(student);
        }
    }


    public void assignAdvisor(Student student, Advisor advisor) {
        student.setStudentAdvisor(advisor);
    }

    public Faculty getFaculty() {
        return faculty;
    }
    public Department getDepartment() {
        return department;
    }
    @Override
    public String getRole(){
        return "DepartmentOfficer";
    }
    @Override
    public String toString() {
        return "DepartmentOfficer: " + getName() + " (" + getUsername() + "), Faculty: " + getFaculty().getFacultyName() + ", Department: " + getDepartment().getDepartmentName();
    }
}
