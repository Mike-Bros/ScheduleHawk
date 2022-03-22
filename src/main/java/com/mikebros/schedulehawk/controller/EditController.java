package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class EditController{

    private String userData;

    @FXML
    private TextField appointment_id;
    @FXML
    private Button back_button;

    public void initialize(){
        System.out.println("......................................................................................");
        System.out.println("Initializing Edit View");

        getUserData();
        setFormFields();

        System.out.println("Finished initializing Edit View");
        System.out.println("......................................................................................\n");
    }

    private void setFormFields() {
        if (Objects.equals(userData, "new")){
            appointment_id.setText("new appt");
        }else{
            appointment_id.setText(userData);
        }
    }

    private void getUserData() {
        ActionEvent loadEvent = ScheduleHawkApplication.lastSceneChangeEvent;
        Node eventNode = (Node) loadEvent.getSource();
        userData = (String) eventNode.getUserData();
    }

    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event,"dashboard-view");
    }
}
