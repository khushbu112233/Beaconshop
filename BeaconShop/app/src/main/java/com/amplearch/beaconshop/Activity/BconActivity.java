package com.amplearch.beaconshop.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BconActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcon);
        imageView = (ImageView) findViewById(R.id.image);


        connectWithHttpPost();


    }


    private void connectWithHttpPost() {

        // Connect with a server is a time consuming process.
        //Therefore we use AsyncTask to handle it
        // From the three generic types;
        //First type relate with the argument send in execute()
        //Second type relate with onProgressUpdate method which I haven't use in this code
        //Third type relate with the return type of the doInBackground method, which also the input type of the onPostExecute method
        class HttpGetAsyncTask extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... params) {

                try {
                    URL url = new URL("http://beacon.ample-arch.com/Images/images.jpg");
                    URLConnection conn = url.openConnection();
                    InputStream inStr = conn.getInputStream();
                    BufferedInputStream buffInStr = new BufferedInputStream(inStr);
                    // Need to check the size of Byte Array Buffer.
                    ByteArrayBuffer baf = new ByteArrayBuffer(500);
                    int current = 0;
                    while ((current = buffInStr.read()) != -1) {
                        baf.append((byte) current);
                    }
                    byte[] array = baf.toByteArray();

                    Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
                    //  ImageView image = (ImageView) findViewById(R.id.imageView1);

                    return bmp;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            // Argument comes for this method according to the return type of the doInBackground() and
            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);

                imageView.setImageBitmap(Bitmap.createScaledBitmap(result, imageView.getWidth(),
                        imageView.getHeight(), false));
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute();

    }
}
