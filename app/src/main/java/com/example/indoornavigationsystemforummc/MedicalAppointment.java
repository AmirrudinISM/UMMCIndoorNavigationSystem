package com.example.indoornavigationsystemforummc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MedicalAppointment extends AppCompatActivity {
    ListView listView;
    FloatingActionButton btnCreateNewAppointment;
    FloatingActionButton btnReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment);

        DBController db = new DBController(this);

        listView = findViewById(R.id.appointmentList);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);

        CustomListAdapter adapter = new CustomListAdapter(db.getAllAppointments(preferences.getString("PatientID", "")), this);
        listView.setAdapter(adapter);

        btnCreateNewAppointment = (FloatingActionButton) findViewById(R.id.btnCreateAppointment);
        btnCreateNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicalAppointment.this, CreateAppointment.class);
                startActivity(intent);
            }
        });

        btnReturnHome = (FloatingActionButton) findViewById(R.id.btnRetunHome);
        btnReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicalAppointment.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }
}