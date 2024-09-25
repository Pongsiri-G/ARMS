package ku.cs.models;

import java.util.ArrayList;

public interface Officer {
    void addRequestManager(String name, String position);
    void removeRequestManager(RequestHandlingOfficer officer);
    void changeRequestManager(RequestHandlingOfficer officer, String name, String position);
    ArrayList<String> getAvailablePositions();

}
