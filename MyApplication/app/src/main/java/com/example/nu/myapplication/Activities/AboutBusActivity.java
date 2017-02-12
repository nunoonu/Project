package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;
import com.example.nu.myapplication.Utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AboutBusActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private AnimationDrawable d;
    private final String TAG = this.getClass().getSimpleName();
    private String idPrent;
    private String idChild;
    private String jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_bus);
        Log.d("About Bus", "INSIDE: onCreate");
        initToolBar();
        setRowOnClick();
        initRefreshButton();
        initBackButton();

        Intent intent = getIntent();
        Log.d(TAG,intent.getStringExtra("idParent"));
        idPrent = intent.getStringExtra("idParent");
        idChild = intent.getStringExtra("idChild");
        jsonobject = intent.getStringExtra("jsonobject");
        post(idChild);
    }
    public void initBackButton(){
        FancyButton fancyButton = (FancyButton) findViewById(R.id.back_button_about_bus);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlarm = new Intent(AboutBusActivity.this, ChildActivity.class);
                intentAlarm.putExtra("idParent",idPrent);
                intentAlarm.putExtra("idChild",idChild);
                intentAlarm.putExtra("jsonobject",jsonobject);
                startActivity(intentAlarm);
                finish();
            }
        });
    }
    public void post(String childId){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("personId",childId)
                .build();
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(GlobalVariables.baseURL+GlobalVariables.GETBUSDETAIL)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString()+call.toString());
                Log.e(TAG,"fail",e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,"responsee"+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectDriver = jsonObject.getJSONObject("driver");
                    JSONArray jsonArrayTeacherOnTheBus = jsonObject.getJSONArray("teachers");
                    addDriverDataToTableView(jsonObjectDriver.get("firstName").toString()+"  "+jsonObjectDriver.get("surName").toString());
                    addTeacherOnTheBusToTableView(jsonArrayTeacherOnTheBus.getJSONObject(0).get("firstName").toString()+"  "+jsonArrayTeacherOnTheBus.getJSONObject(0).get("surName").toString());
                    Log.d(TAG, String.valueOf(jsonObject.getJSONObject("bus")));
                    Log.d(TAG, String.valueOf(jsonObject.getJSONObject("driver")));
                    Log.d(TAG, String.valueOf(jsonObject.getJSONArray("teachers")));
                    d.stop();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void addTeacherOnTheBusToTableView(final String teacherName){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.teacheronthebus);
                textView.setText(teacherName);
            }
        });

    }
    public void addDriverDataToTableView(final String driverName){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = (TextView) findViewById(R.id.driver);
                textView.setText(driverName);
            }
        });

    }
    public void initRefreshButton(){
        final Button refreshBtn=(Button)findViewById(R.id.detail_refresh_btn);
        d = (AnimationDrawable)refreshBtn.getCompoundDrawables()[0];
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.start();
                post(idChild);
            }
        });
        ViewTreeObserver vto = refreshBtn.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.d("about bus","get widthbutton "+refreshBtn.getWidth());
                Log.d("about bus","get height" +
                        "hbutton "+refreshBtn.getHeight());
            }
        });

    }
    public void initToolBar(){
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_aboutbus);
            TextView textView = (TextView) findViewById(R.id.aboutbus_toolbar_title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar =  customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    public void initTooolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_aboutbus);
        Log.d("title toolbar ",""+toolbar.getTitle());
      //  toolbar.setTitle("About Bus");
       /* setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
        TextView textView = (TextView) findViewById(R.id.aboutbus_toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
        textView.setTypeface(typeface);
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so longt
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("about bus","about bus");
            setResult(1, intent);
            finish();
            //onBackPressed();
            Log.d("ABOUT BUS", "finish");
        }

        return super.onOptionsItemSelected(item);
    }
    public void setRowOnClick(){
        TableLayout yourRootLayout = (TableLayout) findViewById(R.id.tableLayoutAboutBus);
        int count = yourRootLayout.getChildCount();
        Log.d("test","count = "+count);
        for(int i = 0; i < count; i++){
            View v = yourRootLayout.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.d("rowid", String.valueOf(v.getId()));
                    switch (v.getId()) {
                        case R.id.driverRow:
                            Log.d("Aboutbus", "onClickDriver");
                            d.stop();
                            break;
                        case R.id.teacheronthebusRow:
                            Log.d("Aboutbus", "onClickTeacherOnTheBus");
                            break;
                        case R.id.showrouteRow:
                            Intent intentShowRoute = new Intent(AboutBusActivity.this,MapsActivity.class);
                            startActivity(intentShowRoute);
                            Log.d("Aboutbus", "onClickShowRouteRow");
                            break;
                        case R.id.remainingtimeRow:
                            Log.d("Aboutbus", "onClickRemainingTime");
                            break;
                        case R.id.remainingdistanceRow:
                            Log.d("Aboutbus", "onClickRemainingDistance");
                            break;
                        case R.id.elapsedtimeRow:
                            Log.d("Aboutbus", "onClickElapsedTime");
                            break;
                        case R.id.cameraRow:
                            Log.d("AboutRow", "onClickCameraRow");
                            break;
                        case R.id.aboutBusRow:
                            Log.d("onclick", "aboutbus");
                            break;
                        default:
                            break;
                    }
                }
            });
            if(v instanceof TableRow){
                TableRow row = (TableRow)v;
                int rowCount = row.getChildCount();
                Log.d("test","rowcount = "+rowCount);
                for (int r = 0; r < rowCount; r++){
                    View v2 = row.getChildAt(r);

                }
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("About bus", "INSIDE: onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("About bus", "INSIDE: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("About bus", "INSIDE: onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("About bus", "INSIDE: onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("About bus", "INSIDE: onResume");
    }


}
