package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;

import mehdi.sakout.fancybuttons.FancyButton;

public class AlarmActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String idPrent ;
    String idChild;
    String jsonobject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("alarm","onCreate1");
        super.onCreate(savedInstanceState);
        Log.d("alarm","onCreate2");
        setContentView(R.layout.activity_alarm);;
        Intent intent = getIntent();
        idPrent = intent.getStringExtra("idParent");
        idChild = intent.getStringExtra("idChild");
        jsonobject = intent.getStringExtra("jsonobject");
        initToolBar();// initToolbar before initbackbutton
        initBackButton();
        Log.d("alarm","onCreate");

    }
    public void initToolBar() {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_alarm);
            TextView textView = (TextView) findViewById(R.id.alarm_toolbar_title);
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public void initBackButton(){
        FancyButton button = new FancyButton(this);
        button.setRadius(10);
        button.setText("");
        button.setIconResource("\uf053");
        button.setFontIconSize(30);
        //button.setBackgroundResource(R.drawable.ic_action_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlarm = new Intent(AlarmActivity.this, ChildActivity.class);
                intentAlarm.putExtra("idParent",idPrent);
                intentAlarm.putExtra("idChild",idChild);
                intentAlarm.putExtra("jsonobject",jsonobject);
                startActivity(intentAlarm);
                finish();
            }
        });
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
        //  params.gravity = Gravity.RIGHT;
        button.setGravity(Gravity.CENTER);
        button.setLayoutParams(params);
        toolbar.addView(button);
    }
    /*public void initToolBar(){
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
    }*/
}
