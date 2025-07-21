module com.example.netrixapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires jdk.jdi;
    requires com.oracle.database.jdbc;

    opens com.example.netrixapp.Vistas to javafx.fxml, javafx.graphics;
    opens com.example.netrixapp.Controladores to javafx.fxml;
    opens com.example.netrixapp.Modelos to javafx.base;

    opens com.example.netrixapp to javafx.fxml;
    exports com.example.netrixapp;
}