package com.example.nu.myapplication.Activities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;

public class PassengersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passengers);
    }
    public void initToolBar(){
        try{
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_passenger);
            TextView textView = (TextView) findViewById(R.id.passengers_toolbar_title);
            CustomToolbar customToolbar = new CustomToolbar(tool,textView);
            toolbar =  customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
