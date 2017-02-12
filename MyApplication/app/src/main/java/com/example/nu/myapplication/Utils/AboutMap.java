package com.example.nu.myapplication.Utils;

import android.graphics.Color;

import com.example.nu.myapplication.Model.Position.Position;
import com.example.nu.myapplication.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by EVILUTION on 25/1/2560.
 */

public class AboutMap {
    public static GoogleMap getLocationZoom(LatLng latLng, float zoom,GoogleMap mGoogleMap) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.moveCamera(update);
        return mGoogleMap;
    }
    public static GoogleMap addMarker(GoogleMap mMap,LatLng latLng){
        mMap.addMarker(new MarkerOptions()
                 .position(latLng)
                 .title("Hello world")
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_refresh1)));
        return mMap = AboutMap.getLocationZoom(latLng,15,mMap);
    }
    public static PolylineOptions getPolylineOptions(Position[] positions){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int z = 0; z < positions.length; z++) {
            LatLng point = new LatLng(positions[z].getLatitude(),positions[z].getLongitude());
            options.add(point);
        }
        return options;

    }
}
