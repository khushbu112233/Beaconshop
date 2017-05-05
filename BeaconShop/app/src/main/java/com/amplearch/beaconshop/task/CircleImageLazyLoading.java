package com.amplearch.beaconshop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import com.amplearch.beaconshop.Model.PbWithCircle;
import java.io.InputStream;
import java.net.URL;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class CircleImageLazyLoading extends AsyncTask<PbWithCircle,Void,Bitmap>
{
    private CircleImageView circleImageView = null;
    private ProgressBar pb = null;

    @Override
    protected Bitmap doInBackground(PbWithCircle... params)
    {
        this.circleImageView = (CircleImageView)params[0].getCircleImageView();
        this.pb = (ProgressBar)params[0].getProgressBar();
        return getCircleBitmapImage((String) circleImageView.getTag());
    }

    protected void onPostExecute(Bitmap result)
    {
        System.out.println("CircleImage Downloaded: " + circleImageView.getId());
        circleImageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        circleImageView.setImageBitmap(result); //set the bitmap to the imageview.
    }

    /** This function downloads the image and returns the Bitmap **/
    private Bitmap getCircleBitmapImage(String url) {
        System.out.println("String URL " + url);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            bitmap = getResizedBitmap(bitmap, 100, 100);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /** decodes image and scales it to reduce memory consumption **/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
