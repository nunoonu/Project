package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;
import com.example.nu.myapplication.Utils.GlobalVariables;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EachRouteActivity  extends FragmentActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private GoogleMap mMap;
    String idParent;
    Polyline line;
    String typeOfUser;
    String routeNumber;
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_route);
        Intent intent = getIntent();
        idParent = intent.getStringExtra("idParent");
        typeOfUser = intent.getStringExtra("typeOfUser");
        routeNumber = intent.getStringExtra("routeNumber");
        Log.d(TAG,routeNumber);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.each_route_map);
        mapFragment.getMapAsync(this);
        initToolBar("route "+routeNumber);
        initBackButton();
        postOkHttpGetAllBusRoute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
           LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(sydney, 15);
          mMap.moveCamera(update);
    }
    public void initToolBar(String title) {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_eachroute);
            TextView textView = (TextView) findViewById(R.id.each_route_toolbar_title);
            textView.setText(title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void postOkHttpGetAllBusRoute() {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .build();
        OkHttpClient client = new OkHttpClient();
        // Request requesst = new Request.Builder().url();
        Log.d(TAG, GlobalVariables.baseURL + GlobalVariables.GETALLBUSROUTE);
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL + GlobalVariables.GETALLBUSROUTE)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString() + call.toString());
                Log.e(TAG, GlobalVariables.GETALLBUSROUTE + "fail", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG, GlobalVariables.GETALLBUSROUTE + response);
                JSONArray jsonarray = null;
                List<LatLng> latLngList =  new ArrayList<LatLng>();
                try {
                    jsonarray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i < jsonarray.length();i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) jsonarray.get(i);
                        Log.d(TAG, jsonobject.toString());
                        if(jsonobject.get("routeNumber").toString().equals(routeNumber)){
                            Log.d(TAG,routeNumber+ jsonobject.getJSONArray("longitudes").length());
                            JSONArray jsonArrayLongitude =  jsonobject.getJSONArray("longitudes");
                            JSONArray jsonArrayLatitudes =  jsonobject.getJSONArray("latitudes");

                            for(int j = 0;j < jsonobject.getJSONArray("longitudes").length();j++){
                                latLngList.add(new LatLng((Double) jsonArrayLatitudes.get(j),(Double) jsonArrayLongitude.get(j)));
                            }
                            runOnUiThreadAddMarkers(latLngList);
                            runOnUiThreadPolyline(latLngList);




                        }else {
                            Log.d(TAG,"else in decode for");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    public void runOnUiThreadAddMarkers(final List<LatLng> latLngList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i< latLngList.size();i++){
                    if(i==0){
                        mMap.addMarker(new MarkerOptions().position( latLngList.get(i)).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    else if(i == latLngList.size()-1){
                        mMap.addMarker(new MarkerOptions().position( latLngList.get(i)).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    else{
                        mMap.addMarker(new MarkerOptions().position( latLngList.get(i)).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }
            }
        });

    }
    public void runOnUiThreadPolyline(final List<LatLng> latLngList){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int z = 0; z < latLngList.size()-1; z++) {
                    LatLng src = latLngList.get(z);
                    LatLng dest = latLngList.get(z + 1);
                    line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(5).color(Color.BLUE).geodesic(true));
                    if(z==latLngList.size()-2){
                        Log.d(TAG,"latLngList.size()-1");
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngList.get(z+1), 15);
                        mMap.moveCamera(update);
                    }
                }
            }
        });

    }
    public void initBackButton() {
        FancyButton button = (FancyButton) findViewById(R.id.back_button_each_route);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EachRouteActivity.this,AllRoutesActivity.class);
                intent.putExtra("idParent", idParent);
                intent.putExtra("typeOfUser",typeOfUser);
                startActivity(intent);
                finish();
            }
        });
    }

}
