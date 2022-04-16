package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class EditCustomerController {

    private String userData;

    @FXML
    private Text err_message_text;
    @FXML
    private TextField address;
    @FXML
    private Label err_message_label;
    @FXML
    private Button back_button;
    @FXML
    private TextField created_by;
    @FXML
    private ComboBox<String> create_date_hour;
    @FXML
    private ComboBox<String> last_update_min;
    @FXML
    private TextField last_updated_by;
    @FXML
    private Button submit_button;
    @FXML
    private TextField phone;
    @FXML
    private ComboBox<String> last_update_hour;
    @FXML
    private TextField customer_name;
    @FXML
    private ComboBox<String> create_date_min;
    @FXML
    private TextField customer_id;
    @FXML
    private ComboBox<String> divison_id;
    @FXML
    private TextField postal_code;
    @FXML
    private DatePicker create_date_date;
    @FXML
    private DatePicker last_update_date;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Edit View");

        getUserData();
        setFormFields();

        System.out.println("Finished initializing Edit View");
        System.out.println("......................................................................................\n");
    }

    private void getUserData() {
        ActionEvent loadEvent = ScheduleHawkApplication.lastSceneChangeEvent;
        Node eventNode = (Node) loadEvent.getSource();
        userData = (String) eventNode.getUserData();
    }

    private void setFormFields() throws Exception {
        setComboBoxes();
        err_message_label.setText("");

        if (Objects.equals(userData, "new")) {
            setNewCustomerFields();
        } else {
            setEditCustomerFields(userData);
        }
    }

    private void setNewCustomerFields() {
        User activeUser = ScheduleHawkApplication.getActiveUser();
        customer_id.setText("new appt");
        submit_button.setText("Create");

        created_by.setText(activeUser.getName());
        last_updated_by.setText(activeUser.getName());
        create_date_date.setValue(LocalDate.now());
        last_update_date.setValue(LocalDate.now());

        System.out.println(LocalTime.now().toString());
        String[] arrStr = LocalTime.now().toString().split(":");
        String localHour = arrStr[0];
        String localMin = arrStr[1];
        create_date_hour.setValue(localHour);
        create_date_min.setValue(localMin);
        last_update_hour.setValue(localHour);
        last_update_min.setValue(localMin);
    }

    private void setEditCustomerFields(String userData) {
        submit_button.setText("Update");

    }

    private void setComboBoxes() throws Exception {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 1; i <= 24; i++) {
            if (i < 10) {
                hours.add("0" + i);
            } else {
                hours.add(String.valueOf(i));
            }
        }
        create_date_hour.setItems(hours);
        create_date_hour.getSelectionModel().selectFirst();
        last_update_hour.setItems(hours);
        last_update_hour.getSelectionModel().selectFirst();

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes.add("0" + i);
            } else {
                minutes.add(String.valueOf(i));
            }
        }
        create_date_min.setItems(minutes);
        create_date_min.getSelectionModel().selectFirst();
        last_update_min.setItems(minutes);
        last_update_min.getSelectionModel().selectFirst();
    }

    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    public void submitButtonClicked(ActionEvent event) {
        err_message_label.setText("");
    }
}
