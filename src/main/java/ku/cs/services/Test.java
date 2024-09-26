package ku.cs.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ku.cs.models.RequestHandlingOfficer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        RequestHandlingOfficersDataSource dataSource = new RequestHandlingOfficersDataSource("test/approver", "test-approver.csv");
        ArrayList<RequestHandlingOfficer> test = dataSource.readData();
        test.add(new RequestHandlingOfficer("test1", "tester"));
        test.add(new RequestHandlingOfficer("test2", "tester"));
        test.add(new RequestHandlingOfficer("test3", "tester"));
        dataSource.writeData(test);

        ArrayList<RequestHandlingOfficer> test2 = dataSource.readData();
        List<RequestHandlingOfficer> test3 = dataSource.readData();
        ObservableList<RequestHandlingOfficer> officers = FXCollections.observableList(test2);
        System.out.println(officers);


        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("erlj", "ewlrtj", "ewrt"));
        List<String> list = arrayList; // casting to List
        ObservableList<String> observableList = FXCollections.observableArrayList(list); // casting to ObservableList

        System.out.println(observableList); // Output: [erlj, ewlrtj, ewrt]

    }
}
