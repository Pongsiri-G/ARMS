package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class DepartmentOfficerListFileDatasource implements Datasource<ArrayList<DepartmentOfficer>> {
    private String directoryName;
    private String departmentOfficerListFileName;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DepartmentOfficerListFileDatasource(String directoryName, String studentListFileName) {
        this.directoryName = directoryName;
        this.departmentOfficerListFileName = studentListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File facultyOfficerListFile = new File(directoryName + File.separator + departmentOfficerListFileName);
        if (!facultyOfficerListFile.exists()) {
            try {
                facultyOfficerListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ArrayList<DepartmentOfficer> readData() {
        ArrayList<DepartmentOfficer> departmentOfficers = new ArrayList<>();

        try {
            File file = new File(directoryName + File.separator + departmentOfficerListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length < 9) continue;

                String username = data[0];
                String password = data[1];
                String name = data[2];
                boolean isSuspended = "ระงับบัญชี".equals(data[3]);
                LocalDateTime lastLogin = "ไม่เคยเข้าใช้งาน".equals(data[4]) ? null : LocalDateTime.parse(data[4], formatter);
                String profilePicturePath = data[5].equals("ไม่มีรูปประจำตัว") ? null : data[5];
                String facultyName = data[6];
                String departmentName = data[7];
                String defaultPassword = data[8];

                Faculty faculty = new Faculty(facultyName);
                Department department = new Department(departmentName);
                DepartmentOfficer departmentOfficer = new DepartmentOfficer(username, password, name, faculty, department, true, isSuspended);
                departmentOfficer.setLastLogin(lastLogin);
                departmentOfficer.setProfilePicturePath(profilePicturePath);
                departmentOfficer.setDefaultPassword(defaultPassword);

                departmentOfficers.add(departmentOfficer);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return departmentOfficers;
    }

    @Override
    public void writeData(ArrayList<DepartmentOfficer> departmentOfficers) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + departmentOfficerListFileName, false);

            for (DepartmentOfficer departmentOfficer : departmentOfficers) {
                String lastLoginStr = departmentOfficer.getLastLogin() == null ? "ไม่เคยเข้าใช้งาน" : departmentOfficer.getLastLogin().format(formatter);
                String profilePicturePath = departmentOfficer.getProfilePicturePath() == null ? "ไม่มีรูปประจำตัว" : departmentOfficer.getProfilePicturePath();

                StringBuilder line = new StringBuilder();
                line.append(departmentOfficer.getUsername()).append(",")
                        .append(departmentOfficer.getPassword()).append(",")
                        .append(departmentOfficer.getName()).append(",")
                        .append(departmentOfficer.getSuspended() ? "ระงับบัญชี" : "ปกติ").append(",")
                        .append(lastLoginStr).append(",")
                        .append(profilePicturePath).append(",")
                        .append(departmentOfficer.getFaculty().getFacultyName()).append(",")
                        .append(departmentOfficer.getDepartment().getDepartmentName()).append(",")
                        .append(departmentOfficer.getDefaultPassword());

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
