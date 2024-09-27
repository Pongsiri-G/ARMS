package ku.cs.services;

import ku.cs.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DepartmentOfficerDatasource implements Datasource<ArrayList<DepartmentOfficer>> {
    private FacDepListFileDatascource facultiesDatasource;
    private String directoryName;
    private String fileName;

    public DepartmentOfficerDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
        this.facultiesDatasource = new FacDepListFileDatascource("data/test", "facdep.csv");
    }

    // ตรวจสอบว่ามีไฟล์ให้อ่านหรือไม่ ถ้าไม่มีให้สร้างไฟล์เปล่า
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

    @Override
    public ArrayList<DepartmentOfficer> readData() {
        FacultyList facultyList = facultiesDatasource.readData();
        ArrayList<DepartmentOfficer> officers = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // เตรียม object ที่ใช้ในการอ่านไฟล์
        FileInputStream fileInputStream = null;

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
            while ((line = buffer.readLine()) != null) {
                // ถ้าเป็นบรรทัดว่าง ให้ข้าม
                if (line.equals("")) continue;

                // แยกสตริงด้วย ,
                String[] data = line.split(",");

                // อ่านข้อมูลตาม index แล้วจัดการประเภทของข้อมูลให้เหมาะสม
                String username = data[1].trim();
                String password = data[2].trim();
                String name = data[3].trim();
                String faculty = data[4].trim();
                String department = data[5].trim();
                // เพิ่มข้อมูลลงใน list
                Faculty fac = facultyList.findFacultyByName(faculty);
                Department dep = fac.findDepartmentByName(department);
                officers.add(new DepartmentOfficer(username, password, name, fac, dep, false, false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return officers;
    }


    @Override
    public void writeData(ArrayList<DepartmentOfficer> data) {
        String filePath = directoryName + File.separator + fileName;
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
            // สร้าง csv และเขียนลงในไฟล์ทีละบรรทัด
            for (DepartmentOfficer officer : data) {
                String line = officer.getRole() + "," + officer.getUsername() + "," + officer.getPassword() + "," + officer.getName() + "," + officer.getFaculty().getFacultyName() + "," + officer.getDepartment().getDepartmentName();
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
