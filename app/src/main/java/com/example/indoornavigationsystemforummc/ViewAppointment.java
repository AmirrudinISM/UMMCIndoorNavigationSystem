package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

public class ViewAppointment extends AppCompatActivity {
    private TextView tvAppointmentID,
            tvStatus,
            tvAppointmentDateAndTime,
            tvDoctorID,
            tvCommonSymptoms,
            tvOtherSymptoms,
            tvWeight,
            tvBloodPressure,
            tvTemperature,
            tvOxygenLevel,
            tvDiagnosis,
            tvAdditionalNotes;
    private Button btnCancelAppointment;
    private TextView tvBack;
    private String appointmentDateString;
    private String appointmentIDString;
    private String appointmentStatusString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        DBController db = new DBController(this);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);

        String appointmentID = getIntent().getStringExtra("appointmentID");
        Cursor cursor = db.getAppointment(appointmentID);


        while (cursor.moveToNext()){
            tvAppointmentID = findViewById(R.id.tvAppointmentID);
            appointmentIDString = cursor.getString(0);
            tvAppointmentID.setText("Appointment ID: " + cursor.getString(0));

            tvStatus = findViewById(R.id.tvAppointmentStatus);
            appointmentStatusString = cursor.getString(6);
            tvStatus.setText("Status: " + cursor.getString(6));

            String date = cursor.getString(4);
            appointmentDateString = cursor.getString(4);
            String time = cursor.getString(5);
            tvAppointmentDateAndTime = (TextView) findViewById(R.id.tvAppointmentDateAndTime);
            tvAppointmentDateAndTime.setText("Appointment Date & Time: " + date + ", " + time);

            tvDoctorID = (TextView)findViewById(R.id.tvDoctorID);
            if(cursor.getString(14).isEmpty()){
                tvDoctorID.setText("Doctor ID: ");
            }
            else {
                tvDoctorID.setText("Doctor ID: " + cursor.getString(14));
            }

            tvCommonSymptoms = (TextView) findViewById(R.id.tvCommonSymptoms);
            if(cursor.getString(2).isEmpty()){
                tvCommonSymptoms.setText("");
            }
            else {
                tvCommonSymptoms.setText(cursor.getString(2));
            }

            tvOtherSymptoms = (TextView) findViewById(R.id.tvOtherSymptoms);
            if(cursor.getString(3).isEmpty()){
                tvOtherSymptoms.setText("");
            }
            else{
                tvOtherSymptoms.setText(cursor.getString(3));
            }

            tvWeight = (TextView) findViewById(R.id.tvWeight);
            tvWeight.setText("Weight (kg): " + cursor.getString(7));

            tvBloodPressure = (TextView) findViewById(R.id.tvBloodPressure);
            tvBloodPressure.setText("Blood Pressure (mmHg): " + cursor.getString(8));

            tvTemperature = (TextView) findViewById(R.id.tvTemperature);
            tvTemperature.setText("Temperature (°C): " + cursor.getString(9));

            tvOxygenLevel = (TextView) findViewById(R.id.tvOxygenLevel);
            tvOxygenLevel.setText("Oxygen Level (%): " + cursor.getString(10));

            tvDiagnosis = (TextView) findViewById(R.id.tvDiagnosis);
            if(cursor.getString(12).isEmpty()){
                tvDiagnosis.setText("Diagnosis: ");
            }
            else{
                tvDiagnosis.setText("Diagnosis: " + cursor.getString(12));
            }

            tvAdditionalNotes = (TextView) findViewById(R.id.tvAdditionalNotes);
            if(cursor.getString(11).isEmpty()){
                tvAdditionalNotes.setText("Additional Notes: ");
            }
            else{
                tvAdditionalNotes.setText("Additional Notes: " + cursor.getString(11));
            }
        }


        btnCancelAppointment= findViewById(R.id.btnCancelAppointment);
        if(appointmentStatusString.equals("CANCELLED")){
            btnCancelAppointment.setVisibility(View.GONE);
        }
        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate appointmentDate = LocalDate.parse(appointmentDateString);


                if(appointmentDate.compareTo(LocalDate.now()) == -1 || appointmentDate.compareTo(LocalDate.now()) == 0){
                    Toast.makeText(getApplicationContext(), "Cannot cancel appointment on the day before or the day itself!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(db.cancelAppointment(appointmentIDString)){
                        Toast.makeText(getApplicationContext(), "Appointment successfully CANCELLED!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ViewAppointment.this, MedicalAppointment.class);
                        startActivity(intent);
                    }
                }
            }
        });

        tvBack = (TextView) findViewById(R.id.tvBack);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAppointment.this, MedicalAppointment.class);
                startActivity(intent);
            }
        });

    }
}