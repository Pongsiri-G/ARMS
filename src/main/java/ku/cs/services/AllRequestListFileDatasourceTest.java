package ku.cs.services;

import ku.cs.models.*;

import java.io.File;

public class AllRequestListFileDatasourceTest {
    private static RequestListFileDatasource datasource;
    private static String testDirectory = "data";
    private static String testFileName = "all-request.csv";

    public static void setup(){
        datasource = new RequestListFileDatasource(testDirectory, testFileName);
        clearTestFile();
    }

    private static void clearTestFile() {
        File file = new File(testDirectory + File.separator + testFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void main(String[] args) {
        setup();
        RequestList requestList = new RequestList();
        Request req1 = new Request("นายแรก", "วิทยาศาสตร์", "เคมี", "คำร้องถูกอนุมัติ");
        Request req2 = new Request("นายก่อนวันพฤหัส", "สังคมศาสตร์", "การเมืองการปกครอง", "คำร้องถูกปฎิเสธ");
        Request req3 = new Request("นายบอม", "แพทย์ศาสตร์", "ไสยแพทย์", "คำร้องถูกอนุมัติ");

        requestList.addRequest(req1);
        requestList.addRequest(req2);
        requestList.addRequest(req3);

        datasource.writeData(requestList);

        RequestList test = datasource.readData();
        for (Request request : test.getRequests()) {
            System.out.println(request.getName() + ", " + request.getFaculty() + ", " + request.getDepartment() + ", " + request.getStatus());
        }

    }
}
