package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdvisorListFileDatasource implements Datasource<AdvisorList> {
    private String directoryName;
    private String advisorListFileName;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdvisorListFileDatasource(String directoryName, String advisorListFileName) {
        this.directoryName = directoryName;
        this.advisorListFileName = advisorListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File advisorListFile = new File(directoryName + File.separator + advisorListFileName);
        if (!advisorListFile.exists()) {
            try {
                advisorListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AdvisorList readData() {
        AdvisorList advisorList = new AdvisorList();
        try {
            File file = new File(directoryName + File.separator + advisorListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length < 10) continue; // Skip incomplete lines

                String username = data[0];
                String password = data[1];
                String name = data[2];
                boolean isSuspended = "suspended".equals(data[3]);
                LocalDateTime lastLogin = "Never".equals(data[4]) ? null : LocalDateTime.parse(data[4], formatter);
                String profilePicturePath = data[5];
                String facultyName = data[6];
                String departmentName = data[7];
                String advisorId = data[8];
                String email = data[9];

                Faculty faculty = new Faculty(facultyName);
                Department department = new Department(departmentName);
                Advisor advisor = new Advisor(username, password, name, faculty, department, advisorId, email, true, isSuspended);
                advisor.setLastLogin(lastLogin);
                advisor.setProfilePicturePath(profilePicturePath);

                advisorList.addAdvisor(advisor);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return advisorList;
    }

    @Override
    public void writeData(AdvisorList advisorList) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + advisorListFileName, false);

            for (Advisor advisor : advisorList.getAdvisors()) {
                StringBuilder line = new StringBuilder();
                String lastLoginStr = advisor.getLastLogin() == null ? "Never" : advisor.getLastLogin().format(formatter);
                String profilePicturePath = advisor.getProfilePicturePath() == null ? User.DEFAULT_PROFILE_PICTURE_PATH : advisor.getProfilePicturePath();

                line.append(advisor.getUsername()).append(",")
                        .append(advisor.getPassword()).append(",")
                        .append(advisor.getName()).append(",")
                        .append(advisor.getSuspended() ? "suspended" : "normal").append(",")
                        .append(lastLoginStr).append(",")
                        .append(profilePicturePath).append(",")
                        .append(advisor.getFaculty().getFacultyName()).append(",")
                        .append(advisor.getDepartment().getDepartmentName()).append(",")
                        .append(advisor.getAdvisorID()).append(",")
                        .append(advisor.getAdvisorEmail());

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}