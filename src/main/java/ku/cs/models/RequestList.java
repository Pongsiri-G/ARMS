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

    // ลาป่วย/กิจ
    public void addNewRequest(String approveName, String status, String type, String text) {
        if (findRequestByStatus(status) != null) {
            Request request = findRequestByStatus(status);
            if (request != null) {
                requests.add(new Request(approveName, status, type, text));
            }
        }
    }

    //ของดเรียนบางวิชาล่าช้า
    public void addNewRequest(String approveName, String status, String type, String text, String subject) {
        if (findRequestByStatus(status) != null) {
            Request request = findRequestByStatus(status);
            if (request != null) {
                requests.add(new Request(approveName, status, type, text, subject));
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
