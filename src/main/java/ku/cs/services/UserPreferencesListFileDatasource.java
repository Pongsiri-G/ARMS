package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.models.UserPreferences;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserPreferencesListFileDatasource {
    private String directoryName;
    private String fileName;
    private UserList userList;

    public UserPreferencesListFileDatasource(String directoryName, String fileName, UserList userList) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        this.userList = userList;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeData() {
        String filePath = directoryName + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            for (User user : userList.getAllUsers()) {
                UserPreferences preferences = user.getPreferences();
                writer.write(String.join(",",
                        user.getUsername(),
                        preferences.getTheme(),
                        preferences.getFontSize(),
                        preferences.getFontFamily()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readData() {
        String filePath = directoryName + File.separator + fileName;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4) continue;

                String username = data[0];
                String theme = data[1];
                String fontSize = data[2];
                String fontFamily = data[3];

                UserPreferences preferences = new UserPreferences(theme, fontSize, fontFamily);
                User user = userList.findUserByUsername(username);
                if (user != null) {
                    user.setPreferences(preferences);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
