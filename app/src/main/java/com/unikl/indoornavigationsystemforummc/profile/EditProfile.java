package com.unikl.indoornavigationsystemforummc.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.unikl.indoornavigationsystemforummc.utils.DBConn;
import com.example.indoornavigationsystemforummc.R;
import com.unikl.indoornavigationsystemforummc.utils.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    EditText txtPhoneNumber;
    EditText txtAddress;
    EditText txtHeight;

    Button btnSaveChanges;
    TextView lblCancel;
   LinearProgressIndicator editProgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences("UMMCApp",MODE_PRIVATE);


        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtHeight = (EditText) findViewById(R.id.txtHeight);

        editProgBar = findViewById(R.id.editProgBar);
        editProgBar.setVisibility(View.VISIBLE);

        DBConn dbConn = new DBConn(EditProfile.this);
        dbConn.viewPatient(preferences.getString("PatientID", ""), new StringCallback() {
            @Override
            public void onSuccess(String response) throws JSONException {
                editProgBar.setVisibility(View.GONE);
                JSONObject profileJSON = new JSONObject(response);
                txtPhoneNumber.setText(profileJSON.getString("phoneNumber"));
                txtAddress.setText(profileJSON.getString("address"));
                txtHeight.setText(profileJSON.getString("height"));
            }

            @Override
            public void onFailure() {
                editProgBar.setVisibility(View.GONE);
                Toast.makeText(EditProfile.this, "ERROR CONNECTING",Toast.LENGTH_SHORT ).show();
            }
        });

        btnSaveChanges = (Button) findViewById(R.id.btnEditProfile);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProgBar.setVisibility(View.VISIBLE);
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
                        dbConn.updateProfile(patientID, phoneNumber, address, height, new StringCallback() {
                            @Override
                            public void onSuccess(String response) throws JSONException {
                                editProgBar.setVisibility(View.GONE);
                                Toast.makeText(EditProfile.this, "Update Successful!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditProfile.this, Profile.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                editProgBar.setVisibility(View.GONE);
                                Toast.makeText(EditProfile.this, "Update Failed!", Toast.LENGTH_LONG).show();
                            }
                        });
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