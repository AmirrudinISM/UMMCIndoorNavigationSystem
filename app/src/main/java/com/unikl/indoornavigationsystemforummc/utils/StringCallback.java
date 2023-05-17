package com.unikl.indoornavigationsystemforummc.utils;

import org.json.JSONException;

public interface StringCallback {
    void onSuccess(String response) throws JSONException;
    void onFailure();
}
