package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class BaseController {
    private String lightTheme = getClass().getResource("/style/light-theme.css").toExternalForm();
    private String darkTheme = getClass().getResource("/style/dark-theme.css").toExternalForm();
    private String currentTheme = darkTheme;
    private String fontSize = "large-font";
    protected String fontFamily = "Noto Sans Thai";

    private void setTheme(BorderPane rootPane, String theme) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add(theme);
    }
    private void setFontSize(BorderPane rootPane, String fontSize) {
        rootPane.getStyleClass().removeAll("small-font", "medium-font", "large-font");
        rootPane.getStyleClass().add(fontSize);
    }

    private void setFontFamily(BorderPane rootPane, String fontClass) {
        rootPane.getStyleClass().removeAll("Noto Sans Thai", "Bai Jamjuree");
        rootPane.getStyleClass().add(fontClass);
    }

    @FXML
    public void setProfilePicture(Circle profilePictureDisplay, String profilePath) {
        try {
            // โหลดรูปจาก profilePath
            Image profileImage = new Image("file:" + profilePath);

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