package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by admin on 09/21/2017.
 */

public class cgTextView extends AppCompatTextView
{
    public cgTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public cgTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public cgTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "century-gothic-1361531616.ttf");
        setTypeface(tf);
    }

}

