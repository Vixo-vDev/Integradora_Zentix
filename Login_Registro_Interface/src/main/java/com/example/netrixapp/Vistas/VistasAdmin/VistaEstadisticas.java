package com.example.netrixapp.Vistas.VistasAdmin;

import com.example.netrixapp.Controladores.ControladorAdmin.ControladorBarraNavegacion;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorEstadisticas;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;

public class VistaEstadisticas {
    private final BorderPane vista;
    private final ControladorBarraNavegacion controladorBarra;
    private final VBox contenedorMasSolicitados;
    private final VBox contenedorGrafico;
    private final TableView<Object[]> tablaMasSolicitados;
    private final ControladorEstadisticas controlador;

    public VistaEstadisticas(ControladorBarraNavegacion controladorBarra) {
        this.controladorBarra = controladorBarra;
        this.vista = new BorderPane();
        this.tablaMasSolicitados = new TableView<>();
        this.contenedorMasSolicitados = new VBox(10);
        this.contenedorGrafico = new VBox(10);
        this.controlador = new ControladorEstadisticas(this);
        inicializarUI();
    }

    private void inicializarUI() {
        vista.setTop(controladorBarra.getBarraSuperior());
        vista.setLeft(controladorBarra.getBarraLateral());
        vista.setStyle("-fx-background-color: #ECF0F1;");
        contenedorGrafico.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-padding: 15;");

        VBox contenidoPrincipal = new VBox(20);
        contenidoPrincipal.setPadding(new Insets(20));

        // Título principal
        Label lblTitulo = new Label("Estadísticas de Solicitudes");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // Filtro de periodo
        HBox filtroBox = new HBox(10);
        Label lblFiltro = new Label("Periodo:");
        lblFiltro.setStyle("-fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        ComboBox<String> comboPeriodo = new ComboBox<>();
        comboPeriodo.getItems().addAll("Semana", "Mes");
        comboPeriodo.setValue("Semana");

        Button btnAplicar = new Button("Aplicar filtros");
        btnAplicar.setOnAction(e -> {
            String seleccion = comboPeriodo.getValue().toLowerCase();
            System.out.println("Aplicando filtro: " + seleccion);
            controlador.cargarDatosEstadisticos(seleccion);
        });

        Button btnQuitarFiltros = new Button("Quitar filtros");
        btnQuitarFiltros.setOnAction(e -> {
            System.out.println("Quitando filtros - cargando datos sin filtro");
            controlador.cargarMasSolicitadosSinFiltro();
        });

        filtroBox.getChildren().addAll(lblFiltro, comboPeriodo, btnAplicar, btnQuitarFiltros);
        filtroBox.setPadding(new Insets(10, 0, 10, 0));

        // Configurar tabla
        configurarTabla();

        // Etiqueta para tabla más solicitados
        Label lblMas = new Label("Equipos Más Solicitados");
        lblMas.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        contenedorMasSolicitados.getChildren().addAll(lblMas, tablaMasSolicitados);
        contenedorMasSolicitados.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #BDC3C7; -fx-border-radius: 8;");
        contenedorMasSolicitados.setPadding(new Insets(15));

        // Inicializar contenedor del gráfico
        contenedorGrafico.getChildren().add(new Label("Cargando datos..."));

        // Separador
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        contenidoPrincipal.getChildren().addAll(lblTitulo, filtroBox, contenedorMasSolicitados, separator, contenedorGrafico);

        ScrollPane scrollPane = new ScrollPane(contenidoPrincipal);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        vista.setCenter(scrollPane);
    }

    private void configurarTabla() {
        tablaMasSolicitados.getStylesheets().add(
                getClass().getResource("/css/tabla.css").toExternalForm()
        );

        tablaMasSolicitados.getColumns().clear();
        tablaMasSolicitados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaMasSolicitados.setStyle("-fx-font-size: 14px;");

        // Crear columnas manualmente
        TableColumn<Object[], String> colEquipo = new TableColumn<>("Equipo");
        colEquipo.setCellValueFactory(data -> {
            Object val = data.getValue()[0];
            return new javafx.beans.property.SimpleStringProperty(val == null ? "" : val.toString());
        });

        TableColumn<Object[], String> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(data -> {
            Object val = data.getValue()[1];
            return new javafx.beans.property.SimpleStringProperty(val == null ? "" : val.toString());
        });

        tablaMasSolicitados.getColumns().addAll(colEquipo, colCantidad);

        tablaMasSolicitados.setRowFactory(tv -> new TableRow<Object[]>() {
            @Override
            protected void updateItem(Object[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + (getIndex() % 2 == 0 ? "#F8F9F9" : "white") + ";");
                }
            }
        });

        tablaMasSolicitados.setPlaceholder(new Label("No hay datos disponibles."));
    }

    private VBox crearGraficoBarrasManual(List<ControladorEstadisticas.EquipoChartData> datos) {
        System.out.println("=== CREANDO GRÁFICO DE BARRAS VERTICALES MANUAL ===");
        System.out.println("Datos recibidos: " + datos.size() + " elementos");
        
        if (datos.isEmpty()) {
            System.out.println("No hay datos para crear gráfico");
            return null;
        }

        // Contenedor principal del gráfico
        VBox contenedorGrafico = new VBox(15);
        contenedorGrafico.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");

        // Título del gráfico
        Label tituloGrafico = new Label("Distribución de Solicitudes por Equipo");
        tituloGrafico.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50; -fx-alignment: CENTER;");
        tituloGrafico.setAlignment(Pos.CENTER);

        // Contenedor para las barras verticales
        HBox contenedorBarras = new HBox(15);
        contenedorBarras.setAlignment(Pos.BOTTOM_CENTER);

        // Encontrar el valor máximo para calcular proporciones
        int maxCantidad = datos.stream().mapToInt(ControladorEstadisticas.EquipoChartData::getCantidad).max().orElse(1);
        System.out.println("Cantidad máxima: " + maxCantidad);

        // Colores para las barras
        String[] colores = {
            "#009475", "#4F46E5", "#10B981", "#F59E0B", "#EC4899",
            "#06B6D4", "#8B5CF6", "#F97316", "#84CC16", "#EF4444"
        };

        // Crear cada barra vertical manualmente
        for (int i = 0; i < datos.size(); i++) {
            ControladorEstadisticas.EquipoChartData item = datos.get(i);
            
            // Contenedor de la barra individual
            VBox barraIndividual = new VBox(8);
            barraIndividual.setAlignment(Pos.BOTTOM_CENTER);

            // Calcular altura proporcional de la barra (máximo 200px)
            double porcentaje = (double) item.getCantidad() / maxCantidad;
            int alturaBarra = (int) (200 * porcentaje);
            
            // Crear la barra visual vertical
            VBox barra = new VBox();
            barra.setStyle("-fx-background-color: " + colores[i % colores.length] + "; -fx-background-radius: 4; -fx-min-width: 40px; -fx-max-width: 40px; -fx-min-height: " + alturaBarra + "px; -fx-max-height: " + alturaBarra + "px;");
            barra.setAlignment(Pos.TOP_CENTER);

            // Etiqueta de cantidad sobre la barra
            Label labelCantidad = new Label(String.valueOf(item.getCantidad()));
            labelCantidad.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 3; -fx-padding: 2 4;");

            // Etiqueta de porcentaje debajo de la barra
            Label labelPorcentaje = new Label(String.format("%.1f%%", item.getPorcentaje()));
            labelPorcentaje.setStyle("-fx-font-size: 11px; -fx-text-fill: #7F8C8D; -fx-font-weight: bold; -fx-alignment: CENTER;");
            labelPorcentaje.setAlignment(Pos.CENTER);

            // Nombre del equipo (abreviado si es muy largo)
            String nombreCompleto = item.getNombreEquipo();
            String nombreMostrado = nombreCompleto.length() > 12 ? nombreCompleto.substring(0, 10) + "..." : nombreCompleto;
            
            Label labelNombre = new Label(nombreMostrado);
            labelNombre.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #2C3E50; -fx-alignment: CENTER; -fx-wrap-text: true;");
            labelNombre.setAlignment(Pos.CENTER);
            labelNombre.setMaxWidth(60);
            labelNombre.setWrapText(true);

            // Agregar elementos a la barra
            barra.getChildren().add(labelCantidad);

            // Agregar elementos a la barra individual (de abajo hacia arriba)
            barraIndividual.getChildren().addAll(labelNombre, barra, labelPorcentaje);

            // Crear tooltip para información completa
            Tooltip tooltip = new Tooltip("Equipo: " + nombreCompleto + 
                                        "\nCantidad: " + item.getCantidad() + 
                                        "\nPorcentaje: " + String.format("%.1f", item.getPorcentaje()) + "%");
            tooltip.setShowDelay(Duration.ZERO);
            tooltip.setStyle("-fx-font-size: 12px; -fx-background-color: #2C3E50; -fx-text-fill: white; -fx-border-color: #E5E7EB;");
            
            // Instalar tooltip solo en el nombre y en la barra visible (no en el espacio vacío)
            Tooltip.install(labelNombre, tooltip);
            Tooltip.install(barra, tooltip);

            // Agregar la barra individual al contenedor horizontal
            contenedorBarras.getChildren().add(barraIndividual);

            System.out.println("  Barra vertical " + (i + 1) + " creada: " + nombreCompleto + 
                             " - Cantidad: " + item.getCantidad() + 
                             " - Porcentaje: " + String.format("%.1f", item.getPorcentaje()) + "%" +
                             " - Altura: " + alturaBarra + "px");
        }

        // Agregar elementos al contenedor principal
        contenedorGrafico.getChildren().addAll(tituloGrafico, contenedorBarras);

        System.out.println("Gráfico vertical manual creado exitosamente con " + datos.size() + " barras");
        return contenedorGrafico;
    }

    public void mostrarGraficoBarras(List<ControladorEstadisticas.EquipoChartData> datos) {
        System.out.println("=== ACTUALIZANDO GRÁFICO VERTICAL MANUAL ===");
        System.out.println("Limpiando contenedor del gráfico...");
        
        contenedorGrafico.getChildren().clear();
        
        if (datos == null) {
            System.out.println("ERROR: Datos nulos recibidos");
            mostrarError("Error: Se recibieron datos nulos del controlador");
            return;
        }
        
        if (datos.isEmpty()) {
            System.out.println("No hay datos para mostrar en el gráfico");
            mostrarMensajeSinDatos();
            return;
        }
        
        System.out.println("Creando nuevo gráfico vertical manual con " + datos.size() + " elementos...");
        
        VBox graficoManual = crearGraficoBarrasManual(datos);
        
        if (graficoManual != null) {
            contenedorGrafico.getChildren().add(graficoManual);
            System.out.println("Gráfico vertical manual actualizado exitosamente");
        } else {
            System.out.println("ERROR: No se pudo crear el gráfico vertical manual");
            mostrarError("Error al crear el gráfico");
        }
    }

    public void mostrarEquiposMasSolicitados(List<Object[]> datos) {
        System.out.println("Actualizando tabla de equipos más solicitados con " + (datos != null ? datos.size() : 0) + " elementos");
        tablaMasSolicitados.getItems().clear();
        if (datos != null && !datos.isEmpty()) {
            tablaMasSolicitados.getItems().addAll(datos);
        }
    }
    
    public void mostrarMensajeSinDatos() {
        System.out.println("Mostrando mensaje de datos vacíos");
        contenedorGrafico.getChildren().clear();
        
        Label titulo = new Label("Gráfico de Barras - Top 10 Equipos Más Solicitados");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        
        Label mensaje = new Label("No hay datos de solicitudes disponibles para generar el gráfico.\n" +
                                 "El gráfico se mostrará cuando existan solicitudes en la base de datos.");
        mensaje.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D; -fx-alignment: CENTER;");
        mensaje.setAlignment(Pos.CENTER);
        mensaje.setWrapText(true);
        
        VBox mensajeBox = new VBox(10, titulo, mensaje);
        mensajeBox.setAlignment(Pos.CENTER);
        mensajeBox.setPadding(new Insets(40));
        
        contenedorGrafico.getChildren().add(mensajeBox);
    }
    
    public void mostrarError(String mensaje) {
        System.out.println("Mostrando mensaje de error: " + mensaje);
        contenedorGrafico.getChildren().clear();
        
        Label titulo = new Label("Error en el Gráfico");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #E74C3C;");
        
        Label errorMsg = new Label(mensaje);
        errorMsg.setStyle("-fx-font-size: 14px; -fx-text-fill: #E74C3C; -fx-alignment: CENTER;");
        errorMsg.setAlignment(Pos.CENTER);
        errorMsg.setWrapText(true);
        
        VBox errorBox = new VBox(10, titulo, errorMsg);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(40));
        
        contenedorGrafico.getChildren().add(errorBox);
    }
    
    public void limpiarGrafico() {
        System.out.println("Limpiando gráfico actual...");
        contenedorGrafico.getChildren().clear();
        
        Label mensajeCarga = new Label("Cargando nuevo gráfico...");
        mensajeCarga.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D; -fx-alignment: CENTER;");
        mensajeCarga.setAlignment(Pos.CENTER);
        
        contenedorGrafico.getChildren().add(mensajeCarga);
    }

    public BorderPane getVista() {
        return vista;
    }
}
