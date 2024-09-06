package ku.cs.models;

public class AbsentRequest extends Request {
    private String subject;
    private String username;
    private String message;
    private int absentDuration;

    public AbsentRequest(String subject, String username, String message, int absentDuration) {
        super("Pending");
        this.subject = subject;
        this.username = username;
        this.message = message;
        this.absentDuration = absentDuration;
    }


    @Override
    public String changeStatus(String newStatus, String approveName) {
        this.status = newStatus;
        this.approveName = approveName;
        return "Status: " + newStatus + " Approved by: " + approveName;
    }
}
