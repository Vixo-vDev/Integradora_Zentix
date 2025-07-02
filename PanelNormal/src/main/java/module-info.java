module com.example.panelnormal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.panelnormal to javafx.fxml;
    exports com.example.panelnormal;
}