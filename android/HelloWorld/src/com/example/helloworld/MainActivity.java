package com.example.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String LOGIN_COUNT = "com.example.helloworld.LOGIN_COUNT";
	public final static String USERNAME = "com.example.helloworld.USERNAME";
	
	private TextView loginMessage;
	private EditText user;
	private EditText password;
	private MainActivity myApp = this; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginMessage = (TextView) findViewById(R.id.LoginMessage);
        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Login button */
    public void login(View view) {
        String stringUrl = "http://enigmatic-retreat-6639.herokuapp.com/users/login";
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new getJSON().execute(stringUrl);
        } else {
            loginMessage.setText("No network connection available.");
        }
    }

    /** Called when the user clicks the Add User button */
    public void addUser(View view) {
        String stringUrl = "http://enigmatic-retreat-6639.herokuapp.com/users/add";
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new getJSON().execute(stringUrl);
        } else {
            loginMessage.setText("No network connection available.");
        }
    }
    
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the JSON data at the URL
    private class getJSON extends AsyncTask {
      
    	protected JSONObject doInBackground(Object... urls) {
           // params comes from the execute() call: params[0] is the url.
           try {
               return postUrl((String) urls[0]);
           } catch (IOException e) {
               return null;
           }
       }
       // onPostExecute reads JSON Object and responds appropriately to the error messages.
       protected void onPostExecute(Object o) {
    	   try {
	           JSONObject json = (JSONObject) o; 
	    	   if (json != null) {
	    		   switch (json.getInt("errCode")) {
	    			   case 1:
	    				   Intent intent = new Intent(myApp, WelcomeMessageActivity.class);
	    				   intent.putExtra(LOGIN_COUNT, json.getInt("count"));
	    				   intent.putExtra(USERNAME, user.getText().toString());
	    				   startActivity(intent);
	    				   break;
	    			   case -1:
	    				   loginMessage.setText("Opps, wrong username and/or password");
	    				   break;
	    			   case -2:
	    				   loginMessage.setText("Opps, username already taken. Please try another one");
	    				   break;
	    			   case -3:
	    				   loginMessage.setText("Opps, invalid username. Username should be at most 128 characters long");
	    				   break;
	    			   case -4:
	    				   loginMessage.setText("Opps, invalid password. Password should be at most 128 characters long");
	    				   break;
	    		   }
	    	   }
    	   } catch (Exception e) {
    		   loginMessage.setText("Something Unexpected happened");
    	   }
       }
       
    // Given a URL, establishes an HttpUrlConnection, POSTS JSON data
    // and retrieves the JSON Object back from the server
    private JSONObject postUrl(String myurl) throws IOException {
        InputStream is = null;   
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
        	// Post JSON data
            JSONObject jsonToSend = new JSONObject();
            jsonToSend.put("user", user.getText().toString());
            jsonToSend.put("password", password.getText().toString());
        	//byte[] outputBytes = jsonString.getBytes("UTF-8");
        	OutputStream os = conn.getOutputStream();
        	os.write(jsonToSend.toString().getBytes());
         	Log.v("postUrl", Integer.valueOf(conn.getResponseCode()).toString());
            if(conn.getResponseCode() == 200) {
                // Connection was established. Get the content. 
            	is = conn.getInputStream();
            	String jsonString = convertStreamToString(is);
            	Log.v("postUrl", jsonString);
	            // Convert the InputStream into a JSON Object
	            return new JSONObject(jsonString);
            } else {
            	return null;
            }
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.
        } catch (Exception e) {
        	return null;
    	} finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

   }
    
}
