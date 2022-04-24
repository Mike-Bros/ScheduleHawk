package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReportsController {

    public Button back_button;

    @FXML
    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }
}
