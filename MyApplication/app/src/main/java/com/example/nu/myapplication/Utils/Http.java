package com.example.nu.myapplication.Utils;

import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import org.apache.http.*;
import com.example.nu.myapplication.Model.Position.Position;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.apache.http.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import cz.msebera.android.httpclient.*;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by EVILUTION on 26/1/2560.
 */

public class Http {
   /* private Exception exception;

    public static void doMysql()
    {
        Log.v("AsyncTask", "accessed doMySql()");

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.e("AsyncTask", "onPreExecute");
            }
            @Override
            protected String doInBackground(Void... params) {
                Log.v("AsyncTask", "doInBackground");

                String msg = "";

                org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://161.246.5.213:8089/getAllStudentInformation");

                ArrayList<org.apache.http.NameValuePair> nameValuePairs = new ArrayList<org.apache.http.NameValuePair>();

                nameValuePairs.add(new org.apache.http.message.BasicNameValuePair("personId", "125"));
                try {
                    Log.v("AsyncTask", "before");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.v("AsyncTask", "after");

                } catch (UnsupportedEncodingException e1) {
                    // TODO Auto-generated catch block
                  //  Log.v("UnsupportedEncodingException",e1.toString());
                    e1.printStackTrace();
                }
                //172.16.101.28
                try {
                    Log.v("AsyncTask", "before");
                    HttpResponse httpresponse = httpclient.execute(httppost);
                    Log.v("AsyncTask", "after");
                    Log.v("AsyncTask    ",httpresponse.getEntity().toString());
                } catch (ClientProtocolException e) {
                    Log.v("AsyncTask", "ClientProtocolException");
                    e.printStackTrace();
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    Log.v("AsyncTask", "IOException");

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return msg;
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.v("AsyncTask", "onPostExecute");

            }
        };
        if(Build.VERSION.SDK_INT >= 11)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }*/
}

