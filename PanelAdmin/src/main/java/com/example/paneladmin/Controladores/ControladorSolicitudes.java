package com.example.paneladmin.Controladores;

import com.example.paneladmin.Modelos.Solicitud;
import com.example.paneladmin.Vistas.VistaSolicitudes;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;

    public ControladorSolicitudes(VistaSolicitudes vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        Label etiquetaTitulo = new Label("Solicitudes Pendientes");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Configurar tabla
        TableView<Solicitud> tabla = new TableView<>();
        configurarTabla(tabla);

        // Configurar botones de acción
        HBox contenedorBotones = crearContenedorBotones();

        // Agregar elementos a la vista
        vista.getVista().getChildren().addAll(etiquetaTitulo, contenedorBotones, tabla);
        vista.setTabla(tabla);
    }

    private void configurarTabla(TableView<Solicitud> tabla) {
        // Columnas
        TableColumn<Solicitud, String> columnaId = new TableColumn<>("ID");
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaId.setPrefWidth(80);

        TableColumn<Solicitud, String> columnaTipo = new TableColumn<>("Tipo");
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnaTipo.setPrefWidth(150);

        TableColumn<Solicitud, String> columnaUsuario = new TableColumn<>("Usuario");
        columnaUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        columnaUsuario.setPrefWidth(150);

        TableColumn<Solicitud, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaFecha.setPrefWidth(120);

        // Configuración adicional de la tabla
        tabla.setPlaceholder(new Label("No hay solicitudes pendientes"));
        tabla.getColumns().addAll(columnaId, columnaTipo, columnaUsuario, columnaFecha);
        tabla.setStyle("-fx-background-color: white;");
    }

    private HBox crearContenedorBotones() {
        HBox contenedorBotones = new HBox(15);
        contenedorBotones.setStyle("-fx-padding: 10 0;");
        contenedorBotones.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button btnAprobar = crearBotonAccion("Aprobar", "#2ecc71");
        Button btnRechazar = crearBotonAccion("Rechazar", "#e74c3c");
        Button btnDetalles = crearBotonAccion("Detalles", "#3498db");
        Button btnFiltrar = crearBotonAccion("Filtrar", "#f39c12");

        // Configurar eventos
        btnAprobar.setOnAction(e -> aprobarSolicitud());
        btnRechazar.setOnAction(e -> rechazarSolicitud());
        btnDetalles.setOnAction(e -> verDetalles());
        btnFiltrar.setOnAction(e -> filtrarSolicitudes());

        contenedorBotones.getChildren().addAll(btnAprobar, btnRechazar, btnDetalles, btnFiltrar);
        return contenedorBotones;
    }

    private Button crearBotonAccion(String texto, String color) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 5;");

        boton.setOnMouseEntered(e -> boton.setStyle(
                "-fx-background-color: derive(" + color + ", -20%); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        boton.setOnMouseExited(e -> boton.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;"
        ));

        return boton;
    }

    // Métodos de acción
    private void aprobarSolicitud() {
        Solicitud seleccionada = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            System.out.println("Aprobando solicitud ID: " + seleccionada.getId());
            // Aquí iría la lógica para aprobar la solicitud
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione una solicitud para aprobar");
        }
    }

    private void rechazarSolicitud() {
        Solicitud seleccionada = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            System.out.println("Rechazando solicitud ID: " + seleccionada.getId());
            // Aquí iría la lógica para rechazar la solicitud
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione una solicitud para rechazar");
        }
    }

    private void verDetalles() {
        Solicitud seleccionada = vista.getTabla().getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            System.out.println("Mostrando detalles de solicitud ID: " + seleccionada.getId());
            // Aquí iría la lógica para mostrar detalles
        } else {
            mostrarAlerta("Selección requerida", "Por favor seleccione una solicitud para ver detalles");
        }
    }

    private void filtrarSolicitudes() {
        System.out.println("Mostrando diálogo de filtrado");
        // Aquí iría la lógica para filtrar las solicitudes
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}