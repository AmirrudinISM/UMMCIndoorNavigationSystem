package com.unikl.indoornavigationsystemforummc.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoornavigationsystemforummc.R;
import com.unikl.indoornavigationsystemforummc.medicalappointment.MedicalAppointment;
import com.unikl.indoornavigationsystemforummc.navigation.IndoorNavigation;
import com.unikl.indoornavigationsystemforummc.profile.Profile;

import es.situm.sdk.SitumSdk;

public class MainMenu extends AppCompatActivity {
    TextView status;
    Button btnMedicalAppointment;
    Button btnIndoorMaps;
    Button btnProfile;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SitumSdk.init(this);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
        status = (TextView) findViewById(R.id.statusDisplay2);
       if(preferences.getBoolean("Login",false)){
            status.setText("Welcome, " + preferences.getString("FirstName","") + "!");
       }
       else {
           status.setText("Welcome, Visitor! Please login to create an appointment." );
       }

        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnIndoorMaps = (Button) findViewById(R.id.btnIndoorMaps);
        btnMedicalAppointment = (Button) findViewById(R.id.btnMedicalAppointment);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnMedicalAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getBoolean("Login",false)){
                    Intent intent = new Intent(MainMenu.this, MedicalAppointment.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainMenu.this, "Please login to use this feature.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainMenu.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        btnIndoorMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, IndoorNavigation.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getBoolean("Login",false)){
                    Intent intent = new Intent(MainMenu.this, Profile.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainMenu.this, "Please login to use this feature.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainMenu.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        if(preferences.getBoolean("Login",false)){
            btnLogout.setText("Logout");
        }
        else {
            btnLogout.setText("Login");
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if logged in
                if(preferences.getBoolean("Login",false)){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("Login", false);
                    editor.apply();

                    Intent intent = new Intent(MainMenu.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(MainMenu.this, Login.class);
                    startActivity(intent);
                }
            }
        });

    }
}