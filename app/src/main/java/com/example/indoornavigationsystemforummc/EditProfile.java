package com.example.indoornavigationsystemforummc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class EditProfile extends AppCompatActivity {
    EditText txtFirstName;
    EditText txtLastName;
    Spinner spnrEthnicity;
    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtHeight;
    Spinner spnrBloodType;
    Button btnSaveChanges;
    TextView lblCancel;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
        db = new DBController(EditProfile.this);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        spnrEthnicity = (Spinner) findViewById(R.id.spnrEthnicity);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterEth = ArrayAdapter.createFromResource(this, R.array.ethnicities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterEth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnrEthnicity.setAdapter(adapterEth);

        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtHeight = (EditText) findViewById(R.id.txtHeight);

        spnrBloodType = (Spinner) findViewById(R.id.spnrBloodType);
        ArrayAdapter<CharSequence> adapterBld = ArrayAdapter.createFromResource(this, R.array.bloodTypes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBld.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spnrBloodType.setAdapter(adapterBld);


        Cursor result = db.findPatient(preferences.getString("Email",""));
        while(result.moveToNext()){
            txtFirstName.setText(result.getString(3));
            txtLastName.setText(result.getString(4));
            txtPhoneNumber.setText(result.getString(7));
            txtAddress.setText(result.getString(8));
            txtHeight.setText(result.getString(9));
        }

        btnSaveChanges = (Button) findViewById(R.id.btnEditProfile);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spnrEthnicity.getSelectedItemPosition() == 0 || spnrBloodType.getSelectedItemPosition() == 0){
                    Toast.makeText(EditProfile.this, "Please select ethnicity and blood type!", Toast.LENGTH_LONG).show();
                }
                else{
                    String patientID = preferences.getString("PatientID","");
                    String firstName = txtFirstName.getText().toString();
                    String lastName = txtLastName.getText().toString();
                    String ethnicity = spnrEthnicity.getSelectedItem().toString();
                    String phoneNumber = txtPhoneNumber.getText().toString();
                    String address = txtAddress.getText().toString();
                    String heightString = txtHeight.getText().toString();
                    float height = Float.valueOf(heightString);
                    String bloodType = spnrBloodType.getSelectedItem().toString();

                    if(db.updatePatient(patientID,firstName,lastName,ethnicity,phoneNumber,address,height,bloodType)){
                        Toast.makeText(EditProfile.this, "Update Successful!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(EditProfile.this, Profile.class);
                        startActivity(intent);
                    }

                }
            }
        });

        lblCancel = (TextView) findViewById(R.id.lblCancel);
        lblCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, Profile.class);
                startActivity(intent);
            }
        });

    }
}