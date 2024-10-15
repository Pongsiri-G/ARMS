package ku.cs.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ku.cs.models.*;

public class RequestListFileDatasource implements Datasource<RequestList> {
    private String directoryName;
    private String fileName;
    private UserList userList;

    public RequestListFileDatasource(String directoryName, String fileName, UserList userList) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        this.userList = userList;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeData(RequestList requestList) {
        String filePath = directoryName + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (Request request : requestList.getRequests()) {
                String approverListStr = String.join("|", request.getApproverList());

                if (request instanceof LeaveOfAbsenceRequest) {
                    LeaveOfAbsenceRequest leaveRequest = (LeaveOfAbsenceRequest) request;
                    writer.write(String.join(",",
                            leaveRequest.getTimestamp(),
                            leaveRequest.getRequestType(),
                            leaveRequest.getStatus(),
                            leaveRequest.getRequester().getUsername(),
                            leaveRequest.getCurrentApprover(),
                            leaveRequest.getNumberPhone(),
                            escapeNewlines(leaveRequest.getReason()),
                            escapeNewlines(leaveRequest.getCurrentAddress()),
                            escapeNewlines(leaveRequest.getRegisteredCourses()),
                            String.valueOf(leaveRequest.getCurrentSemester()),
                            String.valueOf(leaveRequest.getCurrentAcademicYear()),
                            String.valueOf(leaveRequest.getFromSemester()),
                            String.valueOf(leaveRequest.getFromAcademicYear()),
                            String.valueOf(leaveRequest.getToSemester()),
                            String.valueOf(leaveRequest.getToAcademicYear()),
                            leaveRequest.getLastModifiedDateTime(),
                            leaveRequest.getPdfFilePath() == null ? "ไม่มีเอกสาร" : leaveRequest.getPdfFilePath(),
                            String.join("|", leaveRequest.getStatusLog()),
                            approverListStr
                    ));
                    writer.newLine();
                } else if (request instanceof ResignationRequest) {
                    ResignationRequest resignationRequest = (ResignationRequest) request;
                    writer.write(String.join(",",
                            resignationRequest.getTimestamp(),
                            resignationRequest.getRequestType(),
                            resignationRequest.getStatus(),
                            resignationRequest.getRequester().getUsername(),
                            resignationRequest.getCurrentApprover(),
                            resignationRequest.getNumberPhone(),
                            escapeNewlines(resignationRequest.getReason()),
                            resignationRequest.getLastModifiedDateTime(),
                            resignationRequest.getPdfFilePath() == null ? "ไม่มีเอกสาร" : resignationRequest.getPdfFilePath(),
                            String.join("|", resignationRequest.getStatusLog()),
                            approverListStr
                    ));
                    writer.newLine();
                } else if (request instanceof SickLeaveRequest) {
                    SickLeaveRequest sickLeaveRequest = (SickLeaveRequest) request;
                    writer.write(String.join(",",
                            sickLeaveRequest.getTimestamp(),
                            sickLeaveRequest.getRequestType(),
                            sickLeaveRequest.getStatus(),
                            sickLeaveRequest.getRequester().getUsername(),
                            sickLeaveRequest.getCurrentApprover(),
                            sickLeaveRequest.getNumberPhone(),
                            sickLeaveRequest.getLeaveType(),
                            sickLeaveRequest.getFromDateLeave(),
                            sickLeaveRequest.getToDateLeave(),
                            escapeNewlines(sickLeaveRequest.getReason()),
                            escapeNewlines(sickLeaveRequest.getRegisteredCourses()),
                            sickLeaveRequest.getLastModifiedDateTime(),
                            sickLeaveRequest.getPdfFilePath() == null ? "ไม่มีเอกสาร" : sickLeaveRequest.getPdfFilePath(),
                            String.join("|", sickLeaveRequest.getStatusLog()),
                            approverListStr
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String escapeNewlines(String input) {
        return input != null ? input.replace("\n", "\\n") : "";
    }



    @Override
    public RequestList readData() {
        RequestList requestList = new RequestList();
        String filePath = directoryName + File.separator + fileName;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) { continue; }
                String timestamp = data[0];
                String requestType = data[1];
                String status = data[2];
                String requesterUsername = data[3];
                String currentApprover = data[4];
                String numberPhone = data[5];

                Student requester = (Student) userList.findUserByUsername(requesterUsername);

                String approverListStr = data[data.length - 1];
                List<String> approverList = new ArrayList<>();
                if (!approverListStr.isEmpty()) {
                    approverList = Arrays.asList(approverListStr.split("\\|"));
                }

                if ("ลาพักการศึกษา".equalsIgnoreCase(requestType)) {
                    String reason = unescapeNewlines(data[6]);
                    String currentAddress = unescapeNewlines(data[7]);
                    String registeredCourses = unescapeNewlines(data[8]);
                    int currentSemester = Integer.parseInt(data[9]);
                    int currentAcademicYear = Integer.parseInt(data[10]);
                    int fromSemester = Integer.parseInt(data[11]);
                    int fromAcademicYear = Integer.parseInt(data[12]);
                    int toSemester = Integer.parseInt(data[13]);
                    int toAcademicYear = Integer.parseInt(data[14]);
                    String lastModifiedDate = data[15];
                    String pdfFilePath = data[16].equals("ไม่มีเอกสาร") ? null : data[16];
                    List<String> statusLog = Arrays.asList(data[17].split("\\|"));
                    LeaveOfAbsenceRequest leaveRequest = new LeaveOfAbsenceRequest(timestamp, requestType, status, requester, currentApprover, numberPhone,
                            reason, currentAddress, registeredCourses, currentSemester, currentAcademicYear, fromSemester, fromAcademicYear, toSemester, toAcademicYear,
                            lastModifiedDate, pdfFilePath, statusLog, approverList);
                    requestList.addRequest(leaveRequest);

                } else if ("ลาออก".equalsIgnoreCase(requestType)) {
                    String reason = unescapeNewlines(data[6]);
                    String lastModifiedDate = data[7];
                    String pdfFilePath = data[8].equals("ไม่มีเอกสาร") ? null : data[8];
                    List<String> statusLog = Arrays.asList(data[9].split("\\|"));
                    ResignationRequest resignationRequest = new ResignationRequest(timestamp, requestType, status, requester, currentApprover, numberPhone,
                            reason, lastModifiedDate, pdfFilePath, statusLog, approverList);
                    requestList.addRequest(resignationRequest);

                } else if ("ลาป่วยหรือลากิจ".equalsIgnoreCase(requestType)) {
                    String leaveType = data[6];
                    String fromDateLeave = data[7];
                    String toDateLeave = data[8];
                    String reason = unescapeNewlines(data[9]);
                    String registeredCourses = unescapeNewlines(data[10]);
                    String lastModifiedDate = data[11];
                    String pdfFilePath = data[12].equals("ไม่มีเอกสาร") ? null : data[12];
                    List<String> statusLog = Arrays.asList(data[13].split("\\|"));
                    SickLeaveRequest sickLeaveRequest = new SickLeaveRequest(timestamp, requestType, status, requester, currentApprover, numberPhone,
                            leaveType, fromDateLeave, toDateLeave, reason, registeredCourses, lastModifiedDate, pdfFilePath, statusLog, approverList);
                    requestList.addRequest(sickLeaveRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestList;
    }

    private String unescapeNewlines(String input) {
        return input != null ? input.replace("\\n", "\n") : "";
    }

}