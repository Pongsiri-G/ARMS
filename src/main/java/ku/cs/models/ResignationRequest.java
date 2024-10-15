package ku.cs.models;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ResignationRequest extends Request{
    private String reason; // เหตุผลในการลาออก

    public ResignationRequest(Student requester, String numberPhone,String reason) {
        super("ลาออก", requester, numberPhone);
        this.reason = reason;

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")); // timestamp ช่วยแก้ปัญหาชื่อไฟล์ซ้ำกันได้
        String requestPdfPath = "data" + File.separator + "students_requests" + File.separator + requester.getStudentID() + File.separator + requester.getStudentID() + "-" + "คำร้องลาออก" + "_" + timeStamp + ".pdf";
        try {
            ResignationRequestPDF.createRequest(requestPdfPath, this); //สร้างไฟล์ pdf
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setPdfFilePath(requestPdfPath);
    }

    public ResignationRequest(String timestamp, String requestType, String status, Student requester, String currentApprover, String numberPhone, String reason, String lastModifiedDate, String pdfFilePath, List<String> statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDate, pdfFilePath, statusLog, approverList);
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

