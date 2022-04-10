package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Objects;

public class EditController {

    private String userData;

    @FXML
    private DatePicker end_date;
    @FXML
    private TextField appointment_id;
    @FXML
    private TextField description;
    @FXML
    private TextField title;
    @FXML
    private TextField type;
    @FXML
    private ComboBox<?> contact_name;
    @FXML
    private TextField user_id;
    @FXML
    private Button back_button;
    @FXML
    private TextField created_by;
    @FXML
    private ComboBox<String> start_min;
    @FXML
    private ComboBox<String> create_date_hour;
    @FXML
    private ComboBox<String> last_update_min;
    @FXML
    private TextField last_updated_by;
    @FXML
    private ComboBox<String> end_min;
    @FXML
    private ComboBox<String> start_hour;
    @FXML
    private ComboBox<String> last_update_hour;
    @FXML
    private TextField location_field;
    @FXML
    private ComboBox<String> create_date_min;
    @FXML
    private ComboBox<String> end_hour;
    @FXML
    private TextField customer_id;
    @FXML
    private DatePicker create_date_date;
    @FXML
    private DatePicker start_date;
    @FXML
    private DatePicker last_update_date;
    @FXML
    private Button submit_button;
    @FXML
    private Label err_message_label;

    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Edit View");

        getUserData();
        setFormFields();

        System.out.println("Finished initializing Edit View");
        System.out.println("......................................................................................\n");
    }

    private void setFormFields() throws Exception {
        setComboBoxes();
        err_message_label.setText("");
        if (Objects.equals(userData, "new")) {
            appointment_id.setText("new appt");
            submit_button.setText("Create");
        } else {
            setPrefilledFields(userData);
            submit_button.setText("Update");
        }
    }

    @FXML
    private void submitButtonClicked() {
        err_message_label.setText("");
        if (Objects.equals(title.getText(), "")) {
            System.out.println("Title cannot be null");
            err_message_label.setText("Title cannot be empty");
        } else if (last_update_date.getValue() == null) {
            System.out.println("last updated date cannot be null");
            err_message_label.setText("Last Updated Date cannot be empty");
        } else if (last_update_hour.getSelectionModel().isEmpty()
                || last_update_min.getSelectionModel().isEmpty()) {
            System.out.println("last updated time cannot be null");
            err_message_label.setText("Last Updated Time cannot be empty");
        } else if (validDateTimes()) {
            System.out.println("not all dates and times are valid format");
            err_message_label.setText("Make sure all dates and times are a valid format");
        } else {
            if (Objects.equals(userData, "new")) {
                System.out.println("Creating new appointment");
                Appointment appt = createAppointment();
                appt.create();

            } else {
                System.out.println("Updating existing appointment");
                Appointment appt = createAppointment();
                appt.update();
            }
        }
    }

    private void getUserData() {
        ActionEvent loadEvent = ScheduleHawkApplication.lastSceneChangeEvent;
        Node eventNode = (Node) loadEvent.getSource();
        userData = (String) eventNode.getUserData();
    }

    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    private void setComboBoxes() {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 1; i <= 24; i++) {
            if (i < 10) {
                hours.add("0" + String.valueOf(i));
            } else {
                hours.add(String.valueOf(i));
            }
        }
        start_hour.setItems(hours);
        end_hour.setItems(hours);
        create_date_hour.setItems(hours);
        last_update_hour.setItems(hours);

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i <= 60; i++) {
            if (i < 10) {
                minutes.add("0" + String.valueOf(i));
            } else {
                minutes.add(String.valueOf(i));
            }
        }
        start_min.setItems(minutes);
        end_min.setItems(minutes);
        create_date_min.setItems(minutes);
        last_update_min.setItems(minutes);
    }

    private void setPrefilledFields(String appointmentId) throws Exception {
        Appointment appointment = createAppointmentFromDB(appointmentId);

        appointment_id.setText(appointment.getId());
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        location_field.setText(appointment.getLocation());
        type.setText(appointment.getType());
        // need to add functionality to populate contact names and put a placeholder here based on contact_id
        user_id.setText(appointment.getUserId());
        customer_id.setText(appointment.getCustomerId());

        start_date.setValue(getLocalDate(appointment.getStart()));
        start_hour.setValue(getHour(appointment.getStart()));
        start_min.setValue(getMinutes(appointment.getStart()));

        end_date.setValue(getLocalDate(appointment.getEnd()));
        end_hour.setValue(getHour(appointment.getEnd()));
        end_min.setValue(getMinutes(appointment.getEnd()));

        create_date_date.setValue(getLocalDate(appointment.getCreateDate()));
        create_date_hour.setValue(getHour(appointment.getCreateDate()));
        create_date_min.setValue(getMinutes(appointment.getCreateDate()));

        last_update_date.setValue(getLocalDate(appointment.getLastUpdate()));
        last_update_hour.setValue(getHour(appointment.getLastUpdate()));
        last_update_min.setValue(getMinutes(appointment.getLastUpdate()));

        created_by.setText(appointment.getCreatedBy());
        last_updated_by.setText(appointment.getLastUpdatedBy());
    }

    private Appointment createAppointmentFromDB(String appointmentId) throws Exception {
        String query = "SELECT * FROM appointments WHERE Appointment_ID = " + appointmentId + ";";
        ResultSet appointment = DBConnection.query(query);

        Appointment appt = new Appointment();
        appointment.next();

        appt.set_id(appointment.getString("Appointment_ID"));
        appt.set_title(appointment.getString("Title"));
        appt.set_description(appointment.getString("Description"));
        appt.set_location(appointment.getString("Location"));
        appt.set_type(appointment.getString("Type"));
        appt.set_start(appointment.getString("Start"));
        appt.set_end(appointment.getString("End"));
        appt.set_createDate(appointment.getString("Create_Date"));
        appt.set_createdBy(appointment.getString("Created_By"));
        appt.set_lastUpdate(appointment.getString("Last_Update"));
        appt.set_lastUpdatedBy(appointment.getString("Last_Updated_By"));
        appt.set_customerID(appointment.getString("Customer_ID"));
        appt.set_userID(appointment.getString("User_ID"));
        appt.set_contactID(appointment.getString("Contact_ID"));

        return appt;
    }

    private Appointment createAppointment() {
        Appointment appt = new Appointment();
        appt.set_id(appointment_id.getText());
        appt.set_title(title.getText());
        appt.set_description(description.getText());
        appt.set_location(location_field.getText());
        appt.set_type(type.getText());
        // appt.set_start();
        // appt.set_end();
        //appt.set_createDate();
        appt.set_createdBy(created_by.getText());
        // appt.set_lastUpdate();
        appt.set_lastUpdatedBy(last_updated_by.getText());
        appt.set_customerID(customer_id.getText());
        appt.set_userID(user_id.getText());
        // appt.set_contactID();

        return appt;
    }

    private LocalDate getLocalDate(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        for (String a : arrStr)
            System.out.println(a);
        return LocalDate.parse(arrStr[0]);
    }

    private String getHour(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[0];
    }

    private String getMinutes(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[1];
    }

    private Boolean validDateTimes(){
        return true;
    }
}
