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

    public void addNewAdvisor(String username, String password, String name, String faculty, String department, String id, String email, boolean isHashed, boolean suspended, boolean isFirstLogin) {
        Faculty faculty1 = new Faculty(faculty);
        Department department1 = new Department(department);
        Advisor newAdvisor = new Advisor(username, password, name, faculty1, department1, id, email, isHashed, suspended);
        newAdvisor.setFirstLogin(isFirstLogin);
        advisors.add(newAdvisor);
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
