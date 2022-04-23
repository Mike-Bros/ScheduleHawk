package com.mikebros.schedulehawk.models;


import com.mikebros.schedulehawk.DBConnection;
import javafx.scene.control.Button;
import java.time.*;

/**
 * The type Appointment.
 */
public class Appointment {
    private String id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String startDate;
    private String startTime;
    private String end;
    private String endDate;
    private String endTime;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String customerID;
    private String userID;
    private String contactID;
    private Button editButton;
    private Button deleteButton;

    /**
     * Instantiates a new Appointment.
     */
    public Appointment() {
    }

    /**
     * Set id.
     *
     * @param id the id
     */
    public void set_id(String id) {
        this.id = id;
    }

    /**
     * Set title.
     *
     * @param title the title
     */
    public void set_title(String title) {
        this.title = title;
    }

    /**
     * Set description.
     *
     * @param description the description
     */
    public void set_description(String description) {
        this.description = description;
    }

    /**
     * Set location.
     *
     * @param location the location
     */
    public void set_location(String location) {
        this.location = location;
    }

    /**
     * Set type.
     *
     * @param type the type
     */
    public void set_type(String type) {
        this.type = type;
    }

    /**
     * Set start and split dateTime into startDate and startTime.
     *
     * @param start the start
     */
    public void set_start(String start) {
        this.start = start;
        this.startDate = start.split(" ")[0];
        this.startTime = start.split(" ")[1];
    }

    /**
     * Set end and split dateTime into endDate and endTime.
     *
     * @param end the end
     */
    public void set_end(String end) {
        this.end = end;
        this.endDate = end.split(" ")[0];
        this.endTime = end.split(" ")[1];
    }

    /**
     * Set create date.
     *
     * @param createDate the create date
     */
    public void set_createDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Set created by.
     *
     * @param createdBy the created by
     */
    public void set_createdBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Set last update.
     *
     * @param lastUpdate the last update
     */
    public void set_lastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Set last updated by.
     *
     * @param lastUpdatedBy the last updated by
     */
    public void set_lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Set customer id.
     *
     * @param customerID the customer id
     */
    public void set_customerID(String customerID) {
        this.customerID = customerID;
    }

    /**
     * Set user id.
     *
     * @param userID the user id
     */
    public void set_userID(String userID) {
        this.userID = userID;
    }

    /**
     * Set contact id.
     *
     * @param contactID the contact id
     */
    public void set_contactID(String contactID) {
        this.contactID = contactID;
    }


    /**
     * Set edit button.
     *
     * @param button the button
     */
    public void set_editButton(Button button) {
        this.editButton = button;
    }

    public void set_deleteButton(Button button) {
        this.deleteButton = button;
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get title string.
     *
     * @return the string
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get description string.
     *
     * @return the string
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get location string.
     *
     * @return the string
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Get type string.
     *
     * @return the string
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get start string.
     *
     * @return the string
     */
    public String getStart() {
        return this.start;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    /**
     * Get end string.
     *
     * @return the string
     */
    public String getEnd() {
        return this.end;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    /**
     * Get create date string.
     *
     * @return the string
     */
    public String getCreateDate() {
        return this.createDate;
    }

    /**
     * Get created by string.
     *
     * @return the string
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Get last update string.
     *
     * @return the string
     */
    public String getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Get last updated by string.
     *
     * @return the string
     */
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    /**
     * Get customer id string.
     *
     * @return the string
     */
    public String getCustomerId() {
        return this.customerID;
    }

    /**
     * Get user id string.
     *
     * @return the string
     */
    public String getUserId() {
        return this.userID;
    }

    /**
     * Get contact id string.
     *
     * @return the string
     */
    public String getContactId() {
        return this.contactID;
    }


    /**
     * Get edit button string.
     *
     * @return the string
     */
    public Button getEditButton() {
        return this.editButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    private void convertDatesToUTC() {
        this.start = convertUTC(this.start);
        this.end = convertUTC(this.end);
        this.createDate = convertUTC(this.createDate);
        this.lastUpdate = convertUTC(this.lastUpdate);
    }

    private String convertUTC(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());

        dt = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toString();
        dt = dt.replace("Z[UTC]", "");
        dt = dt.replace("T", " ");
        return dt;
    }

    public void create() throws Exception {
        convertDatesToUTC();
        String query = "INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_by, Customer_ID, User_ID, Contact_ID) " +
                "VALUES ('" + this.title + "', '" + this.description + "', '" + this.location + "', '"
                + this.type + "', '" + this.start + "', '" + this.end + "', '" + this.createDate + "', '"
                + this.createdBy + "', '" + this.lastUpdate + "', '" + this.lastUpdatedBy + "', '"
                + this.customerID + "', '" + this.userID + "', '" + this.contactID + "');";
        System.out.println(query);
        DBConnection.update(query);
    }

    public void update() throws Exception {
        convertDatesToUTC();
        String query = "UPDATE appointments " +
                "SET Title = '" + this.title + "', " +
                "Description = '" + this.description + "', " +
                "Location = '" + this.location + "', " +
                "Type = '" + this.type + "', " +
                "Start = '" + this.start + "', " +
                "End = '" + this.end + "', " +
                "Create_Date = '" + this.createDate + "', " +
                "Created_By = '" + this.createdBy + "', " +
                "Last_Update = '" + this.lastUpdate + "', " +
                "Last_Updated_by = '" + this.lastUpdatedBy + "', " +
                "Customer_ID = '" + this.customerID + "', " +
                "User_ID = '" + this.userID + "', " +
                "Contact_ID = '" + this.contactID + "' " +
                "WHERE Appointment_ID = " + this.id + ";";
        System.out.println(query);
        DBConnection.update(query);
    }
}
