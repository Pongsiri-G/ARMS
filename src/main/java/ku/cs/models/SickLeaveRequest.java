package ku.cs.models;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class SickLeaveRequest extends Request{
    private String leaveType; //ประเภทการลา (ลาป่วย หรือ ลากิจ)
    private String fromDateLeave; // ลาตั้งเเต่วันที่
    private String toDateLeave; // ถึงวันที่
    private String registeredCourses; // รายวิชาเรียนในช่วงที่ลา
    private String reason; // เหตุผลที่ลา

    public SickLeaveRequest(Student requester, String numberPhone, String leaveType, String reason, String fromDateLeave, String toDateLeave, String registeredCourses) {
        super("ลาป่วยหรือลากิจ", requester, numberPhone);
        this.leaveType = leaveType;
        this.reason = reason;
        this.fromDateLeave = fromDateLeave;
        this.toDateLeave = toDateLeave;
        this.registeredCourses = registeredCourses;
    }

    public SickLeaveRequest(String timestamp, String requestType, String status, Student requester, String currentApprover, String numberPhone, String leaveType, String fromDateLeave, String toDateLeave, String reason, String registeredCourses, String lastModifiedDate, List<String> statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDate, statusLog, approverList);
        this.leaveType = leaveType;
        this.reason = reason;
        this.fromDateLeave = fromDateLeave;
        this.toDateLeave = toDateLeave;
        this.registeredCourses = registeredCourses;
    }

    public long findDiffDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate fromDate = LocalDate.parse(fromDateLeave, formatter);
        LocalDate toDate = LocalDate.parse(toDateLeave, formatter);

        return ChronoUnit.DAYS.between(fromDate, toDate) + 1;
    }


    public String getLeaveType() { return leaveType; }

    public String getFromDateLeave() { return fromDateLeave; }

    public String getToDateLeave() { return toDateLeave; }

    public String getRegisteredCourses() { return registeredCourses; }

    public String getReason() { return reason; }

    @Override
    public String toString() {
        return "เรียน " + getRequester().getStudentAdvisor().getName() + " (อาจารย์ที่ปรึกษา)"
                + "\nชื่อนิสิต " + getRequester().getName() + "     รหัสประจำตัวนิสิต " + getRequester().getStudentID()
                + "\nคณะ " + getRequester().getEnrolledFaculty().getFacultyName() + "     สาขาวิชาเอก " + getRequester().getEnrolledDepartment().getDepartmentName()
                + "\nหมายเลขโทรศัพท์ " + getNumberPhone()
                + "\nมีความประสงค์ขอ " + getLeaveType()
                + "\nจึงขอหยุดเรียน มีกำหนดระยะเวลา " + findDiffDay() + " นับตั้งแต่วันที่ " + getFromDateLeave() + " ถึงวันที่ " + getToDateLeave()
                + "\nเนื่องจาก " + getReason()
                + "\nโดยมีวิชาที่ขอลาหยุดเรียนดังต่อไปนี้"
                + "\n" + getRegisteredCourses();
    }
}
