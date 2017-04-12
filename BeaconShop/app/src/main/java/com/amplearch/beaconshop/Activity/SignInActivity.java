package com.amplearch.beaconshop.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.CallSoap;
import com.amplearch.beaconshop.Utils.Caller;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignInActivity extends Activity
{
    TextView tvSignIn;
    EditText etEmailAdd, etPass;
    public static String rslt="";

    OkHttpClient client;
    //MediaType JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final  AlertDialog ad=new AlertDialog.Builder(this).create();
        etEmailAdd = (EditText)findViewById(R.id.etEmailAdd);
        etPass = (EditText)findViewById(R.id.etPass);

        tvSignIn = (TextView)findViewById(R.id.tvSignIn);

        client = new OkHttpClient();
        //JSON = MediaType.parse("application/json; charset=utf-8");

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = etEmailAdd.getText().toString();
                String pass = etPass.getText().toString();

                //CallSoap cs=new CallSoap();

                if (!isValidEmail(email)) {
                    etEmailAdd.setError("Email Id is not in Valid Format.");
                }

                else if (!isValidPassword(pass)) {
                    etPass.setError("Minimum 5 characters.");
                }

                else
                {
                    connectWithHttpGet("jay.ample@gmail.com", "jay123");

               /* startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
               */ }
            }
        });
    }


    private void connectWithHttpGet(String givenUsername, String givenPassword) {

        // Connect with a server is a time consuming process.
        //Therefore we use AsyncTask to handle it
        // From the three generic types;
        //First type relate with the argument send in execute()
        //Second type relate with onProgressUpdate method which I haven't use in this code
        //Third type relate with the return type of the doInBackground method, which also the input type of the onPostExecute method
        class HttpGetAsyncTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings as the argument
                //We need to specifically get the givenUsername and givenPassword
                String paramUsername = params[0];
                String paramPassword = params[1];
                System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL
                HttpGet httpGet = new HttpGet("/SQLWebService.asmx/Login?uname=jay.ample@gmail.com&type=simple&pwd=jay123");

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    System.out.println("httpResponse");

                    // getEntity() ; obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the entity.
                    // Now we need a readable source to read the byte stream that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity().getContent();

                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
                    //The buffer size is 8K
                    //Therefore we need a mechanism to append the separately coming chunks in to one String element
                    // We have to use a class that can handle modifiable sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go through each and every chunk of characters
                    //and assign a each chunk to bufferedStrChunk String variable
                    //and append that value one by one to the stringBuilder
                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    //We return that value then the onPostExecute() can handle the content
                    System.out.println("Returning value of doInBackground :" + stringBuilder.toString());

                    // If the Username and Password match, it will return "working" as response
                    // If the Username or Password wrong, it will return "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exception generates caz of httpResponse :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second exception generates caz of httpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of the doInBackground() and
            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
                /*if(result.equals("working")){
                    Toast.makeText(getApplicationContext(), "HTTP GET is working...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid...", Toast.LENGTH_LONG).show();
                }*/
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(givenUsername, givenPassword);

    }

    public class GetTask extends AsyncTask {
        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String getResponse = get("https://publicobject.com/helloworld.txt");
                return getResponse;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(String getResponse) {
            System.out.println(getResponse);
        }

        public String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }
}
