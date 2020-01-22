package com.santi.imagine.ui;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.model.Marker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santi.imagine.R;

import java.util.ArrayList;

public class MapitaMostrar extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mDataBase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapita_mostrar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDataBase = FirebaseFirestore.getInstance();


    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }
}
