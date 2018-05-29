package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class GillSansShowPassEditText extends com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText
{
    public GillSansShowPassEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GillSansShowPassEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GillSansShowPassEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "gillsans.ttf");
        setTypeface(tf);
        setTextSize(17);
    }

}
