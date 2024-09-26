package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    private String timeStamp;
    private String status;
    private String name;
    private String faculty;
    private String department;
    private String type;
    private String text;
    private String subject;

    private void init(String status) {
        this.status = status;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Request(String status, String type, String text, String subject, String name) {
        init(status);
        this.name = name;
        this.type = type;
        this.text = text;
        this.subject = subject;
    }

    public Request(String name, String faculty, String department, String status) {
        init(status);
        this.name = name;
        this.faculty = faculty;
        this.department = department;
        this.type = null;
        this.text = null;
        this.subject = null;
    }

    public void changeStatus(String newStatus) {
            this.status = newStatus;
    }

    public String getStatus() {return status;}

    public String getType() {return timeStamp;}

    public String getName() {return name;}

    public String getFaculty() {return faculty;}

    public String getDepartment() {return department;}
}
