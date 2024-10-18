package ku.cs.services;

import java.io.File;
import java.io.IOException;

import ku.cs.models.*;

public class RequestListFileDatasourceTest {
    private RequestListFileDatasource datasource;
    private UserListFileDatasource userDatasource;
    private UserList userList;
    private String testDirectory = "data/students_requests";
    private String testFileName = "requestlist.csv";

    public static void main(String[] args) throws IOException {
        RequestListFileDatasourceTest test = new RequestListFileDatasourceTest();
        test.runTests();
    }

    public void runTests() throws IOException {
        setup();
        //loadRequestList();
    }

    private void setup() {
        userDatasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        userList = userDatasource.readData();
        datasource = new RequestListFileDatasource(testDirectory, testFileName, userList);
       // RequestList requestList = datasource.readData();
        //clearTestFile();
//        Advisor advisor = (Advisor) userList.findUserByUsername("Jak");
//        ArrayList<Request> requests = advisor.getRequestsByAdvisor(requestList);
//        advisor.acceptRequest(requests.get(0));
//        datasource.writeData(requestList);
    }

    private void clearTestFile() {
        File file = new File(testDirectory + File.separator + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public RequestList loadRequestList() throws IOException {
        RequestList requestList = new RequestList();

        Request request1 = new SickLeaveRequest(
                (Student) userList.findUserByUsername("Artis"), "1234567890",
                "ลาป่วย", "เป็นไข้", "2023-01-10", "2023-01-15",
                "วิชาคณิตศาสตร์"
        );

        Request request2 = new ResignationRequest(
                (Student) userList.findUserByUsername("Artis"), "0987654321",
                "ย้ายมหาวิทยาลัย"
        );
        Request request3 = new LeaveOfAbsenceRequest(
                (Student) userList.findUserByUsername("Artis"), "1234509876",
                "ต้องการพักการเรียน",
                "456 Another St", "วิชาโปรแกรมมิ่ง",
                2, 2023, 2, 2023, 2, 2024
        );
        Request request4 = new SickLeaveRequest(
                (Student) userList.findUserByUsername("Artis"), "1234567890",
                "ลาป่วย", "เป็นคนหล่อ", "2023-01-10", "2023-01-15",
                "วิชาคณิตศาสตร์"
        );



        requestList.addRequest(request1);
        requestList.addRequest(request2);
        requestList.addRequest(request3);
        requestList.addRequest(request4);
        System.out.println(requestList);

        datasource.writeData(requestList);
        return requestList;
    }
}
