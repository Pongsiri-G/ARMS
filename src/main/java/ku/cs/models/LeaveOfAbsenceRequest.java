package ku.cs.models;

import java.util.List;

// ขอลาพักการศึกษา
public class LeaveOfAbsenceRequest extends Request {
    private String reason; // สาเหตุที่ลา
    private String registeredCourses; // วิชาที่ลงทะเบียนเรียนไว้
    private String currentAddress; // ที่อยู่ปุัจจุบัน
    private int currentSemester; //ภาคการศึกษาปัจจุบัน
    private int currentAcademicYear; //ปีการศึกษาปัจจุบัน
    private int fromSemester; // ตั้งเเต่ภาคการศึกษา
    private int toSemester; // ถึงภาคศึกษา
    private int fromAcademicYear; // ตั้งแต่ปีการศึกษา
    private int toAcademicYear; // ถึงปีการศึกษา


    public LeaveOfAbsenceRequest(String requester, String currentApprover, String numberPhone,String reason, String currentAddress, String registeredCourses,
                                 int currentSemester, int currentAcademicYear,
                                 int fromSemester, int fromAcademicYear,
                                 int toSemester, int toAcademicYear) {
        super("ลาพักการศึกษา", requester, currentApprover, numberPhone);
        this.reason = reason;
        this.currentAddress = currentAddress;
        this.registeredCourses = registeredCourses;
        this.currentSemester = currentSemester;
        this.currentAcademicYear = currentAcademicYear;
        this.fromSemester = fromSemester;
        this.toSemester = toSemester;
        this.fromAcademicYear = fromAcademicYear;
        this.toAcademicYear = toAcademicYear;
    }

    public LeaveOfAbsenceRequest(String timestamp, String requestType, String status, String requester, String currentApprover, String numberPhone, String reason, String currentAddress, String registeredCourses, int currentSemester, int currentAcademicYear, int fromSemester, int fromAcademicYear, int toSemester, int toAcademicYear, String lastModifiedDateTime, List<String>statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDateTime, statusLog, approverList);
        this.reason = reason;
        this.currentAddress = currentAddress;
        this.registeredCourses = registeredCourses;
        this.currentSemester = currentSemester;
        this.currentAcademicYear = currentAcademicYear;
        this.fromSemester = fromSemester;
        this.toSemester = toSemester;
        this.fromAcademicYear = fromAcademicYear;
        this.toAcademicYear = toAcademicYear;
    }

    public String getReason() {
        return reason;
    }

    public String getRegisteredCourses() {
        return registeredCourses;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public int getCurrentAcademicYear() {
        return currentAcademicYear;
    }

    public int getFromSemester() {
        return fromSemester;
    }

    public int getToSemester() {
        return toSemester;
    }

    public int getFromAcademicYear() {
        return fromAcademicYear;
    }

    public int getToAcademicYear() {
        return toAcademicYear;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
