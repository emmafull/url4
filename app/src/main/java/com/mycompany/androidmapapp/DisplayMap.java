package com.mycompany.androidmapapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import java.lang.Object.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapView;
import android.*;


/**
 * Created by coltonchilders on 3/3/15.
 */
public class DisplayMap extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        StringBuilder lat = new StringBuilder(intent.getStringExtra(MainActivity.EXTRA_LAT));
        StringBuilder lng = new StringBuilder(intent.getStringExtra(MainActivity.EXTRA_LNG));
        String latString = lat.substring(5, lat.length() - 7);
        String lngString = lng.substring(5, lng.length() - 7);

        OnMapReadyCallback callback;
        MapView mapView;
        GoogleMap map;
    }
}
