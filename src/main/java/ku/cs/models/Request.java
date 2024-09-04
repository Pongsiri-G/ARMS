package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Request {
    protected String timeStamp;
    protected String approveName;
    protected String status;


    Request(String status) {
        this.status = status;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public abstract String changeStatus(String newStatus, String approveName);

    public String getTimeStamp() {return timeStamp;}

    public String getStatus() {return status;}
}
