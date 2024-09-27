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
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + advisorListFileName;
        file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
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

        FileInputStream    fileInputStream = null;

        // สร้าง AdvisorList และกำหนดค่า FacultyList

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(
                fileInputStream,
                StandardCharsets.UTF_8
        );
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            // ใช้ while loop เพื่ออ่านข้อมูลรอบละบรรทัด
            while ( (line = buffer.readLine()) != null ){
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(", ");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String name = data[0].trim();
                String username = data[1].trim();
                String password = data[2].trim();
                String faculty = data[3].trim();
                String department = data[4].trim();
                String id = data[5].trim();

                // เพิ่มข้อมูลลงใน list
                advisors.addNewAdvisor(name, username, password, faculty, department, id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return advisors;
    }


    @Override
    public void writeData(AdvisorList advisors) {
        String filePath = directoryName + File.separator + advisorListFileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการเขียนไฟล์
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                fileOutputStream,
                StandardCharsets.UTF_8
        );
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);

        try {
            // สร้าง csv ของ Student และเขียนลงในไฟล์ทีละบรรทัด
            for (Advisor advisor : advisors.getAdvisors()) {
                String line = advisor.getName() + "," + advisor.getUsername() + "," + advisor.getPassword() + "," + advisor.getFaculty() + "," + advisor.getDepartment() + "," + advisor.getAdvisorID();
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }


}
