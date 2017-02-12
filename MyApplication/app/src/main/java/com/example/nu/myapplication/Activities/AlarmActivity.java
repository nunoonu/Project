package com.example.nu.myapplication.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.nu.myapplication.R;

public class AlarmActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("alarm","onCreate1");
        super.onCreate(savedInstanceState);
        Log.d("alarm","onCreate2");
        setContentView(R.layout.activity_alarm);;

        initToolBar();
        Log.d("alarm","onCreate");

    }
    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_alarm);
        Log.d("title toolbar ",""+toolbar.getTitle());
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView textView = (TextView) findViewById(R.id.alarm_toolbar_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//TypefaceUtils.load(getAssets(), "fonts/myfont-1.otf");
        textView.setTypeface(typeface);
    }
}
