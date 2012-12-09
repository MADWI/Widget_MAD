package mad.widget.activities;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import mad.widget.R;
import mad.widget.connections.HttpConnect;
import mad.widget.connections.WeekParityChecker;
import mad.widget.models.DayParity;
import mad.widget.models.ListViewAdapterWeekParity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CalendarActivity extends Activity {
	private static final String TAG = "CalendarActivity";
	private Resources res;
	private ProgressDialog pd;
	private boolean enableExecuteRefresh = true;

	private final WeekParityChecker checker = new WeekParityChecker();
	private ArrayList<DayParity> parityList = null;

	private ListViewAdapterWeekParity adapter;
	private ListView lvWeekList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		res = getApplicationContext().getResources();
		executeAsyncTask();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_calendar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i(TAG, "onOptionsItemSelected");
		switch (item.getItemId()) {
		case R.id.refresh:
			if (enableExecuteRefresh) {
				if (HttpConnect.isOnline(getApplicationContext()) == true) {
					new AsyncTaskGetParityList().execute(getApplicationContext());
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

	private void executeAsyncTask() {
		if (HttpConnect.isOnline(getApplicationContext()) == true) {
			new AsyncTaskGetParityList().execute(getApplicationContext());

		}
	}

	/** Metoda odswiezajaca ListView ze zmianami w planie */
	private void refreshListView() {
		Log.i(TAG, "refreshListView");
		lvWeekList = (ListView) findViewById(R.id.lvWeekList);

		adapter = new ListViewAdapterWeekParity(getApplicationContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, parityList);

		lvWeekList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				GregorianCalendar gc = parityList.get(position).getGregorianCal();
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime", gc.getTimeInMillis());
				intent.putExtra("allDay", false);
				intent.putExtra("rrule", "FREQ=DAILY");
				intent.putExtra("endTime", gc.getTimeInMillis() + 60 * 60 * 1000);
				intent.putExtra("title", "");
				startActivity(intent);

			}
		});
		lvWeekList.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		if (parityList.size() == 0) {
			Toast.makeText(this, getString(R.string.no_week_parity_list),
					Toast.LENGTH_LONG).show();
		}

	}

	private class AsyncTaskGetParityList extends
			AsyncTask<Context, Boolean, Void> {

		ArrayList<DayParity> tempArray = null;
		Context ctx;

		@Override
		protected Void doInBackground(Context... params) {
			Log.i(TAG, "doInBackground");
			ctx = params[0];

			if (HttpConnect.isOnline(getApplicationContext()) == true) {
				tempArray = checker.getAllParity();
				if (tempArray != null) {
					parityList = tempArray;
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
			if (values[0] == false)
				Toast.makeText(ctx,
						res.getString(R.string.cant_download_week_parity_list),
						Toast.LENGTH_SHORT).show();

		}

		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute");
			pd = ProgressDialog.show(CalendarActivity.this,
					res.getString(R.string.refreshing_week_parity_list_title),
					res.getString(R.string.refreshing_body), true, true);
			pd.setCancelable(false);
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
