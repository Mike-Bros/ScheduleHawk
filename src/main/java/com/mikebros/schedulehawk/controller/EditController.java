package com.mikebros.schedulehawk.controller;

import com.mikebros.schedulehawk.DBConnection;
import com.mikebros.schedulehawk.ScheduleHawkApplication;
import com.mikebros.schedulehawk.models.Appointment;
import com.mikebros.schedulehawk.models.Contact;
import com.mikebros.schedulehawk.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private ComboBox<String> contact_name;
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
            setNewUserFields();
        } else {
            setEditUserFields(userData);
            submit_button.setText("Update");
        }
    }

    @FXML
    private void submitButtonClicked(ActionEvent event) throws Exception {
        err_message_label.setText("");
        if (Objects.equals(title.getText(), "")) {
            System.out.println("Title cannot be null");
            err_message_label.setText("Title cannot be empty");
        } else if (start_date.getValue() == null
                || end_date.getValue() == null
                || create_date_date.getValue() == null
                || last_update_date.getValue() == null) {
            System.out.println("Dates cannot be null");
            err_message_label.setText("At least one date is not set, please set all dates");
        } else if (contact_name.getValue() == null) {
            System.out.println("Contact cannot be null");
            err_message_label.setText("Please select a contact");
        } else if (Objects.equals(customer_id.getText(), "")
                || Objects.equals(user_id.getText(), "")) {
            System.out.println("ID fields cannot be null");
            err_message_label.setText("Neither Customer or User ID can be empty");
        } else {
            if (Objects.equals(userData, "new")) {
                System.out.println("Creating new appointment");
                Appointment appt = createAppointment();
                appt.create();
                ScheduleHawkApplication.changeScene(event, "dashboard-view");
            } else {
                System.out.println("Updating existing appointment");
                Appointment appt = createAppointment();
                appt.update();
                ScheduleHawkApplication.changeScene(event, "dashboard-view");
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

    private void setComboBoxes() throws Exception {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 1; i <= 24; i++) {
            if (i < 10) {
                hours.add("0" + String.valueOf(i));
            } else {
                hours.add(String.valueOf(i));
            }
        }
        start_hour.setItems(hours);
        start_hour.getSelectionModel().selectFirst();
        end_hour.setItems(hours);
        end_hour.getSelectionModel().selectFirst();
        create_date_hour.setItems(hours);
        create_date_hour.getSelectionModel().selectFirst();
        last_update_hour.setItems(hours);
        last_update_hour.getSelectionModel().selectFirst();
        ;

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i <= 60; i++) {
            if (i < 10) {
                minutes.add("0" + String.valueOf(i));
            } else {
                minutes.add(String.valueOf(i));
            }
        }
        start_min.setItems(minutes);
        start_min.getSelectionModel().selectFirst();
        end_min.setItems(minutes);
        end_min.getSelectionModel().selectFirst();
        create_date_min.setItems(minutes);
        create_date_min.getSelectionModel().selectFirst();
        last_update_min.setItems(minutes);
        last_update_min.getSelectionModel().selectFirst();

        contact_name.setItems(getContactNamesFromDB());
    }

    private void setNewUserFields() {
        User activeUser = ScheduleHawkApplication.getActiveUser();
        appointment_id.setText("new appt");
        submit_button.setText("Create");
        user_id.setText(activeUser.getId());
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

    private void setEditUserFields(String appointmentId) throws Exception {
        Appointment appointment = createAppointmentFromDB(appointmentId);

        appointment_id.setText(appointment.getId());
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        location_field.setText(appointment.getLocation());
        type.setText(appointment.getType());
        user_id.setText(appointment.getUserId());
        customer_id.setText(appointment.getCustomerId());
        contact_name.setValue(getContactNameFromDB(appointment.getContactId()));
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
        appt.set_start(convertFromUTC(appointment.getString("Start")));
        appt.set_end(convertFromUTC(appointment.getString("End")));
        appt.set_createDate(convertFromUTC(appointment.getString("Create_Date")));
        appt.set_createdBy(appointment.getString("Created_By"));
        appt.set_lastUpdate(convertFromUTC(appointment.getString("Last_Update")));
        appt.set_lastUpdatedBy(appointment.getString("Last_Updated_By"));
        appt.set_customerID(appointment.getString("Customer_ID"));
        appt.set_userID(appointment.getString("User_ID"));
        appt.set_contactID(appointment.getString("Contact_ID"));

        return appt;
    }

    private ObservableList<String> getContactNamesFromDB() throws Exception {
        String query = "SELECT * FROM contacts";
        ResultSet contacts = DBConnection.query(query);
        ObservableList<String> contactList = FXCollections.observableArrayList();

        while (contacts.next()) {
            Contact contact = new Contact();
            contact.setName(contacts.getString("Contact_Name"));
            contactList.add(contact.getName());
        }
        return contactList;
    }

    private String getContactNameFromDB(String contactId) throws Exception {
        String query = "SELECT * FROM contacts WHERE Contact_ID = " + contactId + ";";
        ResultSet contact = DBConnection.query(query);
        contact.next();
        return contact.getString("Contact_Name");
    }

    private String getContactID(String name) throws Exception {
        String query = "SELECT * FROM contacts WHERE Contact_Name = \"" + name + "\";";
        ResultSet contacts = DBConnection.query(query);
        contacts.next();
        System.out.println(contacts.getString("Contact_ID"));
        return contacts.getString("Contact_ID");
    }

    private Appointment createAppointment() throws Exception {
        Appointment appt = new Appointment();
        appt.set_id(appointment_id.getText());
        appt.set_title(title.getText());
        appt.set_description(description.getText());
        appt.set_location(location_field.getText());
        appt.set_type(type.getText());
        appt.set_start(getDateTimeString(start_date, start_hour, start_min));
        appt.set_end(getDateTimeString(end_date, end_hour, end_min));
        appt.set_createDate(getDateTimeString(create_date_date, create_date_hour, create_date_min));
        appt.set_createdBy(created_by.getText());
        appt.set_lastUpdate(getDateTimeString(last_update_date, last_update_hour, last_update_min));
        appt.set_lastUpdatedBy(last_updated_by.getText());
        appt.set_customerID(customer_id.getText());
        appt.set_userID(user_id.getText());
        if (!contact_name.getSelectionModel().isEmpty()) {
            appt.set_contactID(getContactID(contact_name.getValue()));
        }

        return appt;
    }

    private LocalDate getLocalDate(String dateTime) {
        String[] arrStr = dateTime.split(" ");
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

    private String getDateTimeString(DatePicker datePicker, ComboBox<String> start_hour, ComboBox<String> start_min) {
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = start_hour.getValue() + ":" + start_min.getValue() + ":00";
        return date + " " + time;
    }

    private String convertFromUTC(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"));

        dt = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toString();
        dt = dt.replace("-05:00[America/Chicago]", "");
        dt = dt.replace("T", " ");
        return dt;
    }


}
