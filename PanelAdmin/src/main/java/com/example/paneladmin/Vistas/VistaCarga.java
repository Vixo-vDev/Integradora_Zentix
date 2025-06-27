package com.example.paneladmin.Vistas;

import javafx.scene.layout.StackPane;
import javafx.scene.control.ProgressIndicator;

public class VistaCarga {
    private StackPane vista;

    public VistaCarga() {
        vista = new StackPane();
        vista.getStyleClass().add("loading-container");

        ProgressIndicator progress = new ProgressIndicator();
        progress.getStyleClass().add("progress-large");

        vista.getChildren().add(progress);
    }

    public StackPane getVista() {
        return vista;
    }
}