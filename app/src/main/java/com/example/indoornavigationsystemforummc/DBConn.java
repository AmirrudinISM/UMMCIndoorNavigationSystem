package com.example.indoornavigationsystemforummc;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DBConn {
    private Context context;

    public DBConn(Context context){
        this.context = context;
    }
    public boolean registerPatient(Patient inPatient){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.1.131:8080/UMMCMedicalAppointmentManagementSystem/AndroidServlet";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        Toast.makeText(context, "Data sent successfully!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(context, "Failed to send data: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "register");
                params.put("NRIC", inPatient.getNRIC());
                params.put("email", inPatient.getEmail());
                params.put("password", inPatient.getPassword());
                params.put("firstName", inPatient.getFirstName());
                params.put("lastName", inPatient.getLastName());
                params.put("ethnicity", inPatient.getEthnicity());
                params.put("phoneNumber", "");
                params.put("address", "");
                params.put("height", String.valueOf(inPatient.getHeight()));
                params.put("bloodType", inPatient.getBloodType());
                return params;
            }
        };
        try{
            queue.add(stringRequest);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void login(String email, String password, final ServerCallback callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        Patient inPatient = new Patient();
        String url = "http://192.168.1.131:8080/UMMCMedicalAppointmentManagementSystem/AndroidJsonServlet";

        JSONObject params = new JSONObject();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjectRequest);

    }



}
