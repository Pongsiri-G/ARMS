package ku.cs.services;

import ku.cs.models.Department;
import ku.cs.models.Faculty;
import ku.cs.models.FacultyList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FacDepListFileDatascource implements Datasource<FacultyList> {
    private String directoryName;
    private String fileName;

    public FacDepListFileDatascource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
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

    @Override
    public FacultyList readData() {
        FacultyList faculties = new FacultyList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        String line = "";
        try {
            
            while ((line = br.readLine()) != null) {
                
                if (line.equals("")) continue;

                
                String[] data = line.split(",");

                
                String fac = data[0].trim();
                String facID = data[1].trim();
                String dep = data[2].trim();
                String depID = data[3].trim();

                faculties.addFaculty(fac, facID, dep, depID);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return faculties;
    }

    @Override
    public void writeData(FacultyList faculties) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);

        try {
            for (Faculty fac : faculties.getFaculties()) {
                
                for (Department dep : fac.getDepartments()) {
                    String line = fac.getFacultyName() + "," + fac.getFacultyId() + "," + dep.getDepartmentName() + "," + dep.getDepartmentID();
                    bw.append(line);
                    bw.append("\n");
                }
                
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
