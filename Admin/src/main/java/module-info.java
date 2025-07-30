module interfaz.admin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.sql;
    requires ojdbc8;


    // Exporta para que FXML pueda crear instancias
    exports interfaz.admin.controladores;
    // Abre el paquete para reflexión (necesario para inyección de campos @FXML)
    opens interfaz.admin.controladores to javafx.fxml;
    opens interfaz.admin.modelos to javafx.base;

    opens interfaz.admin to javafx.fxml;
    exports interfaz.admin;
}