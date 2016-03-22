package com.example.parsecomlogin;

import java.util.List;
import java.util.Set;

import com.parse.ParseObject;


/*
 * if user is logged in then "_id" is set and all the JSONObject
 * 
 * data in user:
 * {
    _id,fbid,firstName:,lastName,fbToken,
    fbFriends[_id],blockedFriends:[_id]
    loc:{state,city,lastUpdatedTime,coordinates:[]},
	}
 */

public class UserAndData {
	public String fbid="1241954897";
	public String firstName="Arkadip";
	public String lastName="Saha";
	
	public Set<String> groups;
	public List<ParseObject> SasasParseList;
	
	private static UserAndData instance = null;
    protected UserAndData() {
        
     }
     public static UserAndData getInstance() {
        if(instance == null) {
           instance = new UserAndData();
        }
        return instance;
     }
}
