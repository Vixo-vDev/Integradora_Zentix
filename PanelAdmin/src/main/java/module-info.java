module com.example.paneladmin {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.paneladmin.Vistas to javafx.fxml;
    exports com.example.paneladmin;
    exports com.example.paneladmin.Vistas;
    exports com.example.paneladmin.Controladores;
}