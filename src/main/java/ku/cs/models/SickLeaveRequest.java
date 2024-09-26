package ku.cs.models;

public class SickLeaveRequest extends Request{
    private String location; // ที่อยู่ปัจจุบันที่ติดต่อได้
    private String fromDateLeave; // ลาตั้งเเต่วันที่
    private String toDateLeave; // ถึงวันที่
    private String courseId; // รหัสวิชา
    private String reason; // เหตุผลที่ลา
    private Request request;

    SickLeaveRequest(String timeStamp,String approveName, String status, String type, String text, String id, String numberPhone, String location, String fromDateLeave, String toDateLeave, String courseId, String reason) {
        super(timeStamp,approveName, status, type, text, id, numberPhone);
        this.location = location;
        this.fromDateLeave = fromDateLeave;
        this.toDateLeave = toDateLeave;
        this.courseId = courseId;
        this.reason = reason;
    }

    public String getLocation() { return location; }

    public String getFromDateLeave() { return fromDateLeave; }

    public String getToDateLeave() { return toDateLeave; }

    public String getCourseId() { return courseId; }

    public String getReason() { return reason; }

}
