package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Service.LocationService;
import com.example.nu.myapplication.Utils.GlobalVariables;
import com.example.nu.myapplication.Utils.RoundedImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
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
    String idParent;
    String typeOfUser;
    JSONObject jsonObject = null;
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
        /*idParent = "125"; // car1 || 125
        typeOfUser = "parent";*/
        Intent intent = getIntent();
        idParent = intent.getStringExtra("idParent");
        typeOfUser = intent.getStringExtra("typeOfUser");
        Log.d(TAG," : "+idParent + typeOfUser);
       /* idParent = "car1";
        typeOfUser = "driver";*/
        // init();
        if(typeOfUser.equals("driver"))
        {
            Log.d(TAG,"IF");
            initToolBar(typeOfUser);// para driver parent
            postOkHttpDriver(idParent);
            initNotifyAllButton(idParent,typeOfUser);
            initRefreshForDriver();
            initRouteButton();
        }
        else if(typeOfUser.equals("parent"))
        {
            Log.d(TAG,"else IF");
            initToolBar(typeOfUser);// para driver parent
            postOkHttp(idParent);
        }
        else{
           Log.d(TAG,"else : "+typeOfUser);
        }
        showSizeOfScreen();
       // stopService(new Intent(MyChildrenActivity.this,LocationService.class));


    }
    public void initRouteButton(){
        FancyButton textView = new FancyButton(this);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_mychildren);
        textView.setText("Route");
        textView.setTextSize(33);
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
                Intent intent = new Intent(MyChildrenActivity.this,AllRoutesActivity.class);
                intent.putExtra("idParent", idParent);
                intent.putExtra("typeOfUser",typeOfUser);
                startActivity(intent);
                finish();
            }
        });
    }
    public void initRefreshForDriver(){
        FancyButton button = new FancyButton(this);
        button.setRadius(10);
        button.setText("");
        button.setIconResource("\uf021");
        button.setFontIconSize(30);
        button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.LightSeaGreen));
        //button.setBackgroundResource(R.drawable.ic_action_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        //  params.gravity = Gravity.RIGHT;
        button.setGravity(Gravity.CENTER);
        button.setLayoutParams(params);
        toolbar.addView(button);
    }
    public void initNotifyAllButton(final String idParent, final String typeOfUser){
        FancyButton button = new FancyButton(this);
        button.setBackgroundColor(Color.RED);
        // button.setWidth(5);
        button.setText("NotifyALL");
        button.setTextSize(14);
        button.setCustomTextFont("Capture_it.ttf");
        button.setRadius(10);
        button.setIconResource("\uf12a");
       button.setFontIconSize(30);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,Gravity.RIGHT);
        //  params.gravity = Gravity.RIGHT;
        button.setGravity(Gravity.CENTER);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyChildrenActivity.this,NotifyAllActivity.class);
                intent.putExtra("idParent", idParent);
                intent.putExtra("typeOfUser",typeOfUser);
                startActivity(intent);
                finish();
            }
        });
        toolbar.addView(button);
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
                            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);//layout param of table row
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
                                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,Gravity.CENTER_VERTICAL);//layout param of textview
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





    public void initToolBar(String typeOfUser) {
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
        if(typeOfUser.equals("parent"))
            textView.setText("My Children");
        else if(typeOfUser.equals("driver"))
            textView.setText("Passengers");
        else
            Log.d(TAG,"initToolbarfail");
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
        myIntent.putExtra("idParent",idParent);
        myIntent.putExtra("jsonobject", jsonobect.toString());
        startActivity(myIntent);
        finish();
    }
    public void nextToChildActivityDriver(JSONObject jsonobect)
    {

    }
}
