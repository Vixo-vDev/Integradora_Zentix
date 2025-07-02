package com.example.panelnormal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage escenario) {
        VistaPrincipal vistaPrincipal = new VistaPrincipal();
        vistaPrincipal.mostrar(escenario);
    }

    public static void main(String[] args) {
        launch(args);
    }
}