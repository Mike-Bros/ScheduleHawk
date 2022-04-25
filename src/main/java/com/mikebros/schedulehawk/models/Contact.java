package com.mikebros.schedulehawk.models;

/**
 * The type Contact.
 */
public class Contact {
    private String id;
    private String name;
    private String email;

    /**
     * Instantiates a new Contact.
     */
    public Contact() {
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
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get id string.
     *
     * @return the string
     */
    public String getId(){
        return this.id;
    }

    /**
     * Get name string.
     *
     * @return the string
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get email string.
     *
     * @return the string
     */
    public String getEmail(){
        return this.email;
    }

}
