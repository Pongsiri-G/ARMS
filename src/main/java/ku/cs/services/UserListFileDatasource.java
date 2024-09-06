package ku.cs.services;

import ku.cs.models.Department;
import ku.cs.models.Faculty;
import ku.cs.models.Student;
import ku.cs.models.User;
import ku.cs.models.UserList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
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
    public UserList readData() {
        UserList users = new UserList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(inputStreamReader);

        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                if (line.equals("")) continue;

                String[] data = line.split(",");

                String username = data[0].trim();
                String password = data[1].trim();
                String name = data[2].trim();

                User user = null;

                if (data.length == 4) {
                    String faculty = data[3].trim();
                    //user = new Faculty(username, password, name, faculty);
                }
                else if (data.length == 5) {
                    String faculty = data[3].trim();
                    String department = data[4].trim();
                    //user = new Faculty(username, password, name, faculty, department);
                } else if (data.length == 6) {
                    String faculty = data[3].trim();
                    String department = data[4].trim();
                    String advisorID = data[5].trim();
                    //user = new Advisor(username, password, name, faculty, department, advisorID);
                } else if (data.length == 7) {
                    String faculty = data[3].trim();
                    String department = data[4].trim();
                    String studentID = data[5].trim();
                    String email = data[6].trim();
                    //user = new Student(username, password, name, faculty, department, studentID, email);
                } else {
                    user = new User(username, password, name);
                }

                users.addUser(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return users;
    }


    @Override
    public void writeData(UserList data) {
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
            for (User user : data.getAllUsers()) {
                StringBuilder line = new StringBuilder();

                line.append(user.getUsername()).append(",");
                line.append(user.getPassword()).append(",");
                line.append(user.getName());

                /*
                if (user instanceof Faculty) {
                    Faculty faculty = (Faculty) user;
                    line.append(",").append(faculty.getFacultyName());
                    if (faculty instanceof Department) {
                        Department department = (Department) faculty;
                        line.append(",").append(department.getDepartmentName());
                        if (department instanceof Advisor) {
                            Advisor advisor = (Advisor) department;
                            line.append(",").append(advisor.getAdvisorID());
                        }
                    }
                } else if (user instanceof Student student) {
                    line.append(",").append(student.getEnrolledFaculty());
                    line.append(",").append(student.getEnrolledDepartment());
                    line.append(",").append(student.getStudentID());
                    line.append(",").append(student.getEmail());
                }
                */
                buffer.append(line.toString());
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
