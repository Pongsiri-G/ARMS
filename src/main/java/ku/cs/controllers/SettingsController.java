package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsController {
    @FXML private VBox userInfoPane;
    @FXML private VBox settingPane;
    @FXML private StackPane userDataStackPane;
    @FXML private StackPane changePasswordStackPane;
    @FXML private StackPane changeThemeStackPane;
    @FXML private Label roleLabel;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label idLabel;
    @FXML private Label emailLabel;
    @FXML private Label facultyLabel;
    @FXML private Label departmentLabel;
    @FXML private Label idTitle;
    @FXML private Label emailTitle;
    @FXML private Label facultyTitle;
    @FXML private Label departmentTitle;
    @FXML private Label errorMessageLabel;
    @FXML private Rectangle idRectangle;
    @FXML private Rectangle emailRectangle;
    @FXML private Rectangle facultyRectangle;
    @FXML private Rectangle departmentRectangle;
    @FXML private Circle profilePictureDisplay;
    @FXML private TextField currentPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmNewPassword;
    @FXML private ChoiceBox<String> themeChoiceBox;
    @FXML private ChoiceBox<String> fontChoiceBox;
    @FXML private ChoiceBox<String> fontSizeChoiceBox;
    @FXML private String[] theme = {"Light", "Dark"};
    @FXML private String[] font = {"System", "Karaoke"};
    @FXML private String[] fontSize = {"20", "24", "26"};

    FileChooser fileChooser = new FileChooser();

    private ArrayList<String> data;
    private UserList userList;
    private UserListFileDatasource datasource;
    private User user;


    public SettingsController(){
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
    }

    @FXML
    public void initialize() {
        themeChoiceBox.getItems().addAll(theme);
        fontChoiceBox.getItems().addAll(font);
        fontSizeChoiceBox.getItems().addAll(fontSize);
        themeChoiceBox.setValue("Light");
        fontChoiceBox.setValue("System");
        fontSizeChoiceBox.setValue("24");
        errorMessageLabel.setText("");
        fileChooser.setInitialDirectory(new File(""));
        data = (ArrayList<String>) FXRouter.getData();
        user = userList.findUserByUsername(data.get(1));
        userDataStackPane.setVisible(true);
        userDataInitialize();
        changePasswordStackPane.setVisible(false);
        changeThemeStackPane.setVisible(false);
        nameLabel.setText(user.getName());
        roleLabel.setText(user.getRole());
        setProfilePicture(user.getProfilePicturePath());
        userPaneInitialize();
    }

    private void userDataInitialize(){
        if (user instanceof Student) {
            Student student = (Student) user;
            nameLabel.setText(student.getName());
            usernameLabel.setText(student.getUsername());
            idLabel.setText(student.getStudentID());
            emailLabel.setText(student.getEmail());
            facultyLabel.setText(student.getEnrolledFaculty().getFacultyName());
            departmentLabel.setText(student.getEnrolledDepartment().getDepartmentName());
        } else if (user instanceof Advisor) {
            Advisor advisor = (Advisor) user;
            nameLabel.setText(advisor.getName());
            usernameLabel.setText(advisor.getUsername());
            idLabel.setText(advisor.getAdvisorID());
            emailLabel.setText(advisor.getAdvisorEmail());
            facultyLabel.setText(advisor.getFaculty().getFacultyName());
            departmentLabel.setText(advisor.getDepartment().getDepartmentName());
        } else if (user instanceof FacultyOfficer) {
            FacultyOfficer facultyOfficer = (FacultyOfficer) user;
            nameLabel.setText(facultyOfficer.getName());
            usernameLabel.setText(facultyOfficer.getUsername());
            facultyLabel.setText(facultyOfficer.getFaculty().getFacultyName());
            facultyLabel.setLayoutY(296);
            facultyRectangle.setLayoutY(281);
            facultyTitle.setLayoutY(295);
            idLabel.setVisible(false);
            idRectangle.setVisible(false);
            idTitle.setVisible(false);
            emailTitle.setVisible(false);
            emailRectangle.setVisible(false);
            emailLabel.setVisible(false);
            departmentTitle.setVisible(false);
            departmentLabel.setVisible(false);
            departmentRectangle.setVisible(false);
        } else if (user instanceof DepartmentOfficer) {
            DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
            nameLabel.setText(departmentOfficer.getName());
            usernameLabel.setText(departmentOfficer.getUsername());
            departmentLabel.setText(departmentOfficer.getDepartment().getDepartmentName());
            facultyLabel.setText(departmentOfficer.getFaculty().getFacultyName());
            facultyLabel.setLayoutY(296);
            facultyRectangle.setLayoutY(281);
            facultyTitle.setLayoutY(295);
            departmentTitle.setLayoutY(417);
            departmentRectangle.setLayoutY(403);
            departmentLabel.setLayoutY(418);
            idTitle.setVisible(false);
            idRectangle.setVisible(false);
            idLabel.setVisible(false);
            emailTitle.setVisible(false);
            emailRectangle.setVisible(false);
            emailLabel.setVisible(false);
        }
    }

    @FXML
    protected void onChangeProfileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                String filePath = file.toURI().toString().replace("file:", "");
                Image image = new Image("file:" + filePath);
                profilePictureDisplay.setFill(new ImagePattern(image));
                user.setProfilePicturePath(file.toURI().toString().replace("file:", ""));
                datasource.writeData(userList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onChangePasswordClick() {
        String currentPassword = currentPasswordField.getText().trim();
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmNewPassword.getText().trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            errorMessageLabel.setText("โปรดกรอกข้อมูลให้ครบถ้วน");
            return;
        }

        if (!user.validatePassword(currentPassword)) {
            errorMessageLabel.setText("โปรดกรอกรหัสผ่านปัจจุบันให้ถูกต้อง");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            errorMessageLabel.setText("รหัสผ่านใหม่ไม่ตรงกัน");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            errorMessageLabel.setText("รหัสผ่านใหม่เหมือนกับรหัสผ่านปัจุจบัน");
            return;
        }

        user.setPassword(newPassword, false);
        errorMessageLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
        errorMessageLabel.setStyle("-fx-text-fill: green;");
    }

    private void userPaneInitialize(){
        userInfoPane.getChildren().addAll(createLabel("ชื่อผู้ใช้ " + user.getUsername()));
        if (user instanceof Student){
            Student student = (Student) user;

            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + student.getEnrolledFaculty().getFacultyName()),
                    createLabel("สาขาวิชา " + student.getEnrolledDepartment().getDepartmentName() + " (" + student.getEnrolledFaculty().getFacultyId() + student.getEnrolledDepartment().getDepartmentID() + ")")
            );
        }
        else if (user instanceof FacultyOfficer){
            FacultyOfficer facultyOfficer = (FacultyOfficer) user;
            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + facultyOfficer.getFaculty().getFacultyName())
            );
        }
        else if (user instanceof DepartmentOfficer){
            DepartmentOfficer departmentOfficer = (DepartmentOfficer) user;
            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + departmentOfficer.getFaculty().getFacultyName()),
                    createLabel("สาขาวิชา " + departmentOfficer.getDepartment().getDepartmentName() + " (" + departmentOfficer.getFaculty().getFacultyId() + departmentOfficer.getDepartment().getDepartmentID() + ")")
            );
        }
        else if (user instanceof Advisor) {
            Advisor advisor = (Advisor) user;
            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + advisor.getFaculty()),
                    createLabel("สาขาวิชา " + advisor.getDepartment().getDepartmentName() + " (" + advisor.getFaculty().getFacultyId() + advisor.getDepartment().getDepartmentID() + ")")
            );
        }
    }

    private void setProfilePicture(String profilePath) {
        try {
            // โหลดรูปจาก profilePath
            Image profileImage = new Image("file:" + profilePath);

            profilePictureDisplay.setFill(new ImagePattern(profileImage));

        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            profilePictureDisplay.setFill(Color.GRAY);
        }
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("label");
        label.setFont(new Font("System", 18));
        return label;
    }

    public void backButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo(data.get(0));
    }

    @FXML
    protected void onChangePictureClick() {
        changePasswordStackPane.setVisible(false);
        changeThemeStackPane.setVisible(false);
        userDataStackPane.setVisible(false);
    }

    @FXML
    protected void onUserDataClick() {
        changePasswordStackPane.setVisible(false);
        changeThemeStackPane.setVisible(false);
        userDataStackPane.setVisible(true);
    }

    @FXML
    protected void onChangePasswordclick() {
        changePasswordStackPane.setVisible(true);
        changeThemeStackPane.setVisible(false);
        userDataStackPane.setVisible(false);
    }

    @FXML
    protected void onChangeThemeClick() {
        changePasswordStackPane.setVisible(false);
        changeThemeStackPane.setVisible(true);
        userDataStackPane.setVisible(false);
    }
}
