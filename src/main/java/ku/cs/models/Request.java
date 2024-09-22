package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private String timeStamp;
    private String approveName;
    private String status;
    private String type;
    private String text;
    private String subject;

    Request(String approveName, String status, String type, String text) {
        this.status = status;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.approveName = approveName;
        this.type = type;
        this.text = text;
    }

    Request(String approveName, String status, String type, String text, String subject) {
        this.status = status;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.approveName = approveName;
        this.type = type;
        this.text = text;
    }

    public void changeStatus(String newStatus) {
            this.status = newStatus;
    }

    public String getStatus() {return status;}

    public String getType() {return timeStamp;}

    public String getApproveName() {return approveName;}
}
