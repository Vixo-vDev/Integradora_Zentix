package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorSolicitudes;
import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.TipoEquipo;
import impl.EquipoDaoImpl;
import impl.TipoEquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaSolicitudes {

    // Componentes de la interfaz
    private TextField tfNota;
    private DatePicker dpFechaRecibo;
    private Spinner<Integer> spTiempoUso;
    private ComboBox<String> cbTipoEquipo;
    private ComboBox<Equipo> cbEquipos;
    private Spinner<Integer> spCantidad;
    private Button btnEnviar;

    // Layout principal
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;

    // Acceso a datos
    private final TipoEquipoDaoImpl tipoEquipoDao = new TipoEquipoDaoImpl();
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();
    private final Map<String, Integer> tipoNombreToId = new HashMap<>();

    // Paleta de colores moderna
    private final String COLOR_BOTON_PRINCIPAL = "#009475";  // Color verde solicitado
    private final String COLOR_BOTON_HOVER = "#007A63";       // Versión más oscura para hover
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_FONDO = "#FFFFFF";

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
        new ControladorSolicitudes(this);
    }

    private void inicializarUI() {
        // Configuración del layout principal
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());

        // Contenedor principal con scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: " + COLOR_FONDO + ";");

        // Panel de contenido
        VBox panelContenido = new VBox(20);
        panelContenido.setPadding(new Insets(30));
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO + ";");
        panelContenido.setAlignment(Pos.TOP_CENTER);

        // Título del formulario
        Label tituloFormulario = new Label("Nueva Solicitud de Equipo");
        tituloFormulario.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");
        panelContenido.getChildren().add(tituloFormulario);

        // Formulario en un panel con bordes
        GridPane formulario = new GridPane();
        formulario.setHgap(20);
        formulario.setVgap(15);
        formulario.setPadding(new Insets(25));
        formulario.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");
        formulario.setAlignment(Pos.TOP_CENTER);

        // Configuración de columnas
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(200);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(300);
        formulario.getColumnConstraints().addAll(col1, col2);

        // Campo: Tipo de equipo
        Label lblTipoEquipo = crearEtiqueta("Tipo de equipo:");
        cbTipoEquipo = crearComboBox();
        formulario.add(lblTipoEquipo, 0, 0);
        formulario.add(cbTipoEquipo, 1, 0);

        // Campo: Equipos disponibles
        Label lblEquipos = crearEtiqueta("Equipo solicitado:");
        cbEquipos = new ComboBox<>();
        configurarComboBoxEquipos();
        formulario.add(lblEquipos, 0, 1);
        formulario.add(cbEquipos, 1, 1);

        // Campo: Cantidad
        Label lblCantidad = crearEtiqueta("Cantidad:");
        spCantidad = crearSpinner(1, 100, 1);
        formulario.add(lblCantidad, 0, 2);
        formulario.add(spCantidad, 1, 2);

        // Campo: Nota
        Label lblNota = crearEtiqueta("Notas adicionales:");
        tfNota = crearTextField();
        formulario.add(lblNota, 0, 3);
        formulario.add(tfNota, 1, 3);

        // Campo: Fecha de recibo
        Label lblFecha = crearEtiqueta("Fecha requerida:");
        dpFechaRecibo = crearDatePicker();
        formulario.add(lblFecha, 0, 4);
        formulario.add(dpFechaRecibo, 1, 4);

        // Campo: Tiempo de uso
        Label lblTiempoUso = crearEtiqueta("Tiempo estimado (horas):");
        spTiempoUso = crearSpinner(1, 24, 8);
        formulario.add(lblTiempoUso, 0, 5);
        formulario.add(spTiempoUso, 1, 5);

        // Botón de enviar con el color #009475
        btnEnviar = crearBotonAccion("Enviar Solicitud", COLOR_BOTON_PRINCIPAL, COLOR_BOTON_HOVER);
        HBox contenedorBoton = new HBox(btnEnviar);
        contenedorBoton.setAlignment(Pos.CENTER_RIGHT);
        formulario.add(contenedorBoton, 1, 6);

        panelContenido.getChildren().add(formulario);
        scrollPane.setContent(panelContenido);
        vista.setCenter(scrollPane);

        // Cargar datos iniciales
        cargarTiposEquipo();

        // Eventos
        cbTipoEquipo.setOnAction(e -> {
            String tipoSeleccionado = cbTipoEquipo.getValue();
            cargarEquiposPorTipo(tipoSeleccionado);
        });

        cbEquipos.setOnAction(e -> actualizarCantidadMaxima());
    }

    private Label crearEtiqueta(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold;");
        return label;
    }

    private TextField crearTextField() {
        TextField textField = new TextField();
        textField.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        textField.setMaxWidth(Double.MAX_VALUE);
        return textField;
    }

    private ComboBox<String> crearComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        comboBox.setMaxWidth(Double.MAX_VALUE);
        return comboBox;
    }

    private void configurarComboBoxEquipos() {
        cbEquipos.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        cbEquipos.setMaxWidth(Double.MAX_VALUE);

        cbEquipos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Equipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescripcion() + " (Disponibles: " + item.getDisponible() + ")");
            }
        });

        cbEquipos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescripcion());
            }
        });
    }

    private DatePicker crearDatePicker() {
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        datePicker.setMaxWidth(Double.MAX_VALUE);
        return datePicker;
    }

    private Spinner<Integer> crearSpinner(int min, int max, int inicial) {
        Spinner<Integer> spinner = new Spinner<>(min, max, inicial);
        spinner.setStyle("-fx-font-size: 14px; -fx-padding: 8 12; -fx-background-radius: 6;");
        spinner.setMaxWidth(Double.MAX_VALUE);
        return spinner;
    }

    private Button crearBotonAccion(String texto, String colorNormal, String colorHover) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + colorNormal + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 20; " +
                "-fx-background-radius: 6;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: " + colorHover + "; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10 20; " +
                "-fx-background-radius: 6;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: " + colorNormal + "; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10 20; " +
                "-fx-background-radius: 6;"));
        return boton;
    }

    private void cargarTiposEquipo() {
        tipoNombreToId.clear();
        cbTipoEquipo.getItems().clear();
        try {
            List<TipoEquipo> tipos = tipoEquipoDao.obtenerTodos();
            for (TipoEquipo tipo : tipos) {
                cbTipoEquipo.getItems().add(tipo.getNombre());
                tipoNombreToId.put(tipo.getNombre(), tipo.getId_tipo_equipo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarEquiposPorTipo(String tipoNombre) {
        cbEquipos.getItems().clear();
        if (tipoNombre == null || !tipoNombreToId.containsKey(tipoNombre)) return;

        int idTipo = tipoNombreToId.get(tipoNombre);
        try {
            List<Equipo> equipos = equipoDao.tipoEquipo(idTipo);
            cbEquipos.getItems().addAll(equipos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarCantidadMaxima() {
        Equipo equipoSeleccionado = cbEquipos.getValue();
        if (equipoSeleccionado != null) {
            int maxCantidad = equipoSeleccionado.getDisponible();
            spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCantidad, 1));
        }
    }

    // Getters para los componentes
    public int getIdTipoEquipoSeleccionado() {
        String tipoSeleccionado = cbTipoEquipo.getValue();
        return tipoSeleccionado != null && tipoNombreToId.containsKey(tipoSeleccionado) ?
                tipoNombreToId.get(tipoSeleccionado) : -1;
    }

    public Equipo getEquipoSeleccionado() {
        return cbEquipos.getValue();
    }

    public String getNota() {
        return tfNota.getText().trim();
    }

    public LocalDate getFechaRecibo() {
        return dpFechaRecibo.getValue();
    }

    public int getTiempoUso() {
        return spTiempoUso.getValue();
    }

    public int getCantidad() {
        return spCantidad.getValue();
    }

    public Button getBtnEnviar() {
        return btnEnviar;
    }

    public BorderPane getVista() {
        return vista;
    }

    public TextField getTfNota() {
        return tfNota;
    }

    public void setTfNota(TextField tfNota) {
        this.tfNota = tfNota;
    }

    public DatePicker getDpFecha_recibo() {
        return dpFechaRecibo;
    }

    public void setDpFecha_recibo(DatePicker dpFechaRecibo) {
        this.dpFechaRecibo = dpFechaRecibo;
    }

    public Spinner<Integer> getSpTiempoUso() {
        return spTiempoUso;
    }

    public void setSpTiempoUso(Spinner<Integer> spTiempoUso) {
        this.spTiempoUso = spTiempoUso;
    }

    public ComboBox<String> getCbTipoEquipo() {
        return cbTipoEquipo;
    }

    public void setCbTipoEquipo(ComboBox<String> cbTipoEquipo) {
        this.cbTipoEquipo = cbTipoEquipo;
    }

    public ComboBox<Equipo> getCbEquipos() {
        return cbEquipos;
    }

    public void setCbEquipos(ComboBox<Equipo> cbEquipos) {
        this.cbEquipos = cbEquipos;
    }

    public Spinner<Integer> getSpCantidad() {
        return spCantidad;
    }

    public void setSpCantidad(Spinner<Integer> spCantidad) {
        this.spCantidad = spCantidad;
    }

    public void setBtnEnviar(Button btnEnviar) {
        this.btnEnviar = btnEnviar;
    }

    public ControladorBarraNavegacion getControladorBarra() {
        return controladorBarra;
    }

    public TipoEquipoDaoImpl getTipoEquipoDao() {
        return tipoEquipoDao;
    }

    public EquipoDaoImpl getEquipoDao() {
        return equipoDao;
    }

    public Map<String, Integer> getTipoNombreToId() {
        return tipoNombreToId;
    }
}