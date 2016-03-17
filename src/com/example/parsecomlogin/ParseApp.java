package com.example.parsecomlogin;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApp extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "t9TGVd4RFpupFvA4LtYmRqCdRpIWfhwna7mQv2P1",
				"0Ly6vF5LdfuROJvzoakPn9rWda0ML3p869PxFbgS");
		
		ParseUser.enableAutomaticUser();
		ParseACL defauAcl = new ParseACL();
		
		defauAcl.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defauAcl, true);
	}
}