package com.amplearch.beaconshop.Model;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.siyamed.shapeimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class PbWithImage
{
    private ImageView imageView ;

    private ProgressBar progressBar ;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
