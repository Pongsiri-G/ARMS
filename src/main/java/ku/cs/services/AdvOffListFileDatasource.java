package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AdvOffListFileDatasource implements Datasource<AdvisorList> {
    private String directoryName;
    private String advisorListFileName;

    public AdvOffListFileDatasource(String directoryName, String advisorListFileName) {
        this.directoryName = directoryName;
        this.advisorListFileName = advisorListFileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if not found
        }
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile(); // Create new file if not found
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AdvisorList readData() {
        AdvisorList advisors = new AdvisorList();
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        BufferedReader buffer = null;

        try {
            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) continue; // Skip empty lines

                String[] data = line.split(","); // Split data by comma

                // Ensure correct number of columns
                if (data.length != 12) continue;

                String username = data[0]; // username
                String password = data[1]; // รหัสผ่าน
                String name = data[2]; // ชื่อ
                String role = data[3]; // role เก็บๆไปก่อนเผื่อง่าย
                boolean suspended = Boolean.parseBoolean(data[4]); // สักอย่างจากเฟิสเอามาด้วย
                boolean isFirstLogin = Boolean.parseBoolean(data[5]); // การเข้าใช้งานหากเป็นรหัสที่เข้าใช้ครั้งเเรกจะเป็น true เพื่อเปลี่ยนรหัสผ่านต่อไป
                String lastLoginTime = data[6];  // For potential future use // เวลาล่าสุดที่ใช้
                String profileImagePath = data[7]; // รูป
                String faculty = data[8]; // คณะ
                String department = data[9]; // สาขา
                String advisorID = data[10]; // ไอดีจารย์
                String advisorEmail = data[11]; // อีเมล

                // Add Advisor to list using addNewAdvisor method from AdvisorList
                advisors.addNewAdvisor(username, password, name, faculty, department, advisorID, advisorEmail, false, suspended, isFirstLogin);
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
    public void writeData(AdvisorList advisors) {
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        BufferedWriter buffer = null;

        try {
            buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            // Write the CSV headers
            //buffer.write("username,password,name,role,suspended,isFirstLogin,lastLoginTime,profileImagePath,faculty,department,advisorID,email");
            //buffer.newLine();

            // Write each advisor's data
            for (Advisor advisor : advisors.getAdvisors()) {
                String line = advisor.getUsername() + ","
                        + advisor.getPassword() + ","
                        + advisor.getName() + ","
                        + advisor.getRole() + ","
                        + advisor.getSuspended() + ","
                        + advisor.isFirstLogin() + ","
                        + advisor.getLastLogin() + ","  // Example last login time
                        + advisor.getProfilePicturePath() + ","
                        + advisor.getFaculty() + ","
                        + advisor.getDepartment() + ","
                        + advisor.getAdvisorID() + ","
                        + advisor.getAdvisorEmail();
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

    public void displayAdvisors(AdvisorList advisors) {
        for (Advisor advisor : advisors.getAdvisors()) {
            System.out.println("Advisor: " + advisor.getName() + ", " + advisor.getFaculty() + ", " + advisor.getDepartment() + ", " + advisor.getAdvisorEmail() + ", " + advisor.getAdvisorID());
        }
    }
}
