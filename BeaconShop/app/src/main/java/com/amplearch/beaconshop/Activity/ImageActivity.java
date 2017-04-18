package com.amplearch.beaconshop.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.Images;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ImageActivity extends AppCompatActivity
{

    String urlSrc;
    Bitmap myBitmap;
    Button btnConvert, btnImport, btnExport ;
    ImageView ivBitmapImage;
    TextView tvImageUrl ;

    byte[] byteArray ;

    DatabaseHelper db ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        checkConnection();
        urlSrc = " http://beacon.ample-arch.com/Images/vmart.jpg ";

        tvImageUrl = (TextView) findViewById(R.id.tvImageUrl);
        ivBitmapImage = (ImageView)findViewById(R.id.ivBitmapImage);
        btnConvert = (Button)findViewById(R.id.btnConvert);
        btnImport = (Button)findViewById(R.id.btnImport);
        btnExport = (Button)findViewById(R.id.btnExport);

        tvImageUrl.setText("URL: "+urlSrc);
        Log.d("URL", urlSrc);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MyAsync myAsync = new MyAsync();
                myAsync.execute();
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = "Sorry! No Internet connection.";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
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
            byteArray = stream.toByteArray();

            Toast.makeText(getApplicationContext(),"Byte: "+byteArray,Toast.LENGTH_LONG).show();

            // for convert byte array to bitmap
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//            ivBitmapImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, ivBitmapImage.getWidth(),
//                    ivBitmapImage.getHeight(), false));
            ivBitmapImage.setImageBitmap(bmp);

            Toast.makeText(getApplicationContext(),"Bitmap: "+bmp,Toast.LENGTH_LONG).show();

            db = new DatabaseHelper(getApplicationContext());

            btnImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    db.addImageData("V-Mart",byteArray);
                    Toast.makeText(getApplicationContext(),"Image: V-Mart "+byteArray+ " has to be added.",Toast.LENGTH_LONG).show();
                }
            });

            btnExport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    List<Images> img = db.getImageData();

                    int id = 0;
                    String name = null;
                    byte[] image = new byte[0];
                    for(Images todo : img)
                    {
                         id = todo.getId();
                         name = todo.getName();
                         image = todo.getImage();
                    }
                    // for convert byte array to bitmap
                    Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                    tvImageUrl.setText(id + name);
                    ivBitmapImage.setImageBitmap(bmp);
                }
            });

        }
    }
}
