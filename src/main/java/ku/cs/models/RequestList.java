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

    public void addNewRequest(String status, String type, String text, String subject, String name) {
        if (findRequestByStatus(status) != null) {
            Request request = findRequestByStatus(status);
            if (request != null) {
                requests.add(new Request(status, type, text, subject, name));
                allRequest++;
            }
        }
    }

    public void addNewRequest(String name, String faculty, String department, String status) {
        if (findRequestByStatus(status) != null) {
            Request request = findRequestByStatus(status);
            if (request != null) {
                requests.add(new Request(name, faculty, department, status));
                allRequest++;
            }
        }
    }

    public Request findRequestByStatus(String status) {
        for (Request request : requests) {
            if (request.getStatus().equals(status)) {
                return request;
            }
        }
        return null;
    }

    public int countAllRequests() {
        return allRequest;
    }

    public int countApprovedRequests() {
        return approvedRequest;
    }

    public ArrayList<Request> getRequests() {return requests;}
}
