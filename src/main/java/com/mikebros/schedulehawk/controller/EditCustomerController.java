package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.ScheduleHawkApplication;
import javafx.event.ActionEvent;

public class EditCustomerController {


    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    public void submitButtonClicked(ActionEvent event) {
    }
}
