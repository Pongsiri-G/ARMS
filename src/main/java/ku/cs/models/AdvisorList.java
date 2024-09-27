package ku.cs.models;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.ArrayList;

public class AdvisorList {
    private ArrayList<Advisor> advisors;
    private ArrayList<Student> students;
    private ArrayList<Request> requests;
    //private FacultyList facultyList;

    public AdvisorList() {
        advisors = new ArrayList<>();
        requests = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void addNewAdvisor(String name, String username, String password, String faculty, String department, String advisorID) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        Advisor exist = findAdvisorByID(advisorID);
        if (exist == null) {
            Faculty faculty1 = new Faculty(faculty);
            Department department1 = new Department(department);
            advisors.add(new Advisor(name, username, hashedPassword, faculty1, department1, advisorID));
        }
    }

    public Advisor findAdvisorByID(String id) {
        for (Advisor advisor : advisors) {
            if (advisor.getAdvisorID().equals(id)) {
                return advisor;
            }
        }
        return null;
    }

    public Request findRequestByID(String id) {
        for (Student students : students) {
            if (students.getStudentID().equals(id)) {
                for (Request request : requests) {
                    if (requests != null) return request;
                }
            }
        }return null;
    }

    public void addAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }

    public ArrayList<Advisor> getAdvisors() {
        return advisors;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }
}
