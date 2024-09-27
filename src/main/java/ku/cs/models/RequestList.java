package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    private ArrayList<Object> objects;
    private ArrayList<Request> requests;
    private ArrayList<ResignationRequest> resignationRequests;
    private ArrayList<SickLeaveRequest> sickLeaveRequests;
    private ArrayList<LeaveOFAbsenceRequest> leaveOFAbsenceRequests;
    private int allRequest;
    private int approvedRequest;

    // contructor
    public RequestList() {
        requests = new ArrayList<>();
        resignationRequests = new ArrayList<>();
        sickLeaveRequests = new ArrayList<>();
        leaveOFAbsenceRequests = new ArrayList<>();
        allRequest = 0;
        approvedRequest = 0;
    }

    //เช็คว่าสถานะเป็นยังไงบ้างในคำร้อง เรียกดูน่ะจ๊ะหรือไม่น่ามีหรือมียังไงบอกนะจ๊ะ
    public String checkStatus(Object object) {
        if (object instanceof SickLeaveRequest) {
            SickLeaveRequest sickLeaveRequest = (SickLeaveRequest) object;
            for (SickLeaveRequest request : sickLeaveRequests) {
                return request.getStatus();
            }
        }if (object instanceof LeaveOFAbsenceRequest) {
            LeaveOFAbsenceRequest leaveOFAbsenceRequest = (LeaveOFAbsenceRequest) object;
            for (LeaveOFAbsenceRequest request : leaveOFAbsenceRequests) {
                return request.getStatus();
            }
        }if (object instanceof ResignationRequest) {
            ResignationRequest resignationRequest = (ResignationRequest) object;
            for (ResignationRequest request : resignationRequests) {
                return request.getStatus();
            }
        }
        return null;
    }

    //เปลี่ยนสถานะคำร้องนะจ๊ะ
    public void updateStatus(Object object, String newStatus) {
        if (object instanceof SickLeaveRequest) {
            SickLeaveRequest sickLeaveRequest = (SickLeaveRequest) object;
            sickLeaveRequest.setStatus(newStatus);
        }if (object instanceof LeaveOFAbsenceRequest) {
            LeaveOFAbsenceRequest leaveOFAbsenceRequest = (LeaveOFAbsenceRequest) object;
            leaveOFAbsenceRequest.setStatus(newStatus);
        }if (object instanceof ResignationRequest) {
            ResignationRequest resignationRequest = (ResignationRequest) object;
            resignationRequest.setStatus(newStatus);
        }
    }

    //ต่อมาก็ใส่เหตุลผลตอนเเรกว่าจะรวมกับสถานะเเต่ถ้าเรายอมรับ มันก็ไม่ต้องใส่เหตุผลเลยเเยกกันน่ะจ๊ะ
    public void updateReason(Object object, String newReason) {
        if (object instanceof SickLeaveRequest) {
            SickLeaveRequest sickLeaveRequest = (SickLeaveRequest) object;
            sickLeaveRequest.setStatus(newReason);
        }if (object instanceof LeaveOFAbsenceRequest) {
            LeaveOFAbsenceRequest leaveOFAbsenceRequest = (LeaveOFAbsenceRequest) object;
            leaveOFAbsenceRequest.setStatus(newReason);
        }if (object instanceof ResignationRequest) {
            ResignationRequest resignationRequest = (ResignationRequest) object;
            resignationRequest.setStatus(newReason);
        }
    }

    //เช้คว่าสร้างคำร้องอะไรละส่งเพิ่มคำร้องเเค่นั้นเลยจ๊ะ
    public void processRequest(Object obj) {
        if (obj instanceof SickLeaveRequest) {
            SickLeaveRequest sickLeaveRequest = (SickLeaveRequest) obj;
            // เรียก function addNewRequest สำหรับ SickLeaveRequest
            addNewRequest(
                    sickLeaveRequest.getTimeStamp(),
                    sickLeaveRequest.getApproveName(),
                    sickLeaveRequest.getStatus(),
                    sickLeaveRequest.getType(),
                    sickLeaveRequest.getText(),
                    sickLeaveRequest.getId(),
                    sickLeaveRequest.getNumberPhone(),
                    sickLeaveRequest.getLocation(),
                    sickLeaveRequest.getFromDateLeave(),
                    sickLeaveRequest.getToDateLeave(),
                    sickLeaveRequest.getCourseId(),
                    sickLeaveRequest.getReason()
            );
        } else if (obj instanceof ResignationRequest) {
            ResignationRequest resignationRequest = (ResignationRequest) obj;
            // เรียก function addNewRequest สำหรับ ResignationRequest
            addNewRequest(
                    resignationRequest.getEmail(),
                    resignationRequest.getSemester(),
                    resignationRequest.getAcademicYear(),
                    resignationRequest.getCourseId(),
                    resignationRequest.getTimeStamp(),
                    resignationRequest.getApproveName(),
                    resignationRequest.getStatus(),
                    resignationRequest.getType(),
                    resignationRequest.getText(),
                    resignationRequest.getId(),
                    resignationRequest.getNumberPhone()
            );
        } else if (obj instanceof LeaveOFAbsenceRequest) {
            LeaveOFAbsenceRequest leaveOfAbsenceRequest = (LeaveOFAbsenceRequest) obj;
            // เรียก function addNewRequest สำหรับ LeaveOfAbsenceRequest
            addNewRequest(
                    leaveOfAbsenceRequest.getReason(),
                    leaveOfAbsenceRequest.getLectureName(),
                    leaveOfAbsenceRequest.getCourseId(),
                    leaveOfAbsenceRequest.getSemesters(),
                    leaveOfAbsenceRequest.getFromSemeters(),
                    leaveOfAbsenceRequest.getToSemesters(),
                    leaveOfAbsenceRequest.getAcademicYear(),
                    leaveOfAbsenceRequest.getAcademicLevel(),
                    leaveOfAbsenceRequest.getTimeStamp(),
                    leaveOfAbsenceRequest.getApproveName(),
                    leaveOfAbsenceRequest.getStatus(),
                    leaveOfAbsenceRequest.getType(),
                    leaveOfAbsenceRequest.getText(),
                    leaveOfAbsenceRequest.getId(),
                    leaveOfAbsenceRequest.getNumberPhone()
            );
        }
    }



    // ลาป่วย/กิจ
    public void addNewRequest(String timeStamp,String approveName, String status, String type, String text, String id, String numberPhone, String location, String fromDateLeave, String toDateLeave, String courseId, String reason) {
        allRequest++;
        sickLeaveRequests.add(new SickLeaveRequest(timeStamp, approveName, status, type, text, id, numberPhone, location, fromDateLeave, toDateLeave, courseId, reason));
    }

    //ของดเรียนบางวิชาล่าช้า
    public void addNewRequest(String email, int semester, int academicYear, String courseId, String timeStamp,String approveName, String status, String type, String text, String id, String numberPhone) {
        allRequest++;
        resignationRequests.add(new ResignationRequest(email, semester, academicYear, courseId, timeStamp, approveName, status, type, text, id, numberPhone));
    }
    //ลากพันร้อนนะอันนี้รู้สึก translate มา
    public void addNewRequest(String reason, String advisorName, String courseId, int semesters, int fromSemeters, int toSemesters, int academicYear, int academicLevel,String timeStamp,String approveName, String status, String type, String text, String id, String numberPhone) {
        allRequest++;
        leaveOFAbsenceRequests.add(new LeaveOFAbsenceRequest(reason, advisorName, courseId, semesters, fromSemeters, toSemesters, academicYear, academicLevel, timeStamp, approveName, status, type, text, id, numberPhone));
    }
    // ping : สำหรับแสดงผลใน table view ของ admin หน้า dashboard
    public void addTableRequest(String name, String faculty, String department, String status) {
        allRequest++;
        Request request = new Request(name, faculty, department, status);
        requests.add(request);
        if (request.getStatus().equals("คำร้องถูกอนุมัติ")) approvedRequest++;
    }
    //อันนี้มีไว้ก่อนไม่รู้ทำไมเหมือนกันไม่เอาค่อยลบออก
    public Request findRequestByStatus(String status) {
        for (Request request : requests) {
            if (request.getStatus().equals(status)) {
                return request;
            }
        }
        return null;
    }
    //return ต่างๆ
    public int getAllRequest() {return allRequest;}

    public int getApprovedRequest() {return approvedRequest;}

    public ArrayList<Request> getRequests() {return requests;}

    public ArrayList<ResignationRequest> getResignationRequests() {return resignationRequests;}

    public ArrayList<SickLeaveRequest> getSickLeaveRequests() { return sickLeaveRequests;}

    public ArrayList<LeaveOFAbsenceRequest> getLeaveOFAbsenceRequests() { return leaveOFAbsenceRequests;}
}
