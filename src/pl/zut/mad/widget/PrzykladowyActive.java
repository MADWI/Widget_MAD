package pl.zut.mad.widget;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class PrzykladowyActive extends Activity {
	
	//String stronaPlanStacjonarne;
	String urlPlanNiestacojonarne;
	String[] tab = null;
	
	WidgetDownload wDownload = new WidgetDownload();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String url = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/Stacjonarne/";
        //parse(urlGet(String.valueOf(uri)),"[A-Z]{1,3}[0-9]{1}");
        try{
        	tab = wDownload.getGroups(url,"I1");
        	Log.d("grupy","ok");
        }
        catch (Exception e) {
			Log.d("WidgetDownload", "getGroups error: " + e);
		}
        
        boolean wynik = false;
        try
        {
        	wynik = wDownload.downloadPlan("Stacjonarne", "I1-22");
        }
        catch(Exception e)
        {
        	Log.d("WidgetDownload", "downloadPlan error: " + e);
        }
        
        if(wynik)
        {
        	Toast.makeText(getApplicationContext(), "Pobrano plan", Toast.LENGTH_LONG).show();
        }
        else
        {
        	Toast.makeText(getApplicationContext(), "B³¹d pobrania planu", Toast.LENGTH_LONG).show();
        }
    } 
    
}