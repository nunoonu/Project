package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
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
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychildren);
       tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
      //  init();
      //  initToolBar();
        showSizeOfScreen();
        postOkHttp();


    }
    public void postOkHttp(){
        String personId = "personId";
        String id = "125";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
       // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .add("personId", "125")
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
                            row.setMinimumHeight(100);
                            row.setWeightSum(1);
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                            row.setLayoutParams(lp);
                            final TextView tv = new TextView(getApplicationContext());
                           // final JSONObject finalJsonobject = finalJsonobject;
                            row.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    nextToChildActivity(finalJsonobject);
                                    try {

                                        Log.d(TAG, "id += "+(String) finalJsonobject.get("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            try {
                                tv.setText((String) finalJsonobject.get("firstName")+"  "+ finalJsonobject.get("surName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");

                            // row.addView(imageView);
                            row.addView(tv);
                            tableLayout.addView(row, finalI);
                            }
                        });


                }

                Log.e("response ", "onResponse(): " + response );
            }
        });
    }


    public void postHttp(String id) {
        Log.d("child", "postHttp");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("personId", id);
        client.post(GlobalVariables.baseURL + "getAllStudentInformation", params, new ResponseHandlerInterface() {
            @Override
            public void sendResponseMessage(HttpResponse response) throws IOException {
                result = Converter.convertStreamToString(response.getEntity().getContent());
                Log.d(TAG, "resultFromPostHttp" + result);
                JSONArray myArray = null;
                try {
                    myArray = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0;i<myArray.length();i++){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = myArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    TableRow row = new TableRow(getApplicationContext());
                    row.setMinimumHeight(100);
                    row.setWeightSum(1);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                    row.setLayoutParams(lp);
                    final TextView tv = new TextView(getApplicationContext());
                    final JSONObject finalJsonObject = jsonObject;
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick( View v ) {
                            Student student = null;
                            try {
                                student = new Student((Role) finalJsonObject.get("role")
                                                     ,(String) finalJsonObject.get("id")
                                                     ,(Image) finalJsonObject.get("pic")
                                                     ,(String) finalJsonObject.get("token")
                                                     ,(String) finalJsonObject.get("tel")
                                                     ,(String) finalJsonObject.get("user")
                                                     ,(String) finalJsonObject.get("firstName")
                                                     ,(String) finalJsonObject.get("surName")
                                                     ,(String) finalJsonObject.get("faceBookId")
                                                     ,(ArrayList<Address>)finalJsonObject.get("addresses")
                                                     ,(TypeOfService) finalJsonObject.get("typeOfService")
                                                     ,(String) finalJsonObject.get("studentId"));
                            } catch (JSONException e) {
                                Log.e(TAG,"jsonexception",e);
                                e.printStackTrace();
                            }

                            if(student != null)
                                nextToChildActivity(finalJsonObject);
                            else
                                Log.d(TAG,"student == null");
                        }//(Role role, String id, Image pic, String token, String tel, String user, String firstName, String surName, String faceBookId, ArrayList<Address> address, TypeOfService typeOfService, String studentId
                    } );
                    try {
                        tv.setText((String)jsonObject.get("firstName")+"  "+jsonObject.get("surName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //new DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");

                    // row.addView(imageView);
                    row.addView(tv);
                    tableLayout.addView(row, i);
                }

            }

            @Override
            public void sendStartMessage() {

            }

            @Override
            public void sendFinishMessage() {

            }

            @Override
            public void sendProgressMessage(long bytesWritten, long bytesTotal) {

            }

            @Override
            public void sendCancelMessage() {

            }

            @Override
            public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void sendRetryMessage(int retryNo) {

            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public void setRequestURI(URI requestURI) {

            }

            @Override
            public Header[] getRequestHeaders() {
                return new Header[0];
            }

            @Override
            public void setRequestHeaders(Header[] requestHeaders) {

            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }

            @Override
            public void setUseSynchronousMode(boolean useSynchronousMode) {

            }

            @Override
            public boolean getUsePoolThread() {
                return false;
            }

            @Override
            public void setUsePoolThread(boolean usePoolThread) {

            }

            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                try {
                    Log.d(TAG,"PostProcess"+Converter.convertStreamToString(response.getEntity().getContent()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Object getTag() {
                return null;
            }

            @Override
            public void setTag(Object TAG) {

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
    public void nextToChildActivity(JSONObject jsonobect){
        Log.d(TAG,"nexttochildactivity"+jsonobect.toString());
        Intent myIntent = new Intent(MyChildrenActivity.this, ChildActivity.class);
        myIntent.putExtra("idParent","125");
        myIntent.putExtra("jsonobject", jsonobect.toString());
        startActivity(myIntent);
        finish();
    }
}
