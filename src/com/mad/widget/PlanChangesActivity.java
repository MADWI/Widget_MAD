package com.mad.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlanChangesActivity extends Activity {

	private final GetPlanChanges pars = new GetPlanChanges();
	private final String TAG = "MAD WIZUT Widget Plan Changes";

	private Resources res;
	private ArrayList<MessagePlanChanges> news = new ArrayList<MessagePlanChanges>();
	private ListViewAdapterPlanChanges adapter;
	private ListView lvPlanChanges;
	private ProgressDialog pd;
	private boolean enableExecuteRefresh = true;
	private boolean firstRun = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "<<< ON CREATE >>>");
		setContentView(R.layout.main_plan_changes);
		res = getApplicationContext().getResources();

		if (isOnline() == true && firstRun == true) {
			firstRun = false;
			new AsyncTaskGetPlanChanges().execute(getApplicationContext());

		}
	}

	public void showToast(String text, int duration, Context con) {
		Log.e(TAG, "<<< ShowToast >>>");
		Toast.makeText(con, text, duration).show();
	}

	private void refreshListView() {
		Log.e(TAG, "<<< refreshListView >>>");
		lvPlanChanges = (ListView) findViewById(R.id.listPlanChanges);
		adapter = new ListViewAdapterPlanChanges(getApplicationContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, news);
		lvPlanChanges.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		lvPlanChanges.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView body = (TextView) view.findViewById(R.id.body);
				if (body.getVisibility() == View.GONE) {
					body.setVisibility(View.VISIBLE);
				} else
					body.setVisibility(View.GONE);
			}
		});

	}

	public boolean isOnline() {
		Log.e(TAG, "<<< is Online ? >>>");
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.isConnected()) {
			return true;
		} else {
			showToast(res.getString(R.string.no_Internet), 2000,
					getApplicationContext());
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
			if (enableExecuteRefresh) {
				if (isOnline() == true) {
					new AsyncTaskGetPlanChanges()
							.execute(getApplicationContext());
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

	private class AsyncTaskGetPlanChanges extends
			AsyncTask<Context, Boolean, Void> {

		ArrayList<MessagePlanChanges> tempArray = null;
		Context ctx;

		@Override
		protected Void doInBackground(Context... params) {
			Log.e(TAG, "<<< AsyncTask - doInBackground >>>");
			ctx = params[0];

			if (isOnline() == true) {
				Log.e(TAG, "<<< refreshMessages >>>");

				tempArray = pars.getServerMessages();
				if (tempArray != null) {
					news = tempArray;
				} else {
					publishProgress(false);

				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Boolean... values) {
			super.onProgressUpdate(values);

			if (values[0] == false) {
				showToast(res.getString(R.string.plan_changes_Messages), 3000,
						ctx);
			} else if (values[0] == true) {
				showToast(res.getString(R.string.plan_changes_finished), 3000,
						ctx);
			}

		}

		@Override
		protected void onPreExecute() {
			Log.e(TAG, "<<< AsyncTask - onPreExecute >>>");
			pd = ProgressDialog.show(PlanChangesActivity.this,
					res.getString(R.string.plan_changes_refreshing_title),
					res.getString(R.string.plan_changes_refreshing_body), true,
					true);
			enableExecuteRefresh = false;

		}

		@Override
		protected void onPostExecute(Void result) {
			Log.e(TAG, "<<< AsyncTask - onPostExecute >>>");
			pd.dismiss();
			if (tempArray != null) {
				refreshListView();
				publishProgress(true);
			}
			enableExecuteRefresh = true;
		}

	}

}
