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
            directory.mkdirs(); // สร้างโฟลเดอร์ถ้าไม่พบ
        }
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile(); // สร้างไฟล์ใหม่ถ้าไม่พบ
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AdvisorList readData() {
        AdvisorList advisors = new AdvisorList();
        String filePath = directoryName + File.separator + advisorListFileName; // ใช้ File.separator เพื่อความเข้ากันได้ระหว่างระบบปฏิบัติการ
        File file = new File(filePath);
        BufferedReader buffer = null; // กำหนด buffer reader เป็น null ก่อน

        try {
            buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            String line;
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) continue; // ข้ามบรรทัดว่าง

                String[] data = line.split(","); // แยกข้อมูลโดยใช้ ,

                String name = data[0].trim();
                String faculty = data[1].trim();
                String department = data[2].trim();
                String email = data[3].trim();
                String id = data[4].trim();

                // เพิ่มข้อมูลลงใน AdvisorList
                advisors.addNewAdvisor(name, faculty, department, email, id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // ปิด BufferedReader
            if (buffer != null) {
                try {
                    buffer.close(); // ปิด buffer
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return advisors;
    }

    @Override
    public void writeData(AdvisorList advisors) {
        String filePath = directoryName + File.separator + advisorListFileName; // ใช้ File.separator เพื่อความเข้ากันได้ระหว่างระบบปฏิบัติการ
        File file = new File(filePath);
        BufferedWriter buffer = null; // กำหนด buffer writer เป็น null ก่อน

        try {
            buffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            // เขียนหัวข้อในไฟล์ CSV
            buffer.write("name,faculty,department,email,id");
            buffer.newLine();

            // เขียนข้อมูลของ Advisor แต่ละคน
            for (Advisor advisor : advisors.getAdvisors()) {
                String line = advisor.getName() + "," + advisor.getFaculty() + "," + advisor.getDepartment() + "," + advisor.getAdvisorEmail() + "," + advisor.getAdvisorID();
                buffer.write(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // ปิด BufferedWriter
            if (buffer != null) {
                try {
                    buffer.close(); // ปิด buffer
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
}}
