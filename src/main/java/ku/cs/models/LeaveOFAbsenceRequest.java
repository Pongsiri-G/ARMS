package ku.cs.models;

// ขอลาพักการศึกษา
public class LeaveOFAbsenceRequest extends Request {
    private String reason; // สาเหตุที่ลา
    private String lectureName; // อาจารย์ที่สอน
    private String courseId; // รหัสวิชา
    private int semesters; // เทอม/ภาคเรียน
    private int fromSemeters; // ตั้งเเต่ภาค
    private int toSemesters; // ถึงภาค
    private int academicYear; // ปีการศึกษา
    private int academicLevel; // นิสิตชั้นปี

    LeaveOFAbsenceRequest(String reason, String advisorName, String courseId, int semesters, int fromSemeters, int toSemesters, int academicYear, int academicLevel,String timeStamp,String approveName, String status, String type, String text, String id, String numberPhone) {
        super(timeStamp,approveName,status,type,text,id,numberPhone);
        this.reason = reason;
        this.lectureName = advisorName;
        this.courseId = courseId;
        this.semesters = semesters;
        this.fromSemeters = fromSemeters;
        this.toSemesters = toSemesters;
        this.academicYear = academicYear;
        this.academicLevel = academicLevel;
    }

    public String getReason() { return reason; }

    public String getLectureName() { return lectureName; }

    public String getCourseId() { return courseId; }

    public int getSemesters() { return semesters; }

    public int getFromSemeters() { return fromSemeters; }

    public int getToSemesters() { return toSemesters; }

    public int getAcademicYear() { return academicYear; }

    public int getAcademicLevel() { return academicLevel; }

    public void setReason(String reason) { this.reason = reason; }

}
