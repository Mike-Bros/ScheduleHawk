package com.mikebros.schedulehawk.models;

import com.mikebros.schedulehawk.DBConnection;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private Button editButton;
    private Button deleteButton;

    public Customer(){
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setDivisionID(String divisionID) {
        this.divisionID = divisionID;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public String getDivisionID() {
        return divisionID;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    private void convertDatesToUTC() {
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
}

