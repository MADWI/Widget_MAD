package com.mad.helloservices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloServicesActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final Button btStart = (Button)findViewById(R.id.btStart);
        btStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startService(new Intent(getApplicationContext(),PrzykladowaUsluga.class));         	
            }
        });
        final Button btStop = (Button)findViewById(R.id.btStop);
        btStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	stopService(new Intent(getApplicationContext(), PrzykladowaUsluga.class));
            }
        });        	  
    }    
}