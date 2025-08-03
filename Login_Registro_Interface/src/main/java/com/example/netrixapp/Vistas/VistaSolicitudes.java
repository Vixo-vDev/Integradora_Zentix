package com.example.netrixapp.Vistas;

import com.example.netrixapp.Controladores.ControladorRegistro;
import com.example.netrixapp.Controladores.ControladorSolicitudes;
import com.example.netrixapp.Controladores.ControladorBarraNavegacion;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.TipoEquipo;
import com.example.netrixapp.Modelos.Solicitud;
import impl.EquipoDaoImpl;
import impl.TipoEquipoDaoImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaSolicitudes {

    private Button btnAgregar;
    private Button btnEnviar;
    private TextField tfNota;
    private DatePicker dpFecha_recibo;
    private Spinner<Integer> spTiempoUso;
    private ComboBox<String> cbTipoEquipo;
    private ComboBox<Equipo> cbEquipos;
    private Spinner<Integer> spCantidad;

    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final TipoEquipoDaoImpl tipoEquipoDao = new TipoEquipoDaoImpl();
    private final EquipoDaoImpl equipoDao = new EquipoDaoImpl();

    private final Map<String, Integer> tipoNombreToId = new HashMap<>();

    private final String COLOR_ACCION_PRINCIPAL = "#2980B9";
    private final String COLOR_AGREGAR = "#009475";
    private final String COLOR_FONDO = "#F5F5F5";
    private final String COLOR_TEXTO = "#2C3E50";
    private final String COLOR_FONDO_TABLA = "#FFFFFF";

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();

    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        VBox panelContenido = new VBox(15);
        panelContenido.setPadding(new Insets(20));
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO_TABLA + "; -fx-background-radius: 10;");

        Label lblTipoEquipo = new Label("Tipo de equipo:");
        lblTipoEquipo.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        cbTipoEquipo = new ComboBox<>();
        cbTipoEquipo.setEditable(false);
        cbTipoEquipo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        Label lblEquipos = new Label("Equipos disponibles:");
        lblEquipos.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        cbEquipos = new ComboBox<>();
        cbEquipos.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        cbEquipos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Equipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescripcion());
            }
        });
        cbEquipos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDescripcion());
            }
        });

        Label lblCantidad = new Label("Cantidad:");
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        spCantidad = new Spinner<>(1, 100, 1);
        spCantidad.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        Label lblNota = new Label("Nota:");
        lblNota.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        tfNota = new TextField();
        tfNota.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        Label lblFecha = new Label("Fecha de recibo:");
        lblFecha.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        dpFecha_recibo = new DatePicker(LocalDate.now());
        dpFecha_recibo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        Label lblTiempoUso = new Label("Tiempo de uso (horas):");
        lblTiempoUso.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        spTiempoUso = new Spinner<>(1, 24, 8);
        spTiempoUso.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO + ";");

        btnEnviar = new Button("Enviar");
        btnEnviar.setStyle("-fx-background-color: " + COLOR_ACCION_PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
        new ControladorSolicitudes(this);

        HBox botones = new HBox(10, btnEnviar);
        botones.setAlignment(Pos.CENTER_RIGHT);

        panelContenido.getChildren().addAll(
                lblTipoEquipo, cbTipoEquipo,
                lblEquipos, cbEquipos,
                lblCantidad, spCantidad,
                lblNota, tfNota,
                lblFecha, dpFecha_recibo,
                lblTiempoUso, spTiempoUso,
                botones
        );

        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setCenter(panelContenido);

        cargarTiposEquipo();

        cbTipoEquipo.setOnAction(e -> {
            String tipoSeleccionado = cbTipoEquipo.getValue();
            cargarEquiposPorTipo(tipoSeleccionado);
        });

        cbEquipos.setOnAction(e -> {
            Equipo equipoSeleccionado = cbEquipos.getValue();
            if (equipoSeleccionado != null) {
                int maxCantidad = equipoDao.calcularCantidad(equipoSeleccionado.getDescripcion());
                spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCantidad, 1));
            }
        });
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

    public int getIdTipoEquipoSeleccionado() {
        String tipoSeleccionado = cbTipoEquipo.getValue();
        if (tipoSeleccionado != null && tipoNombreToId.containsKey(tipoSeleccionado)) {
            return tipoNombreToId.get(tipoSeleccionado);
        }
        return -1;
    }

    public ComboBox<String> getCbTipoEquipo() {
        return cbTipoEquipo;
    }

    public Equipo getEquipoSeleccionado() {
        return cbEquipos.getValue();
    }



    public String getNota() {
        return tfNota.getText().trim();
    }

    public LocalDate getFechaRecibo() {
        return dpFecha_recibo.getValue();
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

    public void setTfNota(TextField tfNota) {
        this.tfNota = tfNota;
    }

    public void setDpFecha_recibo(DatePicker dpFecha_recibo) {
        this.dpFecha_recibo = dpFecha_recibo;
    }

    public void setSpTiempoUso(Spinner<Integer> spTiempoUso) {
        this.spTiempoUso = spTiempoUso;
    }

    public void setCbTipoEquipo(ComboBox<String> cbTipoEquipo) {
        this.cbTipoEquipo = cbTipoEquipo;
    }

    public void setCbEquipos(ComboBox<Equipo> cbEquipos) {
        this.cbEquipos = cbEquipos;
    }

    public void setSpCantidad(Spinner<Integer> spCantidad) {
        this.spCantidad = spCantidad;
    }

    public TextField getTfNota() {
        return tfNota;
    }

    public DatePicker getDpFecha_recibo() {
        return dpFecha_recibo;
    }

    public Spinner<Integer> getSpTiempoUso() {
        return spTiempoUso;
    }
    public Spinner<Integer> getSpCantidad() {
        return spCantidad;
    }

    public ComboBox<Equipo> getCbEquipos() {
        return cbEquipos;
    }
}
