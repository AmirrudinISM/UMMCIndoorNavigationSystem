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

import org.w3c.dom.Text;

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
        DBController dbController = new DBController(Profile.this);
        Cursor result = dbController.findPatient(preferences.getString("Email",""));

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

        while(result.moveToNext()){
            lblPatientID.setText(result.getString(0));
            lblEmail.setText(result.getString(1));
            lblFirstName.setText(result.getString(3));
            lblLastName.setText(result.getString(4));
            lblNRIC.setText(result.getString(5));
            lblEthnicity.setText(result.getString(6));
            lblPhoneNumber.setText(result.getString(7));
            lblAddress.setText(result.getString(8));
            lblHeight.setText(result.getString(9));
            lblBloodType.setText(result.getString(10));
        }

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