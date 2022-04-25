package com.mikebros.schedulehawk.models;

import com.mikebros.schedulehawk.DBConnection;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The type Customer.
 */
public class Customer {
    private String id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String divisionID;
    private String country;
    private Button editButton;
    private Button deleteButton;

    /**
     * Instantiates a new Customer.
     */
    public Customer() {
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Sets created by.
     *
     * @param createdBy the created by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Sets last update.
     *
     * @param lastUpdate the last update
     */
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Sets last updated by.
     *
     * @param lastUpdatedBy the last updated by
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Sets division id.
     *
     * @param divisionID the division id
     */
    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets edit button.
     *
     * @param editButton the edit button
     */
    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    /**
     * Sets delete button.
     *
     * @param deleteButton the delete button
     */
    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Gets created by.
     *
     * @return the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Gets last update.
     *
     * @return the last update
     */
    public String getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets last updated by.
     *
     * @return the last updated by
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public String getDivisionID() {
        return divisionID;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets edit button.
     *
     * @return the edit button
     */
    public Button getEditButton() {
        return editButton;
    }

    /**
     * Gets delete button.
     *
     * @return the delete button
     */
    public Button getDeleteButton() {
        return deleteButton;
    }

    /**
     * Convert start and end to UTC.
     */
    private void convertDatesToUTC() {
        this.createDate = convertUTC(this.createDate);
        this.lastUpdate = convertUTC(this.lastUpdate);
    }

    /**
     * Convert given DateTime string to UTC.
     *
     * @param dt the datetime string in local time
     * @return the UTC datetime string
     */
    private String convertUTC(String dt) {
        LocalDate localDate = LocalDate.parse(dt.split(" ")[0]);
        LocalTime localTime = LocalTime.parse(dt.split(" ")[1]);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.systemDefault());

        dt = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toString();
        dt = dt.replace("Z[UTC]", "");
        dt = dt.replace("T", " ");
        return dt;
    }

    /**
     * Creates customer in the database from local Customer object.
     *
     * @throws Exception the exception
     */
    public void create() throws Exception {
        convertDatesToUTC();
        String query = "INSERT INTO customers " +
                "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, " +
                "Last_Update, Last_Updated_by, Division_ID) " +
                "VALUES ('" + this.name + "', '" + this.address + "', '" + this.postalCode + "', '"
                + this.phone + "', '" + this.createDate + "', '"
                + this.createdBy + "', '" + this.lastUpdate + "', '" + this.lastUpdatedBy + "', '"
                + this.divisionID + "');";
        System.out.println(query);
        DBConnection.update(query);
    }

    /**
     * Updates customer in the database from local Customer object.
     *
     * @throws Exception the exception
     */
    public void update() throws Exception {
        convertDatesToUTC();
        String query = "UPDATE customers " +
                "SET Customer_Name = '" + this.name + "', " +
                "Address = '" + this.address + "', " +
                "Postal_Code = '" + this.postalCode + "', " +
                "Phone = '" + this.phone + "', " +
                "Create_Date = '" + this.createDate + "', " +
                "Created_By = '" + this.createdBy + "', " +
                "Last_Update = '" + this.lastUpdate + "', " +
                "Last_Updated_by = '" + this.lastUpdatedBy + "', " +
                "Division_ID = '" + this.divisionID + "' " +
                "WHERE Customer_ID = " + this.id + ";";
        System.out.println(query);
        DBConnection.update(query);
    }
}

