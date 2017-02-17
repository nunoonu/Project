package com.example.nu.myapplication.Utils;

/**
 * Created by EVILUTION on 2/16/2017.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.nu.myapplication.R;

public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
        setTextColorCustom();
        setTextSizeCustom();
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
        setTextColorCustom();
        setTextSizeCustom();
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
        setTextColorCustom();
        setTextSizeCustom();
    }
    private void setTextSizeCustom(){
        setTextSize(TypedValue.COMPLEX_UNIT_DIP,11);
    }
    private void setTextColorCustom(){
        setTextColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.SteelBlue));
    }
    private void setFont() {

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Capture_it.ttf");
        setTypeface(typeface);
      // setTypeface(typeface, Typeface.NORMAL);
    }
}