package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdvOffListFileDatasource implements Datasource<ArrayList<Advisor>> {
    private String directoryName;
    private String advisorListFileName;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdvOffListFileDatasource(String directoryName, String advisorListFileName) {
        this.directoryName = directoryName;
        this.advisorListFileName = advisorListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs(); 
        }
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile(); 
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ArrayList<Advisor> readData() {
        ArrayList<Advisor> advisors = new ArrayList<>();
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        BufferedReader buffer = null;

        try {
            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) continue; 

                String[] data = line.split(","); 

                
                if (data.length != 10) continue;

                String username = data[0]; 
                String password = data[1]; 
                String name = data[2]; 
                boolean suspended = "ระงับบัญชี".equals(data[3]); 
                LocalDateTime lastLogin = "ไม่เคยเข้าใช้งาน".equals(data[4]) ? null : LocalDateTime.parse(data[4], formatter); 
                String profilePicturePath = data[5].equals("ไม่มีรูปประจำตัว") ? null : data[5]; 
                String faculty = data[6]; 
                String department = data[7]; 
                String advisorID = data[8]; 
                String defaultPassword = data[9];

                
                Advisor a = new Advisor(username, password, name, new Faculty(faculty), new Department(department), advisorID, true, suspended);
                a.setLastLogin(lastLogin); 
                a.setProfilePicturePath(profilePicturePath); 
                a.setDefaultPassword(defaultPassword);
                advisors.add(a);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return advisors;
    }


    @Override
    public void writeData(ArrayList<Advisor> advisors) {
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        BufferedWriter buffer = null;

        try {
            buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

            
            for (Advisor advisor : advisors) {
                String lastLoginStr = advisor.getLastLogin() == null ? "ไม่เคยเข้าใช้งาน" : advisor.getLastLogin().format(formatter);
                String profilePicturePath = advisor.getProfilePicturePath() == null ? "ไม่มีรูปประจำตัว" : advisor.getProfilePicturePath();
                String line = advisor.getUsername() + ","
                        + advisor.getPassword() + ","
                        + advisor.getName() + ","
                        + (advisor.getSuspended() ? "ระงับบัญชี" : "ปกติ") + ","
                        + lastLoginStr + ","
                        + (profilePicturePath) + ","
                        + advisor.getFaculty().getFacultyName() + ","
                        + advisor.getDepartment().getDepartmentName() + ","
                        + advisor.getAdvisorID() + ","
                        + advisor.getDefaultPassword();
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
