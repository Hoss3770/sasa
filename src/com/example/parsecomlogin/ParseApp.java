package com.example.parsecomlogin;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApp extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.enableLocalDatastore(this);
		ParseObject.registerSubclass(Sasa.class);
		Parse.initialize(this, "t9TGVd4RFpupFvA4LtYmRqCdRpIWfhwna7mQv2P1", "0Ly6vF5LdfuROJvzoakPn9rWda0ML3p869PxFbgS");
		
		ParseUser.enableAutomaticUser();
		ParseUser.getCurrentUser().saveInBackground();
		ParseACL defaultACL = new ParseACL();
		ParseACL.setDefaultACL(defaultACL, true);
	}
}