package com.mikebros.schedulehawk;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Objects;

public class ScheduleHawkApplication extends Application {

    public ScheduleHawkApplication() {
    }

    public void start(Stage stage) throws Exception {
        String query = "SELECT * FROM users";
        DBConnection.makeConnection();
        DBConnection.closeConnection();
        ResultSet results = DBConnection.query(query);
        while (results.next()) {
            System.out.println("User " + results.getInt("User_ID") + "...");
            System.out.println("Name: " + results.getString("User_Name"));
            System.out.println("Password: " + results.getString("Password"));
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(ScheduleHawkApplication.class.getResource("login-view.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Hawk");
        stage.setScene(scene);
        stage.show();
    }

    public static void changeScene(ActionEvent event, String newSceneName) {
        try {
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(ScheduleHawkApplication.class.getResource(newSceneName + ".fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(newRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("......................................................................................");
            System.out.println("Error occurred while trying to change scene");
            System.out.println("Error: " + e);
            System.out.println("Message: " + e.getMessage());
            System.out.println("Class: " + e.getClass());
            System.out.println("\n Stack Trace: " + Arrays.toString(e.getStackTrace()));
            System.out.println("......................................................................................");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}