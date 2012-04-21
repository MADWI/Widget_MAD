package com.mad.planchanges;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


public class PlanChangesActivity extends Activity{
	
	private final GetPlanChanges pars = new GetPlanChanges();
	private final String TAG = "Plan Changes";
	private ArrayList<MessagePlanChanges> news= new ArrayList<MessagePlanChanges>();
	private ListViewAdapterPlanChanges adapter;
	private ListView lvPlanChanges; 
	boolean enableExecuteRefresh = true;
	
	AsyncTaskGetPlanChanges async = new AsyncTaskGetPlanChanges();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "<<< ON CREATE >>>");

		setContentView(R.layout.main);
		if(isOnline()==true)
		{
			new AsyncTaskGetPlanChanges().execute("");

		}	
	}
	
	public void showToast (String text, int duration,Context con)
	{
		Log.e(TAG, "<<< ShowToast >>>");
		Toast.makeText(con, text, duration).show();
	}
	
	private void refreshListView()
	{
		Log.e(TAG, "<<< refreshListView >>>");
		lvPlanChanges = (ListView)findViewById(R.id.listPlanChanges);
		adapter = new ListViewAdapterPlanChanges(getApplicationContext(),
			android.R.layout.simple_list_item_1,android.R.id.text1,news);
		lvPlanChanges.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
			
	public boolean isOnline() {
			Log.e(TAG, "<<< is Online ? >>>");
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo ni = cm.getActiveNetworkInfo();
		    if (ni!=null && ni.isAvailable() && ni.isConnected()) {
		        return true;
		    } else {
		    	showToast("No Internet Connection",2000,getApplicationContext());
		        return false; 
		    }
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e(TAG, "<<< onOptionsItemSelected >>>");
		switch (item.getItemId()) {
		case R.id.refresh:
			if(enableExecuteRefresh)
			{
				if(isOnline()==true)
				{
					new AsyncTaskGetPlanChanges().execute("");
				}
			}
			return true;
		case R.id.exit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class AsyncTaskGetPlanChanges extends AsyncTask <String, Void, String>{

		ArrayList<MessagePlanChanges> tempArray= null;
		
		@Override
		protected String doInBackground(String... arg0) {
			Log.e(TAG, "<<< AsyncTask - doInBackground >>>");
				
			if(isOnline()==true)
			{
				Log.e(TAG, "<<< refreshMessages >>>");
								
				tempArray = pars.getServerMessages();
				if(tempArray != null)
				{
					news = tempArray;	
				}
				else
				{
					showToast("Nie można pobrać !",2000,getApplicationContext());
				
				}
			}
			return null;
		}
		
		 @Override
		 protected void onPreExecute() 
		 {
			Log.e(TAG, "<<< AsyncTask - onPreExecute >>>");
			enableExecuteRefresh = false;
		    showToast("Pobieram wiadomości...",3000,getApplicationContext());

		 }
		@Override
		protected void onPostExecute(String result)
		{
			Log.e(TAG, "<<< AsyncTask - onPostExecute >>>");
			if(tempArray != null)
			{
				refreshListView();
				showToast("Sukces !",2000,getApplicationContext());
			}
			enableExecuteRefresh = true;
		}
		
	}

}
