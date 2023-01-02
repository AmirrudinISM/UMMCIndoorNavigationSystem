package com.example.indoornavigationsystemforummc;



import static com.azure.android.maps.control.options.StyleOptions.language;
import static com.azure.android.maps.control.options.StyleOptions.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;


public class IndoorNavigation extends AppCompatActivity {
    static {
        AzureMaps.setSubscriptionKey("RoFmB7CvqyzDBjvGj05D5jtuGVpfh8sAHRQ151AIp5U");
    }

    MapControl mapControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_navigation);
        mapControl = findViewById(R.id.mapcontrol);
        mapControl.onCreate(savedInstanceState);
        mapControl.onReady(map -> {
            map.setStyle(
                    language("fr-FR"),
                    view("Auto")
            );
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapControl.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mapControl.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapControl.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapControl.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapControl.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapControl.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapControl.onSaveInstanceState(outState);
    }
}