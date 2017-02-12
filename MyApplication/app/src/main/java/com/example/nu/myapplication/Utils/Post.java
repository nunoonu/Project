package com.example.nu.myapplication.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by EVILUTION on 29/1/2560.
 */

public class Post extends AsyncTask<UrlAndParam, Integer, String> {
    private Activity activity;
    private final String TAG = this.getClass().getSimpleName();
  //  private ProgressDialog progressDialog;
    Post.DataDownloadListener dataDownloadListener;

  /*  public Post(String url){
        Log.d("doingback construct",url);
        this.urll = url;
    }*/
    public Post(){

    }

  public void setDataDownloadListener(Post.DataDownloadListener dataDownloadListener) {
      this.dataDownloadListener = dataDownloadListener;
  }
  @Override
  protected void onPreExecute() {
      super.onPreExecute();
     /* progressDialog = new ProgressDialog(activity);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Downloding  ");
      progressDialog.show();*/
  }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(UrlAndParam... params) {

        Log.d(TAG,params[0].getUrl());

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> param : params[0].getHashMap().entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append('&');
            }
            try {
                stringBuilder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append('=');
            try {
                stringBuilder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        byte[] postData = new byte[0];
        try {
            postData = stringBuilder.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = params[0].getUrl();
        URL    url            = null;
        try {
            url = new URL( request );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn= null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setDoOutput( true );
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects( false );
        try {
            conn.setRequestMethod( "POST" );
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );

        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
            wr.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        try {
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.disconnect();

        return builder.toString();
    }

    protected void onPostExecute(String results)
    {
      //+ progressDialog.dismiss();
        Log.d("onPostExecuted","before");
        if(results != null)
        {
            dataDownloadListener.dataDownloadedSuccessfully(results);
          //  cancel(true);

        }
        else {
            dataDownloadListener.dataDownloadFailed();
        }


    }
    public static interface DataDownloadListener {
        void dataDownloadedSuccessfully(String data);
        void dataDownloadFailed();
    }

}