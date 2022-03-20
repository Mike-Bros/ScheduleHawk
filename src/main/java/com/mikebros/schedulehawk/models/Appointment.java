package com.mikebros.schedulehawk.models;


public class Appointment {
    private String id;
    private String title;
    private String description;
    private String location;
    private String type;
    private String start;
    private String end;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    private String customerID;
    private String userID;
    private String contactID;

    public Appointment(){
        
    }

    public void set_id(String id){
        this.id = id;
    }
    public void set_title(String title){
        this.title = title;
    }
    public void set_description(String description){
        this.description = description;
    }
    public void set_location(String location){
        this.location = location;
    }
    public void set_type(String type){
        this.type = type;
    }
    public void set_start(String start){
        this.start = start;
    }
    public void set_end(String end){
        this.end = end;
    }
    public void set_createDate(String createDate){
        this.createDate = createDate;
    }
    public void set_createdBy(String createdBy){
        this.createdBy = createdBy;
    }
    public void set_lastUpdate(String lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    public void set_lastUpdatedBy(String lastUpdatedBy){
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public void set_customerID(String customerID){
        this.customerID = customerID;
    }
    public void set_userID(String userID){
        this.userID = userID;
    }
    public void set_contactID(String contactID){
        this.contactID = contactID;
    }

    public String getId(){
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getDescription(){
        return this.description;
    }
    public String getLocation(){
        return this.location;
    }
    public String getType(){
        return this.type;
    }
    public String getStart(){
        return this.start;
    }
    public String getEnd(){
        return this.end;
    }
    public String getCreateDate(){
        return this.createDate;
    }
    public String getCreatedBy(){
        return this.createdBy;
    }
    public String getLastUpdate(){
        return this.lastUpdate;
    }
    public String getLastUpdatedBy(){
        return this.lastUpdatedBy;
    }
    public String getCustomerId(){
        return this.customerID;
    }
    public String getUserId(){
        return this.userID;
    }
    public String getContactId(){
        return this.contactID;
    }


}
