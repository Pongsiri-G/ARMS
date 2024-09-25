package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestHandlingOfficer {
    // นี่ไม่ใช่คำร้องน่่เป็นคนจัดการคำร้องพวก หัวหน้าภาควิชา
    private String station; //Faculty or Department name
    private String name;
    private String position;
    private String timeStamp;

    // Begin Constructor
    public RequestHandlingOfficer(String station, String name, String position) {
        this.name = name;
        this.position = position;
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public RequestHandlingOfficer(String station, String name, String position, String timeStamp) {
        this.name = name;
        this.position = position;
        this.timeStamp = timeStamp;
    }
    // End Constructor

    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setTimeStamp() {
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
    @Override
    public String toString() {
        return position + name;
    }


}
