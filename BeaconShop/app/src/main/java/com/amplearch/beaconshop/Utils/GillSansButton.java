package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class GillSansButton extends AppCompatButton
{
    public GillSansButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GillSansButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GillSansButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "gillsans.ttf");
        setTypeface(tf);
    }

}