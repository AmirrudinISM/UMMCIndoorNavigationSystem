package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateAppointment extends AppCompatActivity {
    CheckBox chckFever,
            chckRunnyNose,
            chckCough,
            chckHeadache,
            chckStomachPain,
            chckNausea,
            chckJointPain,
            chckHeavyBreathing,
            chckDiarhea;
    EditText txtOtherSymptoms, txtAppointmentDate;
    DatePickerDialog dateSelect;
    Spinner spnrTime;
    Button btnSubmitAppointment;
    TextView lblCancel;

    //Data to write to database
    ArrayList<String> symptoms;
    String allSymptoms, otherSymptoms, appointmentDate, appointmentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        symptoms = new ArrayList<String>();

        chckFever = (CheckBox) findViewById(R.id.chckFever);
        chckRunnyNose = (CheckBox) findViewById(R.id.chckRunnyNose);
        chckCough = (CheckBox) findViewById(R.id.chckCough);
        chckHeadache = (CheckBox) findViewById(R.id.chckHeadache);
        chckStomachPain = (CheckBox) findViewById(R.id.chckStomachPain);
        chckNausea = (CheckBox) findViewById(R.id.chckNausea);
        chckJointPain = (CheckBox) findViewById(R.id.chckJointPain);
        chckHeavyBreathing = (CheckBox) findViewById(R.id.chckHeavyBreathing);
        chckDiarhea = (CheckBox) findViewById(R.id.chckDiarrhea);
        //other appointment data
        txtOtherSymptoms = (EditText) findViewById(R.id.txtOtherSymptoms);
        txtAppointmentDate = (EditText) findViewById(R.id.txtAppointmentDate);
        spnrTime = (Spinner) findViewById(R.id.spnrTime);
        btnSubmitAppointment = (Button) findViewById(R.id.btnSubmitAppointment);

        chckFever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckFever.isChecked()){
                    symptoms.add("Fever");
                }
                else{
                    symptoms.remove("Fever");
                }
            }
        });

        chckRunnyNose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckRunnyNose.isChecked()){
                    symptoms.add("Runny Nose");
                }
                else{
                    symptoms.remove("Runny Nose");
                }
            }
        });

        chckCough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckCough.isChecked()){
                    symptoms.add("Cough");
                }
                else{
                    symptoms.remove("Cough");
                }
            }
        });

        chckHeadache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckHeadache.isChecked()){
                    symptoms.add("Headache");
                }
                else{
                    symptoms.remove("Headache");
                }
            }
        });

        chckStomachPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckStomachPain.isChecked()){
                    symptoms.add("Stomach pain");
                }
                else{
                    symptoms.remove("Stomach Pain");
                }
            }
        });

        chckNausea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckNausea.isChecked()){
                    symptoms.add("Nausea");
                }
                else{
                    symptoms.remove("Nausea");
                }
            }
        });

        chckJointPain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckJointPain.isChecked()){
                    symptoms.add("Joint pain");
                }
                else{
                    symptoms.remove("Joint pain");
                }
            }
        });

        chckHeavyBreathing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckHeavyBreathing.isChecked()){
                    symptoms.add("Heavy breathing");
                }
                else{
                    symptoms.remove("Heavy breathing");
                }
            }
        });

        chckDiarhea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckDiarhea.isChecked()){
                    symptoms.add("Diarrhea");
                }
                else{
                    symptoms.remove("Diarrhea");
                }
            }
        });

        txtAppointmentDate = findViewById(R.id.txtAppointmentDate);
        txtAppointmentDate.setInputType(InputType.TYPE_NULL);
        txtAppointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dateSelect = new DatePickerDialog(CreateAppointment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int inYear, int inMonth, int inDay) {
                        txtAppointmentDate.setText(inDay + "/" + (inMonth+1) + "/" + inYear);
                    }
                }, year, month, day);
                dateSelect.show();
            }
        });

        //create a list of items for the spinner.
        String[] vaccinationTimes = new String[]{"09:00AM","10:00AM","11:00AM", "12:00PM", "01:00PM", "02:00PM", "03:00PM", "04:00PM", "05:00PM"};

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vaccinationTimes);

        //set the spinners adapter to the previously created one.
        spnrTime.setAdapter(adapterTime);

        btnSubmitAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherSymptoms = txtOtherSymptoms.getText().toString();
                if (symptoms.size() == 0 && otherSymptoms.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select at least one symptom or describe any other symptoms",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(symptoms.isEmpty()){
                        allSymptoms = "";
                    }
                    else {
                        allSymptoms = concatenateSymptomsString();
                    }


                    appointmentDate = txtAppointmentDate.getText().toString();
                    appointmentTime = spnrTime.getSelectedItem().toString();

                    SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
                    String patientID = preferences.getString("PatientID","");
                    DBController db = new DBController(CreateAppointment.this);
                    if(db.createAppointment(allSymptoms, otherSymptoms, appointmentDate, appointmentTime, patientID)){
                        Toast.makeText(getApplicationContext(), "Appointment creation is successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAppointment.this, MedicalAppointment.class);
                        startActivity(intent);
                    }
                }
            }
        });

        lblCancel = (TextView) findViewById(R.id.lblCancel);
        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAppointment.this, MedicalAppointment.class);
                startActivity(intent);
            }
        });
    }

    String concatenateSymptomsString(){
        String res = "";
        for (int i = 0; i < symptoms.size(); i++){
            if(i == (symptoms.size() - 1)){
                res += symptoms.get(i);
            }
            else{
                res += symptoms.get(i) + ", ";
            }

        }
        return res;
    }
}