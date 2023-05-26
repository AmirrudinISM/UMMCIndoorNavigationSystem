package com.unikl.indoornavigationsystemforummc.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.indoornavigationsystemforummc.R;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import es.situm.gettingstarted.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import es.situm.gettingstarted.common.floorselector.FloorSelectorView;
//import es.situm.gettingstarted.common.GetBuildingCaseUse;
//import es.situm.gettingstarted.common.SampleActivity;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import es.situm.sdk.SitumSdk;
import es.situm.sdk.directions.DirectionsRequest;
import es.situm.sdk.error.Error;
import es.situm.sdk.location.LocationListener;
import es.situm.sdk.location.LocationManager;
import es.situm.sdk.location.LocationRequest;
import es.situm.sdk.location.LocationStatus;
import es.situm.sdk.location.util.CoordinateConverter;
import es.situm.sdk.model.cartography.Building;
import es.situm.sdk.model.cartography.Floor;
import es.situm.sdk.model.cartography.Poi;
import es.situm.sdk.model.cartography.Point;
import es.situm.sdk.model.directions.Route;
import es.situm.sdk.model.directions.RouteSegment;
import es.situm.sdk.model.location.CartesianCoordinate;
import es.situm.sdk.model.location.Coordinate;
import es.situm.sdk.model.location.Location;
import es.situm.sdk.model.navigation.NavigationProgress;
import es.situm.sdk.navigation.NavigationListener;
import es.situm.sdk.navigation.NavigationRequest;
import es.situm.sdk.utils.Handler;

public class IndoorNavigation extends SampleActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 3096;
    private static final int LOCATION_BLUETOOTH_REQUEST_CODE = 2209;
    private static final String TAG = "AnimatePositionActivity";

    private static final int UPDATE_LOCATION_ANIMATION_TIME = 600;
    private static final int MIN_CHANGE_IN_BEARING_TO_ANIMATE_CAMERA = 10;

    private GoogleMap map;
    private Marker marker;
    private FloorSelectorView floorSelectorView;

    private final GetBuildingCaseUse getBuildingCaseUse = new GetBuildingCaseUse();

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location current;
    private String buildingId;
    private Building building;
    private GroundOverlay groundOverlay;

    private boolean markerWithOrientation = false;
    private LatLng lastCameraLatLng;
    private float lastCameraBearing;
    private String lastPositioningFloorId;

    PositionAnimator positionAnimator = new PositionAnimator();

    private ProgressBar progressBar;

    private Marker markerDestination;
    private CoordinateConverter coordinateConverter;
    String currentFloorId;
    Point to;
    Boolean navigation = false;
    private NavigationRequest navigationRequest;
    private List<Polyline> polylines = new ArrayList<>();
    private TextView mNavText;
    private RelativeLayout navigationLayout;
    private GetPoisUseCase getPoisUseCase = new GetPoisUseCase();
    private GetPoiCategoryIconUseCase getPoiCategoryIconUseCase = new GetPoiCategoryIconUseCase();
    Collection<Poi> pointsOfInterests;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_navigation);
        locationManager = SitumSdk.locationManager();

        Intent intent = getIntent();
        if (intent != null)
            if (intent.hasExtra(Intent.EXTRA_TEXT))
                buildingId = intent.getStringExtra(Intent.EXTRA_TEXT);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        setup();

    }

    @Override
    public void onResume(){
        checkPermissions();
        super.onResume();
    }

    @Override
    public void onDestroy(){
        getBuildingCaseUse.cancel();
        SitumSdk.locationManager().removeUpdates(locationListener);
        stopLocation();
        getPoisUseCase.cancel();
        super.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;

        getBuildingCaseUse.get(buildingId, new GetBuildingCaseUse.Callback() {

            @Override
            public void onSuccess(Building build, Floor floor, Bitmap bitmap) {
                progressBar.setVisibility(View.GONE);
                building = build;
                coordinateConverter = new CoordinateConverter(building.getDimensions(),building.getCenter(),building.getRotation());
                // Once we got the building and the googleMap, instance a new FloorSelector
                floorSelectorView = findViewById(R.id.situm_floor_selector);
                floorSelectorView.setFloorSelector(building, map);

                map.setOnMapClickListener(latLng -> {
                    getPoint(latLng);
                    markerDestination = map.addMarker(new MarkerOptions().position(latLng).title("destination"));
                    Log.d("DESTINATION", to.toString());
                });
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(IndoorNavigation.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }



    private void getPoint(LatLng latLng) {

        if (markerDestination != null) {
            markerDestination.remove();
        }

        //creates destinationPoint on the map by converting latLong value
        to = createPoint(latLng);
    }

    private Point createPoint(LatLng latLng) {
        Coordinate coordinate = new Coordinate(latLng.latitude, latLng.longitude);
        CartesianCoordinate cartesianCoordinate= coordinateConverter.toCartesianCoordinate(coordinate);
        Point point = new Point(building.getIdentifier(), floorSelectorView.getSelectedFloorId(),coordinate,cartesianCoordinate );
        return point;
    }

    private void setup(){
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.GONE);
        FloatingActionButton startButton = findViewById(R.id.start_button);
        mNavText = (TextView) findViewById(R.id.tv_indication);
        navigationLayout = (RelativeLayout) findViewById(R.id.navigation_layout);

        View.OnClickListener buttonListenerLocation = view -> {
            Log.d(IndoorNavigation.class.getSimpleName(), "startButton clicked");

            if(locationManager.isRunning()){
                removePolylines();
                navigation = false;
                progressBar.setVisibility(ProgressBar.GONE);
                floorSelectorView.reset();
                lastPositioningFloorId = null;
                stopLocation();
                stopNavigation();
                SitumSdk.locationManager().removeUpdates(locationListener);
            }else {
                navigation = true;
                markerWithOrientation = false;
                floorSelectorView.setFocusUserMarker(true);
                startLocation();
            }
        };

        startButton.setOnClickListener(buttonListenerLocation);
    }

    private void startLocation(){
        if(locationManager.isRunning()){
            return;
        }

        progressBar.setVisibility(ProgressBar.VISIBLE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                current = location;

                // Save the first floor
                currentFloorId = location.getFloorIdentifier();
                if(!currentFloorId.equals(lastPositioningFloorId)){
                    lastPositioningFloorId = currentFloorId;
                    floorSelectorView.updatePositioningFloor(currentFloorId);
                }

                // If we are not inside the floor selected, the marker and groundOverlay are hidden
                displayPositioning(location);

                progressBar.setVisibility(ProgressBar.GONE);

                if(to != null){

                    //if we are currently navigating,
                    if(navigation) {
                        getRoute();
                        //Informs NavigationManager object the change of the user's location
                        SitumSdk.navigationManager().updateWithLocation(current);
                    }

                    navigationLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStatusChanged(@NonNull LocationStatus locationStatus) {
                Log.d(TAG, "onStatusChanged(): " + locationStatus);
            }

            @Override
            public void onError(@NonNull Error error) {
                Log.e(TAG, "onError(): " + error.getMessage());
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(IndoorNavigation.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }

        };

        LocationRequest locationRequest = new LocationRequest.Builder()
                .buildingIdentifier(buildingId)
                .useDeadReckoning(true)
                .build();

        SitumSdk.locationManager().requestLocationUpdates(locationRequest, locationListener);

    }

    /**
     * Detects if the user is inside the selected floor or not, then show/hides the marker and ground overlay
     *
     * @param location Location
     */
    private void displayPositioning(Location location){
        if(!floorSelectorView.focusUserMarker() &&
                !location.getFloorIdentifier().equals(floorSelectorView.getSelectedFloorId())) {
            positionAnimator.clear();
            if (groundOverlay != null) {
                groundOverlay.remove();
                groundOverlay = null;
            }
            if(marker != null){
                marker.remove();
                marker = null;
            }
        }else{
            LatLng latLng = new LatLng(location.getCoordinate().getLatitude(),
                    location.getCoordinate().getLongitude());
            if (marker == null){
                initializeMarker(latLng);
            }
            if (groundOverlay == null) {
                initializeGroundOverlay();
            }

            updateMarkerIcon();
            positionAnimator.animate(marker, groundOverlay, location);
            centerInUser(location);
        }
    }

    void getRoute(){
        DirectionsRequest directionsRequest = new DirectionsRequest.Builder()
                .from(current.getPosition(), null)
                .to(to)
                .build();


        SitumSdk.directionsManager().requestDirections(directionsRequest, new Handler<Route>() {
            @Override
            public void onSuccess(Route route) {
                removePolylines();
                drawRoute(route);
                //centerCamera(route);

                navigationRequest = new NavigationRequest.Builder()
                        .route(route)
                        .distanceToGoalThreshold(3d)
                        .outsideRouteThreshold(50d)
                        .build();

                startNavigation();


            }
            @Override
            public void onFailure(Error error) {

            }
        });
    }

    private void drawRoute(Route route) {
        for (RouteSegment segment : route.getSegments()) {
            //For each segment you must draw a polyline
            //Add an if to filter and draw only the current selected floor
            List<LatLng> latLngs = new ArrayList<>();
            for (Point point : segment.getPoints()) {
                if(point.getFloorIdentifier().equals(floorSelectorView.getSelectedFloorId())) {
                    latLngs.add(new LatLng(point.getCoordinate().getLatitude(), point.getCoordinate().getLongitude()));
                }
            }
            List<PatternItem> pattern = Arrays.asList(
                    new Dot(), new Gap(10), new Dot(), new Gap(10));
            PolylineOptions polyLineOptions = new PolylineOptions()
                    .color(0xd94367ff)
                    .jointType(JointType.ROUND)
                    .pattern(pattern)
                    .width(24)
                    .zIndex(3)
                    .addAll(latLngs);
            Polyline polyline = this.map.addPolyline(polyLineOptions);
            polylines.add(polyline);
        }
    }

    void startNavigation(){
        Log.d(TAG, "startNavigation: ");
        SitumSdk.navigationManager().requestNavigationUpdates(navigationRequest, new NavigationListener() {
            @Override
            public void onDestinationReached() {
                Log.d(TAG, "onDestinationReached: ");
                mNavText.setText("Arrived");
                removePolylines();
            }

            @Override
            public void onProgress(NavigationProgress navigationProgress) {
                Context context = getApplicationContext();
                Log.d(TAG, "onProgress: " + navigationProgress.getCurrentIndication().toText(context));
                mNavText.setText(navigationProgress.getCurrentIndication().toText(context));
            }

            @Override
            public void onUserOutsideRoute() {
                Log.d(TAG, "onUserOutsideRoute: ");
                mNavText.setText("Outside of the route");
            }
        });
    }

    private void stopLocation(){
        if (!locationManager.isRunning()){
            return;
        }
        locationManager.removeUpdates(locationListener);
        current = null;

        positionAnimator.clear();

        if (groundOverlay != null) {
            groundOverlay.remove();
            groundOverlay = null;
        }
        if(marker != null){
            marker.remove();
            marker = null;
        }
        //removes polylines
        removePolylines();
    }
    void stopNavigation(){
        removePolylines();
        //to = null;
        navigationRequest = null;
        navigationLayout.setVisibility(View.GONE);
        navigation = false;
        mNavText.setText("Navigation");
    }

    private void removePolylines() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear();
    }

    private void initializeMarker(LatLng latLng) {
        Bitmap bitmapArrow = BitmapFactory.decodeResource(getResources(), R.drawable.position);
        Bitmap arrowScaled = Bitmap.createScaledBitmap(bitmapArrow, bitmapArrow.getWidth() / 4,bitmapArrow.getHeight() / 4, false);

        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .zIndex(100)
                .flat(true)
                .anchor(0.5f,0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(arrowScaled)));
    }

    private void initializeGroundOverlay() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap bitmapPosbg = BitmapFactory.decodeResource(getResources(), R.drawable.situm_posbg, opts);
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(bitmapPosbg))
                .anchor(0.5f, 0.5f)
                .position(new LatLng(0, 0), 2)
                .zIndex(2);
        groundOverlay = map.addGroundOverlay(groundOverlayOptions);
    }

    private void centerInUser(Location location) {
        float tilt = 40;
        float bearing = (location.hasBearing()) && location.isIndoor() ? (float) (location.getBearing().degrees()) : map.getCameraPosition().bearing;

        LatLng latLng = new LatLng(location.getCoordinate().getLatitude(), location.getCoordinate().getLongitude());

        //Skip if no change in location and little bearing change
        boolean skipAnimation = lastCameraLatLng != null && lastCameraLatLng.equals(latLng)
                && (Math.abs(bearing - lastCameraBearing)) < MIN_CHANGE_IN_BEARING_TO_ANIMATE_CAMERA;
        lastCameraLatLng = latLng;
        lastCameraBearing = bearing;
        if (!skipAnimation) {
            CameraPosition cameraPosition = new CameraPosition.Builder(map.getCameraPosition())
                    .target(latLng)
                    .bearing(bearing)
                    .tilt(tilt)
                    .zoom(22)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), UPDATE_LOCATION_ANIMATION_TIME, null);
        }

    }

    private void updateMarkerIcon() {
        boolean newLocationHasOrientation = (current.hasBearing()) && current.isIndoor();
        if (markerWithOrientation == newLocationHasOrientation) {
            return;
        }
        markerWithOrientation = newLocationHasOrientation;

        BitmapDescriptor bitmapDescriptor;
        Bitmap bitmapScaled;
        if(markerWithOrientation){
            Bitmap bitmapArrow = BitmapFactory.decodeResource(getResources(), R.drawable.pose);
            bitmapScaled = Bitmap.createScaledBitmap(bitmapArrow, bitmapArrow.getWidth() / 4,bitmapArrow.getHeight() / 4, false);
        } else {
            Bitmap bitmapCircle = BitmapFactory.decodeResource(getResources(), R.drawable.position);
            bitmapScaled = Bitmap.createScaledBitmap(bitmapCircle, bitmapCircle.getWidth() / 4,bitmapCircle.getHeight() / 4, false);
        }
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmapScaled);
        marker.setIcon(bitmapDescriptor);
    }


    /**
     * Getting the permissions we need about localization.
     *
     */
    private void requestPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(IndoorNavigation.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                    LOCATION_BLUETOOTH_REQUEST_CODE);
        }else {
            ActivityCompat.requestPermissions(IndoorNavigation.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST_CODE);
        }

    }

    /**
     * Checking if we have the requested permissions
     *
     */
    private void checkPermissions() {
        boolean hasFineLocationPermission = ContextCompat.checkSelfPermission(IndoorNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            boolean hasBluetoothScanPermission = ContextCompat.checkSelfPermission(IndoorNavigation.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
            boolean hasBluetoothConnectPermission = ContextCompat.checkSelfPermission(IndoorNavigation.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;

            if(!hasBluetoothConnectPermission || !hasBluetoothScanPermission || !hasFineLocationPermission){
                if (ActivityCompat.shouldShowRequestPermissionRationale(IndoorNavigation.this, Manifest.permission.BLUETOOTH_SCAN)
                        || ActivityCompat.shouldShowRequestPermissionRationale(IndoorNavigation.this, Manifest.permission.BLUETOOTH_CONNECT)
                        || ActivityCompat.shouldShowRequestPermissionRationale(IndoorNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Snackbar.make(findViewById(android.R.id.content),
                                    "Need bluetooth or location permission to enable service",
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction("Open", view -> requestPermissions()).show();
                } else {
                    requestPermissions();
                }
            }
        }else {

            if(!hasFineLocationPermission){
                if (ActivityCompat.shouldShowRequestPermissionRationale(IndoorNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                    Snackbar.make(findViewById(android.R.id.content),
                                    "Need location permission to enable service",
                                    Snackbar.LENGTH_INDEFINITE)
                            .setAction("Open", view -> requestPermissions()).show();
                } else {
                    requestPermissions();
                }
            }
        }
    }

    /**
     *
     * REQUESTCODE = 1 : NO PERMISSIONS
     *
     */

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finishActivity(requestCode);
            }
        }else if(requestCode == LOCATION_BLUETOOTH_REQUEST_CODE){
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                finishActivity(requestCode);
            }
            if (!(grantResults.length > 1
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                finishActivity(requestCode);
            }
        }

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        to = createPoint(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
        return false;
    }
}
