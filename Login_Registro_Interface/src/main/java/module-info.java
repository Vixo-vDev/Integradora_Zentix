module Vistas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jdk.compiler;

    opens com.example.vrpruebainterfaz to javafx.fxml;
    exports Vistas;
    exports Controladores;
    opens Controladores to javafx.fxml;
    opens Vistas to javafx.fxml;
}