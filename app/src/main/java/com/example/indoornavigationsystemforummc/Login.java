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
                    DBController dbController = new DBController(Login.this);
                    Cursor result = dbController.findPatient(email);

                    if(result.getCount()==0){
                        Toast.makeText(Login.this, "User with this email doesn't exist! ", Toast.LENGTH_LONG).show();
                    }else{
                        while(result.moveToNext()){
                            if(result.getString(2).equals(password)){

                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("PatientID", result.getString(0) );
                                editor.putString("Email",result.getString(1) );
                                editor.putString("FirstName",result.getString(3) );
                                editor.putBoolean("Login",true);


                                editor.apply();

                                Intent intent = new Intent(Login.this, MainMenu.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        }
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