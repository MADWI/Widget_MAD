package pl.zut.mad.widget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
public class TestHttpGet 
{
	
    public String executeHttpGet() throws Exception 
    {
        BufferedReader in = null;
        String strona;
        try 
        {
        	//zainicjowanie klienta http oraz wyslanie zapytania GET
            HttpClient client = new DefaultHttpClient();
            HttpGet requestGET = new HttpGet(new URI("http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/Stacjonarne/"));
            // mo¿na te¿ get.setURI ("http://code.google.com/android/");
            
            HttpResponse response = client.execute(requestGET);
            ///////////////////////////////////////////////////////////////////////
            
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            
            while( (line = in.readLine()) != null ) {
            	sb.append(line + NL);
            }
            in.close();
            
           strona = sb.toString();      
        }
        finally 
        {  
        	if (in != null)
        	{
        		try
        		{
        			in.close();
        		}
        		catch(IOException e)
        		{
        			e.printStackTrace();
        		} 
        	}
        }
        return strona;
    }
}