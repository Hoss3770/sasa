package com.example.parsecomlogin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsPage extends ActionBarActivity {
	UserAndData userAndData;
	ListView groupsList;
    ArrayList<String> groupsListItems=new ArrayList<String>();
    ArrayAdapter<String> groupsListAdapter;

    SharedPreferences pref;
    
	Button submitbtn;
	TextView grouptxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups_page);
		pref = this.getSharedPreferences("Share", Context.MODE_PRIVATE);
		
		userAndData = new UserAndData().getInstance();
		//get stuff from memory
		groupsList = (ListView) findViewById(R.id.listview);
		
		for(String s: userAndData.groups)
			groupsListItems.add(s);

		groupsListAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,groupsListItems);
		groupsList.setAdapter(groupsListAdapter);
		
		submitbtn = (Button) findViewById(R.id.submitbtn);
		grouptxt = (TextView) findViewById(R.id.groupcodetxt);
		
		submitbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text = "...";
				byte[] data = Base64.decode(grouptxt.getText().toString(), Base64.DEFAULT);
				try {
					text = new String(data, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//make sure it's not there
				if(groupsListItems.indexOf(text)==-1){
					userAndData.groups.add(text);
					
					SharedPreferences.Editor edit = pref.edit();
					edit.putStringSet("groups", userAndData.groups);
					edit.commit();

					
					groupsListItems.add(text);
					groupsListAdapter.notifyDataSetChanged();
				}
				
			}
		});//submitbtn
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.groups_page, menu);
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
