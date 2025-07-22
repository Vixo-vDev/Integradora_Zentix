package com.example.paneladmin.Vistas;

import com.example.paneladmin.Controladores.ControladorBarraNavegacion;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class VistaSolicitudes {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Colores
    private final String COLOR_EXITO = "#2ECC71";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_ADVERTENCIA = "#F39C12";
    private final String COLOR_FONDO = "#F5F7FA";
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Barras de navegación
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenido principal
        VBox contenido = new VBox(20);
        contenido.setPadding(new Insets(20));

        // Título
        Label lblTitulo = new Label("Solicitudes de Usuarios");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        contenido.getChildren().add(lblTitulo);

        // Filtros
        HBox filtros = new HBox(15);
        filtros.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> cbFiltroEstado = new ComboBox<>();
        cbFiltroEstado.getItems().addAll("Todas", "Pendientes", "Aprobadas", "Rechazadas");
        cbFiltroEstado.setValue("Todas");
        cbFiltroEstado.setPromptText("Filtrar por estado");

        ComboBox<String> cbFiltroUsuario = new ComboBox<>();
        cbFiltroUsuario.getItems().addAll("Todos los usuarios", "jperez", "mgarcia", "lrodriguez");
        cbFiltroUsuario.setValue("Todos los usuarios");
        cbFiltroUsuario.setPromptText("Filtrar por usuario");

        filtros.getChildren().addAll(
                new Label("Filtros:"),
                cbFiltroEstado,
                cbFiltroUsuario
        );
        contenido.getChildren().add(filtros);

        // Tabla de solicitudes
        TableView<Solicitud> tablaSolicitudes = new TableView<>();
        tablaSolicitudes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaSolicitudes.setStyle("-fx-font-size: 14px;");

        // Columnas
        TableColumn<Solicitud, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));

        TableColumn<Solicitud, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<Solicitud, String> colArticulo = new TableColumn<>("Artículo");
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

        TableColumn<Solicitud, String> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<Solicitud, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<Solicitud, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Aprobada":
                            setStyle("-fx-text-fill: " + COLOR_EXITO + "; -fx-font-weight: bold;");
                            break;
                        case "Rechazada":
                            setStyle("-fx-text-fill: " + COLOR_PELIGRO + "; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: " + COLOR_ADVERTENCIA + "; -fx-font-weight: bold;");
                    }
                }
            }
        });

        TableColumn<Solicitud, String> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<Solicitud, String>() {
            private final Button btnAprobar = new Button("Aprobar");
            private final Button btnRechazar = new Button("Rechazar");
            private final Button btnPendiente = new Button("Pendiente");
            private final HBox cajaBotones = new HBox(5, btnAprobar, btnRechazar, btnPendiente);

            {
                btnAprobar.setStyle("-fx-background-color: " + COLOR_EXITO + "; -fx-text-fill: white;");
                btnRechazar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white;");
                btnPendiente.setStyle("-fx-background-color: " + COLOR_ADVERTENCIA + "; -fx-text-fill: white;");

                btnAprobar.setOnAction(e -> {
                    Solicitud solicitud = getTableView().getItems().get(getIndex());
                    cambiarEstadoSolicitud(solicitud, "Aprobada");
                });

                btnRechazar.setOnAction(e -> {
                    Solicitud solicitud = getTableView().getItems().get(getIndex());
                    cambiarEstadoSolicitud(solicitud, "Rechazada");
                });

                btnPendiente.setOnAction(e -> {
                    Solicitud solicitud = getTableView().getItems().get(getIndex());
                    cambiarEstadoSolicitud(solicitud, "Pendiente");
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cajaBotones);
            }
        });

        tablaSolicitudes.getColumns().addAll(colUsuario, colFecha, colArticulo, colCantidad, colEstado, colAcciones);

        // Datos de ejemplo
        tablaSolicitudes.getItems().addAll(
                new Solicitud("Tony", "2023-11-15", "Laptop HP", "1", "Pendiente"),
                new Solicitud("David", "2023-11-14", "Monitor 24\"", "2", "Aprobada"),
                new Solicitud("Eliaquim", "2023-11-13", "Teclado inalámbrico", "1", "Rechazada"),
                new Solicitud("Vega", "2023-11-12", "Mouse óptico", "1", "Pendiente")
        );

        contenido.getChildren().add(tablaSolicitudes);
        vista.setCenter(contenido);
    }

    private void cambiarEstadoSolicitud(Solicitud solicitud, String nuevoEstado) {
        // Aquí iría la lógica para actualizar el estado en la base de datos
        System.out.println("Cambiando estado de solicitud de " + solicitud.getUsuario() +
                " a " + nuevoEstado);

        // Actualizar el estado en la tabla
        solicitud.setEstado(nuevoEstado);

        // Mostrar confirmación
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Estado actualizado");
        alert.setHeaderText("La solicitud ha sido actualizada");
        alert.setContentText("Nuevo estado: " + nuevoEstado);
        alert.show();
    }

    public BorderPane getVista() {
        return vista;
    }

    // Clase modelo para solicitudes
    public static class Solicitud {
        private final String usuario;
        private final String fecha;
        private final String articulo;
        private final String cantidad;
        private String estado;

        public Solicitud(String usuario, String fecha, String articulo, String cantidad, String estado) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.articulo = articulo;
            this.cantidad = cantidad;
            this.estado = estado;
        }

        // Getters
        public String getUsuario() { return usuario; }
        public String getFecha() { return fecha; }
        public String getArticulo() { return articulo; }
        public String getCantidad() { return cantidad; }
        public String getEstado() { return estado; }

        // Setter para estado
        public void setEstado(String estado) { this.estado = estado; }
    }
}