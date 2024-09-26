package ku.cs.services;
import ku.cs.models.Student;
import ku.cs.models.StudentList;
public class StudentListHardCodeDatasource implements Datasource<StudentList>{

    public StudentList readData() {
        StudentList list = new StudentList();
        Student student1 = new Student("Jhon", "6610402213", "b66666@ku.th");
        Student student2 = new Student("Smith", "6610402215", "b77777@ku.th");
        Student student3 = new Student("Thuu", "6610402214", "b6655@ku.th");
        Student student4 = new Student("Yut", "6610402210", "58888@ku.th");
        Student student5 = new Student("Gai", "66104022111", "6666@ku.th");

        list.addStudent(student1);
        list.addStudent(student2);
        list.addStudent(student3);
        list.addStudent(student4);
        list.addStudent(student5);
        return list;
    }

    @Override
    public void writeData(StudentList data) {

    }

}
