package com.example.nu.myapplication.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nu.myapplication.R;
import com.example.nu.myapplication.Utils.CustomToolbar;

import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

public class NotifyActivity extends AppCompatActivity {
    Toolbar toolbar;
    String idChild;
    String idParent;
    private final String TAG = this.getClass().getSimpleName();
    String jsonobject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        Intent intent = getIntent();
        idParent = intent.getStringExtra("idParent");
        idChild = intent.getStringExtra("idChild");
        jsonobject = intent.getStringExtra("jsonobject");
        initToolBar();
        initBackButton();
        EditText edittext = (EditText) findViewById(R.id.editText);
        if(edittext.getText().toString().matches("") )
            Log.d(TAG,"edittext");
        else
            Log.d(TAG,"edittext != nulkl"+edittext.getText().toString().trim());
    }
    public void initToolBar() {
        try {
            Toolbar tool = (Toolbar) findViewById(R.id.toolbar_notify);
            TextView textView = (TextView) findViewById(R.id.notify_toolbar_title);
            textView.setText("Notify");
            CustomToolbar customToolbar = new CustomToolbar(tool, textView);
            toolbar = customToolbar.getToolbar();
            Typeface typeface = Typeface.createFromAsset(getAssets(), "Capture_it.ttf");//
            textView.setTypeface(typeface);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public Boolean isEmptyEditText(EditText editText){
       if( editText.getText().toString().matches(""))
           return true;
        else
           return false;
    }
    public void initBackButton(){
        FancyButton button = new FancyButton(this);
        button.setRadius(10);
        button.setText("");
        button.setIconResource("\uf053");
        button.setFontIconSize(30);
        button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.LightSeaGreen));
        //button.setBackgroundResource(R.drawable.ic_action_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAlarm = new Intent(NotifyActivity.this, ChildActivity.class);
                intentAlarm.putExtra("idParent",idParent);
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

}
