package com.santi.imagine.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        float zoomLevel = 16;
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));
    }

    @Override
    public void onBackPressed() {

    }
}
