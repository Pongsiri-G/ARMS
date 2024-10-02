package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FacultyOfficerListFileDatasource implements Datasource<ArrayList<FacultyOfficer>> {
    private String directoryName;
    private String facultyOfficerListFileName;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FacultyOfficerListFileDatasource(String directoryName, String facultyOfficerListFileName) {
        this.directoryName = directoryName;
        this.facultyOfficerListFileName = facultyOfficerListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File facultyOfficerListFile = new File(directoryName + File.separator + facultyOfficerListFileName);
        if (!facultyOfficerListFile.exists()) {
            try {
                facultyOfficerListFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ArrayList<FacultyOfficer> readData() {
        ArrayList<FacultyOfficer> facultyOfficers = new ArrayList<>();

        try {
            File file = new File(directoryName + File.separator + facultyOfficerListFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length < 7) continue;

                String username = data[0];
                String password = data[1];
                String name = data[2];
                boolean isSuspended = "suspended".equals(data[3]);
                LocalDateTime lastLogin = "Never".equals(data[4]) ? null : LocalDateTime.parse(data[4], formatter);
                String profilePicturePath = data[5].isEmpty() ? User.DEFAULT_PROFILE_PICTURE_PATH : data[5];
                String facultyName = data[6];

                Faculty faculty = new Faculty(facultyName);
                FacultyOfficer facultyOfficer = new FacultyOfficer(username, password, name, faculty, true, isSuspended);
                facultyOfficer.setLastLogin(lastLogin);
                facultyOfficer.setProfilePicturePath(profilePicturePath);

                facultyOfficers.add(facultyOfficer);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
        return facultyOfficers;
    }

    @Override
    public void writeData(ArrayList<FacultyOfficer> facultyOfficers) {
        try {
            FileWriter fileWriter = new FileWriter(directoryName + File.separator + facultyOfficerListFileName, false);

            for (FacultyOfficer facultyOfficer : facultyOfficers) {
                String lastLoginStr = facultyOfficer.getLastLogin() == null ? "Never" : facultyOfficer.getLastLogin().format(formatter);
                String profilePicturePath = facultyOfficer.getProfilePicturePath() == null ? User.DEFAULT_PROFILE_PICTURE_PATH : facultyOfficer.getProfilePicturePath();

                StringBuilder line = new StringBuilder();
                line.append(facultyOfficer.getUsername()).append(",")
                        .append(facultyOfficer.getPassword()).append(",")
                        .append(facultyOfficer.getName()).append(",")
                        .append(facultyOfficer.getSuspended() ? "suspended" : "normal").append(",")
                        .append(lastLoginStr).append(",")
                        .append(profilePicturePath).append(",")
                        .append(facultyOfficer.getFaculty().getFacultyName());

                fileWriter.write(line.toString() + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error writing data to file", e);
        }
    }
}
