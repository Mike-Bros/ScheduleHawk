package com.mikebros.schedulehawk.models;

/**
 * The type First level division.
 */
public class FirstLevelDivision {
    private String id;
    private String division;
    private String countryId;

    /**
     * Instantiates a new First level division.
     */
    public FirstLevelDivision() {

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
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Country id.
     *
     * @param countryId the country id
     */
    public void countryId(String countryId) {
        this.countryId = countryId;
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
     * Gets division.
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Gets country id.
     *
     * @return the country id
     */
    public String getCountryId() {
        return countryId;
    }
}
