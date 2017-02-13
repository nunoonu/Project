package com.example.nu.myapplication.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.nu.myapplication.Utils.*;
import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.HttpResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChildActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private final String TAG = this.getClass().getSimpleName();
   // private ProgressDialog progressDialog;
    String responseFromAsynTaskUrl = "";
    final String httpPath = "http://www.edumobile.org/android/";
    String result;
    private TableLayout tableLayout;
    String idChild;
    String idParent;
    JSONObject jsonObject = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        initToolBar();
        tableLayout = (TableLayout) findViewById(R.id.tableLayout2);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        if(bundle == null)
            Log.d(TAG,"bundle == null");
        else
            Log.d(TAG,"bundle != null");

        Log.d(TAG,"Striing"+(String) intent.getStringExtra("idParent"));
        idParent = intent.getStringExtra("idParent");
        Log.d(TAG," childId : "+GlobalVariables.childId
        );
        try {
            jsonObject = new JSONObject((String) intent.getStringExtra("jsonobject"));
            idChild= (String) jsonObject.get("id");
            postAllStudentInformation(idParent,idChild);
            postGetTeachers(idChild);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        // httpRequest();
        initAlarmButton("driver");
        //initTableView();
        setRowOnClick();
    }
    public void postGetTeachers(String studentId){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = new FormBody.Builder()
                .add("personId",studentId)
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GlobalVariables.baseURL+GlobalVariables.GETTEACHERS)
                .post(body)
                .addHeader("Authorization", "header value") //Notice this request has header if you don't need to send a header just erase this part
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString()+call.toString());
                Log.e(TAG,"fail"+"GETTEACHERS",e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,"GETTEACHERS"+response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0 ; i < jsonArray.length();i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if (jsonObject.get("role").equals("TEACHER")) {
                            Log.d(TAG, "role done");
                            addTeacherDataToTableView(jsonObject);
                        } else
                            Log.d(TAG, "not role done");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void postAllStudentInformation(String idParent, final String idChild){
        String personId = "personId";
        String id = "125";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // RequestBody body = RequestBody.create(JSON, "{"personId":"id"}");
        RequestBody body = new FormBody.Builder()
                .add("personId",idParent)
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
                Log.e("HttpService", "onFailure() Request was: " + Call.class.toString()+call.toString());
                Log.e(TAG,"fail",e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                Log.d(TAG,"responsee"+response);
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
                    try {
                        //if("123" == "123")
                        String idString = (String) jsonobject.get("id");
                        if(idString.equals(idChild))
                        {
                            addChildDataToTableView(jsonobject);
                        }
                        else{
                            Log.d(TAG,"data !="+jsonobject.get("id") +""+ GlobalVariables.childId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                Log.e("response ", "onResponse(): " + response );
            }
        });
    }

    public void addTeacherDataToTableView(JSONObject jsonObject) throws JSONException {
        TextView textViewClassTeacher = (TextView) findViewById(R.id.classteacher);
        TextView textViewClassTel = (TextView) findViewById(R.id.classtel);
        textViewClassTeacher.setText(jsonObject.get("firstName").toString()+"  "+jsonObject.get("surName"));
        textViewClassTel.setText(jsonObject.get("tel").toString());
    }
    public void addChildDataToTableView(JSONObject jsonObject) throws JSONException {
        TextView textViewName = (TextView) findViewById(R.id.name);
        TextView textViewTel = (TextView) findViewById(R.id.tel);
        TextView textViewAddress = (TextView) findViewById(R.id.address);

        TextView textViewIsInBus = (TextView) findViewById(R.id.isinbus);
        TextView textViewService = (TextView) findViewById(R.id.service);
        textViewName.setText((String)jsonObject.get("firstName") +"  "+ jsonObject.get("surName") );
        textViewTel.setText(jsonObject.get("tel").toString());
        textViewAddress.setText(jsonObject.get("addresses").toString());
        textViewService.setText(jsonObject.get("typeOfService").toString());
        //textViewClassTeacher

    }
    public void postHttp(String id) {
        Log.d("child", "postHttp");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("personId","125");
        client.post(GlobalVariables.baseURL+"getAllStudentInformation", params, new ResponseHandlerInterface() {
            @Override
            public void sendResponseMessage(cz.msebera.android.httpclient.HttpResponse response) throws IOException {

                result = convertStreamToString(response.getEntity().getContent());
                Log.d(TAG,"resultFromPostHttp"+ result);
                JSONArray myArray = null;
               /* ArrayList<Student> students = new ArrayList<Student>();

                try {
                    myArray = new JSONArray(result);
                    for (int i = 0; i < myArray.length(); i++) {
                        students.add(JsonToObjectUtil.jsonToStudent(myArray.getJSONObject(i)));
                        Log.d("Child firstname nununu", "" + students.size());
                    }
                } catch (Exception e) {
                    Log.e(TAG,"exception",e);
                    e.printStackTrace();
                }
                Log.d("Child", "sendResponseMessage" + students);*/
//                Log.d("Child firstname gugugu", "" + students.get(0).getFirstName());
            }

            @Override
            public void sendStartMessage() {
                Log.d("Http", "sendStartMessage");
            }

            @Override
            public void sendFinishMessage() {
                Log.d("Http", "sendFinishMessage");

            }

            @Override
            public void sendProgressMessage(long bytesWritten, long bytesTotal) {
                Log.d("Http", "sendProgressMessage");
            }

            @Override
            public void sendCancelMessage() {
                Log.d("Http", "sendCancelMessage");
            }


            @Override
            public void sendSuccessMessage(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d("Http", "  statusCode : " + statusCode + " Header :" + headers + "  sendSuccessMessage : " + responseBody);
            }

            @Override
            public void sendFailureMessage(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Http", "sendFailureMessage");
            }

            @Override
            public void sendRetryMessage(int retryNo) {
                Log.d("Http", "sendRetryMessage");
            }


            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public cz.msebera.android.httpclient.Header[] getRequestHeaders() {
                return new cz.msebera.android.httpclient.Header[0];
            }

            @Override
            public void setRequestURI(URI requestURI) {
                Log.d("Http", "setRequestURI");
            }

            @Override
            public void setRequestHeaders(cz.msebera.android.httpclient.Header[] requestHeaders) {
                Log.d("Http", "setRequestHeaders");
            }

            @Override
            public boolean getUseSynchronousMode() {
                return false;
            }

            @Override
            public void setUseSynchronousMode(boolean useSynchronousMode) {
                Log.d("Http", "setUseSynchronousMode");
            }

            @Override
            public boolean getUsePoolThread() {
                return false;
            }

            @Override
            public void setUsePoolThread(boolean usePoolThread) {
                Log.d("Http", "setUsePoolThread");
            }

            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                Log.d("Http", "onPreProcessResponse");
            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                Log.d("Http Response", " instance : " + instance + " response : " + response);
            }

            @Override
            public Object getTag() {
                return null;
            }

            @Override
            public void setTag(Object TAG) {
                Log.d("Http", "setTag");
            }
        });
        client.getHttpContext();
    }

    private static String convertStreamToString(InputStream is) { // this is not your algorithm

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

   /* public void initAlarmButton(String userType,Toolbar toolbar) {
        Button button = (Button) findViewById(R.id.alarm_btn);
        if(userType.equals("child")){
            button.setBackgroundResource(R.drawable.ic_action_alarm);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAlarm = new Intent(ChildActivity.this, AlarmActivity.class);
                    startActivity(intentAlarm);
                }
            });
        }

        if(userType.equals("driver")){
            button.setText("Notify");
            button.setTextSize(30);
            button.setBackgroundColor(Color.RED);
        }
    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"des");
    }

    public void initToolBar() {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_child);
            TextView textView = (TextView) findViewById(R.id.child_toolbar_title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void initTableView() {
        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout2);


        for (int i = 0; i < 10; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            TableLayout.LayoutParams layoutRow = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);

            row.setLayoutParams(layoutRow);
            final TextView tv = new TextView(this);
            final TextView tv2 = new TextView(this);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //nextChildActivity((String) tv.getText());
                    Log.d("onclick", "row");
                }
            });
            ImageView imageView = new ImageView(this);
            new ChildActivity.DownloadImage(imageView).execute("https://graph.facebook.com/1577990348894620/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D");
            tv.setLayoutParams(lp);
            tv.setText("aaaa" + i);

            tv2.setLayoutParams(lp);
            tv2.setText("bbbb" + i);

            tv.setGravity(Gravity.START);
            tv2.setGravity(Gravity.END);
            row.setGravity(Gravity.NO_GRAVITY);

            tv.setBackgroundColor(Color.RED);
            row.setBackgroundColor(Color.GREEN);
            tv2.setBackgroundColor(Color.MAGENTA);

            //  row.addView(imageView);
            row.addView(tv2);
            row.addView(tv);
            ll.addView(row, i);
        }
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

    public void setRowOnClick() {
        TableLayout yourRootLayout = (TableLayout) findViewById(R.id.tableLayout2);
        int count = yourRootLayout.getChildCount();
        Log.d("test", "count = " + count);
        for (int i = 0; i < count; i++) {
            View v = yourRootLayout.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("rowid", String.valueOf(v.getId()));
                    switch (v.getId()) {
                        case R.id.nameRow:
                            Log.d("onclick", "name");
                            Intent intentt = new Intent(ChildActivity.this, CameraActivity.class);
                            startActivity(intentt);
                            finish();
                            break;
                        case R.id.telRow:
                            Log.d("onclick", "tel");
                            Intent intentTel = new Intent(ChildActivity.this, TelActivity.class);
                            startActivity(intentTel);
                            finish();
                            break;
                        case R.id.addressRow:
                            Log.d("onclick", "address");
                            break;
                        case R.id.classTeacherRow:
                            Log.d("onclick", "classteacher");
                            break;
                        case R.id.classTelRow:
                            Log.d("onclick", "classtel");
                            break;
                        case R.id.isInBusRow:
                            Log.d("onclick", "isinbus");
                            break;
                        case R.id.serviceRow:
                            Log.d("onclick", "service");
                            Intent myIntent = new Intent(ChildActivity.this, ServiceTypeActivity.class);
                            myIntent.putExtra("idParent", idParent);
                            myIntent.putExtra("idChild", idChild);
                            myIntent.putExtra("jsonobject",jsonObject.toString());
                            startActivity(myIntent);
                            finish();
                            break;
                        case R.id.aboutBusRow:
                            Log.d("onclick", "aboutbus");
                            Intent intent = new Intent(ChildActivity.this, AboutBusActivity.class);
                            intent.putExtra("idParent", idParent);
                            intent.putExtra("idChild", idChild);
                            intent.putExtra("jsonobject",jsonObject.toString());
                            startActivity(intent);
                            finish();
                            break;

                        default:
                            break;
                    }
                }
            });
            if (v instanceof TableRow) {
                TableRow row = (TableRow) v;
                int rowCount = row.getChildCount();
                Log.d("test", "rowcount = " + rowCount);
                for (int r = 0; r < rowCount; r++) {
                    View v2 = row.getChildAt(r);

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"before resume");

        Log.d(TAG, "finish_onresume");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//มันไม่ได้เกี่ยวอะไรกับการส่งดาต้ากับเลยต้องรีเควสเอา
        Log.d(TAG, "onActivityResult");
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String stredittext = data.getStringExtra("service_type");
                Log.d("stredittext", stredittext);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            Log.d("child", "finish");
        }

        return super.onOptionsItemSelected(item);
    }
}

