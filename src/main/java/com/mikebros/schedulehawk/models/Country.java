package com.mikebros.schedulehawk.models;

public class Country {
    private String id;
    private String country;

    public Country(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }
}
