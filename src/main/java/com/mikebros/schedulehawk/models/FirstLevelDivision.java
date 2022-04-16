package com.mikebros.schedulehawk.models;

public class FirstLevelDivision {
    private String id;
    private String division;
    private String countryId;

    public FirstLevelDivision(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void countryId(String countryId) {
        this.countryId = countryId;
    }

    public String getId() {
        return id;
    }

    public String getDivision() {
        return division;
    }

    public String getCountryId() {
        return countryId;
    }
}
