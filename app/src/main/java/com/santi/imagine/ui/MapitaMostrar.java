package com.santi.imagine.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.model.Marker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.santi.imagine.R;
import com.santi.imagine.app.CustomInfoWindowAdapter;
import com.santi.imagine.models.Productos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapitaMostrar extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore mDataBase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();
    LatLng latLng;
    private Double latitud, longitud;
    Double latit = 0.00;
    Double lng = 0.00;


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
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        miUbicacion();



        final Task<QuerySnapshot> batata = mDataBase.collection("Productos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final Integer ricardo = task.getResult().size();//   valor  =  5

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        final Productos product = document.toObject(Productos.class);
                        Log.i("batata", product.toString());
                        latitud = Double.valueOf(product.getLatitude());
                        longitud = Double.valueOf(product.getLongitude());
                        Log.i("arriba", product.getLatitude());

                        com.google.android.gms.maps.model.Marker m;
                        String texto = product.getDescripcion() + "\nCantidad: " + product.getCantidad();

                        m = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(product.getProducto()).snippet(texto));


                        Log.i("botones", String.valueOf(markerMap));

                        m.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.iconosmapasforeground));
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getLayoutInflater()));



                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {

                                Intent i = new Intent(MapitaMostrar.this,SolicitudProductos.class);
                                i.putExtra("Usuario",product.getTokenUsuario());
                                i.putExtra("Cantidad",product.getCantidad());
                                i.putExtra("Imagen",product.getUrl());
                                i.putExtra("Titulo",product.getProducto());
                                startActivity(i);
                                finish();

                            }
                        });

                    }
                }
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
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder cerrar = new AlertDialog.Builder(MapitaMostrar.this);
        cerrar.setTitle("Volver");
        cerrar.setMessage("¿Estas seguro de querer volver?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(MapitaMostrar.this,Principal.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();

            }
        });
        cerrar.show();
    }
}