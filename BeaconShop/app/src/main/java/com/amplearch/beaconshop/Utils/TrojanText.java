package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import static java.security.AccessController.getContext;

/**
 * Created by admin on 04/06/2017.
 */

public class TrojanText extends AppCompatTextView
{
    public TrojanText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TrojanText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrojanText(Context context) {
        super(context);
        init();
    }

    private void init() {
    Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
            "TrajanPro-Regular.ttf");
    setTypeface(tf);
}

}
