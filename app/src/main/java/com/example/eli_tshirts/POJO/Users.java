package com.example.eli_tshirts.POJO;

/**
 * Model Class to get details of Users form Firestore
 */
public class Users {
    private String firstName, lastName, email, pass, phone, image;

    /**
     * Non-parameterised constructor
     */
    public Users() {
    }

    /**
     * Parameterized constructor for Users
     * @param firstName - parameter of type String
     * @param lastName - parameter of type String
     * @param email -  parameter of type String
     * @param pass -  parameter of type String
     * @param phone -  parameter of type String
     */
    public Users(String firstName, String lastName, String email, String pass, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
    }

    /**
     * Getter for Image
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Setter for image
     * @param image - parameter for image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Getter for firstName
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName
     * @param firstName - parameter for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName
     * @param lastName - parameter for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for Email
     * @param email - parameter for email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for pass
     * @return pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * Setter for pass
     * @param pass - parameter for pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Getter for phone
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for phone
     * @param phone - parameter for phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}