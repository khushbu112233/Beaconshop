package com.amplearch.beaconshop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amplearch.beaconshop.Model.PbAndImage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ample-arch on 5/3/2017.
 */

public class DownloadImageTask1 extends AsyncTask<PbAndImage, Void, Bitmap>
{
    private ImageView imageView = null;
    private ProgressBar pb = null;

    protected Bitmap doInBackground(PbAndImage... pb_and_images)
    {
        this.imageView = (ImageView)pb_and_images[0].getImge();
        this.pb = (ProgressBar)pb_and_images[0].getPb();
        return getBitmapDownloaded((String) imageView.getTag());
    }

   /* protected void onPreExecute()
    {
        pb.setVisibility(View.VISIBLE);
    }
*/

    protected void onPostExecute(Bitmap result) {
        System.out.println("Downloaded " + imageView.getId());
        imageView.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);  // hide the progressbar after downloading the image.
        imageView.setImageBitmap(result); //set the bitmap to the imageview.
    }

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