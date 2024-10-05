package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import ku.cs.models.Student;
import ku.cs.models.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;

public class StudentRequestListViewController {


    @FXML
    private ImageView optionDropdown;

    @FXML private Label roleLabel;

    @FXML private Label nameLabel;

    @FXML private Label usernameLabel;

    @FXML private Rectangle currentBar1;

    @FXML private Rectangle currentBar2;

    @FXML private Circle profilePictureDisplay;

    private UserList userList;
    private UserListFileDatasource datasource;
    private Student student;

    public StudentRequestListViewController(){
        datasource = new UserListFileDatasource("data/test", "studentlist.csv", "advisorlist.csv", "facultyofficerlist.csv","departmentofficerlist.csv", "facdeplist.csv");
        this.userList = datasource.readData();
    }

    @FXML
    public void initialize() {
        student = (Student) userList.findUserByUsername((String) FXRouter.getData());
        roleLabel.setText("นิสิต | ภาควิชา" + student.getEnrolledDepartment().getDepartmentName());
        nameLabel.setText(student.getName());
        usernameLabel.setText(student.getUsername());
        currentBar1.setVisible(false);
        setProfilePicture(student.getProfilePicturePath());
        System.out.println("[" + student.getName() + " " + student.getUsername() + "]");
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

    @FXML
    public void logoutClick(MouseEvent event) throws IOException {
        FXRouter.goTo("login");
    }

    @FXML
    public void createRequestPageClick(MouseEvent event) throws IOException {
        FXRouter.goTo("student-create-request", student.getUsername());
    }

    @FXML
    public void optionDropdown(MouseEvent event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Option 1");
        MenuItem item2 = new MenuItem("Option 2");
        MenuItem item3 = new MenuItem("Option 3");


        contextMenu.getItems().addAll(item1, item2, item3);

        if (!contextMenu.isShowing()) {
            Bounds optionBounds = optionDropdown.localToScreen(optionDropdown.getBoundsInLocal());
            contextMenu.show(optionDropdown, optionBounds.getMinX(), optionBounds.getMaxY());
        } else {
            contextMenu.hide();
        }

    }
}
