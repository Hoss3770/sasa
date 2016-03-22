package com.example.parsecomlogin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends ActionBarActivity {
	UserAndData userAndData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userAndData = new UserAndData().getInstance();
		if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
			SharedPreferences pref = this.getSharedPreferences("Share", Context.MODE_PRIVATE);
			userAndData.groups = pref.getStringSet("groups", new HashSet<String>());
			Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
			startActivity(intent);
			finish();
		}
	}
}