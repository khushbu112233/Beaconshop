package com.amplearch.beaconshop.WebCall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by ample-arch on 5/4/2017.
 */

public class JoyRequest extends AsyncTask<String, Integer, String>
{
    private String label;
    private OnJoyRequestComplete caller;
    private Context context;
    private String method = "GET";
    private List<NameValuePair> parameters = null;
    private ProgressDialog pDialog = null;


    public JoyRequest(OnJoyRequestComplete c, Activity a, String m, List p, String l) {
        caller = c;
        context = a;
        method = m;
        parameters = p;
        label = l;
    }

    // Three Constructors
    public JoyRequest(Activity a, String m, List<NameValuePair> p) {
        caller = (OnJoyRequestComplete) a;
        context = a;
        method = m;
        parameters = p;
    }

    public JoyRequest(Activity a, String m) {
        caller = (OnJoyRequestComplete) a;
        context = a;
        method = m;
    }

    public JoyRequest(Activity a) {
        caller = (OnJoyRequestComplete) a;
        context = a;
    }

    // Interface to be implemented by calling activity
    public interface OnJoyRequestComplete
    {
        public void joyResponse(String response);
    }


    protected String doInBackground(String... urls) {
        // get url pointing to entry point of API
        String address = urls[0].toString();
        if (method == "POST") {
            return post(address);
        }

        if (method == "GET") {
            return get(address);
        }

        return null;
    }

    protected void onPreExecute()
    {
        /*pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading data.."); // typically you will define such
        // strings in a remote file.
        //pDialog.isIndeterminate();
        pDialog.setCancelable(false);
        pDialog.show();*/
    }

    protected void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }

    protected void onPostExecute(String response)
    {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.joyResponse(response);
    }

    protected void onCancelled(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.joyResponse(response);
    }

    @SuppressWarnings("deprecation")
    protected String get(String address)
    {
        try
        {
            if (parameters != null)
            {
                String query = "";
                String EQ = "="; String AMP = "&";
                for (NameValuePair param : parameters) {
                    query += param.getName() + EQ + URLEncoder.encode(param.getValue()) + AMP;
                }
                if (query != "")
                {
                    address += "?" + query;
                }
            }
            HttpClient client = new DefaultHttpClient();
            HttpGet get= new HttpGet(address);
            HttpResponse response = client.execute(get);
            return stringifyResponse(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return null;
    }

    protected String post(String address) {
        try {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(address);

            if (parameters != null) {
                post.setEntity(new UrlEncodedFormEntity(parameters));
            }

            HttpResponse response = client.execute(post);
            return stringifyResponse(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        return null;
    }

    protected String stringifyResponse(HttpResponse response) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            return sb.toString();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
