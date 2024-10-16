module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.sql;
    requires kernel;
    requires layout;
    requires io;
    requires org.apache.pdfbox;
    requires javafx.swing; // add


    // Open models package to javafx.base for reflection access
    opens ku.cs.models to javafx.base;
    // Export the packages you need
    exports ku.cs.models;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;

}