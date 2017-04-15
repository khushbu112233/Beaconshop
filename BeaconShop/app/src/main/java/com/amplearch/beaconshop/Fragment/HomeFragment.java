package com.amplearch.beaconshop.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ElectronicOfferActivity;
import com.amplearch.beaconshop.Activity.MainActivity;
import com.amplearch.beaconshop.Activity.SignInActivity;
import com.amplearch.beaconshop.Adapter.CategoryAdapter;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Activity.StoreListActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    GridView listCategory;
   // String[] web;
    int[] imageId = {
            R.drawable.ic_round,
            R.drawable.ic_round,
            R.drawable.ic_round,
            R.drawable.ic_round,
            R.drawable.ic_round,
            R.drawable.ic_round,
            R.drawable.ic_round

    };

    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    ArrayList<String> id_array = new ArrayList<String>();
    String category_id;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


       // web = new String[imageId.length];
        listCategory =(GridView) rootView.findViewById(R.id.category_list);

        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
              //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), ElectronicOfferActivity.class);
                category_id = id_array.get(i).toString();
                intent.putExtra("category_id", category_id);
               // Toast.makeText(getContext(), id_array.get(i).toString(), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        connectWithHttpPost();

        return rootView;
    }

    private void connectWithHttpPost() {

        // Connect with a server is a time consuming process.
        //Therefore we use AsyncTask to handle it
        // From the three generic types;
        //First type relate with the argument send in execute()
        //Second type relate with onProgressUpdate method which I haven't use in this code
        //Third type relate with the return type of the doInBackground method, which also the input type of the onPostExecute method
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings as the argument
                //We need to specifically get the givenUsername and givenPassword
              //  String paramUsername = params[0];
               // String paramPassword = params[1];
          //      System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://beacon.ample-arch.com/BeaconWebService.asmx/GetCategories");

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

                if (result.equals("")){
                    Toast.makeText(getContext(), "Category not Loaded..", Toast.LENGTH_LONG).show();
                }else {
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("category");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null){

                            Toast.makeText(getContext(), "No Categories are Available..", Toast.LENGTH_LONG).show();
                        }
                        else {

                           /* JSONArray jsonArrayChanged = jsonObject.getJSONArray("User");

                            String email_id = jsonArrayChanged.getJSONObject(0).get("email_id").toString();
                            String user_id = jsonArrayChanged.getJSONObject(0).get("id").toString();
                            String username = jsonArrayChanged.getJSONObject(0).get("username").toString();
                            String image = jsonArrayChanged.getJSONObject(0).get("image").toString();
                            String dob = jsonArrayChanged.getJSONObject(0).get("dob").toString();
                            String gender = jsonArrayChanged.getJSONObject(0).get("gender").toString();
                            String password = jsonArrayChanged.getJSONObject(0).get("password").toString();
*/

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("category");
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
                                    //JSONObject jObject = jsonArrayChanged.getJSONObject(i);

                                    title_array.add(jsonArrayChanged.getJSONObject(i).get("category_name").toString());
                                    notice_array.add(jsonArrayChanged.getJSONObject(i).get("category_image").toString());
                                    id_array.add(jsonArrayChanged.getJSONObject(i).get("category_id").toString());
                                //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                                    CategoryAdapter adapterViewAndroid = new CategoryAdapter(getContext(), title_array, notice_array);
                                    listCategory.setAdapter(adapterViewAndroid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                           // Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                           /* Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute();

    }



}
