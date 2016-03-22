package com.example.parsecomlogin;

import org.json.JSONArray;
import org.json.JSONException;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SasaDetailPage extends ActionBarActivity {
	Button downbtn;
	String objectId;
	UserAndData userAndData;
	ParseObject currentSasa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sasa_detail_page);
        Bundle extras = getIntent().getExtras();
        userAndData= new UserAndData().getInstance();
        objectId = (String) extras.get("objectId");
        
        
        downbtn = (Button)findViewById(R.id.downbtn);
        
        
        
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sasa");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject sasa, ParseException arg1) {
				currentSasa=sasa;
				int i;
				for(i=0;i<currentSasa.getJSONArray("fbidsDown").length();i++){
					try {
						if(currentSasa.getJSONArray("fbidsDown").get(i).equals(userAndData.fbid)){
							downbtn.setText("I'm not down");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}//end for
			}//end done
        });
		
				
		downbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Sasa");
				query.getInBackground(objectId, new GetCallback<ParseObject>() {
				  public void done(ParseObject sasa, ParseException e) {
				    if (e == null) {
				    	if(downbtn.getText().equals("I'm not down")){
				    		int i,index=0;
				    		JSONArray newfbids= new JSONArray();
				    		for(i=0;i<sasa.getJSONArray("fbidsDown").length();i++){
				    			try {
									if (!sasa.getJSONArray("fbidsDown").get(i).equals(userAndData.fbid)){
										newfbids.put(sasa.getJSONArray("fbidsDown").get(i));				
									}
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
				    		}//end for
				    			
					    	sasa.put("fbidsDown",newfbids);
					    	downbtn.setText("I'm down");
					    	sasa.saveInBackground();				    						    		
				    	}
				    	else {
					    	sasa.put("fbidsDown",sasa.getJSONArray("fbidsDown").put(userAndData.fbid));
					    	downbtn.setText("I'm not down");
					    	sasa.saveInBackground();				    		
				    	}
				    }//end if e==null
					finish();
				  }//end done
				});
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sasa_detail_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
