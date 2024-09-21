package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.util.*;

public class UserListFileDatasource implements Datasource<List<User>> {
    private String directoryName;
    private String userListFileName;

    public UserListFileDatasource(String directoryName, String userListFileName) {
        this.directoryName = directoryName;
        this.userListFileName = userListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File userListFile = new File(directoryName + File.separator + userListFileName);
        if (!userListFile.exists()) {
            try {
                userListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<User> readData() {
        List<User> users = new ArrayList<>();
        try {
            File file = new File(directoryName + File.separator + userListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String role = data[0];
                boolean isSuspended = data.length > 1 && "suspended".equals(data[data.length - 1]);

                switch (role) {
                    case "FacultyOfficer":
                        users.add(new FacultyOfficer(data[1], data[2], data[3], new Faculty(data[4]), true, isSuspended)); // true for already hashed password
                        break;
                    case "DepartmentOfficer":
                        users.add(new DepartmentOfficer(data[1], data[2], data[3], new Faculty(data[4]), new Department(data[5]), true, isSuspended));
                        break;
                    case "Advisor":
                        users.add(new Advisor(data[1], data[2], data[3], new Faculty(data[4]), new Department(data[5]), data[6], true, isSuspended));
                        break;
                    case "Student":
                        users.add(new Student(data[1], data[2], data[3], new Faculty(data[4]), new Department(data[5]), data[6], data[7], true, isSuspended));
                        break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return users;
    }

    @Override
    public void writeData(List<User> users) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + userListFileName, false);

            for (User user : users) {
                StringBuilder line = new StringBuilder();

                if (user instanceof FacultyOfficer) {
                    FacultyOfficer fo = (FacultyOfficer) user;
                    line.append("FacultyOfficer,")
                            .append(fo.getUsername()).append(",")
                            .append(fo.getPassword()).append(",")
                            .append(fo.getName()).append(",")
                            .append(fo.getFaculty().getFacultyName()).append(",")
                            .append(fo.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof DepartmentOfficer) {
                    DepartmentOfficer dofficer = (DepartmentOfficer) user;
                    line.append("DepartmentOfficer,")
                            .append(dofficer.getUsername()).append(",")
                            .append(dofficer.getPassword()).append(",")
                            .append(dofficer.getName()).append(",")
                            .append(dofficer.getFaculty().getFacultyName()).append(",")
                            .append(dofficer.getDepartment().getDepartmentName()).append(",")
                            .append(dofficer.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof Advisor) {
                    Advisor advisor = (Advisor) user;
                    line.append("Advisor,")
                            .append(advisor.getUsername()).append(",")
                            .append(advisor.getPassword()).append(",")
                            .append(advisor.getName()).append(",")
                            .append(advisor.getFaculty().getFacultyName()).append(",")
                            .append(advisor.getDepartment().getDepartmentName()).append(",")
                            .append(advisor.getAdvisorID()).append(",")
                            .append(advisor.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof Student) {
                    Student student = (Student) user;
                    line.append("Student,")
                            .append(student.getUsername()).append(",")
                            .append(student.getPassword()).append(",")
                            .append(student.getName()).append(",")
                            .append(student.getEnrolledFaculty().getFacultyName()).append(",")
                            .append(student.getEnrolledDepartment().getDepartmentName()).append(",")
                            .append(student.getStudentID()).append(",")
                            .append(student.getEmail()).append(",")
                            .append(student.getSuspended() ? "suspended" : "normal");
                }

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
