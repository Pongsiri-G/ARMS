package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requests;
    private int allRequest;
    private int approvedRequest;
    private int users;

    public RequestList() {
        requests = new ArrayList<>();
        allRequest = 0;
        approvedRequest = 0;
        users = 0;
    }

    public void addRequest(Request request) {
        requests.add(request);
        allRequest++;
        if (request.getStatus().equals("Approved")) {
            approvedRequest++;
        }
    }
    public void addUser(User user) {
        users++;
    }

    public int countAllRequests() {
        return allRequest;
    }

    public int countApprovedRequests() {
        return approvedRequest;
    }

    public ArrayList<Request> getRequests() {return requests;}
}
