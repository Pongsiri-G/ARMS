package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Request> requests;

    
    public RequestList() {
        requests = new ArrayList<>();
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    
    public Request getRequestByTimestamp(String timestamp) {
        for (Request request : requests) {
            if (request.getTimestamp() != null && request.getTimestamp().equals(timestamp)) {
                return request;
            }
        }return null;
    }
}