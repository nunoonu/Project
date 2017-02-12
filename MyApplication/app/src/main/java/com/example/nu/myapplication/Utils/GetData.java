package com.example.nu.myapplication.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nu.myapplication.Activities.ChildActivity;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 * Created by EVILUTION on 29/1/2560.
 */

public class GetData extends AsyncTask<ArrayList<String>, Void, String>
{
    private Activity activity;
    private ProgressDialog progressDialog;
    DataDownloadListener dataDownloadListener;
    public GetData()
    {
        //Constructor may be parametric
    }
    public GetData(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Downloding  ");
        progressDialog.show();
    }

    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }
    @Override
    protected String doInBackground(ArrayList<String>... params)
    {
        Log.d("acticity",activity.getClass().getSimpleName());
        URL url = null;
        Log.d("doingback", params[0].get(0));

        try {
            Log.d("url",GlobalVariables.baseURL +params[0].get(0));
         //   String tempUrl = URLEncoder.encode(GlobalVariables.baseURL +params[0].get(0),"UTF-8");//
            url = new URL(GlobalVariables.baseURL +params[0].get(0));
        } catch (MalformedURLException e) {
            Log.e("MYAPP1", "exception", e);
            e.printStackTrace();
            return null;
        }
        Map<String,Object> paramss = new LinkedHashMap<>();
        paramss.put("personId",params[0].get(1));
        Log.d("personId",params[0].get(1));


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : paramss.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("MYAPP2", "exception", e);
                e.printStackTrace();
                return null;
            }
            postData.append('=');
            try {
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("MYAPP3", "exception", e);
                e.printStackTrace();
                return null;
            }
        }
        byte[] postDataBytes = new byte[0];
        try {
            postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("MYAPP4", "exception", e);
            e.printStackTrace();
            return null;
        }

        HttpURLConnection conn = null;
        //conn.setDoInput(true);//
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            Log.e("MYAPP5", "exception", e);
            e.printStackTrace();
            return null;
        }
      /*  try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            Log.e("MYAPP6", "exception", e);
            e.printStackTrace();
            return null;
        }*/
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.setDoInput(true);
        try {
            conn.getOutputStream().write(postDataBytes);
        } catch (IOException e) {
            Log.e("MYAPP7", "exception", e);
            e.printStackTrace();
            return null;
        }

        Reader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            Log.d("in", String.valueOf(in));
          //  in.close();

        } catch (IOException e) {
            Log.e("MYAPP8", "exception", e);
            e.printStackTrace();
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try {
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);

        } catch (IOException e) {
            Log.e("MYAPP9", "exception", e);
            e.printStackTrace();
            return null;
        }
        String response = sb.toString();
        try {
            in.close();//
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();
        return response;
        // do your task...

    }
    @Override
    protected void onPostExecute(String results)
    {
        Log.d("onPostExecuted","before");
        if(results != null)
        {
            dataDownloadListener.dataDownloadedSuccessfully(results);
        }
        else {
            dataDownloadListener.dataDownloadFailed();
        }
        progressDialog.dismiss();
    }
    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(String data);
        void dataDownloadFailed();
    }
}