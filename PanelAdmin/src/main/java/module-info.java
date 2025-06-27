module com.example.paneladmin {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.paneladmin.Modelos to javafx.base;

    opens com.example.paneladmin to javafx.fxml;
    exports com.example.paneladmin;
}