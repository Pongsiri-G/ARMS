package ku.cs.models;

import java.util.ArrayList;

public interface Officer {
    void loadRequestManage(ArrayList<RequestHandlingOfficer> approvers);
    void addRequestManager(String name, String position);
    void removeRequestManager(RequestHandlingOfficer officer);
    void updateRequestManager(RequestHandlingOfficer officer, String name, String position);
    ArrayList<String> getAvailablePositions();
    ArrayList<RequestHandlingOfficer> getRequestManagers();

}
