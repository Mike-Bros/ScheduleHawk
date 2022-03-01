package com.mikebros.schedulehawk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Objects;

public class ScheduleHawkApplication extends Application {
    private static Stage myStage;

    public ScheduleHawkApplication() {
    }

    public void start(Stage stage) throws Exception {
        String query = "SELECT * FROM users";
        DBConnection.makeConnection();
        DBConnection.closeConnection();
        ResultSet results = DBConnection.query(query);
        while(results.next()){
            System.out.println("User " + results.getInt("User_ID") + "...");
            System.out.println("Name: " + results.getString("User_Name"));
            System.out.println("Password: " + results.getString("Password"));
        }

        myStage = stage;
        Parent root = (Parent) FXMLLoader.load(Objects.requireNonNull(ScheduleHawkApplication.class.getResource("login1.0.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Hawk");
        stage.setScene(scene);
        stage.show();
        System.out.println();
    }

    public static void changeScene(String newScene) throws IOException {
        Parent newRoot = (Parent) FXMLLoader.load(Objects.requireNonNull(ScheduleHawkApplication.class.getResource(newScene + ".fxl")));
        myStage.getScene().setRoot(newRoot);
    }

    public static void main(String[] args) {
        launch();
    }
}