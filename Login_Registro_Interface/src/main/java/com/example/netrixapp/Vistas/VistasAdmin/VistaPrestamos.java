package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorPrestamos;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorUsuarios;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class VistaPrestamos {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private ControladorPrestamos controladorSolicitudes;

    private TableView<Solicitud> tablaSolicitudes;
    private Pagination paginador;
    private List<Solicitud> todosLasSolicitudes;
    private static final int FILAS_POR_PAGINA = 50;

    // Colores
    private final String COLOR_EXITO = "#2ECC71";
    private final String COLOR_PELIGRO = "#E74C3C";
    private final String COLOR_ADVERTENCIA = "#F39C12";
    private final String COLOR_FONDO = "#F5F7FA";
    private final String COLOR_TEXTO_OSCURO = "#2C3E50";

    public VistaPrestamos(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        this.controladorSolicitudes= new ControladorPrestamos(this);
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
        Label lblTitulo = new Label("Prestamos");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        contenido.getChildren().add(lblTitulo);

        // Tabla
        tablaSolicitudes = new TableView<>();
        tablaSolicitudes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        configurarColumnasTabla();

        paginador = new Pagination();
        paginador.setPageFactory(this::crearPagina);

        contenido.getChildren().addAll(tablaSolicitudes, paginador);

        vista.setCenter(contenido);
    }
    // Tabla de solicitudes
    private void configurarColumnasTabla(){
        tablaSolicitudes.getStylesheets().add(
                getClass().getResource("/css/tabla.css").toExternalForm()
        );
        // Columnas

        TableColumn<Solicitud, String> colUsuario = new TableColumn<>("Usuario");
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        TableColumn<Solicitud, String> colArticulo = new TableColumn<>("Artículo");
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

        TableColumn<Solicitud, String> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<Solicitud, String> colFechaSolicitud = new TableColumn<>("Fecha Solicitud");
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));

        TableColumn<Solicitud, String> colFechaRecibo = new TableColumn<>("Fecha Recibo");
        colFechaRecibo.setCellValueFactory(new PropertyValueFactory<>("fecha_recibo"));

        TableColumn<Solicitud, Integer> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("en_uso")); // Asume que en_uso es Integer (1 o 0)
        colEstado.setCellFactory(column -> new TableCell<Solicitud, Integer>() {
            private final StackPane pill = new StackPane();
            private final Label lblEstado = new Label();

            {
                // Estilo base para la píldora
                pill.getChildren().add(lblEstado);
                pill.setStyle("-fx-padding: 3px 12px; -fx-background-radius: 15; -fx-border-radius: 15;");
                lblEstado.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            }

            @Override
            protected void updateItem(Integer estado, boolean empty) {
                super.updateItem(estado, empty);

                if (empty || estado == null) {
                    setGraphic(null);
                } else {
                    switch (estado) {
                        case 1 -> { // No entregado/En uso
                            lblEstado.setText("NO ENTREGADO");
                            pill.setStyle(pill.getStyle() +
                                    "-fx-background-color: #FFEBEE; " +
                                    "-fx-text-fill: #C62828; " +
                                    "-fx-border-color: #EF9A9A; " +
                                    "-fx-border-width: 1;");
                            lblEstado.setTextFill(Color.valueOf("#C62828"));
                        }
                        case 0 -> { // Entregado/Disponible
                            lblEstado.setText("ENTREGADO");
                            pill.setStyle(pill.getStyle() +
                                    "-fx-background-color: #E8F5E9; " +
                                    "-fx-text-fill: #2E7D32; " +
                                    "-fx-border-color: #A5D6A7; " +
                                    "-fx-border-width: 1;");
                            lblEstado.setTextFill(Color.valueOf("#2E7D32"));
                        }
                        default -> { // Otros valores
                            lblEstado.setText("DESCONOCIDO");
                            pill.setStyle(pill.getStyle() +
                                    "-fx-background-color: #E3F2FD; " +
                                    "-fx-text-fill: #1565C0; " +
                                    "-fx-border-color: #90CAF9; " +
                                    "-fx-border-width: 1;");
                            lblEstado.setTextFill(Color.valueOf("#1565C0"));
                        }
                    }

                    setGraphic(pill);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        TableColumn<Solicitud, String> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(col -> new TableCell<Solicitud, String>() {
            private final Button btnEntregado = new Button();
            private final HBox cajaBotones = new HBox(5, btnEntregado);

            {

                ImageView iconoEditView = new ImageView(new Image(getClass().getResourceAsStream("/Imagenes/check.png")));
                iconoEditView.setFitWidth(16);
                iconoEditView.setFitHeight(16);
                btnEntregado.setGraphic(iconoEditView);
                btnEntregado.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                btnEntregado.setStyle("-fx-background-color: #009475; -fx-text-fill: white; -fx-padding: 4 8;");

                btnEntregado.setOnAction(e ->{
                    Solicitud solicitud = getTableView().getItems().get(getIndex());
                    controladorSolicitudes.actualizarEstadoPrestamo(solicitud, "aprobado");
                });


            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : cajaBotones);
            }
        });

        tablaSolicitudes.getColumns().addAll(colUsuario, colArticulo, colCantidad, colFechaSolicitud,
                colFechaRecibo, colEstado, colAcciones);

    }

    private VBox crearPagina(int pageIndex) {
        int fromIndex = pageIndex * FILAS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + FILAS_POR_PAGINA, todosLasSolicitudes.size());
        tablaSolicitudes.getItems().setAll(todosLasSolicitudes.subList(fromIndex, toIndex));
        return new VBox(tablaSolicitudes);
    }

    public void mostrarSolicitudes(List<Solicitud> solicitudes) {
        this.todosLasSolicitudes = solicitudes;
        int totalPaginas = (int) Math.ceil((double) todosLasSolicitudes.size() / FILAS_POR_PAGINA);
        if (totalPaginas == 0) totalPaginas = 1;
        paginador.setPageCount(totalPaginas);
        paginador.setCurrentPageIndex(0);
        paginador.setPageFactory(this::crearPagina);
    }

    private void cambiarEstadoSolicitud(Solicitud solicitud, String nuevoEstado) {
        // Aquí iría la lógica para actualizar el estado en la base de datos
        System.out.println("Cambiando estado de solicitud de " + solicitud.getNombreUsuario() +
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

}