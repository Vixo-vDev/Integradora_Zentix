module mx.edu.utez.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens mx.edu.utez.demo to javafx.fxml;
    exports mx.edu.utez.demo;
}