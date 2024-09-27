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
    private String name;
    private String faculty;
    private String department;

    public Request(String timeStamp, String approveName, String status, String type, String text, String id, String numberPhone) {
        this.timeStamp = timeStamp;
        this.approveName = approveName;
        this.status = status;
        this.type = type;
        this.text = text;
        this.id = id;
        this.numberPhone = numberPhone;
    }

    Request(String name, String faculty, String department, String status) {
        this.name = name;
        this.faculty = faculty;
        this.department = department;
        this.status = status;
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

    public String getName() {return name;}

    public String getFaculty() {return faculty;}

    public String getDepartment() {return department;}

    public String getText() {return text;}

    public String getId() {return id;}

    public String getNumberPhone() {return numberPhone;}

    public void setStatus(String newStatus) { status = newStatus;}

}
