package com.santi.imagine.app;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.santi.imagine.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


   private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindows_layout, null);
        String[] info = m.getTitle().split("&");
        String url = m.getSnippet();
        ((TextView)v.findViewById(R.id.info_window_nombre)).setText("Título: " + m.getTitle());
        ((TextView)v.findViewById(R.id.info_window_placas)).setText("Descripcion: " + m.getSnippet());


        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }


}
