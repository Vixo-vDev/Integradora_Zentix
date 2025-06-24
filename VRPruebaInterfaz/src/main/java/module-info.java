module com.example.vrpruebainterfaz {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jdk.compiler;

    opens com.example.vrpruebainterfaz to javafx.fxml;
    exports com.example.vrpruebainterfaz;
}