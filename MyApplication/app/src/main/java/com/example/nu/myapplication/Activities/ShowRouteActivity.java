package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
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

public class ShowRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Polyline line;
    private Toolbar toolbar;
    String idParent ;
    String idChild;
    String jsonobject;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showroute);
        Intent intent = getIntent();
        Log.d(TAG,intent.getStringExtra("idParent"));
        idParent = intent.getStringExtra("idParent");
        idChild = intent.getStringExtra("idChild");
        jsonobject = intent.getStringExtra("jsonobject");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initToolBar("ShowRoute");
        initBackButton();
        postOkHttpGetCurrentRoute(idChild);

    }
    public void initBackButton() {
        FancyButton button = (FancyButton) findViewById(R.id.back_button_showroute);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlarm = new Intent(ShowRouteActivity.this, AboutBusActivity.class);
                intentAlarm.putExtra("idParent",idParent);
                intentAlarm.putExtra("idChild",idChild);
                intentAlarm.putExtra("jsonobject",jsonobject);
                startActivity(intentAlarm);
                finish();
            }
        });
    }
    public void onRefreshOnClick(View view){
            Log.d(TAG,"onRefreshOnClick");
            mMap.clear();
            postOkHttpGetCurrentRoute(idChild);

    }

    public void initToolBar(String title) {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_showroute);
            TextView textView = (TextView) findViewById(R.id.showroute_toolbar_title);
            textView.setText(title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
    public void runOnUiThreadAddMarkers(final JSONObject jsonObject){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(jsonObject.get("status").equals("PERSONSTART")) {
                        mMap.addMarker(new MarkerOptions().position( new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude"))).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    else if(jsonObject.get("status").equals("BUSSTART")){
                        mMap.addMarker(new MarkerOptions().position( new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude"))).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                    else if(jsonObject.get("status").equals("NONE")){
                        mMap.addMarker(new MarkerOptions().position( new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude"))).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                    else if(jsonObject.get("status").equals("FINISH")){
                        mMap.addMarker(new MarkerOptions().position( new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude"))).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    else{
                        Log.d(TAG,"else of addmarker");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
    public void postOkHttpGetCurrentRoute(String id) {
        String personId = "personId";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .add("personId", id)
                .build();
        OkHttpClient client = new OkHttpClient();
        // Request requesst = new Request.Builder().url();
        Log.d(TAG, GlobalVariables.baseURL + GlobalVariables.GETCURRENTROUTE);
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL + GlobalVariables.GETCURRENTROUTE)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString()+call.toString());
                Log.e(TAG,GlobalVariables.GETCURRENTROUTE+"fail",e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,GlobalVariables.GETCURRENTROUTE+response);
                try {
                    List<LatLng> latLngList =  new ArrayList<LatLng>();
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0 ; i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        latLngList.add(new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude")));
                        runOnUiThreadAddMarkers(jsonObject);
                    }

                    runOnUiThreadPolyline(latLngList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
      /*  Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                .width(5)
                .color(Color.RED));*/
      /*  LatLng sydney = new LatLng(-33.852, 151.211);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));*/
       // CameraUpdate update = CameraUpdateFactory.newLatLngZoom(sydney, 15);
      //  mMap.moveCamera(update);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
      //  Position position = new Position(2.2,100,);
       // mMap=AboutMap.addMarker(mMap,new LatLng(122.1,212.5));
       // mMap.addPolyline(AboutMap.getPolylineOptions());


    }
}
