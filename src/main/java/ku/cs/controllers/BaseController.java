package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class BaseController {
    private String currentTheme = "Light";
    private String fontSize = "Medium";
    protected String fontFamily = "Noto Sans Thai";

    private void setTheme(BorderPane rootPane, String theme) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add(getClass().getResource("/style/" + theme + "-theme.css").toExternalForm());
    }
    private void setFontSize(BorderPane rootPane, String fontSize) {
        rootPane.getStyleClass().removeAll("Small", "Medium", "Large");
        rootPane.getStyleClass().add(fontSize);
    }

    private void setFontFamily(BorderPane rootPane, String fontFamily) {
        rootPane.setStyle("-fx-font-family: '" + fontFamily + "';");
    }

    @FXML
    public void setProfilePicture(Circle profilePictureDisplay, String profilePath) {
        try {
            // โหลดรูปจาก profilePath
            Image profileImage;
            if (profilePath == null) {
                profileImage = new Image(getClass().getResource("/images/profile.jpg").toExternalForm());
            }
            else {
                profileImage = new Image("file:" + profilePath);
            }
            profilePictureDisplay.setFill(new ImagePattern(profileImage));

        } catch (Exception e) {
            System.out.println("Error loading profile image: " + e.getMessage());
            profilePictureDisplay.setFill(Color.GRAY);
        }
    }

    @FXML
    public void applyThemeAndFont(BorderPane rootPane) {
        setTheme(rootPane, currentTheme);
        setFontSize(rootPane, fontSize);
        setFontFamily(rootPane, fontFamily);
    }
    @FXML
    public void applyThemeAndFont(BorderPane rootPane, String theme, String fontFamily, String fontSize) {
        this.currentTheme = theme;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        applyThemeAndFont(rootPane);
    }
}