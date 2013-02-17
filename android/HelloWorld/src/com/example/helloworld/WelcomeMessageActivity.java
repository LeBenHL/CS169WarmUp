package com.example.helloworld;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class WelcomeMessageActivity extends Activity {
	
	private TextView welcomeMessage;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USERNAME);
        Integer count = intent.getIntExtra(MainActivity.LOGIN_COUNT, -1); 
        Log.v("onCreate", Integer.valueOf(count).toString());
        Log.v("onCreate", username);
        String message = String.format("Welcome %s.\n You have logged in %d times.", username, count);
        Log.v("onCreate", message);
        setContentView(R.layout.activity_welcome_message);
        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        welcomeMessage.setText(message);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Logout button */
    public void logout(View view) {
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
