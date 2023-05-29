package com.unikl.indoornavigationsystemforummc.medicalappointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.indoornavigationsystemforummc.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.unikl.indoornavigationsystemforummc.utils.DBConn;
import com.unikl.indoornavigationsystemforummc.utils.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewDoctorProfile extends AppCompatActivity {
    private TextView lblName, lblQualification, lblDoctorID, lblDrEmail, lblPhoneNumber, lblLocation;
    private LinearProgressIndicator progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_profile);

        progressBar = findViewById(R.id.drProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        lblName = findViewById(R.id.lblName);
        lblQualification = findViewById(R.id.lblQualification);
        lblDoctorID = findViewById(R.id.lblDoctorID);
        lblDrEmail = findViewById(R.id.lblDrEmail);
        lblPhoneNumber = findViewById(R.id.lblPhoneNumber);
        lblLocation = findViewById(R.id.lblLocation);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);

        String doctorID =  getIntent().getStringExtra("doctorID");

        DBConn dbConn = new DBConn(ViewDoctorProfile.this);

        dbConn.viewDoctor(doctorID, new StringCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                progressBar.setVisibility(View.GONE);

                JSONObject doctorJSON = new JSONObject(response);

                lblName.setText(doctorJSON.getString("name"));
                lblQualification.setText(doctorJSON.getString("qualification"));
                lblDoctorID.setText(doctorJSON.getString("doctorID"));
                lblDrEmail.setText(doctorJSON.getString("email"));
                lblPhoneNumber.setText(doctorJSON.getString("phoneNumber"));
                lblLocation.setText(doctorJSON.getString("location"));

            }

            @Override
            public void onFailure() {
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}