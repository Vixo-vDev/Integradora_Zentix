package com.example.paneladmin;

import com.example.paneladmin.Vistas.VistaPrincipal;
import javafx.application.Application;
import javafx.stage.Stage;


public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        new ControladorPrincipal(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}