package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private String timestamp; //วันเวลาที่คำข้อถูกสร้าง
    private String lastModifiedDateTime; //วันเวลาที่คำขอถูกแก้ไขมากที่สุด
    private String status; //สถานะคำร้อง (กำลังดำเนินการ ปฏิเสธ เสร็จสิ้น)
    private String requestType; //ประเภทคำร้อง
    private Student requester; //ผู้ยื่นคำร้อง (นิสิต)
    private String currentApprover; //ผู้อนุมัติปัจจุบัน (อาจารย์ที่ปรึกษา, เจ้าหน้าที่ภาควิชา, เจ้าหน้าที่คณะ)
    private String numberPhone; //เบอร์มือถือของผู้ยื่นคำร้อง
    private String pdfFilePath; //เก็บ FilePath ของคำร้อง pdf ที่แนบเข้าระบบ
    private List<String> statusLog; //เก็บประวัติการดำเนินการต่างๆข้องคำร้อง
    private List<String> approverList; //เก็บรายการคนที่อนุมัติคำร้อง String (ชื่อคนอนุมัติ - ตำแหน่ง)

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Constructor for creating a new request, with automatic timestamps
    public Request(String requestType, Student requester, String numberPhone) {
        this.requestType = requestType;
        this.timestamp = LocalDateTime.now().format(formatter); // Current time for new requests
        this.lastModifiedDateTime = LocalDateTime.now().format(formatter);
        this.statusLog = new ArrayList<>();
        this.approverList = new ArrayList<>();
        this.requester = requester;
        this.currentApprover = "อาจารย์ที่ปรึกษา";
        this.numberPhone = numberPhone;
        this.status = "กำลังดำเนินการ";
        addStatusLog("ใบคำร้องใหม่");
        addStatusLog("คำร้องส่งต่อให้อาจารย์ที่ปรึกษา");
    }

    // Constructor สำหรับอ่านไฟล์จาก CSV
    public Request(String timestamp, String requestType, String status, Student requester, String currentApprover, String numberPhone, String lastModifiedDateTime, String pdfFilePath, List<String> statusLog, List<String> approverList) {
        this.requestType = requestType;
        this.timestamp = timestamp;
        this.lastModifiedDateTime = lastModifiedDateTime;
        this.statusLog = statusLog != null ? new ArrayList<>(statusLog) : new ArrayList<>();
        this.approverList = approverList != null ? new ArrayList<>(approverList) : new ArrayList<>();
        this.requester = requester;
        this.currentApprover = currentApprover;
        this.numberPhone = numberPhone;
        this.status = status;
        this.pdfFilePath = pdfFilePath;
    }

    //ดำเนินการคำร้อง (เรียกใช้จาก method นี้)
    public void processRequest(String approver, String decision, String detail) {
        if ("อนุมัติ".equalsIgnoreCase(decision)) {
            handleApproval(approver);
        } else if ("ปฏิเสธ".equalsIgnoreCase(decision)) {
            if (detail == null || detail.trim().isEmpty()) {
                throw new IllegalArgumentException("การปฏิเสธคำร้องต้องระบุเหตุผล");
            }
            handleRejection(detail);
        } else if ("สิ้นสุด".equalsIgnoreCase(decision)) {
            handleFinish(approver);
        }
    }

    private void handleApproval(String approver) {
        if ("อาจารย์ที่ปรึกษา".equalsIgnoreCase(this.currentApprover)) {
            this.setCurrentApprover("เจ้าหน้าที่ภาควิชา");
            this.addStatusLog("อนุมัติโดยอาจารย์ที่ปรึกษา");
            this.addStatusLog("คำร้องส่งต่อให้หัวหน้าภาควิชา");
            this.addApprover(approver);
        } else if ("เจ้าหน้าที่ภาควิชา".equalsIgnoreCase(this.currentApprover)) {
            this.setCurrentApprover("เจ้าหน้าที่คณะ");
            this.addStatusLog("อนุมัติโดยหัวหน้าภาควิชา");
            this.addStatusLog("คำร้องส่งต่อให้คณบดี");
            this.addApprover(approver);
        } else if ("เจ้าหน้าที่คณะ".equalsIgnoreCase(this.currentApprover)) {
            this.addApprover(approver);
            this.addStatusLog("อนุมัติโดยคณบดี");
            this.setStatus("เสร็จสิ้น");
        }
    }

    private void handleRejection(String detail) {
        if ("อาจารย์ที่ปรึกษา".equalsIgnoreCase(this.currentApprover)) {
            this.setStatus("ปฏิเสธ");
            this.addStatusLog("ปฏิเสธโดยอาจารย์ที่ปรึกษา  บันทึกเหตุผล: " + detail);
        } else if ("เจ้าหน้าที่ภาควิชา".equalsIgnoreCase(this.currentApprover)) {
            this.setStatus("ปฏิเสธ");
            this.addStatusLog("ปฏิเสธโดยหัวหน้าภาควิชา  บันทึกเหตุผล: " + detail);
        } else if ("เจ้าหน้าที่คณะ".equalsIgnoreCase(this.currentApprover)) {
            this.setStatus("ปฏิเสธ");
            this.addStatusLog("ปฏิเสธโดยคณบดี  บันทึกเหตุผล: " + detail);
        }
    }

    private void handleFinish(String approver) {
        if ("เจ้าหน้าที่ภาควิชา".equalsIgnoreCase(this.currentApprover)) {
            this.addApprover(approver);
            this.setStatus("เสร็จสิ้น");
            this.addStatusLog("อนุมัติโดยหัวหน้าภาควิชา");
        }
    }

    // Getters
    public String getRequestType() {
        return requestType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getStatusLog() {
        return statusLog;
    }

    //ใช้สำหรับในการดึงไปแสดงในหน้า TableView
    public String getRecentStatusLog() {
        if (statusLog.isEmpty()) {
            return null;
        }

        for (int i = statusLog.size() - 1; i >= 0; i--) {
            String lastLog = statusLog.get(i);
            String[] parts = lastLog.split(" {2}");

            for (String part : parts) {
                if (!part.contains("บันทึกเหตุผล")) {
                    String[] logMessage = part.split(" - ", 2);
                    return logMessage.length == 2 ? logMessage[1].trim() : logMessage[0].trim();
                }
            }
        }

        return null;
    }

    public String getRejectionReason() {
            String logEntry = statusLog.getLast();
            if (logEntry.contains("บันทึกเหตุผล: ")) {
                String[] parts = logEntry.split("บันทึกเหตุผล: ", 2);
                if (parts.length == 2) {
                    return parts[1].trim();
                }
        }
        return "-";
    }

    public String getStatus() {
        return status;
    }

    public Student getRequester() {
        return requester;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public String getCurrentApprover() {
        return currentApprover;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public List<String> getApproverList(){
        return approverList;
    }

    public void addApprover(String approver) {
        approverList.add(approver);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentApprover(String currentApprover) {
        this.currentApprover = currentApprover;
    }

    public void setPdfFilePath(String filePath){
        this.pdfFilePath = filePath;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void addStatusLog(String status) {
        String logTime = LocalDateTime.now().format(formatter);
        this.lastModifiedDateTime = logTime;
        statusLog.add(logTime + " - " + status);
    }
}