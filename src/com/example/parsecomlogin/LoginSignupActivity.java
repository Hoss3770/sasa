package com.example.parsecomlogin;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;

import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
 
public class LoginSignupActivity extends Activity {
    // Declare Variables
    UserAndData userAndData;
    ListView listview;
    SasasListViewAdapter listViewAdapter;
    Context context;
            
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        
        context=this;
        userAndData= new UserAndData().getInstance();
        
        Button createbtn= (Button)findViewById(R.id.createbtn);
        Button refreshbtn= (Button)findViewById(R.id.refreshbtn);
        Button addGroupBtn = (Button)findViewById(R.id.addgroupbtn);
        
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                Intent intent = new Intent(context,GroupsPage.class);
                startActivity(intent);
			}
		});
        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateSasa.class);
                startActivity(intent);
            }
        });
        
        refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoteDataTask().execute();
            }
        });
        
        new RemoteDataTask().execute();
    }
 
    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Sasa");
            try {
                userAndData.SasasParseList = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            listview = (ListView) findViewById(R.id.listview);
            listViewAdapter =  new SasasListViewAdapter(context, userAndData.SasasParseList);
            listview.setAdapter(listViewAdapter);
            listview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub
                    position = listViewAdapter.resolveRowPosition(position);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("objectId",listViewAdapter.rowIndexTo_idMap.get(position));
                    Intent intent = new Intent(context,SasaDetailPage.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
                
            });
        }
    }
}

class SasasListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> sasasTitles,sasasfbids,sasasTimes,sasasGroups,rowIndexTo_idMap;
    ArrayList<Integer> sasasNumberDown;
    int tomorrowPosition = -1;
    
    private static LayoutInflater inflater = null;
    UserAndData userAndData= new UserAndData().getInstance();
    
    public SasasListViewAdapter(Context context, List<ParseObject> SasasParseList) {
        this.context = context;
        sasasfbids=new ArrayList<String>();
        sasasTitles=new ArrayList<String>();
        sasasTimes=new ArrayList<String>();
        sasasGroups=new ArrayList<String>();
        sasasNumberDown = new ArrayList<Integer>();
        rowIndexTo_idMap = new ArrayList<String>();
        
        int i,j;
        for(i=0;i<SasasParseList.size();i++){
            for(j=0;j<SasasParseList.size();j++){
                if(SasasParseList.get(i).getDate("eventDate").compareTo(SasasParseList.get(j).getDate("eventDate")) == -1){
                    ParseObject tmp=SasasParseList.get(i);
                    SasasParseList.set(i, SasasParseList.get(j));
                    SasasParseList.set(j, tmp);
                }
            }
        }
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrowDate = calendar.getTime();
        tomorrowDate.setHours(0);tomorrowDate.setMinutes(0);tomorrowDate.setSeconds(0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date afterTomorrowDate = calendar.getTime();
        tomorrowDate.setHours(0);tomorrowDate.setMinutes(0);tomorrowDate.setSeconds(0);
        for (ParseObject sasa : SasasParseList) {
            //make sure its group name exists
            boolean found=false;
            for(String group: userAndData.groups)
				try {
					if(group.equals(sasa.getJSONArray("groups").getString(0)))
						found=true;
				} catch (JSONException e) {
					continue;
				}
            if(!found) continue;
            
            
            //make sure it's after now
            if(nowDate.compareTo(sasa.getDate("eventDate"))==1)//continue if it's before right now
                continue;
            
            Log.d("usr","people down="+sasa.getJSONArray("fbidsDown").toString()+"title="+sasa.getString("title")+"-tomorrow="+tomorrowDate.toLocaleString()+"-event="+sasa.getDate("eventDate").toLocaleString());
            
            if(tomorrowPosition==-1 && tomorrowDate.compareTo(sasa.getDate("eventDate"))==-1 && afterTomorrowDate.compareTo(sasa.getDate("eventDate"))==1){//if it's after tomorrowDate and before afterTomorrowDate
                tomorrowPosition=sasasfbids.size()+1;
            }
            
            rowIndexTo_idMap.add(sasa.getObjectId());
            sasasfbids.add(sasa.getString("fbid"));
            sasasTitles.add(sasa.getString("title"));
            
//            try {
//				sasasGroups.add( sasa.getJSONArray("groups").getString(0));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
            sasasNumberDown.add(sasa.getJSONArray("fbidsDown").length());
            Date d=sasa.getDate("eventDate");
            DateFormat df = new SimpleDateFormat("hh:mm a");
            sasasTimes.add(df.format(d));
        }
        if(tomorrowPosition==-1)
            tomorrowPosition=sasasfbids.size()+1;
        
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
    }
    @Override
    public int getCount() {
        return sasasTitles.size()+2;
    }

    @Override
    public Object getItem(int position) {
        //not sure why i need this
        return sasasTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public int resolveRowPosition(int position){
        if(position>tomorrowPosition)
            return position-2;
        return position-1;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(position==tomorrowPosition||position==0){//0 is todayPosition
            if (vi == null)
                vi = inflater.inflate(R.layout.listview_label, null);
            TextView labelView = (TextView)vi.findViewById(R.id.labeltxt);
            if(position==0)
                labelView.setText("Today");
            else
                labelView.setText("Tomorrow");
        }
        else {
            position = resolveRowPosition(position);
            if (vi == null)
                vi = inflater.inflate(R.layout.listview_item, null);
            TextView titleView = (TextView) vi.findViewById(R.id.title);
            ImageView profileImage = (ImageView) vi.findViewById(R.id.profileImg);
            TextView timeView = (TextView) vi.findViewById(R.id.time);
            TextView numberDowntxt = (TextView) vi.findViewById(R.id.numberDowntxt);
            
            String url="https://graph.facebook.com/"+sasasfbids.get(position)+"/picture?type=large";
            Picasso.with(context).load(url).into(profileImage);
            
            titleView.setText(sasasTitles.get(position));
            timeView.setText(sasasTimes.get(position));
            numberDowntxt.setText(""+sasasNumberDown.get(position)+" down to go");              
        }
        return vi;
    }
    
}