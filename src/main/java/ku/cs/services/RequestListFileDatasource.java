package ku.cs.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ku.cs.models.RequestList;
import ku.cs.models.Request;
import ku.cs.models.LeaveOfAbsenceRequest;
import ku.cs.models.SickLeaveRequest;
import ku.cs.models.ResignationRequest;

public class RequestListFileDatasource implements Datasource<RequestList> {
    private String directoryName;
    private String fileName;

    public RequestListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
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
                            leaveRequest.getRequester(),
                            leaveRequest.getCurrentApprover(),
                            leaveRequest.getNumberPhone(),
                            leaveRequest.getReason(),
                            leaveRequest.getCurrentAddress(),
                            leaveRequest.getRegisteredCourses(),
                            String.valueOf(leaveRequest.getCurrentSemester()),
                            String.valueOf(leaveRequest.getCurrentAcademicYear()),
                            String.valueOf(leaveRequest.getFromSemester()),
                            String.valueOf(leaveRequest.getFromAcademicYear()),
                            String.valueOf(leaveRequest.getToSemester()),
                            String.valueOf(leaveRequest.getToAcademicYear()),
                            leaveRequest.getLastModifiedDateTime(),
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
                            resignationRequest.getRequester(),
                            resignationRequest.getCurrentApprover(),
                            resignationRequest.getNumberPhone(),
                            resignationRequest.getReason(),
                            resignationRequest.getLastModifiedDateTime(),
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
                            sickLeaveRequest.getRequester(),
                            sickLeaveRequest.getCurrentApprover(),
                            sickLeaveRequest.getNumberPhone(),
                            sickLeaveRequest.getCurrentAddress(),
                            sickLeaveRequest.getLeaveType(),
                            sickLeaveRequest.getFromDateLeave(),
                            sickLeaveRequest.getToDateLeave(),
                            sickLeaveRequest.getReason(),
                            sickLeaveRequest.getRegisteredCourses(),
                            sickLeaveRequest.getLastModifiedDateTime(),
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


    @Override
    public RequestList readData() {
        RequestList requestList = new RequestList();
        String filePath = directoryName + File.separator + fileName;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String timestamp = data[0];
                String requestType = data[1];
                String status = data[2];
                String requester = data[3];
                String currentApprover = data[4];
                String numberPhone = data[5];

                String approverListStr = data[data.length - 1];
                List<String> approverList = new ArrayList<>();
                if (!approverListStr.isEmpty()) {
                    approverList = Arrays.asList(approverListStr.split("\\|"));
                }

                if ("ลาพักการศึกษา".equalsIgnoreCase(requestType)) {
                    String reason = data[6];
                    String currentAddress = data[7];
                    String registeredCourses = data[8];
                    int currentSemester = Integer.parseInt(data[9]);
                    int currentAcademicYear = Integer.parseInt(data[10]);
                    int fromSemester = Integer.parseInt(data[11]);
                    int fromAcademicYear = Integer.parseInt(data[12]);
                    int toSemester = Integer.parseInt(data[13]);
                    int toAcademicYear = Integer.parseInt(data[14]);
                    String lastModifiedDate = data[15];
                    List<String> statusLog = Arrays.asList(data[16].split("\\|"));
                    LeaveOfAbsenceRequest leaveRequest = new LeaveOfAbsenceRequest(timestamp, requestType, status, requester, currentApprover, numberPhone,
                            reason, currentAddress, registeredCourses, currentSemester, currentAcademicYear, fromSemester, fromAcademicYear, toSemester, toAcademicYear,
                            lastModifiedDate, statusLog, approverList);
                    requestList.addRequest(leaveRequest);

                } else if ("ลาออก".equalsIgnoreCase(requestType)) {
                    String reason = data[6];
                    String lastModifiedDate = data[7];
                    List<String> statusLog = Arrays.asList(data[8].split("\\|"));
                    ResignationRequest resignationRequest = new ResignationRequest(timestamp, requestType, status, requester, currentApprover, numberPhone,
                            reason, lastModifiedDate, statusLog, approverList);
                    requestList.addRequest(resignationRequest);

                } else if ("ลาป่วยหรือลากิจ".equalsIgnoreCase(requestType)) {
                    String currentAddress = data[6];
                    String leaveType = data[7];
                    String fromDateLeave = data[8];
                    String toDateLeave = data[9];
                    String reason = data[10];
                    String registeredCourses = data[11];
                    String lastModifiedDate = data[12];
                    List<String> statusLog = Arrays.asList(data[13].split("\\|"));
                    SickLeaveRequest sickLeaveRequest = new SickLeaveRequest(timestamp, requestType, status, requester, currentApprover, numberPhone, currentAddress,
                            leaveType, fromDateLeave, toDateLeave, reason, registeredCourses, lastModifiedDate, statusLog, approverList);
                    requestList.addRequest(sickLeaveRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestList;
    }
}
