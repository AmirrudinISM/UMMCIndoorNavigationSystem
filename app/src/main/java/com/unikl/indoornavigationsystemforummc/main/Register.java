package com.unikl.indoornavigationsystemforummc.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indoornavigationsystemforummc.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.unikl.indoornavigationsystemforummc.utils.DBConn;
import com.unikl.indoornavigationsystemforummc.utils.StringCallback;

import org.json.JSONException;

import java.util.ArrayList;

public class Register extends AppCompatActivity {
    private EditText txtNRIC;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtReconfirmPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private Spinner spnrEthnicity;
    private Spinner spnrBloodType;
    private CheckBox chckStroke,
            chckDepression,
            chckDiabetes1,
            chckDiabetes2,
            chckArthritis,
            chckOsteoporosis,
            chckAsthma,
            chckPulmonaryDisease,
            chckKidneyDisease,
            chckHeartDisease;


    private Button btnRegister;
    private TextView lblCancel;
    ArrayList<String> chronicIllnesses;
    String allChronicIllnesses;
    LinearProgressIndicator progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtNRIC = (EditText) findViewById(R.id.txtNRIC);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtReconfirmPassword = (EditText) findViewById(R.id.txtReconfirmPassword);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        spnrEthnicity = (Spinner) findViewById(R.id.spnrEthnicity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterEth = ArrayAdapter.createFromResource(this, R.array.ethnicities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterEth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnrEthnicity.setAdapter(adapterEth);

        spnrBloodType = (Spinner) findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> adapterBld = ArrayAdapter.createFromResource(this, R.array.bloodTypes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBld.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnrBloodType.setAdapter(adapterBld);

        chckStroke = findViewById(R.id.chckStroke);
        chckDepression = findViewById(R.id.chckDepression);
        chckDiabetes1 = findViewById(R.id.chckDiabetes1);
        chckDiabetes2 = findViewById(R.id.chckDiabetes2);
        chckArthritis = findViewById(R.id.chckArthritis);
        chckOsteoporosis = findViewById(R.id.chckOsteoporosis);
        chckAsthma = findViewById(R.id.chckAsthma);
        chckPulmonaryDisease = findViewById(R.id.chckPulmonaryDisease);
        chckKidneyDisease = findViewById(R.id.chckKidneyDisease);
        chckHeartDisease = findViewById(R.id.chckHeartDisease);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        lblCancel = (TextView) findViewById(R.id.lblCancel);

        //all checkboxes
        chronicIllnesses = new ArrayList<>();

        progBar = findViewById(R.id.regProgBar);
        progBar.setVisibility(View.GONE);
        chckStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckStroke.isChecked()){
                    chronicIllnesses.add("Stroke");
                }
                else{
                    chronicIllnesses.remove("Stroke");
                }
            }
        });

        chckDepression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckDepression.isChecked()){
                    chronicIllnesses.add("Depression");
                }
                else{
                    chronicIllnesses.remove("Depression");
                }
            }
        });

        chckDiabetes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckDiabetes1.isChecked()){
                    chronicIllnesses.add("Diabetes 1");
                }
                else{
                    chronicIllnesses.remove("Diabetes 1");
                }
            }
        });

        chckDiabetes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckDiabetes2.isChecked()){
                    chronicIllnesses.add("Diabetes 2");
                }
                else{
                    chronicIllnesses.remove("Diabetes 2");
                }
            }
        });

        chckArthritis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckArthritis.isChecked()){
                    chronicIllnesses.add("Arthritis");
                }
                else{
                    chronicIllnesses.remove("Arthritis");
                }
            }
        });

        chckOsteoporosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckOsteoporosis.isChecked()){
                    chronicIllnesses.add("Osteoporosis");
                }
                else{
                    chronicIllnesses.remove("Osteoporosis");
                }
            }
        });

        chckAsthma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckAsthma.isChecked()){
                    chronicIllnesses.add("Asthma");
                }
                else{
                    chronicIllnesses.remove("Asthma");
                }
            }
        });

        chckPulmonaryDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckPulmonaryDisease.isChecked()){
                    chronicIllnesses.add("Chronic obstructive pulmonary disease (COPD)");
                }
                else{
                    chronicIllnesses.remove("Chronic obstructive pulmonary disease (COPD)");
                }
            }
        });

        chckKidneyDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckKidneyDisease.isChecked()){
                    chronicIllnesses.add("Chronic Kidney Disease");
                }
                else{
                    chronicIllnesses.remove("Chronic Kidney Disease");
                }
            }
        });

        chckHeartDisease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chckHeartDisease.isChecked()){
                    chronicIllnesses.add("Heart disease");
                }
                else{
                    chronicIllnesses.remove("Heart disease");
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progBar.setVisibility(View.VISIBLE);
                //account info
                if(chronicIllnesses.isEmpty()){
                    allChronicIllnesses = "";
                }
                else{
                    allChronicIllnesses = concatenateChronicIllnessesString();
                }

                String nric = txtNRIC.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String reconfirm = txtReconfirmPassword.getText().toString();
                //personal info
                String firstName = txtFirstName.getText().toString();
                String lastName = txtLastName.getText().toString();
                String ethnicity = spnrEthnicity.getSelectedItem().toString();
                String bloodType = spnrBloodType.getSelectedItem().toString();

                //fields are empty
                if(nric.isEmpty() || email.isEmpty() || password.isEmpty() || reconfirm.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || spnrEthnicity.getSelectedItemPosition() == 0 || spnrBloodType.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please provide all required information",Toast.LENGTH_SHORT).show();
                    progBar.setVisibility(View.GONE);
                }
                else{
                    //passwords are equal
                    if (password.contentEquals(reconfirm)){
                        DBConn dbConn = new DBConn(Register.this);
                        //if email is still available
                        Patient inPatient = new Patient(nric,email,password, firstName, lastName, ethnicity, bloodType, allChronicIllnesses);
                        if (dbConn.registerPatient(inPatient, new StringCallback() {
                            @Override
                            public void onSuccess(String response) throws JSONException {
                                progBar.setVisibility(View.GONE);
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                progBar.setVisibility(View.GONE);
                            }
                        })){


                        }
                        else{
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Email taken",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Password mismatch",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });




    }

    private String concatenateChronicIllnessesString() {
        String res = "";
        for (int i = 0; i < chronicIllnesses.size(); i++){
            if(i == (chronicIllnesses.size() - 1)){
                res += chronicIllnesses.get(i);
            }
            else{
                res += chronicIllnesses.get(i) + ", ";
            }

        }
        return res;
    }
}