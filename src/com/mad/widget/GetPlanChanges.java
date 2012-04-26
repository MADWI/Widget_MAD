package com.mad.widget;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class GetPlanChanges 
{
	private static String strona = "";
	private static final String adres = "http://wi.zut.edu.pl/plan-zajec/zmiany-w-planie?format=json";
	private static final Pattern patternQuot = Pattern.compile("(&quot;)");		
	private final HttpConnect con;
	 
	public GetPlanChanges()
	{		
		con = new HttpConnect(10000,adres);
	}
 
	public MessagePlanChanges getLastMessage()
	{
		strona = con.getStrona();
		MessagePlanChanges tempMsg = new MessagePlanChanges();
		
		if(strona != "")
		{
			/* Arrays of JSON and Messages elements */
			JSONArray entry = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			/* parse response from server */
			if (strona != "") {
				try {
					/* initialize JSON object with server response */
					jsonObject = (JSONObject) new JSONTokener(strona)
							.nextValue();
					/* get JSONArray from response */
					entry = jsonObject.getJSONArray("entry");

					/* parse elements of JSONArray */
					
					if (entry.getJSONObject(0).has("title"))
						tempMsg.setTitle(entry.getJSONObject(0).getString(
									"title"));
					if (entry.getJSONObject(0).has("created"))
						tempMsg.setDate(entry.getJSONObject(0).getString(
									"created"));
					if (entry.getJSONObject(0).has("text"))
						tempMsg.setBody(entry.getJSONObject(0).getString(
									"text"));
						
					//replace &quot and set to body String
					String temp = tempMsg.getBody();					
					Matcher matcherString = patternQuot.matcher(temp);
					tempMsg.setBody(matcherString.replaceAll("\""));
				
										
					} catch (JSONException e) {
						e.printStackTrace();
						return null;
						}
					}
			return tempMsg;
		}
		else
			return null;		
	}
    public ArrayList<MessagePlanChanges> getServerMessages() {
    	
    	strona = con.getStrona();
		/* parse response from server */
		if (strona != "") 
		{
	    	ArrayList<MessagePlanChanges> DataArray = new ArrayList<MessagePlanChanges>();    
	    	MessagePlanChanges tempMsg = new MessagePlanChanges();
	    	
			/* Arrays of JSON and Messages elements */
			JSONArray entry = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			
			try {
				/* initialize JSON object with server response */
				jsonObject = (JSONObject) new JSONTokener(strona)
						.nextValue();
				/* get JSONArray from response */
				entry = jsonObject.getJSONArray("entry");

				/* parse elements of JSONArray */
				
				for(int i=0; i<entry.length();i++)
				{
					tempMsg = new MessagePlanChanges();
					/*
					 * checking availability of elements and set fields of
					 * Message objects
					 */
					if (entry.getJSONObject(i).has("title"))
						tempMsg.setTitle(entry.getJSONObject(i).getString(
								"title"));
					if (entry.getJSONObject(i).has("created"))
						tempMsg.setDate(entry.getJSONObject(i).getString(
								"created"));
					if (entry.getJSONObject(i).has("content"))
						tempMsg.setBody(entry.getJSONObject(i).getString(
								"content"));
				
					//replace &quot and set to body String
					String temp = tempMsg.getBody();					
					Matcher matcherString = patternQuot.matcher(temp);
					tempMsg.setBody(matcherString.replaceAll("\""));
					
					DataArray.add(tempMsg);
				}				
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
					}
			return DataArray;
			}
		
		else
			return null;				
	}
}