package com.example.nu.myapplication.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by EVILUTION on 29/1/2560.
 */


public class TestAsyn extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        //Log.d("doingback", params[0]);
       // Log.d("doingback", params[1]);
        String response = "response";
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}

