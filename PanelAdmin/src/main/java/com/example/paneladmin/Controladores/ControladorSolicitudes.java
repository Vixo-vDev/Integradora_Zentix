package com.example.paneladmin.Controladores;
import com.example.paneladmin.Modelos.Solicitud;
import com.example.paneladmin.Vistas.VistaSolicitudes;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ControladorSolicitudes {
    private VistaSolicitudes vista;

    public ControladorSolicitudes(VistaSolicitudes vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        Label titulo = new Label("Solicitudes");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        VBox.setMargin(titulo, new Insets(10, 0, 10, 10));

        // Configurar tabla
        TableView<Solicitud> tabla = new TableView<>();
        tabla.getStylesheets().add(getClass().getResource("/imagenes/estilos.css").toExternalForm());


        configurarTabla(tabla);

        // Configurar botones de acción
        // HBox contenedorBotones = crearContenedorBotones();

        HBox filtroBusqueda = crearBuscador();
        Button btnRegresar = crearBotonRegresar();

        // Agregar elementos a la vista
        // vista.getVista().getChildren().addAll(etiquetaTitulo, contenedorBotones, tabla);
        // vista.setTabla(tabla);

        VBox layout = vista.getVista();
        layout.getChildren().addAll(titulo, tabla, filtroBusqueda, btnRegresar);
        vista.setTabla(tabla);
    }

    private void configurarTabla(TableView<Solicitud> tabla) {
        // Columnas

        TableColumn<Solicitud, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaNombre.setPrefWidth(150);

        TableColumn<Solicitud, String> columnaRol = new TableColumn<>("Rol");
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        columnaRol.setPrefWidth(80);

        TableColumn<Solicitud, String> columnaArticulo = new TableColumn<>("Articulo");
        columnaArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        columnaArticulo.setPrefWidth(150);

        TableColumn<Solicitud, String> columnaEstado = new TableColumn<>("Estado");
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        columnaEstado.setPrefWidth(120);

        TableColumn<Solicitud, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaFecha.setPrefWidth(120);

        TableColumn<Solicitud, Void> columnaAccion = new TableColumn<>("Acción");
        columnaAccion.setPrefWidth(100);
        columnaAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("+");

            {
                btn.setOnAction(event -> {
                    Solicitud seleccionada = getTableView().getItems().get(getIndex());
                    mostrarDetallesSolicitud(seleccionada);
                });
                btn.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Configuración adicional de la tabla
        tabla.getColumns().addAll(columnaNombre, columnaRol, columnaArticulo, columnaEstado, columnaFecha, columnaAccion);
        tabla.setPlaceholder(new Label("No hay solicitudes pendientes"));
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPrefHeight(300);
        tabla.setStyle("-fx-background-color: white;");
    }

    /* private HBox crearContenedorBotones() {
        HBox contenedorBotones = new HBox(15);
        contenedorBotones.setStyle("-fx-padding: 10 0;");
        contenedorBotones.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Button btnAprobar = crearBotonAccion("Aprobar", "#009475");
        Button btnRechazar = crearBotonAccion("Rechazar", "#940000");
        Button btnDetalles = crearBotonAccion("Detalles", "#002E60");
        Button btnFiltrar = crearBotonAccion("Filtrar", "#E09D00");

        // Configurar eventos
        btnAprobar.setOnAction(e -> aprobarSolicitud());
        btnRechazar.setOnAction(e -> rechazarSolicitud());
        btnDetalles.setOnAction(e -> verDetalles());
        btnFiltrar.setOnAction(e -> filtrarSolicitudes());

        contenedorBotones.getChildren().addAll(btnAprobar, btnRechazar, btnDetalles, btnFiltrar);
        return contenedorBotones;
    } */

    /* private Button crearBotonAccion(String texto, String color) {
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
    */

    private HBox crearBuscador() {

    // Campo de búsqueda
    TextField campoBusqueda = new TextField();
    campoBusqueda.setPromptText("Buscar...");
    campoBusqueda.setStyle("-fx-background-color: #ddd;");

    Button botonBuscar = new Button("🔍");
    botonBuscar.setStyle("-fx-background-color: #002E60; -fx-text-fill: white; -fx-font-weight: bold;");
    
    HBox campoConBoton = new HBox(campoBusqueda, botonBuscar);
    campoConBoton.setSpacing(5); 

    ComboBox<String> comboEstado = new ComboBox<>();
    comboEstado.getItems().addAll("Todos", "Pendiente", "Aprobado", "Rechazado");
    comboEstado.setValue("Estado");

    comboEstado.setStyle(
    "-fx-background-color: #009475; " +    
    "-fx-text-fill: white; " +              
    "-fx-font-weight: bold;"                
);

    // Contenedor final que incluye el campo con su botón y el ComboBox
    HBox contenedor = new HBox(10, campoConBoton, comboEstado);
    contenedor.setPadding(new Insets(10));

    return contenedor;
}

    private Button crearBotonRegresar() {
        Button botonRegresar = new Button("Regresar");
        botonRegresar.setStyle("-fx-background-color: #626262; -fx-text-fill: white; -fx-font-weight: bold;");
        VBox.setMargin(botonRegresar, new Insets(10, 0, 0, 10));
        return botonRegresar;
    }

    private void mostrarDetallesSolicitud(Solicitud solicitud) {
        Stage ventana = new Stage();
        ventana.setTitle("DetallesSolicitud");

        Label titulo = new Label("Detalles de la solicitud");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label lblNombre = new Label("Nombre: " + solicitud.getNombre());
        Label lblRol = new Label("Rol: " + solicitud.getRol());
        Label lblArticulo = new Label("Artículo: " + solicitud.getArticulo());
        Label lblFecha = new Label("Fecha: " + solicitud.getFecha());

        VBox info = new VBox(10, titulo, lblNombre, lblRol, lblArticulo, lblFecha);
        info.setPadding(new Insets(20));

        Button btnAprobar = new Button("Aprobar");
        btnAprobar.setStyle("-fx-background-color: #009475; -fx-text-fill: white;");

        Button btnRechazar = new Button("Rechazar");
        btnRechazar.setStyle("-fx-background-color: #940000; -fx-text-fill: white;");

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setStyle("-fx-background-color: #aaa;");
        btnRegresar.setOnAction(e -> ventana.close());

        HBox botones = new HBox(20, btnRegresar, btnAprobar, btnRechazar);
        botones.setPadding(new Insets(0, 0, 20, 20));

        VBox layout = new VBox(10, info, botones);
        layout.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");
        layout.setPadding(new Insets(10));

        Scene escena = new Scene(layout, 350, 250);
        ventana.setScene(escena);
        ventana.show();
    }
}



