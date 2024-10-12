package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requests;
    private int allRequestCount;

    // Constructor
    public RequestList() {
        requests = new ArrayList<>();
        allRequestCount = 0;
    }

    public void addRequest(Request request) {
        requests.add(request);
        allRequestCount++;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public int getAllRequestCount() {
        return allRequestCount;
    }

    public int getApprovedRequestsCount() {
        int count = 0;
        for (Request request : requests) {
            if ("สำเร็จ".equalsIgnoreCase(request.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getPendingRequestCount() {
        int count = 0;
        for (Request request : requests) {
            if ("กำลังดำเนินการ".equalsIgnoreCase(request.getStatus())) {
                count++;
            }
        }
        return count;
    }

    public int getRejectedRequestsCount() {
        int count = 0;
        for (Request request : requests) {
            if("ปฏิเสธ".equalsIgnoreCase(request.getStatus())) {
                count++;
            }
        }
        return count;
    }
}
