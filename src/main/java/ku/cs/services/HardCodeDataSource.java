package ku.cs.services;

import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;
import ku.cs.models.FacultyOfficer;
import ku.cs.models.UserList;

public class HardCodeDataSource {
    public UserList loadUserList(){
        UserList users = new UserList();
        FacultyList facultyList = this.loadFacultyList();
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Science")));
        users.addUser(new FacultyOfficer("S2", "1234", "S2", facultyList.findFacultyByName("Science")));
        users.addUser(new FacultyOfficer("S3", "1234", "S3", facultyList.findFacultyByName("Architecture")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Architecture")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Engineering")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Engineering")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Arts")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Arts")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Economics")));
        users.addUser(new FacultyOfficer("S1", "1234", "S1", facultyList.findFacultyByName("Economics")));

        return users;
    }

    public FacultyList loadFacultyList(){
        FacultyList faculties = new FacultyList();
        Faculty science = new Faculty("Science");
        science.addDepartment("Computer Science");
        science.addDepartment("Math");
        science.addDepartment("Food Science");

        Faculty architecture = new Faculty("Architecture");
        architecture.addDepartment("Thai Architecture");
        architecture.addDepartment("Interior Architecture");
        architecture.addDepartment("Urban Architecture");


        Faculty agriculture = new Faculty("Agriculture");
        agriculture.addDepartment("Agricultural Management");

        Faculty engineering = new Faculty("Engineering");
        engineering.addDepartment("Civil Engineering");
        engineering.addDepartment("Computer Engineering");
        engineering.addDepartment("Electrical Engineering");

        Faculty arts = new Faculty("Arts");
        arts.addDepartment("Graphic Arts");

        Faculty economics = new Faculty("Economics");
        economics.addDepartment("Civil Economics");

        faculties.addFaculty(science);
        faculties.addFaculty(architecture);
        faculties.addFaculty(engineering);
        faculties.addFaculty(arts);
        faculties.addFaculty(economics);
        return faculties;


    }
}
