package com.example.nu.myapplication.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.nu.myapplication.R;

/**
 * Created by EVILUTION on 27/1/2560.
 */

public class CustomToolbar extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView;

    public CustomToolbar(Toolbar toolbar, TextView textView){
        this.toolbar = toolbar;
        this.textView = textView;
    }
    public  Toolbar getToolbar(){
     /*   setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/
        return  toolbar;
    }

}
