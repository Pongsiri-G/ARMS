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


    public LeaveOfAbsenceRequest(Student requester, String numberPhone,String reason, String currentAddress, String registeredCourses,
                                 int currentSemester, int currentAcademicYear,
                                 int fromSemester, int fromAcademicYear,
                                 int toSemester, int toAcademicYear) {
        super("ลาพักการศึกษา", requester, numberPhone);
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

    public LeaveOfAbsenceRequest(String timestamp, String requestType, String status, Student requester, String currentApprover, String numberPhone, String reason, String currentAddress, String registeredCourses, int currentSemester, int currentAcademicYear, int fromSemester, int fromAcademicYear, int toSemester, int toAcademicYear, String lastModifiedDateTime, String pdfFilePath, List<String>statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDateTime, pdfFilePath, statusLog, approverList);
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

    public int calculateTotalSemesters() {
        if (fromAcademicYear == toAcademicYear) {
            return toSemester - fromSemester + 1;
        }

        int semestersInFromYear = 2 - fromSemester + 1;

        int yearsBetween = toAcademicYear - fromAcademicYear - 1;
        int semestersInBetweenYears = yearsBetween * 2;

        int semestersInToYear = toSemester;

        return semestersInFromYear + semestersInBetweenYears + semestersInToYear;
    }

    @Override
    public String toString() {
        return "เรียน " + getRequester().getStudentAdvisor().getName() + " (อาจารย์ที่ปรึกษา)"
                + "\nชื่อนิสิต " + getRequester().getName() + "     รหัสประจำตัวนิสิต " + getRequester().getStudentID()
                + "\nคณะ " + getRequester().getEnrolledFaculty().getFacultyName() + "     สาขาวิชาเอก " + getRequester().getEnrolledDepartment().getDepartmentName()
                + "\nหมายเลขโทรศัพท์ " + getNumberPhone() + "     ที่อยู่ปัจจุบัน " + getCurrentAddress()
                + "\nสาเหตุที่ลา " + getReason()
                + "\nมีความประสงค์ขอลาพักการศึกษาเป็นจำนวน " + calculateTotalSemesters() + " ภาคการศึกษา     ตั้งแต่ภาค " +  getFromSemester() + " ปีการศึกษา " + getFromAcademicYear() + "     ถึงภาค " + getToSemester() + " ปีการศึกษา " + getToAcademicYear()
                + "\nอนึ่ง ข้าพเจ้าได้ลงทะเบียนไว้ในภาค " + getCurrentSemester() + " ปีการศึกษา " + getCurrentAcademicYear() + " ดังนี้"
                + "\n" + getRegisteredCourses()
                + "\nจึงเรียนมาเพื่อโปรดพิจารณา";
    }
}
