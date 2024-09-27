package ku.cs.services;

import ku.cs.models.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class RequestListFileDatasource implements Datasource<RequestList> {
    private String directoryName;
    private String fileName;

    public RequestListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted() {
        // check directory
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
        }
        // check file
        String filePath = directoryName + File.separator + fileName;
        //System.out.println("Loading file from: " + filePath);
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
    public RequestList readData() {
        RequestList requests = new RequestList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        //Object preparing
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
            // Looping read data for each line
            while ((line = br.readLine()) != null) {
                // Skip empty line
                if (line.equals("")) continue;

                // split ", "
                String[] data = line.split(", ");

                // Read data from index and manage type
                String name = data[0].trim();
                String faculty = data[1].trim();
                String department = data[2].trim();
                String status = data[3].trim();

                requests.addTableRequest(name, faculty, department, status);
                //System.out.println("Added request: " + name + ", " + faculty + ", " + department + ", " + status);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return requests;
    }

    @Override
    public void writeData(RequestList data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        // object preparasion for write file
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);

        try {
            for (Request req : data.getRequests()) {
                String line = req.getName() + ", " + req.getFaculty() + ", " + req.getDepartment() + ", " + req.getStatus();
                bw.append(line);
                bw.append("\n");
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
