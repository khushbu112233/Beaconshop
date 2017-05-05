package com.amplearch.beaconshop.Model;

import android.widget.ProgressBar;

import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class PbWithRoundImage
{
    private RoundedImageView roundedImageView ;
    private ProgressBar progressBar ;

    public RoundedImageView getRoundedImageView() {
        return roundedImageView;
    }

    public void setRoundedImageView(RoundedImageView roundedImageView) {
        this.roundedImageView = roundedImageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
