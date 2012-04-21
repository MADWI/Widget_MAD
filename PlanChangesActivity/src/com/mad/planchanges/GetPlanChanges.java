package com.mad.planchanges;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class GetPlanChanges 
{
	static String strona = "";
	static final String adres = "http://wi.zut.edu.pl/plan-zajec/zmiany-w-planie?format=json";
	static final Pattern patternQuot = Pattern.compile("(&quot;)");	
	HttpConnect con;
	 
	public GetPlanChanges()
	{		
		con = new HttpConnect(10000,adres);
	}
 
    public ArrayList<DataPlanChanges> getServerMessages() {

    	ArrayList<DataPlanChanges> DataArray = new ArrayList<DataPlanChanges>();    
    	DataPlanChanges tempMsg = new DataPlanChanges();
    	
    	strona = con.getStrona();
    	
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
				
				for(int i=0; i<entry.length();i++)
				{
					tempMsg = new DataPlanChanges();
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
					if (entry.getJSONObject(i).has("text"))
						tempMsg.setBody(entry.getJSONObject(i).getString(
								"text"));
					
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
				}
		
		return DataArray;
	}
}