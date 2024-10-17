package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;

public class AdminSettingsController extends BaseController {
    @FXML private Circle profilePictureDisplay;


    @FXML private ChoiceBox<String> themeChoiceBox;
    @FXML private ChoiceBox<String> fontFamilyChoiceBox;
    @FXML private ChoiceBox<String> fontSizeChoiceBox;
    @FXML private BorderPane rootPane;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Label errorLabel;

    private String data;
    private AdminPasswordFileDataSource adminDatasource;
    private Admin admin;

    private File uploadedPicture;


    public AdminSettingsController() {
        adminDatasource = new AdminPasswordFileDataSource("data/csv_files", "admin.csv");
        admin = adminDatasource.readData();
    }

    @FXML
    public void initialize() {
        data = (String) FXRouter.getData();
        setProfilePicture(profilePictureDisplay, admin.getProfilePicturePath());

        themeChoiceBox.getItems().addAll("Light", "Dark", "Autumn", "Coffee", "Fallen");
        fontFamilyChoiceBox.getItems().addAll("Noto Sans Thai", "Noto Sans Thai Looped", "Bai Jamjuree");
        fontSizeChoiceBox.getItems().addAll("Small", "Medium", "Large");

        themeChoiceBox.setValue(admin.getPreferences().getTheme());
        fontFamilyChoiceBox.setValue(admin.getPreferences().getFontFamily());
        fontSizeChoiceBox.setValue(admin.getPreferences().getFontSize());

        applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
        adminDatasource.writeData(admin);

        // Add listeners for immediate change on selection
        themeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            admin.getPreferences().setTheme(newValue);
            applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
            adminDatasource.writeData(admin);
        });

        fontFamilyChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            admin.getPreferences().setFontFamily(newValue);
            applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
            adminDatasource.writeData(admin);
        });

        fontSizeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            admin.getPreferences().setFontSize(newValue);
            applyThemeAndFont(rootPane, admin.getPreferences().getTheme(), admin.getPreferences().getFontFamily(), admin.getPreferences().getFontSize());
            adminDatasource.writeData(admin);
        });
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

    public void backButtonClick(MouseEvent event) throws IOException {
        FXRouter.goTo(data);
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
            String filePath = directory + admin.getUsername() + extension;

            FileStorage.saveFileToDirectory(uploadedPicture, filePath);
            admin.setProfilePicturePath(filePath);  // Set the profile picture path for the user

            // Update the Circle profile picture display
            setProfilePicture(filePath);

            // Save changes (if necessary) and update the data source
            adminDatasource.writeData(admin);
        }
    }

    @FXML
    public void changePassword(){
        try {
        if (!newPasswordField.getText().equals(confirmNewPasswordField.getText())) {
            throw new IllegalArgumentException("รหัสผ่านใหม่และยืนยันรหัสผ่านใหม่ไม่ตรงกัน");
            }
        if (!admin.validatePassword(currentPasswordField.getText())){
            throw new IllegalArgumentException("รหัสผ่านปัจจุบันไม่ถูกต้อง");
            }
        admin.setPassword(confirmNewPasswordField.getText(), false);
        adminDatasource.writeData(admin);
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
        String filePath = "src/main/resources/images/profile.jpg";
        admin.setProfilePicturePath(filePath);  // Set the profile picture path for the user
        // Update the Circle profile picture display
        setProfilePicture(filePath);
        // Save changes (if necessary) and update the data source
        adminDatasource.writeData(admin);
    }
}
