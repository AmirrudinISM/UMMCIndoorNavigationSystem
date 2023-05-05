package com.unikl.indoornavigationsystemforummc.medicalappointment;

public class Appointment {
    private String appointmentID;
    private String createdDateTime;
    private String symptoms;
    private String otherDescription;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentStatus;
    private float weight;
    private float bloodPressure;
    private float temperature;
    private float oxygenLevel;
    private String additionalNotes;
    private String diagnosis;
    private String patientID;
    private String doctorID;

    public Appointment(
            String appointmentID,
            String createdDateTime,
            String symptoms,
            String otherDescription,
            String appointmentDate,
            String appointmentTime,
            String appointmentStatus,
            float weight,
            float bloodPressure,
            float temperature,
            float oxygenLevel,
            String additionalNotes,
            String diagnosis,
            String patientID,
            String doctorID) {
        this.appointmentID = appointmentID;
        this.symptoms = symptoms;
        this.otherDescription = otherDescription;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.additionalNotes = additionalNotes;
        this.diagnosis = diagnosis;
        this.patientID = patientID;
        this.doctorID = doctorID;
    }

    public Appointment() {

    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getOtherDescription() {
        return otherDescription;
    }

    public void setOtherDescription(String otherDescription) {
        this.otherDescription = otherDescription;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String getAppointmentStatus) {
        this.appointmentStatus = getAppointmentStatus;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(float bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(float oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
