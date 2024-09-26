package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestHandlingOfficer {
    // นี่ไม่ใช่คำร้องน่่เป็นคนจัดการคำร้องพวก หัวหน้าภาควิชา
    private String facDep;
    private String position;
    private String name;
    private String lastUpdate;

    // Begin Constructor
    public RequestHandlingOfficer(String facDep, String position, String name) {
        this.facDep = facDep;
        this.position = position;
        this.name = name;
        this.lastUpdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public RequestHandlingOfficer(String facDep, String position, String name, String lastUpdate) {
        this.facDep = facDep;
        this.position = position;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }
    // End Constructor

    public void setName(String name) {
        this.name = name;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public void setTimeStamp() {
        this.lastUpdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void update(String position, String name) {
        setPosition(position);
        setName(name);
        setTimeStamp();
    }


    public String getFacDep() {
        return facDep;
    }
    public String getName() {
        return name;
    }
    public String getPosition() {
        return position;
    }
    public String getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public String toString() {
        return "RequestHandlingOfficer{" +
                "position='" + position + '\'' +
                ", name='" + name + '\'' +
                ", timeStamp='" + lastUpdate + '\'' +
                '}';
    }



}
