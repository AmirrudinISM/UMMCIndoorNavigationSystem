package com.unikl.indoornavigationsystemforummc.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unikl.indoornavigationsystemforummc.utils.DBController;
import com.example.indoornavigationsystemforummc.R;

public class EditProfile extends AppCompatActivity {

    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtHeight;

    Button btnSaveChanges;
    TextView lblCancel;
    DBController db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);
        db = new DBController(EditProfile.this);

        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtHeight = (EditText) findViewById(R.id.txtHeight);

        Cursor result = db.findPatient(preferences.getString("Email",""));
        while(result.moveToNext()){
            txtPhoneNumber.setText(result.getString(7));
            txtAddress.setText(result.getString(8));
            txtHeight.setText(result.getString(9));
        }

        btnSaveChanges = (Button) findViewById(R.id.btnEditProfile);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String patientID = preferences.getString("PatientID","");
                String phoneNumber = txtPhoneNumber.getText().toString();
                String address = txtAddress.getText().toString();
                String heightString = txtHeight.getText().toString();
                float height = Float.valueOf(heightString);

                if(phoneNumber.isEmpty() || address.isEmpty()){
                    Toast.makeText(EditProfile.this, "Please enter all fields!", Toast.LENGTH_LONG).show();
                }
                else{
                    if(height < 20 || height > 300){
                        Toast.makeText(EditProfile.this, "Please enter value between 20cm to 300cm", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(db.updatePatient(patientID,phoneNumber,address,height)){
                            Toast.makeText(EditProfile.this, "Update Successful!", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(EditProfile.this, Profile.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(EditProfile.this, "Update Failed!", Toast.LENGTH_LONG).show();
                        }
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