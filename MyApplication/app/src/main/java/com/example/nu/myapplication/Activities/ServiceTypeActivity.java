package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nu.myapplication.Model.Enumerator.ServiceType;
import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;
import com.example.nu.myapplication.Utils.GlobalVariables;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceTypeActivity extends AppCompatActivity {
    private int stateRadioButton = 0;
    private FancyButton button2;
    private Toolbar toolbar;
    private final String TAG = this.getClass().getSimpleName();
    private ServiceType tempServiceType = ServiceType.Empty;
    String idPrent ;
    String idChild;
    String jsonobject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);
        Log.d("Service Type", "INSIDE: onCreate");
        initButton();
        initToolBar();
        initBackButton();
        Intent intent = getIntent();
        Log.d(TAG,intent.getStringExtra("idParent"));
        idPrent = intent.getStringExtra("idParent");
        idChild = intent.getStringExtra("idChild");
        jsonobject = intent.getStringExtra("jsonobject");



    }
    public void initToolBar() {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_servicetype);
            TextView textView = (TextView) findViewById(R.id.service_type_toolbar_title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void post(String studentId,String typeOfService){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("personId",studentId)
                .add("typeOfService", typeOfService)
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL+GlobalVariables.SETTYPEOFSERVICES)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString()+call.toString());
                Log.e(TAG,"fail"+"sentTypeOfservice",e);
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Post data to server failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,"GETTEACHERS"+response);
                Boolean b = Boolean.valueOf(response);
                Log.d(TAG,"GETTEACHERS"+b);
                Intent intentAlarm = new Intent(ServiceTypeActivity.this, ChildActivity.class);
                intentAlarm.putExtra("idParent",idPrent);
                intentAlarm.putExtra("idChild",idChild);
                intentAlarm.putExtra("jsonobject",jsonobject);
                startActivity(intentAlarm);
                finish();

            }
        });
    }
    public void initBackButton() {
        FancyButton button = (FancyButton) findViewById(R.id.back_button_service_type);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlarm = new Intent(ServiceTypeActivity.this, ChildActivity.class);
                intentAlarm.putExtra("idParent",idPrent);
                intentAlarm.putExtra("idChild",idChild);
                intentAlarm.putExtra("jsonobject",jsonobject);
                startActivity(intentAlarm);
                finish();
            }
        });
    }
    /*public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_servicetype);
        toolbar.setTitle("Service Type");
        //  Button button = new Button(this);
        // toolbar.addView(button);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        //TextView textView = new TextView(this);
        Log.d("service type","childtoolbar"+toolbar.getChildCount());
        for(int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                textView.setLayoutParams(params);

                // Apply custom font using the Calligraphy library
                Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
                textView.setTypeface(typeface);
                //  toolbar.setTitle(textView);
                Log.d("toolbar", "error");
            }
        }}*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activityf in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra("service type","service type");
            setResult(2, intent);
            finish();
            Log.d("child", "finish");
        }

        return super.onOptionsItemSelected(item);
    }
    public void initButton(){
        button2 = (FancyButton) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onButtonPressed();
            }
        });
    }
    public void failUpdateDatabase(){
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    public void onButtonPressed() {
       // super.onBackPressed();
        String service_type = null;
        if(tempServiceType != ServiceType.Empty){
            if(tempServiceType == ServiceType.ToSchool){
                service_type = "GO";
                post(idChild,service_type);
            }
            if(tempServiceType == ServiceType.ToHome)
            {
                service_type = "BACK";
                post(idChild,service_type);
            }
            if(tempServiceType == ServiceType.Both){
                service_type = "BOTH";
                post(idChild,service_type);
            }
            if(tempServiceType == ServiceType.None) {
                service_type = "NONE";
                post(idChild,service_type);
            }
           /* Intent intentAlarm = new Intent(ServiceTypeActivity.this, ChildActivity.class);
            intentAlarm.putExtra("idParent",idPrent);
            intentAlarm.putExtra("idChild",idChild);
            intentAlarm.putExtra("jsonobject",jsonobject);
            startActivity(intentAlarm);
            finish();*/
        }
        else{
            Toast.makeText(getApplicationContext(), "Didnt selectpe service type", Toast.LENGTH_LONG).show();
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_to_school:
                if (checked)
                   tempServiceType = ServiceType.ToSchool;
                    // Pirates are the best
                    break;
            case R.id.radio_to_home:
                if (checked)
                    tempServiceType = ServiceType.ToHome;
                    // Ninjas rule
                    break;
            case R.id.radio_both:
                if(checked)
                   tempServiceType = ServiceType.Both;
                    break;
            case R.id.radio_none:
                if(checked)
                   tempServiceType = ServiceType.None;
                    break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Service Type", "INSIDE: onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Service Type", "INSIDE: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Service Type", "INSIDE: onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Service Type", "INSIDE: onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Service Type", "INSIDE: onResume");
    }
}
