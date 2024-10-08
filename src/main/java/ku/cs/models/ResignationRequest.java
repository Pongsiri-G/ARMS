package ku.cs.models;

import java.util.List;

public class ResignationRequest extends Request{
    private String reason; // เหตุผลในการลาออก

    public ResignationRequest(String requester, String currentApprover, String numberPhone,String reason) {
        super("ลาออก", requester, currentApprover, numberPhone);
        this.reason = reason;
    }

    public ResignationRequest(String timestamp, String requestType, String status, String requester, String currentApprover, String numberPhone, String reason, String lastModifiedDate, List<String> statusLog, List<String> approverList) {
        super(timestamp, requestType, status, requester, currentApprover, numberPhone, lastModifiedDate, statusLog, approverList);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

