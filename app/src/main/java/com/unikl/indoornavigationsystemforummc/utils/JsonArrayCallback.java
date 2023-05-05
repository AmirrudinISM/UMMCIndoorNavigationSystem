package com.unikl.indoornavigationsystemforummc.utils;

import org.json.JSONArray;
import org.json.JSONException;

public interface JsonArrayCallback {
    void onSuccess(JSONArray response) throws JSONException;
}
