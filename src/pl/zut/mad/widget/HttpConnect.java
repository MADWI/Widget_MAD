package pl.zut.mad.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpConnect {

	private String strona;
	private final HttpClient client;
	private HttpGet requestGET;
	private HttpResponse response;
	private HttpEntity entity;
	
	public HttpConnect(int timeout, String adres)
	{
		strona="";

		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		client = new DefaultHttpClient(params);
		try {
			requestGET = new HttpGet(new URI(adres));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}     
	}
	
	public String getStrona()
	{

		if(executeHttpGet()==false)
		{
			return "";
		}
		return strona;
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
}