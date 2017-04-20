package com.amplearch.beaconshop;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ImageUpload extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView imgView;
    private Button upload;
    private EditText caption;
    private Bitmap bitmap;
    private ProgressDialog dialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        imgView = (ImageView) findViewById(R.id.ImageView);
        upload = (Button) findViewById(R.id.Upload);
        caption = (EditText) findViewById(R.id.Caption);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE);

        upload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                SendHttpRequestTask t = new SendHttpRequestTask();

               // String[] params = new String[]{url, param1, param2};
              //  t.execute(params);

                if (bitmap == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please select image", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = ProgressDialog.show(ImageUpload.this, "Uploading",
                            "Please wait...", true);
                    new ImageUploadTask().execute();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.imageupload_menu, menu);
        return true;
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String param1 = params[1];
            String param2 = params[2];
            Bitmap b = BitmapFactory.decodeResource(ImageUpload.this.getResources(), R.drawable.ic_heart_blue);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 0, baos);

      /*      try {
                HttpClient client = new DefaultHttpClient();
                client.connectForMultipart();
                client.addFormPart("param1", param1);
                client.addFormPart("param2", param2);
                client.addFilePart("file", "logo.png", baos.toByteArray());
                client.finishMultipart();
                String data = client.getResponse();
            }*/
         /*   catch(Throwable t) {
                t.printStackTrace();
            }*/

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            //item.setActionView(null);

        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ic_menu_gallery:
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            PICK_IMAGE);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            e.toString(),
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String filePath = null;

                    try {
                        // OI FILE Manager
                        String filemanagerstring = selectedImageUri.getPath();

                        // MEDIA GALLERY
                        String selectedImagePath = getPath(selectedImageUri);

                        if (selectedImagePath != null) {
                            filePath = selectedImagePath;
                        } else if (filemanagerstring != null) {
                            filePath = filemanagerstring;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unknown path",
                                    Toast.LENGTH_LONG).show();
                            Log.e("Bitmap", "Unknown path");
                        }

                        if (filePath != null) {
                            decodeFile(filePath);
                        } else {
                            bitmap = null;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Internal error",
                                Toast.LENGTH_LONG).show();
                        Log.e(e.getClass().getName(), e.getMessage(), e);
                    }
                }
                break;
            default:
        }
    }

    class ImageUploadTask extends AsyncTask <Void, Void, String>{
        @Override
        protected String doInBackground(Void... unsued) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/UploadFile");

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();

                entity.addPart("f", new ByteArrayBody(data,
                        "myImage.jpg"));
                entity.addPart("fileName", new StringBody("first"));
                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        e.toString(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
                return null;
            }

            // (null);
        }

        @Override
        protected void onProgressUpdate(Void... unsued) {

        }

        @Override
        protected void onPostExecute(String sResponse) {
            try {
                if (dialog.isShowing())
                    dialog.dismiss();

                Toast.makeText(getApplicationContext(), sResponse, Toast.LENGTH_LONG).show();
               /* if (sResponse != null) {
                    JSONObject JResponse = new JSONObject(sResponse);
                    int success = JResponse.getInt("SUCCESS");
                    String message = JResponse.getString("MESSAGE");
                    if (success == 0) {
                        Toast.makeText(getApplicationContext(), message,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Photo uploaded successfully",
                                Toast.LENGTH_SHORT).show();
                        caption.setText("");
                    }
                }*/
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.toString(),
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
            break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        imgView.setImageBitmap(bitmap);

    }
}