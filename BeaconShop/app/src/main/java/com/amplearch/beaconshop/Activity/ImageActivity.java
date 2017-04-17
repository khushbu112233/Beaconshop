package com.amplearch.beaconshop.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageActivity extends AppCompatActivity
{

    String urlSrc;
    Bitmap myBitmap;
    ImageView ivBitmapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        urlSrc = " http://beacon.ample-arch.com/Images/vmart.jpg ";

        TextView tvImageUrl = (TextView) findViewById(R.id.tvImageUrl);
        ivBitmapImage = (ImageView)findViewById(R.id.ivBitmapImage);
        Button btnConvert = (Button)findViewById(R.id.btnConvert);

        tvImageUrl.setText("URL: "+urlSrc);
        Log.d("URL", urlSrc);



//        Bitmap bitmap = getBitmapFromURL(urlSrc);

//        ivBitmapImage.setImageBitmap(bitmap);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MyAsync myAsync = new MyAsync();
                myAsync.execute();
            }
        });
    }

    public class MyAsync extends AsyncTask<Void, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(Void... params)
        {
            // for converting url to bitmap
            try {
                URL url = new URL(urlSrc);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);

//            ivBitmapImage.setImageBitmap(bitmap);

            // for convert bitmap to byte array
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Toast.makeText(getApplicationContext(),"Byte: "+byteArray,Toast.LENGTH_LONG).show();

            // for convert byte array to bitmap
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            ivBitmapImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, ivBitmapImage.getWidth(),
//                    ivBitmapImage.getHeight(), false));
            ivBitmapImage.setImageBitmap(bmp);

            Toast.makeText(getApplicationContext(),"Bitmap: "+bmp,Toast.LENGTH_LONG).show();

        }
    }
}
