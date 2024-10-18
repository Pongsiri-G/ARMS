package ku.cs.services;

import ku.cs.models.RequestHandlingOfficer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RequestHandlingOfficersDataSource implements Datasource<ArrayList<RequestHandlingOfficer>> {
    private String directoryName;
    private String fileName;

    public RequestHandlingOfficersDataSource(String directoryName, String fileName) {
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
    public ArrayList<RequestHandlingOfficer> readData() {
        ArrayList<RequestHandlingOfficer> approvers = new ArrayList<>();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        
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
            
            while ((line = buffer.readLine()) != null) {
                
                if (line.equals("")) continue;

                
                String[] data = line.split(",");

                
                String facDep = data[0].trim();
                String position = data[1].trim();
                String name = data[2].trim();
                String lastUpdate = data[3].trim();

                
                approvers.add(new RequestHandlingOfficer(facDep, position, name, lastUpdate));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return approvers;
    }

    @Override
    public void writeData(ArrayList<RequestHandlingOfficer> data) {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        
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
            
            for (RequestHandlingOfficer approver : data) {
                String line = approver.getFacDep() + "," + approver.getPosition() + "," + approver.getName() + "," + approver.getLastUpdate();
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

