package com.amplearch.beaconshop.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.amplearch.beaconshop.Model.PbWithImage;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class LazyLoadingTask  extends AsyncTask<PbWithImage, Void, Bitmap>
{
    private ImageView imageview = null ;
    private CircleImageView circleimageview = null ;
    private RoundedImageView roundedimageview = null ;
    private ProgressBar progressbar = null ;

    @Override
    protected Bitmap doInBackground(PbWithImage... params)
    {
        Bitmap bitmap = null ;

        this.imageview = (ImageView)params[0].getImageView();
        this.progressbar = (ProgressBar)params[0].getProgressBar();
       return getImageViewBitmap((String) imageview.getTag());
       /* try
        {
            this.imageview = (ImageView)params[0].getImageView();
            this.progressbar = (ProgressBar)params[0].getProgressBar();
             getImageViewBitmap((String) imageview.getTag());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

       /* try
        {
            this.circleimageview = (CircleImageView)params[0].getCircleImageView();
            this.progressbar = (ProgressBar)params[0].getProgressBar();
            return getCircleImageBitmap((String) circleimageview.getTag());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

        /*try
        {
            this.roundedimageview = (RoundedImageView)params[0].getRoundedImageView();
            this.progressbar = (ProgressBar)params[0].getProgressBar();
             getRoundedImageBitmap((String) roundedimageview.getTag());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

    }

    protected void onPostExecute(Bitmap result)
    {
        /*System.out.println("Downloaded " + imageview.getId());
        System.out.println("Downloaded " + circleimageview.getId());
        System.out.println("Downloaded " + roundedimageview.getId());*/

        imageview.setVisibility(View.VISIBLE);
        circleimageview.setVisibility(View.VISIBLE);
        roundedimageview.setVisibility(View.VISIBLE);

        progressbar.setVisibility(View.GONE);// hide the progressbar after downloading the image.

        imageview.setImageBitmap(result); //set the bitmap to the imageview.
        circleimageview.setImageBitmap(result);
        roundedimageview.setImageBitmap(result);
    }

    private Bitmap getBitmapImage(String string)
    {
        Bitmap bitmap = null ;

        try
        {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(string).getContent());
            bitmap = getResizedBitmap(bitmap, 100, 100);
            return bitmap;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
    /** This function downloads the image from byte[] string and returns the Bitmap **/
    private Bitmap getImageViewBitmap(String string)
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

    /** This function downloads the image from url string and returns the Bitmap **/
    private Bitmap getCircleImageBitmap(String url)
    {
        System.out.println("String URL " + url);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url)
                    .getContent());
            bitmap = getResizedBitmap(bitmap, 100, 100);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /** This function downloads the image from byte[] string and returns the Bitmap **/
    private Bitmap getRoundedImageBitmap(String string)
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
    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
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
