package com.example.parsecomlogin;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

public class Welcome extends ActionBarActivity {

	Button logout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		String struser = currentUser.getUsername().toString();
		
		TextView txtUser = (TextView) findViewById(R.id.txtuser);
		txtUser.setText("You are logged in as " + struser);
		
		logout = (Button) findViewById(R.id.logout);
		
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				finish();
			}
		});
	}
}