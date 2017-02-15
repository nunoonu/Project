package com.example.nu.myapplication.Activities;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.AboutMap;
import com.example.nu.myapplication.Utils.GlobalVariables;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Polyline line;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        postOkHttpGetCurrentRoute("123");
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
                        latLngList.add(new LatLng((Double) jsonObject.get("latitude"),(Double)jsonObject.get("longitude"));
                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng sydney = new LatLng(latitude, longitude);
                                    mMap.addMarker(new MarkerOptions().position(sydney)
                                            .title("Marker in Sydney"));
                                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(sydney, 15);
                                    mMap.moveCamera(update);
                                }
                            });*/

                    }

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
