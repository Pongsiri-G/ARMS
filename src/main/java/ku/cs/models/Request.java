package ku.cs.models;

public class Request {
    private int allRequest;
    private int approved;
    private int users;

    Request(int request, int approved, int users) {
        this.allRequest = request;
        this.approved = approved;
        this.users = users;
    }

    public int getRequest() {
        return allRequest;
    }

    public int getApproved() {
        return approved;
    }

    public int getUsers() {
        return users;
    }

}
