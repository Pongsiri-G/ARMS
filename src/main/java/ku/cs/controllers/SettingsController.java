package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import ku.cs.models.Student;
import ku.cs.models.User;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsController {
    @FXML
    private Label roleLabel;

    @FXML private Label nameLabel;

    @FXML private Label usernameLabel;

    @FXML private Circle profilePictureDisplay;

    @FXML private ImageView optionDropdown;

    @FXML private Rectangle currentBar1;

    @FXML private Rectangle currentBar2;

    @FXML private ChoiceBox<String> typeRequestChoice;


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
        data = (ArrayList<String>) FXRouter.getData();
        user = userList.findUserByUsername(data.get(1));
        nameLabel.setText(user.getName());
        roleLabel.setText(user.getRole());
        setProfilePicture(user.getProfilePicturePath());
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
}
