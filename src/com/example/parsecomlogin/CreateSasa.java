package com.example.parsecomlogin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;

import com.parse.ParseObject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateSasa extends ActionBarActivity {
	UserAndData userAndData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_sasa);
		userAndData= new UserAndData().getInstance();
		int i=0;//i use that for the id
        for(String group: userAndData.groups){
        	i++;
        	RadioButton rdbtn = new RadioButton(this);
        	rdbtn.setId(i*i);
        	rdbtn.setChecked(true);
        	rdbtn.setId(33);
        	rdbtn.setText(group);
        	((ViewGroup) findViewById(R.id.groupNameRadio)).addView(rdbtn);
        }
        
		Button submitbtn = (Button)findViewById(R.id.submitbtn);
		submitbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Sasa sasa= new Sasa();
				
				TimePicker timePicker=(TimePicker) findViewById(R.id.timePicker);
				


				///////////////////////get Date from date radio//////////////
				RadioGroup dateRadioGroup= (RadioGroup) findViewById(R.id.dateRadio);
				int selectedDateid=dateRadioGroup.getCheckedRadioButtonId();
				RadioButton dateRadioButton= (RadioButton)findViewById(selectedDateid);
				String todaytomorrow=(String) dateRadioButton.getText();
				int hour = timePicker.getCurrentHour();
				int minute = timePicker.getCurrentMinute();
				Date dd=new Date();
				dd.setHours(hour);
				dd.setMinutes(minute);
				Calendar c = Calendar.getInstance();
				c.setTime(dd);
				if(todaytomorrow.equals("tomorrow"))
					c.add(Calendar.DATE, 1);
				dd=c.getTime();
				sasa.put("eventDate", dd);
				//////////////////////////////////////////////////
				
				
				TextView titleTxt=(TextView)findViewById(R.id.titletxt);
				String title= titleTxt.getText().toString();
				sasa.put("title", title);
				
				///////////////////////get Date from date radio//////////////
				dateRadioGroup= (RadioGroup) findViewById(R.id.groupNameRadio);
				selectedDateid=dateRadioGroup.getCheckedRadioButtonId();
				dateRadioButton= (RadioButton)findViewById(selectedDateid);
				String groupName=(String) dateRadioButton.getText();
				sasa.put("groups", new JSONArray().put(groupName));
				//////////////////////////////////////
				
				sasa.put("fbid", userAndData.fbid);
				sasa.put("firstName", userAndData.firstName);
				sasa.put("fullName", userAndData.firstName +" "+ userAndData.lastName);
				
				sasa.put("fbidsDown", new JSONArray());
				sasa.put("fbidsNotDown", new JSONArray());
				
				// Immediately save the data asynchronously
				sasa.saveInBackground();
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_sasa, menu);
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