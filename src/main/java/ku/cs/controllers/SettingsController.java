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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import ku.cs.models.*;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;

public class SettingsController {
    @FXML private VBox userInfoPane;

    @FXML private VBox settingPane;

    @FXML private Label roleLabel;

    @FXML private Label nameLabel;

    @FXML private Circle profilePictureDisplay;



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
        userPaneInitialize();
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
}
