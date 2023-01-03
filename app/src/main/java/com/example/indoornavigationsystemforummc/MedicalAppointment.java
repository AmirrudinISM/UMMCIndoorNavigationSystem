package com.example.indoornavigationsystemforummc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MedicalAppointment extends AppCompatActivity {
    ListView listView;
    FloatingActionButton btnCreateNewAppointment;
    FloatingActionButton btnReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_appointment);

        DBController db = new DBController(this);
        db.refreshAppointments();

        listView = findViewById(R.id.appointmentList);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);

        CustomListAdapter adapter = new CustomListAdapter(db.getAllAppointments(preferences.getString("PatientID", "")), this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                TextView lblID = (TextView)view.findViewById(R.id.lblAppointmentID);
                                                String id = lblID.getText().toString();

                                                //passes the AppointmentID & status of the Appointment status to the ViewAppointment activity
                                                Intent intent = new Intent(MedicalAppointment.this, ViewAppointment.class);
                                                intent.putExtra("appointmentID",id);
                                                startActivity(intent);
                                            }
                                        });
                                        btnCreateNewAppointment = (FloatingActionButton) findViewById(R.id.btnCreateAppointment);
        btnCreateNewAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get all appointments for this patient
                ArrayList<Appointment> allCurrentAppointments = db.getAllAppointments(preferences.getString("PatientID", ""));
                int pendingOrConfirmed = 0;
                for (Appointment appointment:allCurrentAppointments) {
                    if(appointment.getAppointmentStatus().equals("PENDING") || appointment.getAppointmentStatus().equals("CONFIRMED")){
                        pendingOrConfirmed++;
                    }
                }

                if(pendingOrConfirmed > 0){
                    Toast.makeText(MedicalAppointment.this, "No new appointments are allowed to be made if there are any PENDING OR CONFIRMED appointments.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(MedicalAppointment.this, CreateAppointment.class);
                    startActivity(intent);
                }

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