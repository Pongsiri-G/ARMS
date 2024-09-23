package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.util.*;

public class UserListFileDatasource implements Datasource<List<User>> {
    private String directoryName;
    private String userListFileName;
    private StudentListFileDatasource studentDatasource;

    public UserListFileDatasource(String directoryName, String userListFileName, String studentListFileName) {
        this.directoryName = directoryName;
        this.userListFileName = userListFileName;
        this.studentDatasource = new StudentListFileDatasource(directoryName, studentListFileName);
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
        Map<String, Student> studentMap = studentDatasource.readStudentMap();

        try {
            File file = new File(directoryName + File.separator + userListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String role = data[0];
                String username = data[1];
                String password = data[2];
                String name = data[3];
                boolean isSuspended = data.length > 4 && "suspended".equals(data[4]);

                switch (role) {
                    case "FacultyOfficer":
                        users.add(new FacultyOfficer(username, password, name, null, true, isSuspended));
                        break;
                    case "DepartmentOfficer":
                        users.add(new DepartmentOfficer(username, password, name, null, null, true, isSuspended));
                        break;
                    case "Advisor":
                        users.add(new Advisor(username, password, name, null, null, "", true, isSuspended));
                        break;
                    case "Student":
                        // Link student data from studentlist.csv
                        Student student = studentMap.get(name);  // Assuming name is used to link data
                        if (student != null) {
                            student.setUsername(username);
                            student.setPassword(password, true);
                            student.setSuspended(isSuspended);
                            users.add(student);
                        }
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
            List<Student> studentList = new ArrayList<>();

            for (User user : users) {
                StringBuilder line = new StringBuilder();

                if (user instanceof FacultyOfficer) {
                    FacultyOfficer fo = (FacultyOfficer) user;
                    line.append("FacultyOfficer,")
                            .append(fo.getUsername()).append(",")
                            .append(fo.getPassword()).append(",")
                            .append(fo.getName()).append(",")
                            .append(fo.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof DepartmentOfficer) {
                    DepartmentOfficer dofficer = (DepartmentOfficer) user;
                    line.append("DepartmentOfficer,")
                            .append(dofficer.getUsername()).append(",")
                            .append(dofficer.getPassword()).append(",")
                            .append(dofficer.getName()).append(",")
                            .append(dofficer.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof Advisor) {
                    Advisor advisor = (Advisor) user;
                    line.append("Advisor,")
                            .append(advisor.getUsername()).append(",")
                            .append(advisor.getPassword()).append(",")
                            .append(advisor.getName()).append(",")
                            .append(advisor.getSuspended() ? "suspended" : "normal");
                } else if (user instanceof Student) {
                    studentList.add((Student) user);  // Collect student data for studentlist.csv
                    line.append("Student,")
                            .append(user.getUsername()).append(",")
                            .append(user.getPassword()).append(",")
                            .append(user.getName()).append(",")
                            .append(user.getSuspended() ? "suspended" : "normal");
                }

                if (line.length() > 0) {
                    fileWriter.write(line.toString() + "\n");
                }
            }
            fileWriter.close();

            // Write student-specific data to studentlist.csv
            studentDatasource.writeData(studentList);

        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
