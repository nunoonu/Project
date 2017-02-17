package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AllRoutesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String idParent;
    String typeOfUser;
    TableLayout tableLayout = null;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allroutes);
        tableLayout = (TableLayout) findViewById(R.id.table_route);
        Intent intent = getIntent();
        idParent = intent.getStringExtra("idParent");
        typeOfUser = intent.getStringExtra("typeOfUser");
        initToolBar("route");
        initBackButton();
        postOkHttpGetAllBusRoute();
        initAddRouteButton();

    }
    public void initToolBar(String title) {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_route);
            TextView textView = (TextView) findViewById(R.id.route_toolbar_title);
            textView.setText(title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);



        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void initBackButton(){
        FancyButton fancyButton = (FancyButton) findViewById(R.id.back_button_route);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoutesActivity.this, MyChildrenActivity.class);
                intent.putExtra("idParent", idParent);
                intent.putExtra("typeOfUser",typeOfUser);
                startActivity(intent);
                finish();
            }
        });
    }
    public void initAddRouteButton(){
        FancyButton textView = new FancyButton(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_all_routes);
        textView.setText("ADD route");
        textView.setTextSize(22);
        textView.setCustomTextFont("Capture_it.ttf");
        textView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.SlateGray));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(params);
        relativeLayout.addView(textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllRoutesActivity.this,AddRouteActivity.class);
                intent.putExtra("idParent", idParent);
                intent.putExtra("typeOfUser",typeOfUser);
                startActivity(intent);
                finish();
            }
        });
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
                try {
                    jsonarray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i < jsonarray.length();i++) {
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) jsonarray.get(i);
                        Log.d(TAG,jsonobject.toString());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final JSONObject finalJsonobject = jsonobject;
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableRow row = new TableRow(getApplicationContext());
                            row.setMinimumHeight(140);
                            row.setWeightSum(1);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            row.setGravity(Gravity.CENTER_VERTICAL);
                            TextView textView = new TextView(getApplicationContext());
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AllRoutesActivity.this,EachRouteActivity.class);
                                    intent.putExtra("idParent", idParent);
                                    intent.putExtra("typeOfUser",typeOfUser);
                                    try {
                                        intent.putExtra("routeNumber",finalJsonobject.get("routeNumber").toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            try {
                                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL);//layout param of textview
                               // params.setMargins(30,0,0,0);
                                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                                textView.setLayoutParams(params);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                                textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.SteelBlue));
                                textView.setText( "ROUTE "+finalJsonobject.get("routeNumber").toString());
                                Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
                                textView.setTypeface(typeface);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            row.addView(textView);
                            tableLayout.addView(row, finalI);
                        }
                    });

                }
            }
        });
    }
}
