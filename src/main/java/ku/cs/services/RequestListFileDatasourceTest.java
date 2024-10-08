package ku.cs.services;

import java.io.File;

import ku.cs.models.RequestList;
import ku.cs.models.Request;
import ku.cs.models.LeaveOfAbsenceRequest;
import ku.cs.models.SickLeaveRequest;
import ku.cs.models.ResignationRequest;

public class RequestListFileDatasourceTest {
    private RequestListFileDatasource datasource;
    private String testDirectory = "data/test";
    private String testFileName = "requestlist.csv";

    public void runTests() {
        setup();
        loadRequestList();
    }

    private void setup() {
        datasource = new RequestListFileDatasource(testDirectory, testFileName);
        clearTestFile();
    }

    private void clearTestFile() {
        File file = new File(testDirectory + File.separator + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public RequestList loadRequestList() {
        RequestList requestList = new RequestList();

        Request request1 = new SickLeaveRequest(
                "Artis", "อาจารย์ที่ปรึกษา", "1234567890",
                "ลาป่วย", "เป็นไข้",
                "123 Some St", "2023-01-10", "2023-01-15",
                "วิชาคณิตศาสตร์"
        );

        Request request2 = new ResignationRequest(
                "Babie", "อาจารย์ที่ปรึกษา", "0987654321",
                "ย้ายมหาวิทยาลัย"
        );
        Request request3 = new LeaveOfAbsenceRequest(
                "Artis", "อาจารย์ที่ปรึกษา", "1234509876",
                "ต้องการพักการเรียน",
                "456 Another St", "วิชาโปรแกรมมิ่ง",
                2, 2023, 2, 2023, 2, 2024
        );
        Request request4 = new SickLeaveRequest(
                "Artis", "อาจารย์ที่ปรึกษา", "1234567890",
                "ลาป่วย", "เป็นคนหล่อ",
                "123 Some St", "2023-01-10", "2023-01-15",
                "วิชาคณิตศาสตร์"
        );



        requestList.addRequest(request1);
        requestList.addRequest(request2);
        requestList.addRequest(request3);
        requestList.addRequest(request4);

        datasource.writeData(requestList);
        return requestList;
    }
}
