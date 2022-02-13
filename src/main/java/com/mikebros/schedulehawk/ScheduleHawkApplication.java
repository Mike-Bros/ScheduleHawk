package com.mikebros.schedulehawk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ScheduleHawkApplication extends Application {
    private static Stage myStage;

    public ScheduleHawkApplication() {
    }

    public void start(Stage stage) throws IOException {
        myStage = stage;
        Parent root = (Parent) FXMLLoader.load((URL) Objects.requireNonNull(HelloApplication.class.getResource("login1.0.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Hawk");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String newScene) throws IOException {
        Parent newRoot = (Parent) FXMLLoader.load((URL) Objects.requireNonNull(HelloApplication.class.getResource(newScene)));
        myStage.getScene().setRoot(newRoot);
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}