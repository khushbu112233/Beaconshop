package com.amplearch.beaconshop.Model;

import android.widget.ImageView;
import android.widget.ProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 5/3/2017.
 */

public class PbAndImage
{
        private CircleImageView img;
        private ImageView imge ;
        private ProgressBar pb;

    public ImageView getImge() {
        return imge;
    }

    public void setImge(ImageView imge) {
        this.imge = imge;
    }

    public CircleImageView getImg() {
            return img;
        }

    public void setImg(CircleImageView img) {
            this.img = img;
        }

    public ProgressBar getPb() {
            return pb;
        }

    public void setPb(ProgressBar pb) {
            this.pb = pb;
        }

    }

