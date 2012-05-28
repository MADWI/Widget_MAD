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
	private static final String TAG = "SebaJestZajebistymKoderem";
	WidgetDownload wDownload = new WidgetDownload();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String url = "http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/";
        try{
        	tab = wDownload.getGroups("Stacjonarne","Informatyka",1,2);
        	Log.d(TAG,"grupy - ok");
        }
        catch (Exception e) {
			Log.d("WidgetDownload", "getGroups error: " + e);
		}
        
        boolean wynik = false;
        try
        {
        	wynik = wDownload.downloadPlan(url, "Stacjonarne", tab[1]);
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