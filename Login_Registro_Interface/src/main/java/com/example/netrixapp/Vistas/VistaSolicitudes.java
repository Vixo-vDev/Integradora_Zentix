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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VistaSolicitudes {

    private Button btnAgregar;
    private Button btnEnviar;
    private TextField tfNota;
    private DatePicker dpFecha_recibo;
    private ComboBox<String> cbHoraEntrega;
    private Spinner<Integer> spTiempoUso;
    private ComboBox<String> cbTipoEquipo;
    private ComboBox<Equipo> cbEquipos;
    private Spinner<Integer> spCantidad;
    private Label lblDisponibilidad;
    private Label lblValidacionAnticipacion;

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
    private final String COLOR_INFO = "#7F8C8D";

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        VBox panelContenido = new VBox(12);
        panelContenido.setPadding(new Insets(20, 25, 20, 25));
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO_TABLA + "; " +
                "-fx-background-radius: 8; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);");

        Label lblTitulo = new Label("SOLICITUD DE EQUIPOS");
        lblTitulo.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 16px;");
        lblTitulo.setPadding(new Insets(0, 0, 10, 0));
        panelContenido.getChildren().add(lblTitulo);

        Label lblTipoEquipo = new Label("Tipo de equipo:");
        lblTipoEquipo.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        cbTipoEquipo = new ComboBox<>();
        cbTipoEquipo.setEditable(false);
        cbTipoEquipo.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        cbTipoEquipo.setPrefWidth(280);

        Label lblEquipos = new Label("Equipo disponible:");
        lblEquipos.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        cbEquipos = new ComboBox<>();
        cbEquipos.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        cbEquipos.setPrefWidth(280);

        lblDisponibilidad = new Label();
        lblDisponibilidad.setStyle("-fx-text-fill: " + COLOR_INFO + "; -fx-font-size: 12px; -fx-font-style: italic;");
        lblDisponibilidad.setPadding(new Insets(-5, 0, 5, 5));

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
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        spCantidad = new Spinner<>(1, 100, 1);
        spCantidad.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        spCantidad.setPrefWidth(280);

        Label lblNota = new Label("Nota adicional:");
        lblNota.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        tfNota = new TextField();
        tfNota.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        tfNota.setPrefWidth(280);

        Label lblFecha = new Label("Fecha de recibo:");
        lblFecha.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        dpFecha_recibo = new DatePicker(LocalDate.now());
        dpFecha_recibo.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        dpFecha_recibo.setPrefWidth(280);
        
        // Establecer fecha mínima como la fecha actual
        dpFecha_recibo.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

        // Agregar tooltip explicativo para fecha
        Tooltip tooltipFecha = new Tooltip(
            "Selecciona la fecha en que deseas recibir el equipo.\n" +
            "La fecha debe ser futura y permitir al menos 4 horas de anticipación."
        );
        tooltipFecha.setStyle("-fx-font-size: 11px;");
        dpFecha_recibo.setTooltip(tooltipFecha);

        Label lblHoraEntrega = new Label("Hora de entrega:");
        lblHoraEntrega.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        cbHoraEntrega = new ComboBox<>();
        cbHoraEntrega.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        cbHoraEntrega.setPrefWidth(280);
        cbHoraEntrega.setEditable(false);
        
        // Llenar el ComboBox con horas disponibles (de 7:00 AM a 8:00 PM)
        for (int hora = 7; hora <= 20; hora++) {
            String horaFormateada = String.format("%02d:00", hora);
            cbHoraEntrega.getItems().add(horaFormateada);
        }
        // Establecer hora por defecto (hora actual + 1)
        LocalTime horaActual = LocalTime.now();
        int horaPorDefecto = Math.min(horaActual.getHour() + 1, 20);
        cbHoraEntrega.setValue(String.format("%02d:00", horaPorDefecto));

        // Agregar tooltip explicativo
        Tooltip tooltipHora = new Tooltip(
            "Selecciona la hora en que deseas recibir el equipo.\n" +
            "Recuerda que solo se permiten solicitudes con al menos 4 horas de anticipación.\n" +
            "Horario disponible: 7:00 AM a 8:00 PM"
        );
        tooltipHora.setStyle("-fx-font-size: 11px;");
        cbHoraEntrega.setTooltip(tooltipHora);

        // Mensaje informativo sobre la regla de 4 horas
        Label lblInfoAnticipacion = new Label("ℹ️ Solo se admiten solicitudes con 4 horas de anticipación");
        lblInfoAnticipacion.setStyle("-fx-text-fill: " + COLOR_INFO + "; -fx-font-size: 11px; -fx-font-style: italic;");
        lblInfoAnticipacion.setPadding(new Insets(-5, 0, 5, 5));

        // Indicador de validación de anticipación
        lblValidacionAnticipacion = new Label();
        lblValidacionAnticipacion.setStyle("-fx-font-size: 11px; -fx-font-style: italic;");
        lblValidacionAnticipacion.setPadding(new Insets(-5, 0, 5, 5));

        Label lblTiempoUso = new Label("Tiempo de uso (horas):");
        lblTiempoUso.setStyle("-fx-text-fill: " + COLOR_TEXTO + "; -fx-font-weight: bold; -fx-font-size: 13px;");
        spTiempoUso = new Spinner<>(1, 24, 8);
        spTiempoUso.setStyle("-fx-font-size: 13px; -fx-text-fill: " + COLOR_TEXTO + "; " +
                "-fx-background-radius: 4; -fx-border-radius: 4;");
        spTiempoUso.setPrefWidth(280);

        btnEnviar = new Button("  Enviar Solicitud  ");
        btnEnviar.setStyle("-fx-background-color: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;");
        btnEnviar.setOnMouseEntered(e -> btnEnviar.setStyle("-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"));
        btnEnviar.setOnMouseExited(e -> btnEnviar.setStyle("-fx-background-color: " + COLOR_ACCION_PRINCIPAL + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 16; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand;"));

        new ControladorSolicitudes(this);

        HBox botones = new HBox(10, btnEnviar);
        botones.setAlignment(Pos.CENTER_RIGHT);
        botones.setPadding(new Insets(10, 0, 0, 0));

        panelContenido.getChildren().addAll(
                lblTipoEquipo, cbTipoEquipo,
                lblEquipos, cbEquipos,
                lblDisponibilidad,
                lblCantidad, spCantidad,
                lblNota, tfNota,
                lblFecha, dpFecha_recibo,
                lblHoraEntrega, cbHoraEntrega,
                lblInfoAnticipacion,
                lblValidacionAnticipacion,
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
            if (tipoSeleccionado != null) {
                lblDisponibilidad.setText("Mostrando equipos de tipo: " + tipoSeleccionado);
            } else {
                lblDisponibilidad.setText("");
            }
        });

        cbEquipos.setOnAction(e -> {
            Equipo equipoSeleccionado = cbEquipos.getValue();
            if (equipoSeleccionado != null) {
                int maxCantidad = 0;
                try {
                    maxCantidad = equipoDao.calcularCantidad(equipoSeleccionado.getDescripcion());
                    lblDisponibilidad.setText("Disponibles: " + maxCantidad + " unidades de " + equipoSeleccionado.getDescripcion());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCantidad, 1));
            }
        });

        // Validación en tiempo real de la anticipación
        dpFecha_recibo.setOnAction(e -> validarAnticipacionTiempoReal(lblValidacionAnticipacion));
        cbHoraEntrega.setOnAction(e -> validarAnticipacionTiempoReal(lblValidacionAnticipacion));
        
        // Validación inicial
        validarAnticipacionTiempoReal(lblValidacionAnticipacion);
    }

    /**
     * Valida en tiempo real si la fecha y hora seleccionadas cumplen con la regla de 4 horas
     */
    private void validarAnticipacionTiempoReal(Label lblValidacion) {
        LocalDate fechaRecibo = dpFecha_recibo.getValue();
        LocalTime horaEntrega = getHoraEntrega();
        
        if (fechaRecibo == null || horaEntrega == null) {
            lblValidacion.setText("");
            lblValidacion.setStyle("-fx-text-fill: " + COLOR_INFO + "; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }

        LocalDateTime fechaHoraEntrega = LocalDateTime.of(fechaRecibo, horaEntrega);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        
        if (fechaHoraEntrega.isBefore(fechaHoraActual)) {
            lblValidacion.setText("❌ Fecha y hora en el pasado");
            lblValidacion.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 11px; -fx-font-style: italic;");
            return;
        }
        
        long horasDiferencia = java.time.Duration.between(fechaHoraActual, fechaHoraEntrega).toHours();
        
        if (horasDiferencia < 4) {
            lblValidacion.setText("⚠️ Anticipación insuficiente: " + horasDiferencia + " horas (mínimo 4)");
            lblValidacion.setStyle("-fx-text-fill: #F39C12; -fx-font-size: 11px; -fx-font-style: italic;");
        } else {
            lblValidacion.setText("✅ Anticipación válida: " + horasDiferencia + " horas");
            lblValidacion.setStyle("-fx-text-fill: #27AE60; -fx-font-size: 11px; -fx-font-style: italic;");
        }
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

    public LocalTime getHoraEntrega() {
        String horaSeleccionada = cbHoraEntrega.getValue();
        if (horaSeleccionada != null && !horaSeleccionada.isEmpty()) {
            String[] partes = horaSeleccionada.split(":");
            int hora = Integer.parseInt(partes[0]);
            return LocalTime.of(hora, 0);
        }
        return LocalTime.of(8, 0); // Hora por defecto si no hay selección
    }

    public ComboBox<String> getCbHoraEntrega() {
        return cbHoraEntrega;
    }

    public void setCbHoraEntrega(ComboBox<String> cbHoraEntrega) {
        this.cbHoraEntrega = cbHoraEntrega;
    }

    public Label getLblValidacionAnticipacion() {
        return lblValidacionAnticipacion;
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