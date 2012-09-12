package mad.widget.activities;

/**
 * Activity for display plan changes in ListView.
 *
 * @author Sebastian Swierczek
 * @version 1.3.1
 */

import java.util.ArrayList;

import mad.widget.R;
import mad.widget.connections.GetPlanChanges;
import mad.widget.models.ListViewAdapterPlanChanges;
import mad.widget.models.MessagePlanChanges;
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
	private static final String TAG = "PlanChangesActivity";

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
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main_plan_changes);
		res = getApplicationContext().getResources();

		if (isOnline() == true && firstRun == true) {
			firstRun = false;
			new AsyncTaskGetPlanChanges().execute(getApplicationContext());

		}
	}

	public void showToast(String text, int duration, Context con) {
		Log.i(TAG, "showToast");
		Toast.makeText(con, text, duration).show();
	}

	private void refreshListView() {
		Log.i(TAG, "refreshListView");
		lvPlanChanges = (ListView) findViewById(R.id.listPlanChanges);
		adapter = new ListViewAdapterPlanChanges(getApplicationContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, news);
		lvPlanChanges.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		lvPlanChanges.setOnItemClickListener(new OnItemClickListener() {

			@Override
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
		Log.i(TAG, "isOnline");
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
		Log.i(TAG, "onOptionsItemSelected");
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
			Log.i(TAG, "doInBackground");
			ctx = params[0];

			if (isOnline() == true) {
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
			Log.i(TAG, "onProgressUpdate");
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
			Log.i(TAG, "onPreExecute");
			pd = ProgressDialog.show(PlanChangesActivity.this,
					res.getString(R.string.plan_changes_refreshing_title),
					res.getString(R.string.plan_changes_refreshing_body), true,
					true);
			enableExecuteRefresh = false;

		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i(TAG, "onPostExecute");
			pd.dismiss();
			if (tempArray != null) {
				refreshListView();
				publishProgress(true);
			}
			enableExecuteRefresh = true;
		}

	}

}
