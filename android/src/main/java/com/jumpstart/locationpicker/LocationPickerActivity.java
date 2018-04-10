package com.jumpstart.locationpicker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationPickerActivity extends AppCompatActivity implements OnMapReadyCallback {

    Boolean enableBackButton = true;
    Context context = this;
    Activity activity = this;
    String actionBarDefColor = "#F44336";
    String statusBarDefColor = "#B71C1C";
    String navBarDefColor = "#F44336";
    String titleDefColor = "#FFFFFF";
    Boolean closeIfLocationDenied = false;
    GoogleMap mMap;
    FusedLocationProviderClient mFusedLocationClient;
    Boolean useGeoCoder = true;
    LatLng selectedLocation;
    Boolean enableMyLocation = true;
    Geocoder geocoder;

    static final int DEFAULT_ZOOM = 14;

    static final int LOCATION_PERMISSION_ID = 10;

    Marker mapCenterMarker;

    Map<String, Object> args;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        geocoder = new Geocoder(context, Locale.getDefault());
        args = (Map<String, Object>) LocationPickerPlugin.call.arguments;

        selectedLocation = new LatLng((double)args.get("initialLat"), (double)args.get("initialLong"));
        enableMyLocation = (Boolean) args.get("enableMyLocation");
        useGeoCoder = (Boolean) args.get("useGeoCoder");

        closeIfLocationDenied = (Boolean) args.get("closeIfLocationDenied");
        if(enableBackButton)
            setBackButton((String)args.get("androidTitleAndBackButtonColor"));

        setAppBarTitle((String)args.get("titleText"), (String)args.get("androidTitleAndBackButtonColor"));
        setBarColors((String)args.get("androidStatusBarColor"),(String)args.get("androidAppBarColor"),(String)args.get("androidNavBarColor"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setFab((String)args.get("androidSelectButtonColor"));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        if(checkOrRequestPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_ID))
        {
            handleLocationEnabled();
        }
        else
        {
            mapCenterMarker = mMap.addMarker(new MarkerOptions().position(selectedLocation));
        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(mMap!= null) {
                    selectedLocation = mMap.getCameraPosition().target;

                    if(mapCenterMarker != null)
                        mapCenterMarker.setPosition(selectedLocation);
                }
            }
        });
    }

    void handleLocationEnabled()
    {
        if(enableMyLocation)
            mMap.setMyLocationEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    selectedLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(selectedLocation)
                            .zoom(DEFAULT_ZOOM)
                            .bearing(0)
                            .tilt(40)
                            .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mapCenterMarker = mMap.addMarker(new MarkerOptions().position(selectedLocation));
                }
            }
        });
    }

    //UI METHODS
    void setBackButton(@Nullable  String color)
    {
        if(color == null)
            color = titleDefColor;

        Drawable backArrow = ContextCompat.getDrawable(context, R.drawable.arrow_back);
        if(backArrow != null)
        {
            backArrow.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        }
        else
        {
            Log.e("BACK BUTTON", "error loading Arrow Drawable ");
        }

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else
        {
            Log.e("ACTIONBAR BACK BUTTON", "ACTIONBAR IS NULL");
        }
    }

    void setAppBarTitle(String appBarTitle, @Nullable String titleColor)
    {
        if(titleColor == null)
            titleColor = titleDefColor;

        if(appBarTitle == null)
            appBarTitle = "";

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + "'#" + titleColor + "'>" + appBarTitle + " </font>"));
        }else
        {
            Log.e("ACTIONBAR TITLE ERROR", "ACTIONBAR IS NULL");
        }
    }

    void setBarColors(@Nullable String statusBarColor, @Nullable String actionBarColor, @Nullable String navBarColor){

        if(statusBarColor == null)
            statusBarColor = statusBarDefColor;

        if(actionBarColor == null)
            actionBarColor = actionBarDefColor;

        if(navBarColor == null)
            navBarColor = navBarDefColor;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(statusBarColor));
            window.setNavigationBarColor(Color.parseColor(navBarColor));
            if(getSupportActionBar() != null)
            {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColor)));
            } else
            {
                Log.e("ACTIONBAR COLOR ERROR", "ACTIONBAR IS NULL");
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(enableBackButton) {
            if (item.getItemId() == android.R.id.home) {
                Map<String, Double> response = new HashMap<String, Double>();

                response.put("cancel", 0.0);

                finish();
                LocationPickerPlugin.result.success(response);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean checkOrRequestPermission(Activity activity, String permission, int code)
    {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.M)
        {
            return true;
        }
        if(activity != null)
        {
            int granted = ContextCompat.checkSelfPermission(activity, permission);
            if (granted != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            }
            return granted == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_ID: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //PERMISSION GRANTED
                    handleLocationEnabled();

                } else {

                    //PERMISSION DENIED
                    if(closeIfLocationDenied) {
                        Map<String, String> response = new HashMap<String, String>();

                        response.put("locationDenied", "Location Denied by user");

                        finish();
                        LocationPickerPlugin.result.success(response);
                    }
                }
            }
        }
    }

    void setFab(@Nullable String color)
    {
        if(color == null)
            color = actionBarDefColor;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> response = new HashMap<String, Object>();
                response.put("latitude", selectedLocation.latitude);
                response.put("longitude", selectedLocation.longitude);

                if(useGeoCoder) {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(selectedLocation.latitude, selectedLocation.longitude, 1);
                        Address address = addresses.get(0);
                        response.put("administrativeArea", address.getAdminArea());
                        response.put("country", address.getCountryName());
                        response.put("locality", address.getLocality());
                        response.put("subLocality", address.getSubLocality());
                        response.put("postalCode", address.getPostalCode());
                        response.put("thoroughfare", address.getThoroughfare());
                        response.put("line0", address.getAddressLine(0));
                        response.put("line1", address.getAddressLine(1));


                    } catch (Exception e) {
                        Log.e("GEOCODER ERROR", e.getMessage());
                    }
                }

                finish();
                LocationPickerPlugin.result.success(response);
            }
        });
    }
}
