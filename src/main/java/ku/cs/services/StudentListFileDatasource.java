package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentListFileDatasource implements Datasource<ArrayList<Student>> {
    private String directoryName;
    private String studentListFileName;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    public ArrayList<Student> readData() {
        ArrayList<Student> studentList = new ArrayList<>();

        try {
            File file = new File(directoryName + File.separator + studentListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length < 11) continue;

                String username = data[0].equals(" ") ? null : data[0];
                String password = data[1].equals(" ") ? null : data[1];
                String name = data[2];
                boolean isSuspended = "ระงับบัญชี".equals(data[3]);
                LocalDateTime lastLogin = "ไม่เคยเข้าใช้งาน".equals(data[4]) ? null : LocalDateTime.parse(data[4], formatter);
                String profilePicturePath = data[5].equals("ไม่มีรูปประจำตัว") ? null : data[5];
                String facultyName = data[6];
                String departmentName = data[7];
                String studentId = data[8];
                String email = data[9];
                String advisorName = data[10];

                Faculty faculty = new Faculty(facultyName);
                Department department = new Department(departmentName);

                Student student;
                if (username == null && password == null) {
                    student = new Student(name, faculty, department, studentId, email);
                } else {
                    student = new Student(username, password, name, faculty, department, studentId, email, true, isSuspended);
                }

                student.setLastLogin(lastLogin);
                student.setProfilePicturePath(profilePicturePath);
                if (!advisorName.equals(" ")) {
                    student.setStudentAdvisor(new Advisor(advisorName));
                }
                studentList.add(student);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return studentList;
    }

    @Override
    public void writeData(ArrayList<Student> studentList) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + studentListFileName, false);

            for (Student student : studentList) {
                String username = student.getUsername() == null ? " " : student.getUsername();
                String password = student.getPassword() == null ? " " : student.getPassword();
                String lastLoginStr = student.getLastLogin() == null ? "ไม่เคยเข้าใช้งาน" : student.getLastLogin().format(formatter);
                String profilePicturePath = student.getProfilePicturePath()== null ? "ไม่มีรูปประจำตัว" : student.getProfilePicturePath();
                String studentAdvisor = student.getStudentAdvisor() == null ? " " : student.getStudentAdvisor().getName();

                StringBuilder line = new StringBuilder();
                line.append(username).append(",")
                        .append(password).append(",")
                        .append(student.getName()).append(",")
                        .append(student.getSuspended() ? "ระงับบัญชี" : "ปกติ").append(",")
                        .append(lastLoginStr).append(",")
                        .append(profilePicturePath).append(",")
                        .append(student.getEnrolledFaculty().getFacultyName()).append(",")
                        .append(student.getEnrolledDepartment().getDepartmentName()).append(",")
                        .append(student.getStudentID()).append(",")
                        .append(student.getEmail()).append(",")
                        .append(studentAdvisor);

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}