package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.util.*;

public class StudentListFileDatasource implements Datasource<List<Student>> {
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
    public List<Student> readData() {
        List<Student> students = new ArrayList<>();
        try {
            File file = new File(directoryName + File.separator + studentListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length < 6) continue; // Skip malformed lines

                String facultyName = data[0];
                String departmentName = data[1];
                String studentName = data[2];
                String studentId = data[3];
                String email = data[4];
                String advisor = data[5];

                Faculty faculty = new Faculty(facultyName);
                Department department = new Department(departmentName);
                Student student = new Student(null, null, studentName, faculty, department, studentId, email, true, false);

                students.add(student);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return students;
    }

    public Map<String, Student> readStudentMap() {
        List<Student> students = readData();
        Map<String, Student> studentMap = new HashMap<>();
        for (Student student : students) {
            studentMap.put(student.getName(), student);
        }
        return studentMap;
    }

    @Override
    public void writeData(List<Student> students) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + studentListFileName, false);

            for (Student student : students) {
                StringBuilder line = new StringBuilder();
                line.append(student.getEnrolledFaculty().getFacultyName()).append(",")
                        .append(student.getEnrolledDepartment().getDepartmentName()).append(",")
                        .append(student.getName()).append(",")
                        .append(student.getStudentID()).append(",")
                        .append(student.getEmail()).append(",")
                        .append("Advisor info"); // Placeholder for advisor information

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
