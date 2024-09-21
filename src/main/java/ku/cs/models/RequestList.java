package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requests;
    private int allRequest;
    private int approvedRequest;

    public RequestList() {
        requests = new ArrayList<>();
        allRequest = 0;
        approvedRequest = 0;
    }

    public void addRequest(Request request) {
        requests.add(request);
        allRequest++;
        if (request.getStatus().equals("Approved")) {
            approvedRequest++;
        }
    }

    public int countAllRequests() {
        return allRequest;
    }

    public int countApprovedRequests() {
        return approvedRequest;
    }

    public ArrayList<Request> getRequests() {return requests;}
}
