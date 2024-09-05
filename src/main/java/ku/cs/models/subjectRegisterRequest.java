package ku.cs.models;

public class subjectRegisterRequest extends Request{
    private String subject;
    private String username;
    private String message;
    private int section;
    // 1 = lecture , 0 = lab
    private boolean lecture;

    public subjectRegisterRequest(String subject, String username, String message, int section, boolean lecture) {
        super("Pending");
        this.subject = subject;
        this.username = username;
        this.message = message;
        this.section = section;
        this.lecture = lecture;
    }

    @Override
    public String changeStatus(String newStatus, String approveName) {
        return "Status: " + newStatus + " Approved by: " + approveName;
    }
}
