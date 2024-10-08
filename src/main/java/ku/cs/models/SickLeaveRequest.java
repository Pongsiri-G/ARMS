package ku.cs.models;

import java.util.List;

public class SickLeaveRequest extends Request{
    private String currentAddress; // ที่อยู่ปัจจุบันที่ติดต่อได้
    private String leaveType; //ประเภทการลา (ลาป่วย หรือ ลากิจ)
    private String fromDateLeave; // ลาตั้งเเต่วันที่
    private String toDateLeave; // ถึงวันที่
    private String registeredCourses; // รายวิชาเรียนในช่วงที่ลา
    private String reason; // เหตุผลที่ลา

    public SickLeaveRequest(String requester, String currentApprover, String numberPhone, String leaveType, String reason, String currentAddress, String fromDateLeave, String toDateLeave, String registeredCourses) {
        super("ลาป่วยหรือลากิจ", requester, currentApprover, numberPhone);
        this.currentAddress = currentAddress;
        this.leaveType = leaveType;
        this.reason = reason;
        this.fromDateLeave = fromDateLeave;
        this.toDateLeave = toDateLeave;
        this.registeredCourses = registeredCourses;
    }

    public SickLeaveRequest(String timestamp, String requestType, String status, String requester, String currentApprover, String numberPhone, String currentAddress, String leaveType, String fromDateLeave, String toDateLeave, String reason, String registeredCourses, String lastModifiedDate, List<String> statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, numberPhone, currentApprover, lastModifiedDate, statusLog, approverList);
        this.currentAddress = currentAddress;
        this.leaveType = leaveType;
        this.reason = reason;
        this.fromDateLeave = fromDateLeave;
        this.toDateLeave = toDateLeave;
        this.registeredCourses = registeredCourses;
    }

    public String getLeaveType() { return leaveType; }

    public String getCurrentAddress() { return currentAddress; }

    public String getFromDateLeave() { return fromDateLeave; }

    public String getToDateLeave() { return toDateLeave; }

    public String getRegisteredCourses() { return registeredCourses; }

    public String getReason() { return reason; }

}
