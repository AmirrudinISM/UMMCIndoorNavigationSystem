package com.unikl.indoornavigationsystemforummc.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JsonObjectCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onFailure();
}
