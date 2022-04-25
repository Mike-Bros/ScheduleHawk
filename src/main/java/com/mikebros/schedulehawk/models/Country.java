package com.mikebros.schedulehawk.models;

/**
 * The type Country.
 */
public class Country {
    private String id;
    private String country;

    /**
     * Instantiates a new Country.
     */
    public Country() {

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
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
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
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }
}
