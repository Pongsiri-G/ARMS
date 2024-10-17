package ku.cs.services;

import ku.cs.models.Admin;
import ku.cs.models.UserPreferences;
import ku.cs.models.User;

import java.io.*;

public class AdminPasswordFileDataSource implements Datasource<Admin> {
    private String directoryName;
    private String fileName;

    public AdminPasswordFileDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        // Check directory
        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // Check file
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create file: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public Admin readData() {
        Admin admin = null;
        String filePath = directoryName + File.separator + fileName;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, java.nio.charset.StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                String[] data = line.split(",");
                if (data.length >= 5) { // Ensure all data fields are available
                    String password = data[0].trim();
                    String profilePicturePath = data[1].equals("ไม่มีรูปประจำตัว") ? null : data[1].trim();
                    String theme = data[2].trim();
                    String fontSize = data[3].trim();
                    String fontFamily = data[4].trim();

                    // Create Admin and set values
                    admin = new Admin(password, true, false);
                    admin.setProfilePicturePath(profilePicturePath);

                    UserPreferences preferences = new UserPreferences(theme, fontSize, fontFamily);
                    admin.setPreferences(preferences);
                } else {
                    System.err.println("Error: Incomplete admin data in the file.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admin data: " + e.getMessage());
        }
        return admin;
    }

    @Override
    public void writeData(Admin admin) {
        String filePath = directoryName + File.separator + fileName;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, java.nio.charset.StandardCharsets.UTF_8))) {
            String profilePicturePath = admin.getProfilePicturePath() == null ? "ไม่มีรูปประจำตัว" : admin.getProfilePicturePath();
            String theme = admin.getPreferences().getTheme();
            String fontSize = admin.getPreferences().getFontSize();
            String fontFamily = admin.getPreferences().getFontFamily();

            String line = String.join(",", admin.getPassword(), profilePicturePath, theme, fontSize, fontFamily);
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error writing admin data: " + e.getMessage());
        }
    }

}