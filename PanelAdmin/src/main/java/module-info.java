module com.example.paneladmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // Asegúrate de que este esté presente para las Vistas y Controladores

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop; // Mantenlo si lo necesitas para otras funcionalidades, si no, puedes quitarlo

    // Abre el paquete base 'com.example.paneladmin' a los módulos necesarios.
    // javafx.fxml: para tu hello-view.fxml (si lo mantienes)
    // javafx.graphics: para que JavaFX pueda acceder y crear tus Vistas y Controladores
    // javafx.base: IMPORTANTE para getClass().getResourceAsStream() para acceder a los recursos (imagenes, iconos)
    opens com.example.paneladmin to javafx.fxml, javafx.graphics, javafx.base;

    // También es una buena práctica abrir los paquetes de Controladores, Modelos y Vistas
    // a javafx.graphics y javafx.base si interactúan mucho con el framework de JavaFX.
    // En tu caso, 'opens com.example.paneladmin' ya cubre sus subpaquetes implícitamente para recursos,
    // pero para las clases de Vistas/Controladores es mejor ser explícito si no están en el paquete raíz.
    opens com.example.paneladmin.Controladores to javafx.graphics;
    opens com.example.paneladmin.Modelos to javafx.base; // Para PropertyValueFactory en tablas
    opens com.example.paneladmin.Vistas to javafx.graphics;

    exports com.example.paneladmin; // Exporta el paquete principal para que la clase HelloApplication sea visible
}