package com.santi.imagine.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.santi.imagine.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaRegistro extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String lat,lon;
    Geocoder geocoder;
    String direccion,ciudad,estado,pais,codigoPostal;
    List<Address> ubicaciones;
    public int CODIGO_FINAL = 101;
    Double latit=0.00,lng=0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(status== ConnectionResult.SUCCESS){


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        }else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,(Activity)getApplicationContext(),10);
            dialog.show();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat = String.valueOf(latLng.latitude);
                lon = String.valueOf(latLng.longitude);

                geocoder = new Geocoder(MapaRegistro.this, Locale.getDefault());
                try {
                    ubicaciones = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    direccion = ubicaciones.get(0).getAddressLine(0);
                    ciudad = ubicaciones.get(0).getLocality();
                    estado = ubicaciones.get(0).getAdminArea();
                    pais = ubicaciones.get(0).getCountryName();
                    codigoPostal = ubicaciones.get(0).getPostalCode();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent i = new Intent();
                i.putExtra("direccion",direccion);
                i.putExtra("ciudad",ciudad);
                i.putExtra("estado",estado);
                i.putExtra("pais",pais);
                i.putExtra("codigoPostal",codigoPostal);
                i.putExtra("latitude",lat);
                i.putExtra("longitude",lon);


                setResult(Activity.RESULT_OK,i);
                finish();

            }
        });
    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            latit = location.getLatitude();
            lng = location.getLongitude();
            LatLng latLng = new LatLng(latit,lng);

            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(latLng)
                    .zoom(15)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void miUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);

    }


    @Override
    public void onBackPressed() {

    }
}
