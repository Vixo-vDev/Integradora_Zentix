package com.example.paneladmin.Controladores;

import com.example.paneladmin.Vistas.VistaEstadisticas;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class ControladorEstadisticas {
    private VistaEstadisticas vista;

    public ControladorEstadisticas(VistaEstadisticas vista) {
        this.vista = vista;
    }

    public void inicializarUI() {
        // Título
        Label etiquetaTitulo = new Label("Estadísticas");
        etiquetaTitulo.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Contenedor principal
        StackPane contenedorGrafico = new StackPane();
        contenedorGrafico.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-color: #bdc3c7;");

        // Mensaje cuando no hay datos (siempre visible inicialmente)
        Label mensajeSinDatos = new Label("No hay datos estadísticos disponibles");
        mensajeSinDatos.setStyle("-fx-font-size: 16; -fx-text-fill: #7f8c8d;");

        // Gráfico vacío
        PieChart grafico = new PieChart();
        grafico.setTitle("Distribución de datos");
        grafico.setLegendVisible(false); // Ocultar leyenda cuando no hay datos

        contenedorGrafico.getChildren().addAll(grafico, mensajeSinDatos);

        // Botones (deshabilitados inicialmente)
        HBox contenedorBotones = new HBox(15);
        contenedorBotones.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnGenerar = crearBotonDeshabilitado("Generar Reporte");
        Button btnExportar = crearBotonDeshabilitado("Exportar");
        Button btnImprimir = crearBotonDeshabilitado("Imprimir");

        contenedorBotones.getChildren().addAll(btnGenerar, btnExportar, btnImprimir);

        // Configurar la vista completa
        VBox.setVgrow(contenedorGrafico, Priority.ALWAYS);
        vista.getVista().getChildren().addAll(etiquetaTitulo, contenedorGrafico, contenedorBotones);
    }

    private Button crearBotonDeshabilitado(String texto) {
        Button boton = new Button(texto);
        boton.setStyle("-fx-background-color: #bdc3c7; " +
                "-fx-text-fill: #7f8c8d; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8 15; " +
                "-fx-background-radius: 5;");
        boton.setDisable(true);
        return boton;
    }

    // Método para cargar datos cuando estén disponibles
    public void cargarDatos(PieChart.Data... datos) {
        PieChart grafico = (PieChart) ((StackPane) vista.getVista().getChildren().get(1)).getChildren().get(0);
        grafico.getData().clear();
        grafico.getData().addAll(datos);
        grafico.setLegendVisible(true);

        // Habilitar botones cuando hay datos
        HBox botones = (HBox) vista.getVista().getChildren().get(2);
        botones.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setDisable(false);
                node.setStyle("-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 15; " +
                        "-fx-background-radius: 5;");
            }
        });
    }
}