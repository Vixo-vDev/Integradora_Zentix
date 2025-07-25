package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorLogin;
import com.example.netrixapp.Controladores.ControladorSolicitudes;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.TipoEquipo;
import impl.EquipoDaoImpl;
import impl.TipoEquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VistaSolicitudes {

    String descripcion = "";
    int cantidad = 0;

    //Variables de formulario
    private Button btnAgregar;
    private Button btnEnviar;
    private TextField tfNota;
    private DatePicker dpFecha_recibo;
    private Spinner<Integer> spTiempoUso;
    private ComboBox<String> cbTipoEquipo;
    private ComboBox<String> cbEquipos;
    private Spinner<Integer> spCantidad;

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    TipoEquipoDaoImpl tipoEquipoDao = new TipoEquipoDaoImpl();
    EquipoDaoImpl equipoDao = new EquipoDaoImpl();

    // Colores mejorados para mejor contraste
    private final String COLOR_ACCION_PRINCIPAL = "#2980B9";
    private final String COLOR_PELIGRO = "#C0392B";
    private final String COLOR_FONDO = "#F5F5F5";
    private final String COLOR_AGREGAR = "#009475";
    private final String COLOR_TEXTO = "#2C3E50";
    private final String COLOR_FONDO_TABLA = "#FFFFFF";
    private final String COLOR_TEXTO_TABLA = "#2C3E50";

    public Button getBtnAgregar() {
        return btnAgregar;
    }

    public Button getBtnEnviar() {
        return btnEnviar;
    }

    public String getNota() {
        return tfNota.getText().trim();
    }

    public LocalDate getFecha_recibo() {
        return dpFecha_recibo.getValue();
    }

    public String getTiempoUso() {
        return spTiempoUso.getValue().toString().trim();
    }

    public String getEquipos() {
        return cbEquipos.getSelectionModel().getSelectedItem().toString();
    }

    public int getCantidad() {
        return spCantidad.getValue().intValue();
    }

    public void setSpCantidad(Spinner<Integer> spCantidad) {
        this.spCantidad = spCantidad;
    }

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        new ControladorSolicitudes(this);
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Panel de contenido principal con mejor contraste
        VBox panelContenido = new VBox();
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO_TABLA + "; -fx-background-radius: 10;");
        panelContenido.setPadding(new Insets(20));

        // Título de la sección con mejor visibilidad
        Label lblTituloSeccion = new Label("Artículos a solicitar");
        lblTituloSeccion.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO + ";");

        // Tabla de artículos con texto visible
        TableView<ArticuloSolicitud> tablaArticulos = crearTablaArticulos();

        // Formulario con texto visible
        GridPane formulario = crearFormularioCampos();

        // Configuración del layout principal
        VBox contenidoPrincipal = new VBox(20, lblTituloSeccion, formulario, tablaArticulos);
        contenidoPrincipal.setStyle("-fx-text-fill: " + COLOR_TEXTO + ";");
        VBox.setVgrow(tablaArticulos, Priority.ALWAYS);

        ScrollPane scrollPrincipal = new ScrollPane(contenidoPrincipal);
        scrollPrincipal.setFitToWidth(true);
        scrollPrincipal.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPrincipal.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        panelContenido.getChildren().add(scrollPrincipal);
        VBox.setVgrow(scrollPrincipal, Priority.ALWAYS);

        // Construir vista completa
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setCenter(panelContenido);

        BorderPane.setMargin(panelContenido, new Insets(20));
        BorderPane.setMargin(controladorBarra.getBarraLateral(), new Insets(0, 20, 0, 0));
    }

    private TableView<ArticuloSolicitud> crearTablaArticulos() {
        TableView<ArticuloSolicitud> tablaArticulos = new TableView<>();
        tablaArticulos.setStyle("-fx-background-color: " + COLOR_FONDO_TABLA + "; -fx-text-fill: " + COLOR_TEXTO_TABLA + ";");

        // Configurar política de redimensionamiento
        tablaArticulos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Crear columnas con estilo legible
        Function<TableColumn<ArticuloSolicitud, String>, TableColumn<ArticuloSolicitud, String>> estiloColumna = col -> {
            col.setStyle("-fx-text-fill: " + COLOR_TEXTO_TABLA + ";");
            return col;
        };

        TableColumn<ArticuloSolicitud, String> columnaArticulo = estiloColumna.apply(
                new TableColumn<>("Artículo"));
        columnaArticulo.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<ArticuloSolicitud, String> columnaFecha = estiloColumna.apply(
                new TableColumn<>("Fecha de Solicitud"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        TableColumn<ArticuloSolicitud, String> columnaCantidad = estiloColumna.apply(
                new TableColumn<>("Cantidad"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<ArticuloSolicitud, String> columnaNota = estiloColumna.apply(
                new TableColumn<>("Nota"));
        columnaNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        TableColumn<ArticuloSolicitud, String> columnaTiempoUso = estiloColumna.apply(
                new TableColumn<>("Tiempo de Uso"));
        columnaTiempoUso.setCellValueFactory(new PropertyValueFactory<>("tiempoUso"));

        tablaArticulos.getColumns().addAll(columnaArticulo, columnaFecha, columnaCantidad,
                columnaNota, columnaTiempoUso);

        // Añadir datos de ejemplo con estilo
        tablaArticulos.setRowFactory(tv -> new TableRow<ArticuloSolicitud>() {
            @Override
            protected void updateItem(ArticuloSolicitud item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    setStyle("-fx-text-fill: " + COLOR_TEXTO_TABLA + ";");
                }
            }
        });

        tablaArticulos.getItems().addAll(
                new ArticuloSolicitud("Laptop HP", "2023-10-15", "1", "Para nuevo empleado", "8"),
                new ArticuloSolicitud("Monitor 24\"", "2023-10-16", "2", "Reemplazo equipos antiguos", "4"),
                new ArticuloSolicitud("Teclado inalámbrico", "2023-10-17", "1", "", "8")
        );

        return tablaArticulos;
    }

    private GridPane crearFormularioCampos() {
        GridPane formulario = new GridPane();
        formulario.setHgap(20);
        formulario.setVgap(15);
        formulario.setPadding(new Insets(10, 0, 10, 0));
        formulario.setStyle("-fx-text-fill: " + COLOR_TEXTO + ";");

        // Configurar columnas
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        formulario.getColumnConstraints().addAll(col1, col2);

        // Función para aplicar estilo común a los controles
        Function<Control, Control> aplicarEstilo = control -> {
            control.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14;");
            return control;
        };

        // Artículo (ComboBox)
        /*Label lblArticulo = new Label("Artículo:");
        lblArticulo.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        ComboBox<String> cbArticulo = (ComboBox<String>) aplicarEstilo.apply(new ComboBox<>());
        cbArticulo.getItems().addAll("Laptop HP", "Monitor 24\"", "Teclado inalámbrico", "Mouse óptico");
        HBox.setHgrow(cbArticulo, Priority.ALWAYS);
        formulario.add(lblArticulo, 0, 0);
        formulario.add(cbArticulo, 0, 1);*/

        // Fecha (DatePicker)
        Label lblFecha = new Label("Fecha:");
        lblFecha.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        dpFecha_recibo = (DatePicker) aplicarEstilo.apply(new DatePicker());
        HBox.setHgrow(dpFecha_recibo, Priority.ALWAYS);
        formulario.add(lblFecha, 1, 0);
        formulario.add(dpFecha_recibo, 1, 1);


        // Cantidad (TextField)
        /*Label lblCantidad = new Label("Cantidad:");
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        TextField tfCantidad = (TextField) aplicarEstilo.apply(new TextField());
        HBox.setHgrow(tfCantidad, Priority.ALWAYS);
        formulario.add(lblCantidad, 0, 2);
        formulario.add(tfCantidad, 0, 3);*/

        // Nota (TextField)
        Label lblNota = new Label("Nota:");
        lblNota.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        tfNota = (TextField) aplicarEstilo.apply(new TextField());
        HBox.setHgrow(tfNota, Priority.ALWAYS);
        formulario.add(lblNota, 1, 2);
        formulario.add(tfNota, 1, 3);

        // Tiempo de uso (Spinner)
        Label lblTiempoUso = new Label("Tiempo de Uso (horas):");
        lblTiempoUso.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        spTiempoUso = (Spinner<Integer>) aplicarEstilo.apply(new Spinner<>());
        spTiempoUso.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 24, 8));
        HBox.setHgrow(spTiempoUso, Priority.ALWAYS);
        formulario.add(lblTiempoUso, 0, 4);
        formulario.add(spTiempoUso, 0, 5);

        // Label y ComboBox para tipo de equipo
        Label lblTipoEquipo = new Label("Tipo de equipo:");
        lblTipoEquipo.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        cbTipoEquipo = new ComboBox<>();
        cbTipoEquipo.setEditable(true); // permite escritura para búsqueda predictiva
        HBox.setHgrow(cbTipoEquipo, Priority.ALWAYS);
        formulario.add(lblTipoEquipo, 0, 0);
        formulario.add(cbTipoEquipo, 0, 1);

        // Label dinámico con el nombre seleccionado
        Label lblNombreTipoSeleccionado = new Label();
        lblNombreTipoSeleccionado.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-size: 14px; -fx-font-weight: bold;");
        formulario.add(lblNombreTipoSeleccionado, 0, 8);

        // ComboBox de equipos filtrado
        Label lblEquipos = new Label("Equipos disponibles:");
        lblEquipos.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        cbEquipos = new ComboBox<>();
        HBox.setHgrow(cbEquipos, Priority.ALWAYS);
        formulario.add(lblEquipos, 0, 9);
        formulario.add(cbEquipos, 0, 10);

        Label lblCantidad = new Label("Cantidad:");
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold;");
        spCantidad = (Spinner<Integer>) aplicarEstilo.apply(new Spinner<>());
        HBox.setHgrow(spCantidad, Priority.ALWAYS);
        formulario.add(lblCantidad, 0, 2);
        formulario.add(spCantidad, 0, 3);

        cbEquipos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                descripcion = newValue.toString();
                cantidad = equipoDao.calcularCantidad(descripcion.trim());
                spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, cantidad , 1));
                System.out.println("El usuario seleccionó: " + newValue);
            }
        });


        // DAO para obtener los tipos de equipo y equipos
        // Map para vincular nombre ↔ ID de tipo equipo
        Map<String, Integer> tipoNombreToId = new HashMap<>();
        Map<Integer, String> tipoIdToNombre = new HashMap<>();

        // Llenar ComboBox con tipos de equipo
        for (TipoEquipo tipo : tipoEquipoDao.obtenerTodos()) {
            cbTipoEquipo.getItems().add(tipo.getNombre());
            tipoNombreToId.put(tipo.getNombre(), tipo.getId_tipo_equipo());
            tipoIdToNombre.put(tipo.getId_tipo_equipo(), tipo.getNombre());
        }

        // Evento: cuando el usuario selecciona un tipo de equipo
        cbTipoEquipo.setOnAction(event -> {
            String seleccionado = cbTipoEquipo.getValue();
            if (seleccionado != null && tipoNombreToId.containsKey(seleccionado)) {
                int idTipo = tipoNombreToId.get(seleccionado);

                // Actualizar el label
                lblNombreTipoSeleccionado.setText("Seleccionaste: " + seleccionado);

                // Limpiar ComboBox de equipos
                cbEquipos.getItems().clear();

                try {
                    // Cargar equipos correspondientes
                    List<Equipo> equipos = equipoDao.tipoEquipo(idTipo);
                    for (Equipo eq : equipos) {
                        cbEquipos.getItems().add(eq.getDescripcion());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // Botones con mejor contraste
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: " + COLOR_PELIGRO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        btnAgregar = new Button("Agregar");
        btnAgregar.setStyle("-fx-background-color: " + COLOR_AGREGAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        btnEnviar = new Button("Enviar");
        btnEnviar.setStyle("-fx-background-color: " + COLOR_ACCION_PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");

        HBox contenedorBotones = new HBox(15, btnAgregar, btnCancelar, btnEnviar);
        contenedorBotones.setAlignment(Pos.CENTER_RIGHT);
        formulario.add(contenedorBotones, 1, 5);

        return formulario;
    }

    public BorderPane getVista() {
        return vista;
    }

    //Clase para representar los artículos en solicitud
    public static class ArticuloSolicitud {
        private final String nombre;
        private final String fecha;
        private final String cantidad;
        private final String nota;
        private final String tiempoUso;

        public ArticuloSolicitud(String nombre, String fecha, String cantidad, String nota, String tiempoUso) {
            this.nombre = nombre;
            this.fecha = fecha;
            this.cantidad = cantidad;
            this.nota = nota;
            this.tiempoUso = tiempoUso;
        }

        public String getNombre() { return nombre; }
        public String getFecha() { return fecha; }
        public String getCantidad() { return cantidad; }
        public String getNota() { return nota; }
        public String getTiempoUso() { return tiempoUso; }
    }


}