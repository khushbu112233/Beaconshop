package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by ample-arch on 4/6/2017.
 */

public class TrojanCheckBox extends AppCompatCheckBox {

    public TrojanCheckBox(Context context) {
        super(context);
        init();
    }

    public TrojanCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrojanCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "TrajanPro-Regular.ttf");
        setTypeface(tf);
    }
}
