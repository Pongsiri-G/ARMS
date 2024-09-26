module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires bcrypt;
    requires java.sql;
    requires java.base; // add


    // Open models package to javafx.base for reflection access
    opens ku.cs.models to javafx.base;
    // Export the packages you need
    exports ku.cs.models;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;

}