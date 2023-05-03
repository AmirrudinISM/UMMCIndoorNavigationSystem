/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.indoornavigationsystemforummc;

/**
 *
 * @author Amirrudin
 */
public class Patient {
    private String patientID;
    private String NRIC;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String ethnicity;
    private String phoneNumber;
    private String address;
    private float height;
    private String bloodType;

    public Patient(String NRIC, String email, String password, String firstName, String lastName, String ethnicity, String phoneNumber, String address, float height, String bloodType) {
        this.NRIC = NRIC;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ethnicity = ethnicity;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.height = height;
        this.bloodType = bloodType;
    }

    public Patient() {

    }

    public Patient(String nric, String email, String password, String firstName, String lastName, String ethnicity, String bloodType) {
        this.NRIC = nric;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ethnicity = ethnicity;
        this.bloodType = bloodType;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getNRIC() {
        return NRIC;
    }

    public void setNRIC(String NRIC) {
        this.NRIC = NRIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    
}
