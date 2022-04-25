package com.mikebros.schedulehawk;

import com.mikebros.schedulehawk.models.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;

/**
 * Schedule Hawk application main class.
 */
public class ScheduleHawkApplication extends Application {
    public static ActionEvent lastSceneChangeEvent;
    public static User activeUser;

    /**
     * Instantiates a new Schedule Hawk application.
     */
    public ScheduleHawkApplication() {
    }

    /**
     * Implementation of application start method .
     *
     * @param stage        the initial stage of the application
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScheduleHawkApplication.class.getResource("/com/mikebros/schedulehawk/views/login-view.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Schedule Hawk");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Change the scene to a specified new one.
     *
     * @param event        the FX action event that calls this method
     * @param newSceneName the name of the fxml file this method will change to without the .fxml added
     */
    public static void changeScene(ActionEvent event, String newSceneName) {
        try {
            lastSceneChangeEvent = event;
//            if (Objects.equals(((Node) lastSceneChangeEvent.getSource()).getId(), "loginButton")){
//
//            }
            FXMLLoader loader = new FXMLLoader(ScheduleHawkApplication.class.getResource("/com/mikebros/schedulehawk/views/" + newSceneName + ".fxml"));
            Parent newRoot = loader.load();
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
            System.out.println("\n Stack Trace: ");
            e.printStackTrace();
            System.out.println("......................................................................................");
        }
    }

    /**
     * API for controllers to sets active user.
     *
     * @param activeUser    the active user
     */
    public static void setActiveUser(User activeUser) {
        ScheduleHawkApplication.activeUser = activeUser;
    }

    /**
     * Gets active user.
     *
     * @return User         the active user
     */
    public static User getActiveUser() {
        return activeUser;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }
}