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
import javafx.scene.text.TextAlignment;

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

    // Paleta de colores moderna y consistente
    private final String COLOR_PRIMARIO = "#4F46E5";
    private final String COLOR_SECUNDARIO = "#10B981";
    private final String COLOR_ACCION_PRINCIPAL = "#2980B9";
    private final String COLOR_AGREGAR = "#009475";
    private final String COLOR_PELIGRO = "#EF4444";
    private final String COLOR_ADVERTENCIA = "#F59E0B";
    private final String COLOR_FONDO = "#F8FAFC";
    private final String COLOR_FONDO_CARD = "#FFFFFF";
    private final String COLOR_TEXTO_OSCURO = "#1F2937";
    private final String COLOR_TEXTO_NORMAL = "#6B7280";
    private final String COLOR_BORDE = "#E5E7EB";
    private final String COLOR_SOMBRA = "rgba(0, 0, 0, 0.08)";

    public VistaSolicitudes(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setStyle("-fx-background-color: " + COLOR_FONDO + ";");

        // Crear ScrollPane para permitir scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox panelContenido = new VBox(30); // Aumentar espaciado entre secciones
        panelContenido.setPadding(new Insets(30));
        panelContenido.setStyle("-fx-background-color: " + COLOR_FONDO_CARD + "; " +
                "-fx-background-radius: 16; " +
                "-fx-effect: dropshadow(three-pass-box, " + COLOR_SOMBRA + ", 10, 0, 0, 3);");

        // Header con título y descripción
        VBox headerSection = crearHeaderSection();
        panelContenido.getChildren().add(headerSection);

        // Formulario organizado en secciones
        VBox formulario = crearFormularioOrganizado();
        panelContenido.getChildren().add(formulario);

        // Panel de botones de acción
        HBox panelBotones = crearPanelBotonesAccion();
        panelContenido.getChildren().add(panelBotones);

        // Agregar el contenido al ScrollPane
        scrollPane.setContent(panelContenido);

        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setCenter(scrollPane);

        cargarTiposEquipo();

        // Configurar eventos
        configurarEventos();
        
        // Validación inicial
        validarAnticipacionTiempoReal(lblValidacionAnticipacion);
    }

    private VBox crearHeaderSection() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        Label lblTitulo = new Label("📋 SOLICITUD DE EQUIPOS");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 28));
        lblTitulo.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        Label lblSubtitulo = new Label("Solicita equipos para tus actividades académicas");
        lblSubtitulo.setFont(Font.font("System", 16));
        lblSubtitulo.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + ";");

        // Información importante sobre reglas
        VBox infoReglas = new VBox(8);
        infoReglas.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-background-radius: 8; " +
                "-fx-padding: 15; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");

        Label lblReglaAnticipacion = new Label("⏰ Solo se admiten solicitudes con 4 horas de anticipación");
        lblReglaAnticipacion.setStyle("-fx-text-fill: " + COLOR_ADVERTENCIA + "; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label lblReglaHorario = new Label("🕐 Horario disponible: 7:00 AM a 8:00 PM");
        lblReglaHorario.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px;");

        infoReglas.getChildren().addAll(lblReglaAnticipacion, lblReglaHorario);

        header.getChildren().addAll(lblTitulo, lblSubtitulo, infoReglas);
        return header;
    }

    private VBox crearFormularioOrganizado() {
        VBox formulario = new VBox(30); // Aumentar espaciado entre secciones
        formulario.setPadding(new Insets(25));
        formulario.setStyle("-fx-background-color: " + COLOR_FONDO + "; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 12;");

        // Sección 1: Selección de Equipo
        VBox seccionEquipo = crearSeccionEquipo();
        formulario.getChildren().add(seccionEquipo);

        // Sección 2: Fecha y Hora (mover arriba)
        VBox seccionFechaHora = crearSeccionFechaHora();
        formulario.getChildren().add(seccionFechaHora);

        // Sección 3: Detalles de la Solicitud (mover abajo)
        VBox seccionDetalles = crearSeccionDetalles();
        formulario.getChildren().add(seccionDetalles);

        return formulario;
    }

    private VBox crearSeccionEquipo() {
        VBox seccion = new VBox(20); // Aumentar espaciado
        seccion.setPadding(new Insets(25));
        seccion.setStyle("-fx-background-color: " + COLOR_FONDO_CARD + "; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");

        Label lblTituloSeccion = new Label("🎯 SELECCIÓN DE EQUIPO");
        lblTituloSeccion.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTituloSeccion.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Tipo de equipo
        VBox filtroTipoEquipo = new VBox(10);
        Label lblTipoEquipo = new Label("Tipo de equipo:");
        lblTipoEquipo.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        cbTipoEquipo = new ComboBox<>();
        cbTipoEquipo.setEditable(false);
        cbTipoEquipo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
        filtroTipoEquipo.getChildren().addAll(lblTipoEquipo, cbTipoEquipo);

        // Equipo disponible
        VBox filtroEquipos = new VBox(10);
        Label lblEquipos = new Label("Equipo disponible:");
        lblEquipos.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        cbEquipos = new ComboBox<>();
        cbEquipos.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");

        lblDisponibilidad = new Label();
        lblDisponibilidad.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px; -fx-font-style: italic;");
        lblDisponibilidad.setPadding(new Insets(-5, 0, 5, 5));

        filtroEquipos.getChildren().addAll(lblEquipos, cbEquipos, lblDisponibilidad);

        // Cantidad
        VBox filtroCantidad = new VBox(10);
        Label lblCantidad = new Label("Cantidad:");
        lblCantidad.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        spCantidad = new Spinner<>(1, 100, 1);
        spCantidad.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
        filtroCantidad.getChildren().addAll(lblCantidad, spCantidad);

        seccion.getChildren().addAll(lblTituloSeccion, filtroTipoEquipo, filtroEquipos, filtroCantidad);
        return seccion;
    }

    private VBox crearSeccionDetalles() {
        VBox seccion = new VBox(20); // Aumentar espaciado
        seccion.setPadding(new Insets(25));
        seccion.setStyle("-fx-background-color: " + COLOR_FONDO_CARD + "; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");

        Label lblTituloSeccion = new Label("📝 DETALLES DE LA SOLICITUD");
        lblTituloSeccion.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTituloSeccion.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Nota adicional
        VBox filtroNota = new VBox(10);
        Label lblNota = new Label("Nota adicional:");
        lblNota.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        tfNota = new TextField();
        tfNota.setPromptText("Describe brevemente el motivo de tu solicitud...");
        tfNota.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
        filtroNota.getChildren().addAll(lblNota, tfNota);

        // Tiempo de uso
        VBox filtroTiempo = new VBox(10);
        Label lblTiempoUso = new Label("Tiempo de uso (horas):");
        lblTiempoUso.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        spTiempoUso = new Spinner<>(1, 24, 8);
        spTiempoUso.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
        filtroTiempo.getChildren().addAll(lblTiempoUso, spTiempoUso);

        seccion.getChildren().addAll(lblTituloSeccion, filtroNota, filtroTiempo);
        return seccion;
    }

    private VBox crearSeccionFechaHora() {
        VBox seccion = new VBox(20); // Aumentar espaciado
        seccion.setPadding(new Insets(25));
        seccion.setStyle("-fx-background-color: " + COLOR_FONDO_CARD + "; " +
                "-fx-background-radius: 8; " +
                "-fx-border-color: " + COLOR_BORDE + "; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 8;");

        Label lblTituloSeccion = new Label("📅 FECHA Y HORA DE RECIBO");
        lblTituloSeccion.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTituloSeccion.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + ";");

        // Fecha de recibo
        VBox filtroFecha = new VBox(10);
        Label lblFecha = new Label("Fecha de recibo:");
        lblFecha.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        dpFecha_recibo = new DatePicker(LocalDate.now());
        dpFecha_recibo.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
        
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

        filtroFecha.getChildren().addAll(lblFecha, dpFecha_recibo);

        // Hora de entrega
        VBox filtroHora = new VBox(10);
        Label lblHoraEntrega = new Label("Hora de entrega:");
        lblHoraEntrega.setStyle("-fx-text-fill: " + COLOR_TEXTO_OSCURO + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        cbHoraEntrega = new ComboBox<>();
        cbHoraEntrega.setStyle("-fx-font-size: 14px; -fx-text-fill: " + COLOR_TEXTO_OSCURO + "; " +
                "-fx-background-radius: 6; -fx-border-radius: 6; -fx-pref-width: 350px; -fx-min-height: 40px;");
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

        filtroHora.getChildren().addAll(lblHoraEntrega, cbHoraEntrega);

        // Indicador de validación de anticipación
        lblValidacionAnticipacion = new Label();
        lblValidacionAnticipacion.setStyle("-fx-font-size: 12px; -fx-font-style: italic; -fx-padding: 8;");
        lblValidacionAnticipacion.setStyle(lblValidacionAnticipacion.getStyle() + "-fx-background-color: " + COLOR_FONDO + "; -fx-background-radius: 6;");

        seccion.getChildren().addAll(lblTituloSeccion, filtroFecha, filtroHora, lblValidacionAnticipacion);
        return seccion;
    }

    private HBox crearPanelBotonesAccion() {
        HBox panelBotones = new HBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(20, 0, 0, 0));

        btnEnviar = crearBotonEstilizado("🚀 Enviar Solicitud", COLOR_SECUNDARIO);
        btnEnviar.setStyle(btnEnviar.getStyle() + "-fx-font-size: 16px; -fx-padding: 15 30;");

        panelBotones.getChildren().add(btnEnviar);
        return panelBotones;
    }

    private Button crearBotonEstilizado(String texto, String colorFondo) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: " + colorFondo + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 12 24; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);");
        
        boton.setOnMouseEntered(e -> boton.setStyle(boton.getStyle().replace(colorFondo, 
                colorFondo.equals(COLOR_SECUNDARIO) ? "#059669" : "#3498db")));
        boton.setOnMouseExited(e -> boton.setStyle(boton.getStyle().replace(
                colorFondo.equals(COLOR_SECUNDARIO) ? "#059669" : "#3498db", colorFondo)));
        
        return boton;
    }

    private void configurarEventos() {
        // Configurar ComboBox de equipos
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

        // Eventos de cambio
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
                    lblDisponibilidad.setText("✅ Disponibles: " + maxCantidad + " unidades de " + equipoSeleccionado.getDescripcion());
                    lblDisponibilidad.setStyle("-fx-text-fill: " + COLOR_SECUNDARIO + "; -fx-font-size: 12px; -fx-font-weight: bold;");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, maxCantidad, 1));
            }
        });

        // Validación en tiempo real de la anticipación
        dpFecha_recibo.setOnAction(e -> validarAnticipacionTiempoReal(lblValidacionAnticipacion));
        cbHoraEntrega.setOnAction(e -> validarAnticipacionTiempoReal(lblValidacionAnticipacion));

        // Crear controlador
        new ControladorSolicitudes(this);
    }

    /**
     * Valida en tiempo real si la fecha y hora seleccionadas cumplen con la regla de 4 horas
     */
    private void validarAnticipacionTiempoReal(Label lblValidacion) {
        LocalDate fechaRecibo = dpFecha_recibo.getValue();
        LocalTime horaEntrega = getHoraEntrega();
        
        if (fechaRecibo == null || horaEntrega == null) {
            lblValidacion.setText("");
            lblValidacion.setStyle("-fx-text-fill: " + COLOR_TEXTO_NORMAL + "; -fx-font-size: 12px; -fx-font-style: italic; -fx-padding: 8; -fx-background-color: " + COLOR_FONDO + "; -fx-background-radius: 6;");
            return;
        }

        LocalDateTime fechaHoraEntrega = LocalDateTime.of(fechaRecibo, horaEntrega);
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        
        if (fechaHoraEntrega.isBefore(fechaHoraActual)) {
            lblValidacion.setText("❌ Fecha y hora en el pasado");
            lblValidacion.setStyle("-fx-text-fill: " + COLOR_PELIGRO + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8; -fx-background-color: #FEE2E2; -fx-background-radius: 6;");
            return;
        }
        
        long horasDiferencia = java.time.Duration.between(fechaHoraActual, fechaHoraEntrega).toHours();
        
        if (horasDiferencia < 4) {
            lblValidacion.setText("⚠️ Anticipación insuficiente: " + horasDiferencia + " horas (mínimo 4)");
            lblValidacion.setStyle("-fx-text-fill: " + COLOR_ADVERTENCIA + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8; -fx-background-color: #FEF3C7; -fx-background-radius: 6;");
        } else {
            lblValidacion.setText("✅ Anticipación válida: " + horasDiferencia + " horas");
            lblValidacion.setStyle("-fx-text-fill: " + COLOR_SECUNDARIO + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8; -fx-background-color: #D1FAE5; -fx-background-radius: 6;");
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

    // Getters y setters
    public int getIdTipoEquipoSeleccionado() {
        String tipoSeleccionado = cbTipoEquipo.getValue();
        if (tipoSeleccionado != null && tipoNombreToId.containsKey(tipoSeleccionado)) {
            return tipoNombreToId.get(tipoSeleccionado);
        }
        return -1;
    }

    public ComboBox<String> getCbTipoEquipo() { return cbTipoEquipo; }
    public Equipo getEquipoSeleccionado() { return cbEquipos.getValue(); }
    public String getNota() { return tfNota.getText().trim(); }
    public LocalDate getFechaRecibo() { return dpFecha_recibo.getValue(); }
    public LocalTime getHoraEntrega() {
        String horaSeleccionada = cbHoraEntrega.getValue();
        if (horaSeleccionada != null && !horaSeleccionada.isEmpty()) {
            String[] partes = horaSeleccionada.split(":");
            int hora = Integer.parseInt(partes[0]);
            return LocalTime.of(hora, 0);
        }
        return LocalTime.of(8, 0);
    }
    public ComboBox<String> getCbHoraEntrega() { return cbHoraEntrega; }
    public void setCbHoraEntrega(ComboBox<String> cbHoraEntrega) { this.cbHoraEntrega = cbHoraEntrega; }
    public Label getLblValidacionAnticipacion() { return lblValidacionAnticipacion; }
    public int getTiempoUso() { return spTiempoUso.getValue(); }
    public int getCantidad() { return spCantidad.getValue(); }
    public Button getBtnEnviar() { return btnEnviar; }
    public BorderPane getVista() { return vista; }
    public void setTfNota(TextField tfNota) { this.tfNota = tfNota; }
    public void setDpFecha_recibo(DatePicker dpFecha_recibo) { this.dpFecha_recibo = dpFecha_recibo; }
    public void setSpTiempoUso(Spinner<Integer> spTiempoUso) { this.spTiempoUso = spTiempoUso; }
    public void setCbTipoEquipo(ComboBox<String> cbTipoEquipo) { this.cbTipoEquipo = cbTipoEquipo; }
    public void setCbEquipos(ComboBox<Equipo> cbEquipos) { this.cbEquipos = cbEquipos; }
    public void setSpCantidad(Spinner<Integer> spCantidad) { this.spCantidad = spCantidad; }
    public TextField getTfNota() { return tfNota; }
    public DatePicker getDpFecha_recibo() { return dpFecha_recibo; }
    public Spinner<Integer> getSpTiempoUso() { return spTiempoUso; }
    public Spinner<Integer> getSpCantidad() { return spCantidad; }
    public ComboBox<Equipo> getCbEquipos() { return cbEquipos; }
}