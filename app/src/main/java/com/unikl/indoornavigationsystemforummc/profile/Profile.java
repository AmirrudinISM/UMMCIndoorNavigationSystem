package com.unikl.indoornavigationsystemforummc.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unikl.indoornavigationsystemforummc.utils.DBConn;
import com.unikl.indoornavigationsystemforummc.main.MainMenu;
import com.example.indoornavigationsystemforummc.R;
import com.unikl.indoornavigationsystemforummc.utils.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    TextView lblPatientID;
    TextView lblEmail;
    TextView lblFirstName;
    TextView lblLastName;
    TextView lblNRIC;
    TextView lblEthnicity;
    TextView lblPhoneNumber;
    TextView lblAddress;
    TextView lblHeight;
    TextView lblBloodType;
    Button btnEditProfile;
    TextView lblMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
//        DBController dbController = new DBController(Profile.this);
//        Cursor result = dbController.findPatient(preferences.getString("Email",""));



        lblPatientID = (TextView) findViewById(R.id.lblPatientID);
        lblEmail = (TextView) findViewById(R.id.lblEmail);
        lblFirstName = (TextView) findViewById(R.id.lblFirstName);
        lblLastName = (TextView) findViewById(R.id.lblLastName);
        lblNRIC = (TextView) findViewById(R.id.lblNRIC);
        lblEthnicity = (TextView) findViewById(R.id.lblEthnicity);
        lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
        lblAddress = (TextView) findViewById(R.id.lblAddress);
        lblHeight = (TextView) findViewById(R.id.lblHeight);
        lblBloodType = (TextView) findViewById(R.id.lblBloodType);

        DBConn conn = new DBConn(Profile.this);
        conn.viewPatient(preferences.getString("PatientID", ""), new StringCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                JSONObject profileJSON = new JSONObject(response);

                System.out.println(response);

                lblPatientID.setText(profileJSON.getString("patientID"));
                lblEmail.setText(profileJSON.getString("email"));
                lblFirstName.setText(profileJSON.getString("firstName"));
                lblLastName.setText(profileJSON.getString("lastName"));
                lblNRIC.setText(profileJSON.getString("NRIC"));
                lblEthnicity.setText(profileJSON.getString("ethnicity"));
                lblPhoneNumber.setText(profileJSON.getString("phoneNumber"));
                lblAddress.setText(profileJSON.getString("address"));
                lblHeight.setText(profileJSON.getString("height"));
                lblBloodType.setText(profileJSON.getString("bloodType"));
            }

            @Override
            public void onFailure() {
                Toast.makeText(Profile.this, "This doesn't work", Toast.LENGTH_LONG).show();
            }
        });


        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });

        lblMainMenu = (TextView) findViewById(R.id.lblMainMenu);
        lblMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }
}