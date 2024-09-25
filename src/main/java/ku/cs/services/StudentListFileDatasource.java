package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.util.*;

public class StudentListFileDatasource implements Datasource<StudentList> {
    private String directoryName;
    private String studentListFileName;

    public StudentListFileDatasource(String directoryName, String studentListFileName) {
        this.directoryName = directoryName;
        this.studentListFileName = studentListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File studentListFile = new File(directoryName + File.separator + studentListFileName);
        if (!studentListFile.exists()) {
            try {
                studentListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public StudentList readData() {
        StudentList studentList = new StudentList();  // แทนที่จะเป็น List<Student>
        try {
            File file = new File(directoryName + File.separator + studentListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length < 5) continue; // ข้ามบรรทัดที่ไม่สมบูรณ์

                String facultyName = data[0];
                String departmentName = data[1];
                String studentName = data[2];
                String studentId = data[3];
                String email = data[4];

                Faculty faculty = new Faculty(facultyName);
                Department department = new Department(departmentName);
                Student student = new Student(null, null, studentName, faculty, department, studentId, email, true, false);

                studentList.addStudent(student);  // เพิ่มเข้า StudentList
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("ไม่พบไฟล์", e);
        }
        return studentList;
    }

    @Override
    public void writeData(StudentList studentList) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + studentListFileName, false);

            for (Student student : studentList.getStudents()) {
                StringBuilder line = new StringBuilder();
                line.append(student.getEnrolledFaculty().getFacultyName()).append(",")
                        .append(student.getEnrolledDepartment().getDepartmentName()).append(",")
                        .append(student.getName()).append(",")
                        .append(student.getStudentID()).append(",")
                        .append(student.getEmail());

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("เกิดข้อผิดพลาดในการเขียนข้อมูลลงไฟล์", e);
        }
    }
}
