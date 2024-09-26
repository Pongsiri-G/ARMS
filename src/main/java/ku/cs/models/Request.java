package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private String timeStamp;
    private String approveName;
    private String status;
    private String type;
    private String text;
    private String id;
    private String numberPhone;

    public Request(String timeStamp, String approveName, String status, String type, String text, String id, String numberPhone) {
        this.timeStamp = timeStamp;
        this.approveName = approveName;
        this.status = status;
        this.type = type;
        this.text = text;
        this.id = id;
        this.numberPhone = numberPhone;
    }

    // เดี๋ยวค่อยเปลียน
    public void setTimeStamp() {this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));}
    public void setApproveName(String approveName) {this.approveName = approveName;}

    public void changeStatus(String newStatus) {
            this.status = newStatus;
    }

    public String getStatus() {return status;}

    public String getType() {return timeStamp;}

    public String getApproveName() {return approveName;}

    public String getTimeStamp() {return timeStamp;}

    public String getText() {return text;}

    public String getId() {return id;}

    public String getNumberPhone() {return numberPhone;}

    public void setStatus(String newStatus) { status = newStatus;}

}
