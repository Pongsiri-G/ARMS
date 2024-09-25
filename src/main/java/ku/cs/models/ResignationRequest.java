package ku.cs.models;

public class ResignationRequest extends Request{
    private int semester; // ภาคเรียน
    private int academicYear; // ปีการศึกษา
    private String courseId; // รหัสวิชา
    private String email;

    ResignationRequest(String email, int semester, int academicYear, String courseId, String timeStamp, String approveName, String status, String type, String text, String id, String numberPhone) {
        super(timeStamp,approveName,status,type,text,id,numberPhone);
        this.email = email;
        this.semester = semester;
        this.academicYear = academicYear;
        this.courseId = courseId;
    }

    public String getEmail() { return email; }

    public int getSemester() { return semester; }

    public int getAcademicYear() { return academicYear; }

    public String getCourseId() { return courseId; }

}
