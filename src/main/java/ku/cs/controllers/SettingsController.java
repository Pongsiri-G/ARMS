package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.FileStorage;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.UserPreferencesListFileDatasource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsController extends BaseController {
    @FXML private VBox userInfoPane;

    @FXML private Label roleLabel;

    @FXML private Label nameLabel;


    @FXML private Circle profilePictureDisplay;


    @FXML private ChoiceBox<String> themeChoiceBox;
    @FXML private ChoiceBox<String> fontFamilyChoiceBox;
    @FXML private ChoiceBox<String> fontSizeChoiceBox;
    @FXML private BorderPane rootPane;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Label errorLabel;

    private ArrayList<String> data;
    private UserList userList;
    private UserListFileDatasource datasource;
    private User user;
    private UserPreferencesListFileDatasource preferencesListFileDatasource;

    private File uploadedPicture;


    public SettingsController() {
        datasource = new UserListFileDatasource("data/csv_files", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
        preferencesListFileDatasource = new UserPreferencesListFileDatasource("data/csv_files", "preferences.csv", userList);
        this.preferencesListFileDatasource.readData();
    }

    @FXML
    public void initialize() {
        data = (ArrayList<String>) FXRouter.getData();
        user = userList.findUserByUsername(data.get(1));
        nameLabel.setText(user.getName());
        roleLabel.setText(user.getRole());
        setProfilePicture(profilePictureDisplay, user.getProfilePicturePath());

        themeChoiceBox.getItems().addAll("Light", "Dark", "Autumn", "Coffee", "Fallen");
        fontFamilyChoiceBox.getItems().addAll("Noto Sans Thai", "Noto Sans Thai Looped", "Bai Jamjuree");
        fontSizeChoiceBox.getItems().addAll("Small", "Medium", "Large");

        userPaneInitialize();

        themeChoiceBox.setValue(user.getPreferences().getTheme());
        fontFamilyChoiceBox.setValue(user.getPreferences().getFontFamily());
        fontSizeChoiceBox.setValue(user.getPreferences().getFontSize());

        applyThemeAndFont(rootPane, user.getPreferences().getTheme(), user.getPreferences().getFontFamily(), user.getPreferences().getFontSize());
        preferencesListFileDatasource.writeData();

        // Add listeners for immediate change on selection
        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            user.getPreferences().setTheme(newValue);
            applyThemeAndFont(rootPane, user.getPreferences().getTheme(), user.getPreferences().getFontFamily(), user.getPreferences().getFontSize());
            preferencesListFileDatasource.writeData();
        });

        fontFamilyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            user.getPreferences().setFontFamily(newValue);
            applyThemeAndFont(rootPane, user.getPreferences().getTheme(), user.getPreferences().getFontFamily(), user.getPreferences().getFontSize());
            preferencesListFileDatasource.writeData();
        });

        fontSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            user.getPreferences().setFontSize(newValue);
            applyThemeAndFont(rootPane, user.getPreferences().getTheme(), user.getPreferences().getFontFamily(), user.getPreferences().getFontSize());
            preferencesListFileDatasource.writeData();
        });
    }


    private void userPaneInitialize(){
        userInfoPane.getChildren().addAll(createLabel("ชื่อผู้ใช้ " + user.getUsername()));
        if (user instanceof Student){
            Student student = (Student) user;

            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + student.getEnrolledFaculty().getFacultyName() +  " (" + student.getEnrolledFaculty().getFacultyId() + ")"),
                    createLabel("สาขาวิชา " + student.getEnrolledDepartment().getDepartmentName() + " (" + student.getEnrolledDepartment().getDepartmentID() + ")"),
                    createLabel("อาจารย์ที่ปรึกษา " + (student.getStudentAdvisor() == null ? "ไม่มี" : student.getStudentAdvisor().getName() + " (" + student.getStudentAdvisor().getAdvisorID() + ")"))
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
                    createLabel("คณะ " + departmentOfficer.getFaculty().getFacultyName() + " (" + departmentOfficer.getFaculty().getFacultyId() + ")"),
                    createLabel("สาขาวิชา " + departmentOfficer.getDepartment().getDepartmentName() + " (" + departmentOfficer.getDepartment().getDepartmentID() + ")")
            );
        }
        else if (user instanceof Advisor) {
            Advisor advisor = (Advisor) user;
            userInfoPane.getChildren().addAll(
                    createLabel("คณะ " + advisor.getFaculty().getFacultyName() + " (" + advisor.getFaculty().getFacultyId() + ")"),
                    createLabel("สาขาวิชา " + advisor.getDepartment().getDepartmentName() + " (" + advisor.getDepartment().getDepartmentID() + ")")
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
        return label;
    }

    public void backButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo(data.get(0));
    }

    @FXML
    public void uploadProfilePictureButton(MouseEvent event) throws IOException {
        // Create a FileChooser to select an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        // Filter to show only image files (JPG, PNG)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png")
        );

        // Show open file dialog
        uploadedPicture = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (uploadedPicture != null) {
            // Set the file label to the name of the uploaded picture

            // Save the uploaded file to a directory (you can modify the directory path as needed)
            String directory = "data" + File.separator + "user-profile-picture" + File.separator;

            // Ensure the directory exists
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs(); // Create the directory if it doesn't exist
            }

            String extension = uploadedPicture.getName().substring(uploadedPicture.getName().lastIndexOf(".")); //ตัวบอกว่าไฟล์ที่อัพไปมีนามสกุลอะไร
            String filePath = directory + user.getUsername() + extension;

            FileStorage.saveFileToDirectory(uploadedPicture, filePath);
            user.setProfilePicturePath(filePath);  // Set the profile picture path for the user

            // Update the Circle profile picture display
            setProfilePicture(filePath);

            // Save changes (if necessary) and update the data source
            datasource.writeData(userList);
        }
    }

    @FXML
    public void changePassword(){
        try {
        if (!newPasswordField.getText().equals(confirmNewPasswordField.getText())) {
            throw new IllegalArgumentException("รหัสผ่านใหม่และยืนยันรหัสผ่านใหม่ไม่ตรงกัน");
            }
        if (!user.validatePassword(currentPasswordField.getText())){
            throw new IllegalArgumentException("รหัสผ่านปัจจุบันไม่ถูกต้อง");
            }
        user.setPassword(confirmNewPasswordField.getText(), false);
        datasource.writeData(userList);
        FXRouter.goTo("login");
        }
        catch (IllegalArgumentException e){
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void changeProfileToDefaultButton(MouseEvent event) throws IOException {
        user.setProfilePicturePath(null);  // Set the profile picture path for the user
        // Update the Circle profile picture display
        // Save changes (if necessary) and update the data source
        datasource.writeData(userList);
        Image profileImage = new Image(getClass().getResource("/images/profile.jpg").toExternalForm());
        profilePictureDisplay.setFill(new ImagePattern(profileImage));

    }
}
