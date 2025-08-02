package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorSolicitudes;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorUsuarios;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class VistaSolicitudes {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private ControladorSolicitudes controladorSolicitudes;

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

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        this.controladorSolicitudes= new ControladorSolicitudes(this);
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

           // Columnas

           TableColumn<Solicitud, String> colUsuario = new TableColumn<>("Usuario");
           colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

           TableColumn<Solicitud, String> colArticulo = new TableColumn<>("Artículo");
           colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

           TableColumn<Solicitud, String> colCantidad = new TableColumn<>("Cantidad");
           colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

           TableColumn<Solicitud, String> colTiempoUso = new TableColumn<>("Tiempo");
           colTiempoUso.setCellValueFactory(new PropertyValueFactory<>("tiempo_uso"));

           TableColumn<Solicitud, String> colRazon = new TableColumn<>("Razon");
           colRazon.setCellValueFactory(new PropertyValueFactory<>("razon"));

           TableColumn<Solicitud, String> colFechaSolicitud = new TableColumn<>("Fecha Solicitud");
           colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<>("fecha_solicitud"));

           TableColumn<Solicitud, String> colFechaRecibo = new TableColumn<>("Fecha Recibo");
           colFechaRecibo.setCellValueFactory(new PropertyValueFactory<>("fecha_recibo"));

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

           tablaSolicitudes.getColumns().addAll(colUsuario, colArticulo, colCantidad, colTiempoUso,
                   colRazon, colFechaSolicitud, colFechaRecibo, colEstado, colAcciones);

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
        System.out.println("Cambiando estado de solicitud de " + solicitud.getNombreUsario() +
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