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
    public ArrayList<Advisor> readData() {
        ArrayList<Advisor> advisors = new ArrayList<>();
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        BufferedReader buffer = null;

        try {
            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) continue; // ข้ามบรรทัดที่ว่าง

                String[] data = line.split(","); // แบ่งข้อมูลด้วยเครื่องหมายจุลภาค

                // ตรวจสอบจำนวนคอลัมน์ว่าถูกต้อง
                if (data.length != 11) continue;

                String username = data[0]; // ชื่อผู้ใช้
                String password = data[1]; // รหัสผ่าน
                String name = data[2]; // ชื่อ
                boolean suspended = Boolean.parseBoolean(data[3]); // สถานะพักการใช้งาน
                boolean isFirstLogin = Boolean.parseBoolean(data[4]); // การเข้าใช้งานครั้งแรก
                LocalDateTime lastLogin = "Never".equals(data[5]) ? null : LocalDateTime.parse(data[5], formatter); // ถ้าเป็น "Never" ให้ค่าเป็น null
                String profilePicturePath = data[6].isEmpty() ? User.DEFAULT_PROFILE_PICTURE_PATH : data[6]; // ค่าพาธรูปโปรไฟล์
                String faculty = data[7]; // คณะ
                String department = data[8]; // ภาควิชา
                String advisorID = data[9]; // รหัสอาจารย์
                String advisorEmail = data[10]; // อีเมล

                // เพิ่ม Advisor ไปยัง list ด้วยวิธี addNewAdvisor
                Advisor a = new Advisor(username, password, name, new Faculty(faculty), new Department(department), advisorID, advisorEmail, true, suspended);
                a.setLastLogin(lastLogin); // กำหนดค่า lastLogin
                a.setProfilePicturePath(profilePicturePath); // กำหนดค่าพาธรูปโปรไฟล์
                a.setFirstLogin(isFirstLogin);
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
            // Write the CSV headers
            //buffer.write("username,password,name,role,suspended,isFirstLogin,lastLoginTime,profileImagePath,faculty,department,advisorID,email");
            //buffer.newLine();

            // Write each advisor's data
            for (Advisor advisor : advisors) {
                String lastLoginStr = advisor.getLastLogin() == null ? "Never" : advisor.getLastLogin().format(formatter);
                String profilePicturePath = advisor.getProfilePicturePath() == null ? User.DEFAULT_PROFILE_PICTURE_PATH : advisor.getProfilePicturePath();
                String line = advisor.getUsername() + ","
                        + advisor.getPassword() + ","
                        + advisor.getName() + ","
                        + (advisor.getSuspended() ? "suspended" : "normal") + ","
                        + advisor.isFirstLogin() + ","
                        + lastLoginStr + ","  // Example last login time
                        + (profilePicturePath) + ","
                        + advisor.getFaculty().getFacultyName() + ","
                        + advisor.getDepartment().getDepartmentName() + ","
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

    public void displayAdvisors(ArrayList<Advisor> advisors) {
        for (Advisor advisor : advisors) {
            System.out.println("Advisor: " + advisor.getName() + ", " + advisor.getFaculty() + ", " + advisor.getDepartment() + ", " + advisor.getAdvisorEmail() + ", " + advisor.getAdvisorID());
        }
    }
}
