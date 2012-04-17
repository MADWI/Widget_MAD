package com.mad.planchanges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
public class GetPlanChanges 
{
	private String strona;
	private final HttpClient client;
	private HttpGet requestGET;
	private HttpResponse response;
	private HttpEntity entity;
	static final Pattern patternQuot = Pattern.compile("(&quot;)");
	
	public GetPlanChanges(int timeout)
	{
		strona=" ";
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		client = new DefaultHttpClient(params);
		try {
			requestGET = new HttpGet(new URI("http://wi.zut.edu.pl/plan-zajec/zmiany-w-planie?format=json"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}     
		
	}
    private boolean executeHttpGet()
    {
        BufferedReader in = null;
        try 
        {
            response = client.execute(requestGET);
            entity =response.getEntity();
            if(entity ==null)
            	return false;
            
            in = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            
            while( (line = in.readLine()) != null ) {
            	sb.append(line + NL);
            }            
           strona = sb.toString();  
           
        }
        catch(Exception e)
		{
			e.printStackTrace();
			return false;
		} 
        finally 
        {   
        	try {
				entity.consumeContent();
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			} 
        	if (in != null)
        	{
        		try
        		{
        			in.close();
        		}
        		catch(IOException e)
        		{
        			e.printStackTrace();
        			return false;
        		} 
        	}
        }
        return true;
    }
 
    public ArrayList<DataPlanChanges> getServerMessages() {

    	ArrayList<DataPlanChanges> DataArray = new ArrayList<DataPlanChanges>();
    
		if(executeHttpGet()==false)
		{
			return null;
		}    	
    	DataPlanChanges tempMsg = new DataPlanChanges();
		/* Arrays of JSON and Messages elements */
		JSONArray entry = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		/* parse response from server */
		if (strona != " ") {
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