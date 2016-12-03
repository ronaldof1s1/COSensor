package com.example.ronaldofs.cosensor;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMaps extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int PLACE_PICKER_REQUEST_CODE = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private GoogleApiClient mApiClient;
    private boolean ApisNotAdded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

//         Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isGooglePlayServicesAvaliable() && ApisNotAdded){
            addAPIs();
        }

        getPermissions();

        initPlaceAPI();
    }

    private void initPlaceAPI() {


        PlacePicker.IntentBuilder pickerBuilder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(pickerBuilder.build(this), PLACE_PICKER_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        PlaceAutocomplete.IntentBuilder autoCompleteBuilder;
        autoCompleteBuilder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);

        try {
            startActivityForResult(autoCompleteBuilder.build(this), PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);
                Toast.makeText(this, place.getName(), Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "autocomplete ok", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void getPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "LocationServices not Permitted", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            Toast.makeText(this, "map null", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            if (ActivityCompat.checkSelfPermission(this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "not permitted", Toast.LENGTH_SHORT).show();
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//        }
//
//        mMap.setMyLocationEnabled(true);
        locationEnabled();

        geoLocateCarWorkshops();
    }

    private void addAPIs() {
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
        ApisNotAdded = false;
    }

    private void locationEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    public boolean isGooglePlayServicesAvaliable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvaliable = api.isGooglePlayServicesAvailable(this);
        if (isAvaliable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvaliable)) {
            Dialog dialog = api.getErrorDialog(this, isAvaliable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "nao conecta com gpServices", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "location = null", Toast.LENGTH_SHORT).show();
        }
        else{
            LatLng mlocation = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mlocation, 14);
//            mMap.animateCamera(update);
        }
    }

    public void geoLocateCarWorkshops(){

//        Geocoder gc = new Geocoder(this);
//        List<Address> addresses = null;
//
//        try {
//            addresses = gc.getFromLocationName("oficinas automotivas",10);
//        } catch (IOException e) {
//            Toast.makeText(this, "could not search for car workshops", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (addresses.isEmpty()){
//            Toast.makeText(this, "No workshops found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        putMarkers(addresses);



    }

    private void putMarkers(List<Address> addresses) {
        for (int i = 0; i < addresses.size(); i++){
            Address address = addresses.get(i);

            putMarker(address);
        }


    }

    private void putMarker(Address address) {
        double lat = address.getLatitude();
        double lon = address.getLongitude();

        LatLng latLng = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(latLng).title(address.getLocality()));

    }

    public void zoomToLocation(View view){
        EditText et = (EditText) findViewById(R.id.editText1);

        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> addresses = null;
        try {
             addresses = gc.getFromLocationName(location, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "could not find location", Toast.LENGTH_SHORT).show();
            return;
        }
        Address address = addresses.get(0);
        LatLng ll = new LatLng(address.getLatitude(), address.getLongitude());
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 14);
        mMap.animateCamera(update);
    }
}
