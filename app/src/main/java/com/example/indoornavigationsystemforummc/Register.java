package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private EditText txtNRIC;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtReconfirmPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private Spinner spnrEthnicity;
    private Spinner spnrBloodType;

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

        btnRegister = (Button) findViewById(R.id.btnRegister);
        lblCancel = (TextView) findViewById(R.id.lblCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //account info

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
                }
                else{
                    //passwords are equal
                    if (password.contentEquals(reconfirm)){
                        DBConn dbConn = new DBConn(Register.this);
                        //if email is still available
                        Patient inPatient = new Patient(nric,email,password, firstName, lastName, ethnicity, bloodType);
                        if (dbConn.registerPatient(inPatient)){

                            Intent intent = new Intent(Register.this, Login.class);
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
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });




    }
}