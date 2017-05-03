package com.amplearch.beaconshop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amplearch.beaconshop.Model.ImageVoucherPb;
import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * Created by ample-arch on 5/3/2017.
 */
public class DownloadImageTask3 extends AsyncTask<ImageVoucherPb, Void, Bitmap>
{
    private ProgressBar pb1 = null;
    private RoundedImageView roundedImageView1 = null ;

    protected Bitmap doInBackground(ImageVoucherPb... ivp)
    {
        this.roundedImageView1 = (RoundedImageView)ivp[0].getRoundedImageView();
        this.pb1 = (ProgressBar)ivp[0].getProgressBar();
        return getBitmapDownloaded((String) roundedImageView1.getTag());
    }


    protected void onPostExecute(Bitmap result) {
        System.out.println("Downloaded " + roundedImageView1.getId());
        roundedImageView1.setVisibility(View.VISIBLE);
        pb1.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        roundedImageView1.setImageBitmap(result); //set the bitmap to the imageview.
    }

   /* protected void onPreExecute()
    {
        pb1.setVisibility(View.VISIBLE);
    }*/


    private Bitmap getBitmapDownloaded(String string)
    {
        Bitmap bitmap = null ;
        try
        {
            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            bitmap = getResizedBitmap(bitmap, 100, 100);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }

    /** decodes image and scales it to reduce memory consumption **/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
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