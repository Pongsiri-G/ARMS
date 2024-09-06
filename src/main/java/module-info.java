module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires bcrypt;


    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;
}