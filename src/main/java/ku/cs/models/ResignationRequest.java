package ku.cs.models;

import java.util.List;

public class ResignationRequest extends Request{
    private String reason; // เหตุผลในการลาออก

    public ResignationRequest(Student requester, String numberPhone,String reason) {
        super("ลาออก", requester, numberPhone);
        this.reason = reason;
    }

    public ResignationRequest(String timestamp, String requestType, String status, Student requester, String currentApprover, String numberPhone, String reason, String lastModifiedDate, List<String> statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDate, statusLog, approverList);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "เรียน " + getRequester().getStudentAdvisor().getName() + " (อาจารย์ที่ปรึกษา)"
                + "\nชื่อนิสิต " + getRequester().getName() + "     รหัสประจำตัวนิสิต " + getRequester().getStudentID()
                + "\nคณะ " + getRequester().getEnrolledFaculty().getFacultyName() + "     สาขาวิชาเอก " + getRequester().getEnrolledDepartment().getDepartmentName()
                + "\nหมายเลขโทรศัพท์ " + getNumberPhone()
                + "\nมีความประสงค์ขอลาออก เนื่องจาก  " + getReason()
                + "\nจึงขอลาออกตั้งแต่บัดนี้เป็นต้นไป และข้าพเจ้าไม่มีหนี้สินค้างชำระ";
    }
}

