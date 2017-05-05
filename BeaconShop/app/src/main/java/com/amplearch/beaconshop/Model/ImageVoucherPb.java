package com.amplearch.beaconshop.Model;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * Created by ample-arch on 5/3/2017.
 */

public class ImageVoucherPb
{
    private ImageView image ;
    private ProgressBar progressBar;
    private RoundedImageView roundedImageView ;

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public RoundedImageView getRoundedImageView() {
        return roundedImageView;
    }

    public void setRoundedImageView(RoundedImageView roundedImageView) {
        this.roundedImageView = roundedImageView;
    }
}
