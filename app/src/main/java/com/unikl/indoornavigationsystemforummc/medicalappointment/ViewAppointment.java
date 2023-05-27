package com.unikl.indoornavigationsystemforummc.medicalappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unikl.indoornavigationsystemforummc.utils.DBConn;
import com.unikl.indoornavigationsystemforummc.utils.DBController;
import com.example.indoornavigationsystemforummc.R;
import com.unikl.indoornavigationsystemforummc.utils.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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


        btnCancelAppointment= findViewById(R.id.btnCancelAppointment);
        tvAppointmentID = findViewById(R.id.tvAppointmentID);
        tvStatus = findViewById(R.id.tvAppointmentStatus);
        tvAppointmentDateAndTime = (TextView) findViewById(R.id.tvAppointmentDateAndTime);
        tvDoctorID = (TextView)findViewById(R.id.tvDoctorID);
        tvCommonSymptoms = (TextView) findViewById(R.id.tvCommonSymptoms);
        tvOtherSymptoms = (TextView) findViewById(R.id.tvOtherSymptoms);
        tvWeight = (TextView) findViewById(R.id.tvWeight);
        tvBloodPressure = (TextView) findViewById(R.id.tvBloodPressure);
        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvOxygenLevel = (TextView) findViewById(R.id.tvOxygenLevel);
        tvDiagnosis = (TextView) findViewById(R.id.tvDiagnosis);
        tvAdditionalNotes = (TextView) findViewById(R.id.tvAdditionalNotes);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);

        String appointmentID = getIntent().getStringExtra("appointmentID");

        DBConn dbConn = new DBConn(ViewAppointment.this);

        dbConn.viewAppointment(appointmentID, new StringCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {

                JSONObject appointmentJSON = new JSONObject(response);

                System.out.println(response);

                appointmentIDString = appointmentJSON.getString("appointmentID");
                tvAppointmentID.setText("Appointment ID: " + appointmentIDString);


                appointmentStatusString = appointmentJSON.getString("appointmentStatus");
                tvStatus.setText("Status: " + appointmentStatusString);

                String date = appointmentJSON.getString("appointmentDate");
                appointmentDateString = date;
                String time = appointmentJSON.getString("appointmentTime");

                tvAppointmentDateAndTime.setText("Appointment Date & Time: " + date + ", " + time);

                System.out.println("DoctorID: " + appointmentJSON.getString("doctorID"));
                if(appointmentJSON.getString("doctorID").isEmpty()){
                    tvDoctorID.setText("Doctor ID: ");
                }
                else {
                    tvDoctorID.setText("Doctor ID: " + appointmentJSON.getString("doctorID"));
                }

                System.out.println("Symptoms: " + appointmentJSON.getString("symptoms"));
                if(appointmentJSON.getString("symptoms").isEmpty()){
                    tvCommonSymptoms.setText("");
                }
                else {
                    tvCommonSymptoms.setText(appointmentJSON.getString("symptoms"));
                }

                System.out.println("Other Description: " + appointmentJSON.getString("otherDescription"));
                if(appointmentJSON.getString("otherDescription").isEmpty()){
                    tvOtherSymptoms.setText("");
                }
                else{
                    tvOtherSymptoms.setText(appointmentJSON.getString("otherDescription"));
                }

                System.out.println("Weight: " + appointmentJSON.getDouble("weight"));
                tvWeight.setText("Weight (kg): " + appointmentJSON.getDouble("weight"));

                System.out.println("Blood Pressure: " + appointmentJSON.getDouble("bloodPressure"));
                tvBloodPressure.setText("Blood Pressure (mmHg): " + appointmentJSON.getDouble("bloodPressure"));

                System.out.println("Temperature: " + appointmentJSON.getDouble("temperature"));
                tvTemperature.setText("Temperature (°C): " + appointmentJSON.getDouble("temperature"));

                System.out.println("Oxygen Level: " + appointmentJSON.getDouble("oxygenLevel"));
                tvOxygenLevel.setText("Oxygen Level (%): " + appointmentJSON.getDouble("oxygenLevel"));


                if(appointmentJSON.getString("diagnosis").isEmpty()){
                    tvDiagnosis.setText("Diagnosis: ");
                }
                else{
                    tvDiagnosis.setText("Diagnosis: " + appointmentJSON.getString("diagnosis"));
                }


                if(appointmentJSON.getString("additionalNotes").isEmpty()){
                    tvAdditionalNotes.setText("Additional Notes: ");
                }
                else{
                    tvAdditionalNotes.setText("Additional Notes: " + appointmentJSON.getString("additionalNotes"));
                }

                if(appointmentStatusString.equals("CANCELLED") || appointmentStatusString.equals("MISSED") || appointmentStatusString.equals("COMPLETED")){
                    btnCancelAppointment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {

            }
        });

        btnCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate appointmentDate = LocalDate.parse(appointmentDateString);


                if(appointmentDate.compareTo(LocalDate.now()) == -1 || appointmentDate.compareTo(LocalDate.now()) == 0){
                    Toast.makeText(getApplicationContext(), "Cannot cancel appointment on the day before or the day itself!",Toast.LENGTH_SHORT).show();
                }
                else{
                    dbConn.cancelAppointment(appointmentID, new StringCallback() {
                        @Override
                        public void onSuccess(String response) throws JSONException {
                            Toast.makeText(getApplicationContext(), "Appointment successfully CANCELLED!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ViewAppointment.this, MedicalAppointment.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getApplicationContext(), "Failed to cancel appointment!",Toast.LENGTH_SHORT).show();
                        }
                    });
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