package com.unikl.indoornavigationsystemforummc.utils;

import static com.unikl.indoornavigationsystemforummc.utils.IDGenerator.generateAppointmentID;
import static com.unikl.indoornavigationsystemforummc.utils.IDGenerator.generatePatientID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.unikl.indoornavigationsystemforummc.medicalappointment.Appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper{

    SQLiteDatabase db = this.getWritableDatabase();

    public DBController(@Nullable Context context) {
        super(context, "UMMCMedicalAppointmentSystem.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("" +
                "CREATE TABLE Patients(" +
                "PatientID TEXT PRIMARY KEY, " +
                "Email TEXT, " +
                "Password TEXT, " +
                "FirstName TEXT, " +
                "LastName TEXT, " +
                "NRIC TEXT, " +
                "Ethnicity TEXT," +
                "PhoneNumber TEXT," +
                "Address TEXT," +
                "Height REAL," +
                "BloodType TEXT)");

        sqLiteDatabase.execSQL("" +
                "CREATE TABLE Appointments(" +
                "AppointmentID TEXT PRIMARY KEY, " +
                "CreatedTime TEXT," +
                "Symptoms TEXT, " +
                "OtherDescription TEXT, " +
                "AppointmentDate TEXT, " +
                "AppointmentTime TEXT, " +
                "AppointmentStatus TEXT," +
                //remember to make this REAL (decimal)
                "Weight TEXT," +
                //make this one real too
                "BloodPressure TEXT," +
                "Temperature REAL," +
                "OxygenLevel REAL," +
                "AdditionalNotes TEXT," +
                "Diagnosis TEXT," +
                "PatientID TEXT," +
                "DoctorID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS Patients");
        DB.execSQL("DROP TABLE IF EXISTS Appointments");
        onCreate(DB);
    }

    public boolean createPatient(String nric, String email, String password, String firstName, String lastName, String ethnicity, String bloodType){
        Cursor cursor = db.rawQuery("SELECT * FROM Patients WHERE Email = ? OR NRIC = ?", new String[]{email, nric});
        if(cursor.getCount() == 0){
            ContentValues contentValues= new ContentValues();
            contentValues.put("PatientID", generatePatientID(10));
            contentValues.put("NRIC", nric);
            contentValues.put("email", email);
            contentValues.put("password", password);
            contentValues.put("FirstName", firstName);
            contentValues.put("LastName", lastName);
            contentValues.put("Ethnicity", ethnicity);
            contentValues.put("PhoneNumber", "");
            contentValues.put("Address", "");
            contentValues.put("Height", 0.0);
            contentValues.put("BloodType", bloodType);

            db.insert("Patients",null,contentValues);
            return true;
        }
        else {
            return false;
        }
    }

    //for login
    public Cursor findPatient (String email)
    {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Patients WHERE Email = ?", new String[]{email});
        return cursor;
    }

    public boolean updatePatient(String patientID, String phoneNumber, String address, float height){
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put("PhoneNumber", phoneNumber);
        values.put("Address", address);
        values.put("Height", height);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update("Patients", values, "PatientID = ?", new String[]{patientID});
        db.close();
        return true;
    }

    public ArrayList<Appointment> getAllAppointments(String inUserID){
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Appointments WHERE PatientID = ? ORDER BY CreatedTime DESC";
        Cursor cursor = db.rawQuery(query, new String[]{inUserID});

        try{
            /*
            traverse the returned query and creates an instance of Appointment to be added into
            an arrayList on each iteration.
            */
            while (cursor.moveToNext()){
                Appointment temp = new Appointment();
                temp.setAppointmentID(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentID")));
                temp.setCreatedDateTime(cursor.getString(cursor.getColumnIndexOrThrow("CreatedTime")));
                temp.setSymptoms(cursor.getString(cursor.getColumnIndexOrThrow("Symptoms")));
                temp.setOtherDescription(cursor.getString(cursor.getColumnIndexOrThrow("OtherDescription")));
                temp.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentDate")));
                temp.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentTime")));
                temp.setAppointmentStatus(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentStatus")));
                temp.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow("Weight")));
                temp.setBloodPressure(cursor.getFloat(cursor.getColumnIndexOrThrow("BloodPressure")));
                temp.setTemperature(cursor.getFloat(cursor.getColumnIndexOrThrow("Temperature")));
                temp.setOxygenLevel(cursor.getFloat(cursor.getColumnIndexOrThrow("OxygenLevel")));
                temp.setAdditionalNotes(cursor.getString(cursor.getColumnIndexOrThrow("AdditionalNotes")));
                temp.setDiagnosis(cursor.getString(cursor.getColumnIndexOrThrow("Diagnosis")));
                temp.setPatientID(cursor.getString(cursor.getColumnIndexOrThrow("PatientID")));
                temp.setDoctorID(cursor.getString(cursor.getColumnIndexOrThrow("DoctorID")));
                appointments.add(temp);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return  appointments;
    }

    public boolean createAppointment(String allSymptoms, String otherSymptoms, String appointmentDate, String appointmentTime, String patientID) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the date and time as a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = currentDateTime.format(formatter);

        ContentValues values = new ContentValues();
        values.put("AppointmentID", generateAppointmentID(10));
        values.put("CreatedTime", dateTimeString);
        values.put("Symptoms", allSymptoms);
        values.put("OtherDescription", otherSymptoms);
        values.put("AppointmentDate", appointmentDate);
        values.put("AppointmentTime", appointmentTime);
        values.put("AppointmentStatus", "PENDING");
        values.put("Weight", 0.0);
        values.put("BloodPressure", 0.0);
        values.put("Temperature", 0.0);
        values.put("OxygenLevel", 0.0);
        values.put("AdditionalNotes", "");
        values.put("Diagnosis","");
        values.put("PatientID", patientID);
        values.put("DoctorID", "");

        db.insert("Appointments",null,values);
        db.close();

        return true;
    }

    public Cursor getAppointment(String appointmentID){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM Appointments WHERE appointmentID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{appointmentID});
        return cursor;
    }

    public boolean cancelAppointment(String appointmentID){
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put("AppointmentStatus", "CANCELLED");


        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update("Appointments", values, "AppointmentID = ?", new String[]{appointmentID});
        db.close();
        return true;

    }

    public void refreshAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT AppointmentID,AppointmentDate,AppointmentTime,AppointmentStatus FROM Appointments ORDER BY CreatedTime DESC";
        Cursor cursor = db.rawQuery(query,null);

        //get relevant appointment values from database and put them into arraylist.
        while (cursor.moveToNext()){
            Appointment temp = new Appointment();
            temp.setAppointmentID(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentID")));
            temp.setAppointmentDate(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentDate")));
            temp.setAppointmentTime(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentTime")));
            temp.setAppointmentStatus(cursor.getString(cursor.getColumnIndexOrThrow("AppointmentStatus")));
            appointments.add(temp);
        }

        /*** TODO
         1. Compare current date
         2. Compare time
         3. Update appointment status to "MISSED"
         4. Update database
         ***/

    }
}
