package com.amplearch.beaconshop.Utils;

import android.content.Context;
import android.graphics.Typeface;
<<<<<<< HEAD
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import static java.security.AccessController.getContext;

=======
import android.util.AttributeSet;

>>>>>>> 15d6d0a93a36a372c1b59230999f34c88e86b97b
/**
 * Created by admin on 04/06/2017.
 */

<<<<<<< HEAD
public class TrojanText extends AppCompatTextView
{
=======
public class TrojanText extends android.support.v7.widget.AppCompatTextView {

>>>>>>> 15d6d0a93a36a372c1b59230999f34c88e86b97b
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
<<<<<<< HEAD
    Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
            "TrajanPro-Regular.ttf");
    setTypeface(tf);
}
=======
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "TrajanPro-Regular.ttf");
        setTypeface(tf);
    }
>>>>>>> 15d6d0a93a36a372c1b59230999f34c88e86b97b

}
