package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nu.myapplication.Model.Enumerator.Role;
import com.example.nu.myapplication.Model.Enumerator.TypeOfService;
import com.example.nu.myapplication.Model.Person.Student;
import com.example.nu.myapplication.Model.Position.Address;
import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Model.*;
import com.example.nu.myapplication.Utils.Converter;
import com.example.nu.myapplication.Utils.GlobalVariables;
import com.example.nu.myapplication.Utils.RoundedImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Nu on 29-Dec-16.
 */

public class MyChildrenActivity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Toolbar toolbar;
    TableLayout tableLayout = null;
    private int heightOfScreen;
    private int weidhtOfScreen;
    private String result = null;
    private String idCar = "car1";
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychildren);
       tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
       // init();
        initToolBar();
        showSizeOfScreen();

      //  postOkHttp("125");
        postOkHttpDriver(idCar);

    }
    public void postOkHttpDriver(String idCar){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .add("carNumber", idCar)
                .build();
        OkHttpClient client = new OkHttpClient();
        // Request requesst = new Request.Builder().url();
        Log.d(TAG,GlobalVariables.baseURL+GlobalVariables.GETALLPASSENGERINFORMATION);
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL+GlobalVariables.GETALLPASSENGERINFORMATION)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString());

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,response);
                JSONObject jsonObject = null;
                JSONArray jsonArray=null;
                try {
                    jsonObject = new JSONObject(response);
                    Log.d(TAG, String.valueOf(jsonObject.get("students")));
                    jsonArray = (JSONArray) jsonObject.get("students");
                    Log.d(TAG, jsonArray.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) jsonArray.get(i);
                    } catch (JSONException e) {
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
                            TextView tv = new TextView(getApplicationContext());
                            // final JSONObject finalJsonobject = finalJsonobject;
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nextToChildActivityDriver(finalJsonobject);
                                    try {

                                        Log.d(TAG, "id += "+(String) finalJsonobject.get("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,Gravity.CENTER_VERTICAL);
                                params.setMargins(30,0,0,0);
                                tv.setLayoutParams(params);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.SteelBlue));
                                tv.setText((String) finalJsonobject.get("firstName")+"  "+ finalJsonobject.get("surName"));
                                Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
                                tv.setTypeface(typeface);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");
                            ImageView imageView = (ImageView) new ImageView(getApplicationContext());
                            Resources res = getApplicationContext().getResources();
                            int id = R.drawable.mind;
                            Bitmap b = BitmapFactory.decodeResource(res, id);
                            imageView.setImageBitmap(RoundedImageView.getCroppedBitmap(b,120));
                            row.addView(imageView);
                            row.addView(tv);
                            tableLayout.addView(row, finalI);
                        }
                    });


                }
            }
        });
    }
    public void postOkHttp(final String idParent){
        String personId = "personId";
        String id = "125";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
       // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .add("personId", idParent)
                .build();
        OkHttpClient client = new OkHttpClient();
       // Request requesst = new Request.Builder().url();
        Log.d(TAG,GlobalVariables.baseURL+"getAllStudentInformation");
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL+"getAllStudentInformation")
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString());

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {

               String response = r.body().string();
                JSONArray jsonarray = null;
                try {
                    jsonarray = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i < jsonarray.length();i++){
                    JSONObject jsonobject = null;
                    try {
                        jsonobject = (JSONObject) jsonarray.get(i);
                    } catch (JSONException e) {
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
                            TextView tv = new TextView(getApplicationContext());
                           // final JSONObject finalJsonobject = finalJsonobject;
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nextToChildActivity(finalJsonobject,idParent);
                                    try {

                                        Log.d(TAG, "id += "+(String) finalJsonobject.get("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,Gravity.CENTER_VERTICAL);
                                params.setMargins(30,0,0,0);
                                tv.setLayoutParams(params);
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                                tv.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.SteelBlue));
                                tv.setText((String) finalJsonobject.get("firstName")+"  "+ finalJsonobject.get("surName"));
                                Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
                                tv.setTypeface(typeface);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");
                                ImageView imageView = (ImageView) new ImageView(getApplicationContext());
                                Resources res = getApplicationContext().getResources();
                                int id = R.drawable.mind;
                                Bitmap b = BitmapFactory.decodeResource(res, id);
                                imageView.setImageBitmap(RoundedImageView.getCroppedBitmap(b,120));
                                 row.addView(imageView);
                                  row.addView(tv);
                                  tableLayout.addView(row, finalI);
                            }
                        });


                }

                Log.e("response ", "onResponse(): " + response );
            }
        });
    }





    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_mychildren);
        Log.d("title toolbar ", "" + toolbar.getTitle());
        //  toolbar.setTitle("About Bus");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // no title
        }
        TextView textView = (TextView) findViewById(R.id.mychildren_toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
        textView.setTypeface(typeface);
    }

   public void showSizeOfScreen() {


    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    Log.d("Mychildren","width"+size.x);
    Log.d("Mychildren","heigth"+size.y);
       weidhtOfScreen = size.x;
       heightOfScreen = size.y;
    }
    public void init() {

        tableLayout.setWeightSum(3);

        for (int i = 0; i < 20; i++) {

            TableRow row = new TableRow(this);
            row.setMinimumHeight(100);
            row.setWeightSum(1);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            final TextView tv = new TextView(this);
            row.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    nextChildActivity();
                }
            } );
            //  checkBox = new CheckBox(this);

            ImageView imageView = new ImageView(this);
            // addBtn = new ImageButton(this);
            //   addBtn.setImageResource(R.drawable.add);
            //    minusBtn = new ImageButton(this);
            //     minusBtn.setImageResource(R.drawable.minus);
            //    qty = new TextView(this);
            //     checkBox.setText("hello");
            //    qty.setText("10");
            //   row.addView(checkBox);
            //    row.addView(minusBtn);
            //     row.addView(qty);
            //       row.addView(addBtn);
            new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");

            tv.setText("aaaa" + i);
           // row.addView(imageView);
            row.addView(tv);
            tableLayout.addView(row, i);
        }
    }
    public void initTable() {

        tableLayout.setWeightSum(3);

        for (int i = 0; i < 20; i++) {

            TableRow row = new TableRow(this);
            row.setMinimumHeight(100);
            row.setWeightSum(1);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            final TextView tv = new TextView(this);
            row.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    nextChildActivity();

                }
            } );
            //  checkBox = new CheckBox(this);

            ImageView imageView = new ImageView(this);
            // addBtn = new ImageButton(this);
            //   addBtn.setImageResource(R.drawable.add);
            //    minusBtn = new ImageButton(this);
            //     minusBtn.setImageResource(R.drawable.minus);
            //    qty = new TextView(this);
            //     checkBox.setText("hello");
            //    qty.setText("10");
            //   row.addView(checkBox);
            //    row.addView(minusBtn);
            //     row.addView(qty);
            //       row.addView(addBtn);
            new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");

            tv.setText("aaaa" + i);
            // row.addView(imageView);
            row.addView(tv);
            tableLayout.addView(row, i);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public void nextChildActivity(){
        Log.d(TAG,"nextchildactivity");
        Intent myIntent = new Intent(this, ChildActivity.class);
      //  myIntent.putExtra("student",student);
        startActivity(myIntent);
        //finishActivity();
    }
    public void nextToChildActivity(JSONObject jsonobect,String idParent){
        Log.d(TAG,"nexttochildactivity"+jsonobect.toString());
        Intent myIntent = new Intent(MyChildrenActivity.this, ChildActivity.class);
        myIntent.putExtra("idParent","125");
        myIntent.putExtra("jsonobject", jsonobect.toString());
        startActivity(myIntent);
        finish();
    }
    public void nextToChildActivityDriver(JSONObject jsonobect)
    {

    }
}
