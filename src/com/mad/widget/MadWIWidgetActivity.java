package com.mad.widget;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.CheckBoxPreference;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
 
    public class MadWIWidgetActivity extends AppWidgetProvider { 
    	
    	public static final String ACTION_WIDGET_GET_PLAN_CHANGES = "PlanChangesWidget";
    	public static String ACTION_WIDGET_REFRESH = "ActionRefreshWidget";
    	public static String ACTION_WIDGET_PREFERENCES ="ActionPreferencesWidget";
    	
    	private static final String PREFERENCES_NAME = "MAD_WIZUT_Preferences";
    	private SharedPreferences ustawienia;   	
    
    	private final String TAG = "MAD WIZUT Widget";
    	   	
    	static final long seconds = 5;
    	
    	//obiekt do pobierania zmian w planie
    	private final GetPlanChanges pars = new GetPlanChanges();
    	//ostatnia zmiana w planie
    	private MessagePlanChanges lastMessage = null;
    	
    	private Timer timer= new Timer(); 
    	
    	
        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
           
        	//pobarnie chceckoboxa z info o automatycznej aktualizacji z shared preferences
        	ustawienia = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_WORLD_READABLE);
        	
        	Boolean autoRefresh = ustawienia.getBoolean("checkbox", true);
        	Log.e(TAG, "<<< checkbox =" + autoRefresh.toString());
        	
        	if(autoRefresh == true)
        	{
        		timer. scheduleAtFixedRate(new Aktualizacja(context, appWidgetManager),0,  seconds*1000);
        		Log.e(TAG, "<<< AUTOREFRESH ON >>>");
        	}
        	else
        	{
        		Log.e(TAG, "<<< AUTOREFRESH OFF  >>>");
        		timer.cancel();
        		
        	}
            
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget1);
            
            //onclik zmiany w planie
    		Intent changesIntent = new Intent(context, PlanChangesActivity.class);
    		changesIntent.setAction(ACTION_WIDGET_GET_PLAN_CHANGES);    	
    		PendingIntent changesPendingIntent = PendingIntent.getActivity(context, 0, changesIntent, 0);  
    		remoteViews.setOnClickPendingIntent(R.id.btZmianyPlanu, changesPendingIntent);
    		
    		//onclik ustawienia
    		Intent configIntent = new Intent(context, MyPreferences.class);
    		configIntent.setAction(ACTION_WIDGET_PREFERENCES);    	
    		PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);  
    		remoteViews.setOnClickPendingIntent(R.id.imb_ustawienia, configPendingIntent);
    		
    		//onclick refresh
    		Intent active = new Intent(context, MadWIWidgetActivity.class);
    		active.setAction(ACTION_WIDGET_REFRESH);    		
    		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);     
    		remoteViews.setOnClickPendingIntent(R.id.imb_odswiez, actionPendingIntent);           
             
    		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
            
            }
 
        
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		final String action = intent.getAction();
    		
    		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
    			final int appWidgetId = intent.getExtras().getInt(
    					AppWidgetManager.EXTRA_APPWIDGET_ID,
    					AppWidgetManager.INVALID_APPWIDGET_ID);
    			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
    				this.onDeleted(context, new int[] { appWidgetId });
    			}
    		} else {

    			if (intent.getAction().equals(ACTION_WIDGET_REFRESH)) {    				
    				Log.e(TAG, "<<< onClick Refresh Button  >>>");
    				if(timer != null)
    					timer.schedule(new Aktualizacja(context,AppWidgetManager.getInstance(context)),0);
    				
    			} else {
    			}     
    			super.onReceive(context, intent);
    		}
    	}
        
        private class Aktualizacja extends TimerTask{
 
            RemoteViews remoteViews;
            ComponentName thisWidget;
            AppWidgetManager appWidgetManager; 
 
            public Aktualizacja(Context context,AppWidgetManager appWidgetManager){
                this.appWidgetManager = appWidgetManager;
                remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget1);
                thisWidget = new ComponentName(context, MadWIWidgetActivity.class);  
            }
  
            @Override
            public void run(){
            	Log.e(TAG, "<<< RUN refresh>>");
            	
                //uruchomienie progress bara
                remoteViews.setViewVisibility(R.id.progressCircle, View.VISIBLE);          
                remoteViews.setTextViewText(R.id.tv_zmiany_tytul," ");
                remoteViews.setTextViewText(R.id.tv_zmiany_tresc,"£adowanie zmian w planie...");
                
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);                 
             
                //pobranie daty
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());               	
            	remoteViews.setTextViewText(R.id.tv_data,currentDateTimeString + " r.");
            	
            	appWidgetManager.updateAppWidget(thisWidget, remoteViews); 
            	
                Log.e(TAG, "<<< getlast message  with AsyncTask>>>");
                
                //new AsyncTaskGetLastMessage().execute();
                                
                if(lastMessage != null)
                {
                	remoteViews.setTextViewText(R.id.tv_zmiany_tytul,lastMessage.getTitle());
                	remoteViews.setTextViewText(R.id.tv_zmiany_tresc,lastMessage.getBody());
                	
                }
                //schowanie progress bara
                remoteViews.setViewVisibility(R.id.progressCircle, View.INVISIBLE);    
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);    
                
                                     
            }
        }
        /*class AsyncTaskGetLastMessage extends AsyncTask <String, String, String>{

			@Override
			protected String doInBackground(String... arg0) {
				lastMessage = pars.getLastMessage();
				return null;
			}*/
    		
    
   }
    
    