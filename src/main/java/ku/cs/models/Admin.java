package ku.cs.models;

import java.util.HashMap;
import java.util.Map;

public class Admin extends User {
    private int allStudents = 0;
    private int allAdvisors = 0;
    private int allFacultyOfficers = 0;
    private int allDepartmentOfficers = 0;
    private int allRequests = 0;
    private int allApprovedRequests = 0;
    private int allPendingRequests = 0;
    private int allRejectRequests = 0;

    private Map<String, Integer> facultyUserCount = new HashMap<>();
    private Map<String, Integer> departmentUserCount = new HashMap<>();

    private Map<String, Integer> facultyApprovedRequests = new HashMap<>();
    private Map<String, Integer> departmentApprovedRequests = new HashMap<>();

    public Admin(String password, boolean isHashed, boolean suspended) {
        super(null, password, null, isHashed, suspended);
    }

    public void increaseUserCount(User user) {
        if (user instanceof Student) {
            allStudents++;
        } else if (user instanceof Advisor) {
            allAdvisors++;
        } else if (user instanceof FacultyOfficer) {
            allFacultyOfficers++;
        } else if (user instanceof DepartmentOfficer) {
            allDepartmentOfficers++;
        }
    }

    public void increaseRequestCount(Request request) {
        allRequests++;
        switch (request.getStatus()) {
            case "เสร็จสิ้น":
                allApprovedRequests++;
                break;
            case "กำลังดำเนินการ":
                allPendingRequests++;
                break;
            case "ปฏิเสธ":
                allRejectRequests++;
                break;
            default:
                break;
        }
    }

    public void increaseFacultyUserCount(String facultyName) {
        facultyUserCount.put(facultyName, facultyUserCount.getOrDefault(facultyName, 0) + 1);
    }

    public void increaseDepartmentUserCount(String departmentName) {
        departmentUserCount.put(departmentName, departmentUserCount.getOrDefault(departmentName, 0) + 1);
    }

    public int getTotalUsers() {
        return allStudents + allAdvisors + allFacultyOfficers + allDepartmentOfficers;
    }

    public int getAllStudents() {
        return allStudents;
    }

    public int getAllAdvisors() {
        return allAdvisors;
    }

    public int getAllFacultyOfficers() {
        return allFacultyOfficers;
    }

    public int getAllDepartmentOfficers() {
        return allDepartmentOfficers;
    }

    public int getAllRequests() {
        return allRequests;
    }

    public int getAllApprovedRequests() {
        return allApprovedRequests;
    }

    public int getAllPendingRequests() {
        return allPendingRequests;
    }

    public int getAllRejectRequests() {
        return allRejectRequests;
    }

    public int getAllFacultyUsers(String facultyName) {
        return facultyUserCount.getOrDefault(facultyName, 0);
    }

    public int getAllDepartmentUsers(String departmentName) {
        return departmentUserCount.getOrDefault(departmentName, 0);
    }

    @Override
    public String getRole() {
        return "ผู้ดูแลระบบ";
    }
}
