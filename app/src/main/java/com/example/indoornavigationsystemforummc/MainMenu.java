package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {
    TextView status;
    Button btnMedicalAppointment;
    Button btnIndoorMaps;
    Button btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
        status = (TextView) findViewById(R.id.statusDisplay2);
        status.setText("Welcome, " + preferences.getString("FirstName","") + "!");

        btnProfile = (Button) findViewById(R.id.btnProfile);
        btnIndoorMaps = (Button) findViewById(R.id.btnIndoorMaps);
        btnMedicalAppointment = (Button) findViewById(R.id.btnMedicalAppointment);


        btnMedicalAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, MedicalAppointment.class);
                startActivity(intent);
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
                Intent intent = new Intent(MainMenu.this, Profile.class);
                startActivity(intent);
            }
        });

    }
}