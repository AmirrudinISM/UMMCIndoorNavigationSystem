package com.unikl.indoornavigationsystemforummc.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unikl.indoornavigationsystemforummc.main.Patient;
import com.unikl.indoornavigationsystemforummc.medicalappointment.Appointment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DBConn {
    private Context context;
    private final String url = "http://192.168.1.131:8080/UMMCMedicalAppointmentManagementSystem/AndroidServlet";
    public DBConn(Context context){
        this.context = context;
    }
    public boolean registerPatient(Patient inPatient){
        RequestQueue queue = Volley.newRequestQueue(context);

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
                params.put("chronicIllnesses", inPatient.getChronicIllnesses());
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

    public void login(String email, String password, final JsonObjectCallback callback) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        Patient inPatient = new Patient();
        String url = "http://192.168.1.131:8080/UMMCMedicalAppointmentManagementSystem/AndroidJsonServlet";

        JSONObject params = new JSONObject();
        params.put("action", "login");
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
                callback.onFailure();
            }
        });

        queue.add(jsonObjectRequest);

    }

    public void createAppointment(Appointment inAppointment){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle response
                        Toast.makeText(context, "Appointment creation is successful",Toast.LENGTH_SHORT).show();
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
                LocalDateTime currentDateTime = LocalDateTime.now();

                // Format the date and time as a string
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTimeString = currentDateTime.format(formatter);

                Map<String, String> params = new HashMap<>();
                params.put("action", "createAppointment");
                params.put("createdTime", dateTimeString);
                params.put("symptoms", inAppointment.getSymptoms());
                params.put("otherDescription", inAppointment.getOtherDescription());
                params.put("appointmentDate", inAppointment.getAppointmentDate());
                params.put("appointmentTime", inAppointment.getAppointmentTime());
                params.put("appointmentStatus", "PENDING");
                params.put("patientID", inAppointment.getPatientID());



                return params;
            }
        };
        try{
            queue.add(stringRequest);
        }
        catch (Exception e){
        }
    }

    public void getAppointments(String patientID , StringCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        callback.onFailure();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "getAppointments");
                params.put("patientID", patientID);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void viewAppointment(String appointmentID, StringCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "viewAppointment");
                params.put("appointmentID", appointmentID);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void cancelAppointment(String appointmentID, StringCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "cancelAppointment");
                params.put("appointmentID", appointmentID);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void viewPatient(String patientID, StringCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "viewProfile");
                params.put("patientID", patientID);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void updateProfile(String patientID, String phoneNumber, String address, float height, StringCallback callback){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            Log.d("ERROR", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error", error.getMessage());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "updateProfile");
                params.put("patientID", patientID);
                params.put("phoneNumber", phoneNumber);
                params.put("address", address);
                params.put("height", String.valueOf(height));
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
