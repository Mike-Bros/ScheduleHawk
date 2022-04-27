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

import java.security.spec.RSAOtherPrimeInfo;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The edit appointment controller.
 */
public class EditAppointmentController {

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

    /**
     * Initialize the edit-view.fxml.
     *
     * @throws Exception the exception
     */
    public void initialize() throws Exception {
        System.out.println("......................................................................................");
        System.out.println("Initializing Edit View");

        getUserData();
        setFormFields();

        System.out.println("Finished initializing Edit View");
        System.out.println("......................................................................................\n");
    }

    /**
     * Helper for initialize, Gets user data from the last change scene event.
     */
    private void getUserData() {
        ActionEvent loadEvent = ScheduleHawkApplication.lastSceneChangeEvent;
        Node eventNode = (Node) loadEvent.getSource();
        userData = (String) eventNode.getUserData();
    }

    /**
     * Helper for initialize, Sets various form fields.
     *
     * @throws Exception the exception
     */
    private void setFormFields() throws Exception {
        setComboBoxes();
        err_message_label.setText("");

        if (Objects.equals(userData, "new")) {
            setNewUserFields();
        } else {
            setEditUserFields(userData);
        }
    }

    /**
     * Submit button clicked.
     *
     * @param event the button click event
     * @throws Exception the exception
     */
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
        } else if (!apptIsWithinBusinessHours()) {
            System.out.println("Appointments cannot be scheduled outside of business hours");
            err_message_label.setText("Appointments cannot be scheduled outside of business hours (8:00 a.m. to 10:00 p.m. EST Mon-Fri)");
        } else if (customerHasOverlappingAppt()) {
            System.out.println("Customer has an overlapping appointment");
            err_message_label.setText("Cannot overlap appointment times with an existing customer appointment");
        } else if (!apptStartIsBeforeEnd()) {
            System.out.println("Appointment starts after the end time");
            err_message_label.setText("Cannot have a start time after the end time");
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

    /**
     * Checks if appointment is within business hours.
     *
     * @return the boolean if appointment is with business hours
     * @throws ParseException the parse exception
     */
    private boolean apptIsWithinBusinessHours() throws ParseException {
        Calendar apptCal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date localDate = formatter.parse(convertLocalToEST(getDateTimeString(start_date, start_hour, start_min)));
        apptCal.setTime(localDate);
        if (apptCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || apptCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return false;
        }
        localDate = formatter.parse(convertLocalToEST(getDateTimeString(end_date, end_hour, end_min)));
        apptCal.setTime(localDate);
        if (apptCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || apptCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return false;
        }

        int estStart = Integer.parseInt(getHour(convertLocalToEST(getDateTimeString(start_date, start_hour, start_min))));
        int estEnd = Integer.parseInt(getHour(convertLocalToEST(getDateTimeString(end_date, end_hour, end_min))));

        if (estStart >= 8 && estStart <= 22 && estEnd >= 8 && estEnd <= 22) {
            System.out.println("Within Business Hours");
            return true;
        } else {
            System.out.println("Not within Business Hours");
            return false;
        }
    }

    /**
     * Checks if customer has overlapping appointments.
     * <p>
     * Uses lambda function to more easily iterate over an ObservableList where the contents of the objects within
     * the list are needed without the need to define a new helper method
     *
     * @return the boolean if customer has overlapping appointments
     * @throws Exception the exception
     */
    private boolean customerHasOverlappingAppt() throws Exception {
        AtomicReference<Boolean> overlappingApptExists = new AtomicReference<>(false);
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments WHERE Customer_ID = " + customer_id.getText();
        ResultSet appointments = DBConnection.query(query);

        while (appointments.next()) {
            Appointment appt = new Appointment();
            appt.set_id(appointments.getString("Appointment_ID"));
            appt.set_start(convertFromUTC(appointments.getString("Start")));
            appt.set_end(convertFromUTC(appointments.getString("End")));
            appointmentList.add(appt);
        }
        if (appointmentList.isEmpty()) {
            return false;
        } else {
            appointmentList.forEach((appt) -> {
                try {
                    if (!Objects.equals(appt.getId(), appointment_id.getText())) {
                        if (onSameDay(appt.getStart(), getDateTimeString(start_date, start_hour, start_min)) || onSameDay(appt.getStart(), getDateTimeString(end_date, end_hour, end_min))) {
                            LocalTime startA = LocalTime.of(Integer.parseInt(getHour(appt.getStart())), Integer.parseInt(getMinutes(appt.getStart())));
                            LocalTime stopA = LocalTime.of(Integer.parseInt(getHour(appt.getEnd())), Integer.parseInt(getMinutes(appt.getEnd())));

                            LocalTime startB = LocalTime.of(Integer.parseInt(start_hour.getValue()), Integer.parseInt(start_hour.getValue()));
                            LocalTime stopB = LocalTime.of(Integer.parseInt(end_hour.getValue()), Integer.parseInt(end_min.getValue()));

                            if (startA.isBefore(stopB) && stopA.isAfter(startB)) {
                                overlappingApptExists.set(true);
                            }
                        }
                    }
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            });
        }
        return overlappingApptExists.get();
    }

    /**
     * Check if appointment start datetime is before end datetime.
     *
     * @return the boolean, true if the start is before the end
     * @throws ParseException the parse exception
     */
    private boolean apptStartIsBeforeEnd() throws ParseException {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        Date startDate = formatter.parse(getDateTimeString(start_date, start_hour, start_min));
        Date endDate = formatter.parse(getDateTimeString(end_date, end_hour, end_min));
        startCal.setTime(startDate);
        endCal.setTime(endDate);

        System.out.println(startCal.compareTo(endCal));

        if(startCal.compareTo(endCal) < 0){
            System.out.println("here");
            return true;
        } else{
            return false;
        }
    }

    /**
     * Checks if given datetimes are on the same day boolean.
     *
     * @param dt_1 the first datetime
     * @param dt_2 the second datetim
     * @return the boolean
     * @throws ParseException the parse exception
     */
    private boolean onSameDay(String dt_1, String dt_2) throws ParseException {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date date1 = formatter.parse(dt_1);
        Date date2 = formatter.parse(dt_2);
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * Back button clicked.
     *
     * @param event the button click event
     */
    public void backButtonClicked(ActionEvent event) {
        System.out.println("Back button clicked");
        ScheduleHawkApplication.changeScene(event, "dashboard-view");
    }

    /**
     * Helper function for setFormFields, Sets combo boxes.
     *
     * @throws Exception the exception
     */
    private void setComboBoxes() throws Exception {
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i <= 23; i++) {
            if (i < 10) {
                hours.add("0" + i);
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

        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                minutes.add("0" + i);
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

    /**
     * Helper function for setFormFields, Sets new user fields.
     */
    private void setNewUserFields() {
        User activeUser = ScheduleHawkApplication.getActiveUser();
        appointment_id.setText("new appt");
        submit_button.setText("Create");
        user_id.setText(activeUser.getId());
        created_by.setText(activeUser.getName());
        last_updated_by.setText(activeUser.getName());
        create_date_date.setValue(LocalDate.now());
        last_update_date.setValue(LocalDate.now());

        String[] arrStr = LocalTime.now().toString().split(":");
        String localHour = arrStr[0];
        String localMin = arrStr[1];
        create_date_hour.setValue(localHour);
        create_date_min.setValue(localMin);
        last_update_hour.setValue(localHour);
        last_update_min.setValue(localMin);
    }

    /**
     * Helper function for setFormFields, Sets edit user fields.
     *
     * @param appointmentId the appointment id
     * @throws Exception the exception
     */
    private void setEditUserFields(String appointmentId) throws Exception {
        Appointment appointment = createAppointmentFromDB(appointmentId);
        submit_button.setText("Update");
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

    /**
     * Create appointment from DB.
     *
     * @param appointmentId the id of the appoitment to create
     * @return the appointment
     * @throws Exception the exception
     */
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

    /**
     * Gets contact names from DB.
     *
     * @return the observable list of contact names from DB
     * @throws Exception the exception
     */
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

    /**
     * Gets contact name from DB given contactId.
     *
     * @param contactId the contact id
     * @return the contact name from DB
     * @throws Exception the exception
     */
    private String getContactNameFromDB(String contactId) throws Exception {
        String query = "SELECT * FROM contacts WHERE Contact_ID = " + contactId + ";";
        ResultSet contact = DBConnection.query(query);
        contact.next();
        return contact.getString("Contact_Name");
    }

    /**
     * Gets contact id from DB given contact name
     *
     * @param name the name
     * @return the contact id
     * @throws Exception the exception
     */
    private String getContactID(String name) throws Exception {
        String query = "SELECT * FROM contacts WHERE Contact_Name = \"" + name + "\";";
        ResultSet contacts = DBConnection.query(query);
        contacts.next();
        System.out.println(contacts.getString("Contact_ID"));
        return contacts.getString("Contact_ID");
    }

    /**
     * Create Appointment from form fields on view.
     *
     * @return the appointment
     * @throws Exception the exception
     */
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

    /**
     * Gets LocalDate object from dateTime string
     *
     * @param dateTime the dateTime string
     * @return the localDate
     */
    private LocalDate getLocalDate(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        return LocalDate.parse(arrStr[0]);
    }

    /**
     * Gets hour given dateTime string.
     *
     * @param dateTime the dateTime string
     * @return the hour
     */
    private String getHour(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[0];
    }

    /**
     * Gets minutes given dateTime string.
     *
     * @param dateTime the dateTime string
     * @return the minutes
     */
    private String getMinutes(String dateTime) {
        String[] arrStr = dateTime.split(" ");
        arrStr = arrStr[1].split(":");
        return arrStr[1];
    }

    /**
     * Puts together dateTime string from form fields.
     *
     * @param datePicker the date picker
     * @param start_hour the start hour combobox
     * @param start_min  the start min combobox
     * @return the date time string
     */
    private String getDateTimeString(DatePicker datePicker, ComboBox<String> start_hour, ComboBox<String> start_min) {
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = start_hour.getValue() + ":" + start_min.getValue() + ":00";
        return date + " " + time;
    }

    /**
     * Convert from UTC dateTime string to local dateTime string.
     *
     * @param dt the UTC dateTime
     * @return the string
     */
    private String convertFromUTC(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"));

        dt = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toString();
        dt = dt.substring(0, 16); // removes zone info
        dt = dt.replace("T", " ");
        return dt;
    }

    /**
     * Convert local dateTime to EST dateTime string.
     *
     * @param dt the local dateTime
     * @return the string
     */
    private String convertLocalToEST(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of(ZoneId.systemDefault().toString()));

        dt = String.valueOf(zonedDateTime.withZoneSameInstant(ZoneId.of("US/Eastern")));
        dt = dt.substring(0, 16); // removes zone info
        dt = dt.replace("T", " ");
        return dt;
    }
}
