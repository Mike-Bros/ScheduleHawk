package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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
    private TextField contact_id;
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
        if (Objects.equals(userData, "new")) {
            appointment_id.setText("new appt");
        } else {
            setPrefilledFields(userData);
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
        for (int i=1; i<=24; i++){
            if (i<10){
                hours.add("0" + String.valueOf(i));
            }else{
                hours.add(String.valueOf(i));
            }
        }
        start_hour.setItems(hours);
        end_hour.setItems(hours);
        create_date_hour.setItems(hours);
        last_update_hour.setItems(hours);

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i=0; i<=60; i++){
            if (i<10){
                minutes.add("0" + String.valueOf(i));
            }else{
                minutes.add(String.valueOf(i));
            }
        }
        start_min.setItems(minutes);
        end_min.setItems(minutes);
        create_date_min.setItems(minutes);
        last_update_min.setItems(minutes);
    }

    private void setPrefilledFields(String appointmentId) throws Exception {
        Appointment appointment = createAppointment(appointmentId);

        appointment_id.setText(appointment.getId());
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        location_field.setText(appointment.getLocation());
        type.setText(appointment.getType());
        // need to add functionality to populate contact names and put a placeholder here based on contact_id
        contact_id.setText(appointment.getContactId());
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

    private Appointment createAppointment(String appointmentId) throws Exception {
        String query = "SELECT * FROM appointments WHERE Appointment_ID = " + appointmentId + ";";
        ResultSet appointment = DBConnection.query(query);

        Appointment appt = new Appointment();
        Button editButton = new Button();
        editButton.setText("edit");

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

    private LocalDate getLocalDate(String dateTime){
        String[] arrStr = dateTime.split(" ");
        for (String a : arrStr)
            System.out.println(a);
        return LocalDate.parse(arrStr[0]);
    }

    private String getHour(String dateTime){
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[0];
    }

    private String getMinutes(String dateTime){
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[1];
    }
}
