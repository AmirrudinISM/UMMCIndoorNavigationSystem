package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView lblRegister;
    private TextView lblReturnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("UMMCApp", Context.MODE_PRIVATE);

        txtEmail = (EditText) findViewById(R.id.emailInput);
        txtPassword = (EditText) findViewById(R.id.passwordInput);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        lblRegister = (TextView) findViewById(R.id.lblRegister);
        lblReturnHome = (TextView) findViewById(R.id.lblReturnToHome);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                if(!email.isEmpty()){
                    Patient inPatient = new Patient();
                    DBConn dbConn = new DBConn(Login.this);
                    try {
                        dbConn.login(email, password, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) throws JSONException {
                                inPatient.setPatientID(result.getString("patientID"));
                                inPatient.setEmail(result.getString("email"));
                                inPatient.setPassword(result.getString("password"));
                                inPatient.setFirstName(result.getString("firstName"));
                                Toast.makeText(Login.this, "Patient ID: " + inPatient.getPatientID(), Toast.LENGTH_LONG).show();

                                if(inPatient.getPatientID() == null){
                                    Toast.makeText(Login.this, "User with this email doesn't exist! ", Toast.LENGTH_LONG).show();
                                }else{

                                    if(Objects.equals(inPatient.getPassword(), password)){

                                        SharedPreferences.Editor editor = preferences.edit();

                                        editor.putString("PatientID", inPatient.getPatientID() );
                                        editor.putString("Email",inPatient.getEmail() );
                                        editor.putString("FirstName",inPatient.getFirstName() );
                                        editor.putBoolean("Login",true);


                                        editor.apply();

                                        Intent intent = new Intent(Login.this, MainMenu.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
                else{
                    Toast.makeText(Login.this, "Please provide email!", Toast.LENGTH_LONG).show();
                }
            }
        });

        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        lblReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,MainMenu.class);
                startActivity(intent);
            }
        });
    }
}