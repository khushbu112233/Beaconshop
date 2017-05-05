package com.amplearch.beaconshop.Model;

import android.widget.ProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class PbWithCircle
{
    private CircleImageView circleImageView ;
    private ProgressBar progressBar ;

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
