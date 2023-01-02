package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText txtNRIC;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtReconfirmPassword;
    private Button btnRegister;
    private TextView lblCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtNRIC = (EditText) findViewById(R.id.txtNRIC);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtReconfirmPassword = (EditText) findViewById(R.id.txtReconfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        lblCancel = (TextView) findViewById(R.id.lblCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nric = txtNRIC.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String reconfirm = txtReconfirmPassword.getText().toString();

                //fields are empty
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please provide email and password",Toast.LENGTH_SHORT).show();
                }
                else{
                    //passwords are equal
                    if (password.contentEquals(reconfirm)){
                        DBController dbController = new DBController(Register.this);
                        //if email is still available
                        if (dbController.createPatient(nric,email,password)){
                            Toast.makeText(getApplicationContext(), "Registration SUCCESSFUL!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Email taken",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Password mismatch",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }
}